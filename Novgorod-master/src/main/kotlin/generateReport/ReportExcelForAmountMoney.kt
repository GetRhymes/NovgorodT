package generateReport

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun createReportForAmountMoney(dateLeft: String, dateRight: String, nameMoney: MutableMap<String, Double>) {

    val columnsFirstRow = listOf("ФИО", "Зарплата")
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Зарплата $dateLeft-$dateRight")

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

    val zeroRow = sheet.createRow(0)

    zeroRow.createCell(0).setCellValue(columnsFirstRow[0])
    zeroRow.createCell(1).setCellValue(columnsFirstRow[1])
    zeroRow.getCell(0).cellStyle = headerCellStyle
    zeroRow.getCell(1).cellStyle = headerCellStyle

    var rowIdx = 1
    var amountMoney = 0.0
    for (person in nameMoney) {
        val row = sheet.createRow(rowIdx)
        row.createCell(0).setCellValue(person.key)
        row.createCell(1).setCellValue(person.value)
        amountMoney += person.value
        rowIdx++
    }

    val summarize = sheet.createRow(sheet.lastRowNum)
    summarize.createCell(0).setCellValue("ИТОГ")
    summarize.createCell(1).setCellValue(amountMoney)
    summarize.getCell(0).cellStyle = headerCellStyle
    summarize.getCell(1).cellStyle = headerCellStyle

    File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по ЗП\\${LocalDate.now().year}").mkdir()
    val fileOut = FileOutputStream("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по ЗП\\${LocalDate.now().year}\\Зарплата $dateLeft-$dateRight.xlsx")
    workbook.write(fileOut)
    fileOut.close()
    workbook.close()
    Desktop.getDesktop().open(File("C:\\Users\\GetRhymes\\Desktop\\Novgorod\\Novgorod-master\\Reports\\Отчеты по ЗП\\${LocalDate.now().year}\\Зарплата $dateLeft-$dateRight.xlsx"))
}