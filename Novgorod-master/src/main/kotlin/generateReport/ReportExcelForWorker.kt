package generateReport

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun createReportExcelForWorker(listDataWP: List<DataWorkerPeriod>, name: String, dataLeft: String, dataRight: String) {
// АНАЛОГИЧНЫЙ ОТЧЕТ reportTable.ReportExcel.kt
    val columnsFirstRow = listOf("ФИО", "ДАТА И ВРЕМЯ", "ГОТОВАЯ ПРОДУКЦИЯ", "НЕЗАВЕРШЕННАЯ ПРОДУКЦИЯ", "ВСЕГО")
    val columnsSecondRow = listOf("Наименование", "КГ", "Стоимость")

    val workbook = XSSFWorkbook()

    val sheet = workbook.createSheet("$name $dataLeft-$dataRight")

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

    for (i in 2..7) {
        sheet.setColumnWidth(i, 4390)
    }
    val zeroRow = sheet.createRow(0)
    val firstRow = sheet.createRow(1)
    val secondRow = sheet.createRow(2)

    for (i in 0..7) {
        zeroRow.createCell(i)
        zeroRow.getCell(i).cellStyle = headerCellStyle
        if (i == 0) zeroRow.getCell(i).setCellValue(columnsFirstRow[0])
        if (i == 1) zeroRow.getCell(i).setCellValue(columnsFirstRow[1])
        if (i == 2) zeroRow.getCell(i).setCellValue(columnsFirstRow[2])
        if (i == 5) zeroRow.getCell(i).setCellValue(columnsFirstRow[3])
    }
    for (i in 0..7) {
        firstRow.createCell(i)
        firstRow.getCell(i).cellStyle = headerCellStyle
        if (i == 2 || i == 5) firstRow.getCell(i).setCellValue(columnsSecondRow[0])
        if (i == 3 || i == 6) firstRow.getCell(i).setCellValue(columnsSecondRow[1])
        if (i == 4 || i == 7) firstRow.getCell(i).setCellValue(columnsSecondRow[2])
    }
    sheet.addMergedRegion(CellRangeAddress(0, 1 , 0, 0))
    sheet.addMergedRegion(CellRangeAddress(0, 1 , 1, 1))
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 2, 4))
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 5, 7))

    secondRow.createCell(0).setCellValue(name)

    var rowIdx: Int
    var finishedTotal = 0.0
    var unfinishedTotal = 0.0
    for (data in listDataWP) {
        finishedTotal += data.totalFinish.toDouble()
        unfinishedTotal += data.totalUnfinished.toDouble()
        rowIdx = if (sheet.lastRowNum == 2) 2
        else sheet.lastRowNum + 2

        val lastRowIdx = rowIdx

        for (product in data.listFinishedProd) {
            if (sheet.lastRowNum + 1 <= rowIdx) {
                val row = sheet.createRow(rowIdx)
                row.createCell(1).setCellValue("${data.date} ${data.timeBegin}-${data.timeFinish}")
                row.createCell(2).setCellValue(product[0])
                row.createCell(3).setCellValue(product[1])
                row.createCell(4).setCellValue(product[2])
                rowIdx++
            } else {
                val row = sheet.getRow(rowIdx)
                row.createCell(1).setCellValue("${data.date} ${data.timeBegin}-${data.timeFinish}")
                row.createCell(2).setCellValue(product[0])
                row.createCell(3).setCellValue(product[1])
                row.createCell(4).setCellValue(product[2])
                rowIdx++
            }
        }
        rowIdx = lastRowIdx
        for (product in data.listUnfinishedProd) {
            if (sheet.lastRowNum + 1 <= rowIdx) {
                val row = sheet.createRow(rowIdx)
                row.createCell(1).setCellValue("${data.date} ${data.timeBegin}-${data.timeFinish}")
                row.createCell(5).setCellValue(product[0])
                row.createCell(6).setCellValue(product[1])
                row.createCell(7).setCellValue(product[2])
                rowIdx++
            } else {
                val row = sheet.getRow(rowIdx)
                row.createCell(1).setCellValue("${data.date} ${data.timeBegin}-${data.timeFinish}")
                row.createCell(5).setCellValue(product[0])
                row.createCell(6).setCellValue(product[1])
                row.createCell(7).setCellValue(product[2])
                rowIdx++
            }
        }
    }
    val totalPrice = finishedTotal + unfinishedTotal
    val rowForFinishedPrice = sheet.createRow(sheet.lastRowNum + 1)
    val rowForUnfinishedPrice = sheet.createRow(sheet.lastRowNum + 1)
    val rowForTotalPrice = sheet.createRow(sheet.lastRowNum + 1)

    rowForFinishedPrice.createCell(0).setCellValue("${columnsFirstRow[2]}:")
    rowForUnfinishedPrice.createCell(0).setCellValue("${columnsFirstRow[3]}:")
    rowForTotalPrice.createCell(0).setCellValue("${columnsFirstRow[4]}:")

    rowForFinishedPrice.createCell(1).setCellValue(finishedTotal)
    rowForUnfinishedPrice.createCell(1).setCellValue(unfinishedTotal)
    rowForTotalPrice.createCell(1).setCellValue(totalPrice)

    val desk = System.getProperty("user.home") + File.separator + "Desktop"
    File("$desk${File.separator}Отчеты").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по сотрудникам").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по сотрудникам${File.separator}${LocalDate.now().year}").mkdir()
    val fileOut = FileOutputStream("$desk${File.separator}Отчеты${File.separator}Отчеты по сотрудникам${File.separator}${LocalDate.now().year}${File.separator}$name $dataLeft-$dataRight.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    Desktop.getDesktop().open(File("$desk${File.separator}Отчеты${File.separator}Отчеты по сотрудникам${File.separator}${LocalDate.now().year}${File.separator}$name $dataLeft-$dataRight.xlsx"))
}

