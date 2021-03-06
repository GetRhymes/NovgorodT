package reportTable

import dataBase.DataReportRequest
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

private var columnsFirstTable = listOf("Общие сведения", "Бригада", "Готовая продукция", "Незавершенная продукция") // колонки первого ряда
private var nameAndTotal = listOf("Наименование", "Значения", "Количество", "Стоимость") // второго ряда
private var generalInformation = listOf("Бригадир", "Дата и время начала", "Дата и время окончания", "Общая сумма", "Сумма на одного сотрудников", "Участок") // общие сведения

fun createReportExcel(data: DataReportRequest) {
    val workbook = XSSFWorkbook() // создаю книгу
    val sheet = workbook.createSheet("${data.captain} ${data.dateFinish}") // создаю лист

    val headerFont = workbook.createFont()
    headerFont.bold = true // шрифт жирный


    val headerCellStyle = workbook.createCellStyle()
    headerCellStyle.setFont(headerFont)
    headerCellStyle.setAlignment(HorizontalAlignment.CENTER)
    headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER)


    val hcStyleS = workbook.createCellStyle()
    hcStyleS.setAlignment(HorizontalAlignment.CENTER)

    sheet.setColumnWidth(0, 10976) // это число соотвествует ширине ячейки в экселе 300 пикселей
    sheet.setColumnWidth(1, 7317)  // 200 пикселей
    sheet.setColumnWidth(2, 10976)
    for (i in 3..8) {
        sheet.setColumnWidth(i, 4390) // 120 пикселей
    }
    val zeroRow = sheet.createRow(0)
    val firstRow = sheet.createRow(1)

    for (i in 0..8) { // первая строка таблицы
        zeroRow.createCell(i)
        zeroRow.getCell(i).cellStyle = headerCellStyle
        if (i == 0) zeroRow.getCell(i).setCellValue(columnsFirstTable[0])
        if (i == 2) zeroRow.getCell(i).setCellValue(columnsFirstTable[1])
        if (i == 3) zeroRow.getCell(i).setCellValue(columnsFirstTable[2])
        if (i == 6) zeroRow.getCell(i).setCellValue(columnsFirstTable[3])
    }
    for (i in 0..8) { // вторая
        firstRow.createCell(i)
        firstRow.getCell(i).cellStyle = headerCellStyle
        if (i == 0 || i == 3 || i == 6) firstRow.getCell(i).setCellValue(nameAndTotal[0])
        if (i == 1) firstRow.getCell(i).setCellValue(nameAndTotal[1])
        if (i == 4 || i == 7) firstRow.getCell(i).setCellValue(nameAndTotal[2])
        if (i == 5 || i == 8) firstRow.getCell(i).setCellValue(nameAndTotal[3])
    }
    // объединение ячеек
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 0, 1))
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 3, 5))
    sheet.addMergedRegion(CellRangeAddress(0, 0 , 6, 8))
    sheet.addMergedRegion(CellRangeAddress(0, 1 , 2, 2))

    var rowIdx = 2
    // заливаю данные которые полученые были в колонках отчета (из дата класса который формируется из данных БД или отчета)
    for (info in generalInformation) {
        val row = sheet.createRow(rowIdx)
        row.createCell(0).setCellValue(info)
        when (rowIdx) {
            2 -> row.createCell(1).setCellValue(data.captain.second)
            3 -> row.createCell(1).setCellValue("${data.dateBegin} ${data.timeBegin}")
            4 -> row.createCell(1).setCellValue("${data.dateFinish} ${data.timeFinish}")
            5 -> row.createCell(1).setCellValue(data.total)
            6 -> row.createCell(1).setCellValue(data.salary)
            7 -> row.createCell(1).setCellValue(data.place.second)
        }
        rowIdx++
    }

    rowIdx = 2
    for (workers in data.workers) {
        if (sheet.lastRowNum + 1 == rowIdx) {
            val row = sheet.createRow(rowIdx)
            row.createCell(2).setCellValue(workers.value)
            row.getCell(2).cellStyle = hcStyleS
        } else {
            val row = sheet.getRow(rowIdx)
            row.createCell(2).setCellValue(workers.value)
            row.getCell(2).cellStyle = hcStyleS
        }
        rowIdx++
    }

    rowIdx = 2
    for (finish in data.productsFinished) {
        if (sheet.lastRowNum + 1 == rowIdx) {
            val row = sheet.createRow(rowIdx)
            row.createCell(3).setCellValue(finish.value[0])
            row.createCell(4).setCellValue(finish.value[1])
            row.createCell(5).setCellValue(finish.value[2])
            row.getCell(4).cellStyle = hcStyleS
            row.getCell(5).cellStyle = hcStyleS
            rowIdx++
        } else {
            val row = sheet.getRow(rowIdx)
            row.createCell(3).setCellValue(finish.value[0])
            row.createCell(4).setCellValue(finish.value[1])
            row.createCell(5).setCellValue(finish.value[2])
            row.getCell(4).cellStyle = hcStyleS
            row.getCell(5).cellStyle = hcStyleS
            rowIdx++
        }
    }
    rowIdx = 2
    for (unFinish in data.productsUnfinished) {
        if (sheet.lastRowNum + 1 == rowIdx) {
            val row = sheet.createRow(rowIdx)
            row.createCell(6).setCellValue(unFinish.value[0])
            row.createCell(7).setCellValue(unFinish.value[1])
            row.createCell(8).setCellValue(unFinish.value[2])
            row.getCell(7).cellStyle = hcStyleS
            row.getCell(8).cellStyle = hcStyleS
            rowIdx++
        } else {
            val row = sheet.getRow(rowIdx)
            row.createCell(6).setCellValue(unFinish.value[0])
            row.createCell(7).setCellValue(unFinish.value[1])
            row.createCell(8).setCellValue(unFinish.value[2])
            row.getCell(7).cellStyle = hcStyleS
            row.getCell(8).cellStyle = hcStyleS
            rowIdx++
        }
    }
    val desk = System.getProperty("user.home") + File.separator + "Desktop"
    File("$desk${File.separator}Отчеты").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по за день").mkdir()
    File("$desk${File.separator}Отчеты${File.separator}Отчеты по за день${File.separator}${LocalDate.now().year}").mkdir()
    val fileOut = FileOutputStream("$desk${File.separator}Отчеты${File.separator}Отчеты по за день${File.separator}${LocalDate.now().year}${File.separator}${data.captain.second} ${data.dateFinish}.xlsx") // создается файл xlsx с именем
    workbook.write(fileOut)
    fileOut.close()
    workbook.close() // закрываются потоки
    Desktop.getDesktop().open(File("$desk${File.separator}Отчеты${File.separator}Отчеты по за день${File.separator}${LocalDate.now().year}${File.separator}${data.captain.second} ${data.dateFinish}.xlsx"))
}
