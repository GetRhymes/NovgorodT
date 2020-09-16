package generateReport

import javafx.stage.FileChooser
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import sun.security.krb5.internal.crypto.Des
import tornadofx.chooseFile
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

fun createReportExcelForTime(dateLeft: String, dateRight: String, listDays: List<Int>, month: Pair<Int, String>, mapOfTime: MutableMap<String, MutableMap<Int, Int>>) {
    val columnsRow = listOf("Номер", "ФИО", "ИК", "ИТОГО")
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("${month.second} Табель времени")

    val headerFont = workbook.createFont()
    headerFont.bold = true

    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.setAlignment(HorizontalAlignment.CENTER)
    headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER)

    val trivialCellStyle = workbook.createCellStyle()
    trivialCellStyle.setFont(headerFont)
    trivialCellStyle.setAlignment(HorizontalAlignment.CENTER)
    trivialCellStyle.setVerticalAlignment(VerticalAlignment.CENTER)

    val firstRow = sheet.createRow(0)
    val secondRow = sheet.createRow(1)
    val thirdRow = sheet.createRow(2)

    sheet.setColumnWidth(0, 2378)
    sheet.setColumnWidth(1, 9147)
    sheet.setColumnWidth(2, 5488)

    for (i in 0..2) {
        firstRow.createCell(i).setCellValue(columnsRow[i])
        firstRow.getCell(i).cellStyle = headerCellStyle
    }
    firstRow.createCell(3).setCellValue(month.second)
    firstRow.getCell(3).cellStyle = headerCellStyle
    var indexDaysForRow = 0
    var cond = false

    if (listDays.size % 2 == 0) {
        indexDaysForRow = listDays.size / 2
        cond = false
    } else if (listDays.size % 2 == 1) {
        indexDaysForRow = (listDays.size + 1) / 2
        cond = true
    }

    for (i in 0 until indexDaysForRow) {
        if (cond) {
            if (i == indexDaysForRow - 1) {
                secondRow.createCell(i + 3).setCellValue("X")
                secondRow.getCell(i + 3).cellStyle = headerCellStyle
            } else {
                secondRow.createCell(i + 3).setCellValue(listDays[i].toString())
                secondRow.getCell(i + 3).cellStyle = headerCellStyle
            }
            thirdRow.createCell(i + 3).setCellValue(listDays[i + indexDaysForRow - 1].toString())
            thirdRow.getCell(i + 3).cellStyle = headerCellStyle
        }
        else {
            secondRow.createCell(i + 3).setCellValue(listDays[i].toString())
            secondRow.getCell(i + 3).cellStyle = headerCellStyle
            thirdRow.createCell(i + 3).setCellValue(listDays[i + indexDaysForRow].toString())
            thirdRow.getCell(i + 3).cellStyle = headerCellStyle
        }
    }
    firstRow.createCell(indexDaysForRow + 3).setCellValue(columnsRow[3])
    firstRow.getCell(indexDaysForRow + 3).cellStyle = headerCellStyle

    sheet.addMergedRegion(CellRangeAddress(0, 2 , 0, 0))
    sheet.addMergedRegion(CellRangeAddress(0, 2 , 1, 1))
    sheet.addMergedRegion(CellRangeAddress(0, 2 , 2, 2))
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 3, indexDaysForRow + 2))
    sheet.addMergedRegion(CellRangeAddress(0, 2 , indexDaysForRow + 3, indexDaysForRow + 3))

    var idx = 3
    var count = 1
    for (worker in mapOfTime) {
        var total = 0
        val nameAndCode = worker.key.split("#")
        val rowFirst = sheet.createRow(idx)
        val rowLast = sheet.createRow(idx + 1)
        sheet.addMergedRegion(CellRangeAddress(idx, idx + 1 , 0, 0))
        sheet.addMergedRegion(CellRangeAddress(idx, idx + 1, 1, 1))
        sheet.addMergedRegion(CellRangeAddress(idx, idx + 1, 2, 2))
        sheet.addMergedRegion(CellRangeAddress(idx, idx  + 1 , indexDaysForRow + 3, indexDaysForRow + 3))

        rowFirst.createCell(0).setCellValue(count.toString())
        rowFirst.getCell(0).cellStyle = trivialCellStyle
        rowFirst.createCell(1).setCellValue(nameAndCode[0])
        rowFirst.getCell(1).cellStyle = trivialCellStyle
        rowFirst.createCell(2).setCellValue(nameAndCode[1])
        rowFirst.getCell(2).cellStyle = trivialCellStyle



        for (i in 0 until indexDaysForRow) {
            if (cond && i == indexDaysForRow - 1) {
                rowFirst.createCell(i + 3).setCellValue("X")
                rowFirst.getCell(i + 3).cellStyle = trivialCellStyle
            }
            else {
                val valueCellFirstRow = if (worker.value.contains(i + 1)) worker.value[i + 1] else 0
                rowFirst.createCell(i + 3).setCellValue(valueCellFirstRow.toString())
                rowFirst.getCell(i + 3).cellStyle = trivialCellStyle
                total += valueCellFirstRow!!
            }
            val valueCellLastRow = if (worker.value.contains(i + indexDaysForRow)) worker.value[i + indexDaysForRow] else 0
            rowLast.createCell(i + 3).setCellValue(valueCellLastRow.toString())
            rowLast.getCell(i + 3).cellStyle = trivialCellStyle
            total += valueCellLastRow!!
        }
        rowFirst.createCell(indexDaysForRow + 3).setCellValue(total.toString())
        rowFirst.getCell(indexDaysForRow + 3).cellStyle = trivialCellStyle

        idx += 2
        count++
    }
    val desk = System.getProperty("user.home") + File.separator + "Desktop"
    File("$desk${File.separator}Отчеты").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Табель рабочего времени").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Табель рабочего времени${File.separator}${LocalDate.now().year}").mkdir()
    val fileOut = FileOutputStream("$desk${File.separator}Отчеты${File.separator}Табель рабочего времени${File.separator}${LocalDate.now().year}${File.separator}Табель рабочего времени ${month.second}.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    Desktop.getDesktop().open(File("$desk${File.separator}Отчеты${File.separator}Табель рабочего времени${File.separator}${LocalDate.now().year}${File.separator}Табель рабочего времени ${month.second}.xlsx"))
}
