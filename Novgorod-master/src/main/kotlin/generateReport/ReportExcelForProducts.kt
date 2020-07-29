package generateReport

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun createReportExcelForProducts(dataLeft: String, dataRight: String, data: MutableMap<String, MutableList<List<String>>>, cond: Boolean) {

    val columnsFirstRow = listOf("Наименование", "КГ", "Стоимость", "ДАТА И ВРЕМЯ")

    val workbook = XSSFWorkbook()
    var prod = "ГП"
    prod = if (cond) "ГП" else "НП"
    val sheet = workbook.createSheet("$prod $dataLeft-$dataRight")

    val headerFont = workbook.createFont()
    headerFont.bold = true

    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.setAlignment(HorizontalAlignment.CENTER)
    headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER)

    val hcStyleS = workbook.createCellStyle()
    hcStyleS.setAlignment(HorizontalAlignment.CENTER)

    sheet.setColumnWidth(0, 7317)
    sheet.setColumnWidth(1, 4390)
    sheet.setColumnWidth(2, 4390)
    sheet.setColumnWidth(3, 7317)


    val zeroRow = sheet.createRow(0)

    for (i in 0..3) {
        zeroRow.createCell(i)
        zeroRow.getCell(i).cellStyle = headerCellStyle
        if (i == 0) zeroRow.getCell(i).setCellValue(columnsFirstRow[0])
        if (i == 1) zeroRow.getCell(i).setCellValue(columnsFirstRow[1])
        if (i == 2) zeroRow.getCell(i).setCellValue(columnsFirstRow[2])
        if (i == 3) zeroRow.getCell(i).setCellValue(columnsFirstRow[3])
    }
    var rowIdx = 1
    for (products in data) {
        var amountK = 0.0
        var amountR = 0.0

        for (list in products.value) {
            val row = sheet.createRow(rowIdx)
            row.createCell(0).setCellValue(products.key)
            row.createCell(1).setCellValue(list[1])
            row.createCell(2).setCellValue(list[2])
            row.createCell(3).setCellValue(list[0])
            amountK += list[1].toDouble()
            amountR += list[2].toDouble()
            rowIdx++
        }
        val summarizeFirst = sheet.createRow(sheet.lastRowNum + 1)
        val summarizeSecond = sheet.createRow(sheet.lastRowNum + 1)

        summarizeFirst.createCell(0).setCellValue("ИТОГ КГ:")
        summarizeFirst.createCell(1).setCellValue(amountK)
        summarizeFirst.getCell(0).cellStyle = headerCellStyle

        summarizeSecond.createCell(0).setCellValue("ИТОГ РУБ:")
        summarizeSecond.createCell(1).setCellValue(amountR)
        summarizeSecond.getCell(0).cellStyle = headerCellStyle

        rowIdx = sheet.lastRowNum + 2
    }
    if (cond) File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Готовая продукция").mkdir()
    else File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Незавершенная продукция").mkdir()

    val fileOut = if (cond) FileOutputStream("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Готовая продукция\\$dataLeft-$dataRight.xlsx")
                  else FileOutputStream("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Незавершенная продукция\\$dataLeft-$dataRight.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    val file = if (cond) File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Готовая продукция\\$dataLeft-$dataRight.xlsx")
            else File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по продукции\\Незавершенная продукция\\$dataLeft-$dataRight.xlsx")
    Desktop.getDesktop().open(file)

}