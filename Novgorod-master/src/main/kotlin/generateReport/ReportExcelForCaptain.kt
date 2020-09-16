package generateReport

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun createReportForCaptain(dateLeft: String, dateRight: String, captainsDatePlace: MutableList<List<Pair<Int,String>>>) {
    println(captainsDatePlace)
    val columnsFirstRow = listOf("ФИО", "ДАТА", "УЧАСТОК")

    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Бригадиры $dateLeft-$dateRight")

    val headerFont = workbook.createFont()
    headerFont.bold = true

    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.setAlignment(HorizontalAlignment.CENTER)
    headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER)

    val hcStyleS = workbook.createCellStyle()
    hcStyleS.setAlignment(HorizontalAlignment.CENTER)

    sheet.setColumnWidth(0, 10976)
    sheet.setColumnWidth(1, 7317)
    sheet.setColumnWidth(2, 10976)

    val zeroRow = sheet.createRow(0)

    zeroRow.createCell(0).setCellValue(columnsFirstRow[0])
    zeroRow.createCell(1).setCellValue(columnsFirstRow[1])
    zeroRow.createCell(2).setCellValue(columnsFirstRow[2])
    zeroRow.getCell(0).cellStyle = headerCellStyle
    zeroRow.getCell(1).cellStyle = headerCellStyle
    zeroRow.getCell(2).cellStyle = headerCellStyle

    var rowIdx = 1
    for (value in captainsDatePlace) {
        val row = sheet.createRow(rowIdx)
        row.createCell(0).setCellValue(value[0].second)
        row.createCell(1).setCellValue(value[1].second)
        row.createCell(2).setCellValue(value[2].second)
        rowIdx++
    }
    val desk = System.getProperty("user.home") + File.separator + "Desktop"
    File("$desk${File.separator}Отчеты").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по бригадирам").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по бригадирам${File.separator}${LocalDate.now().year}").mkdir()
    val fileOut = FileOutputStream("$desk${File.separator}Отчеты${File.separator}Отчеты по бригадирам${File.separator}${LocalDate.now().year}${File.separator}Бригадиры $dateLeft-$dateRight.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    Desktop.getDesktop().open(File("$desk${File.separator}Отчеты${File.separator}Отчеты по бригадирам${File.separator}${LocalDate.now().year}${File.separator}Бригадиры $dateLeft-$dateRight.xlsx"))

}