package dataBase

import generateReport.DataWorkerPeriod
import java.time.LocalDate
import java.time.Month

fun checkDataForReport(captain: String, place: String, captainNew: String, placeNew: String ): Boolean {
    if (captain == captainNew && place == placeNew) return true
    return false
}
fun getDataReportForNeedDate(date: String, captain: String, place: String): DataReportRequest {
    val dbHandler = DatabaseHandler()
    val fullDate = dbHandler.getReport()
    val needData = DataReportRequest(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
    while (fullDate!!.next()) {
        if (fullDate.getString(8) == date && checkDataForReport(captain, place, fullDate.getString(7),  fullDate.getString(14))) {
            needData.workers = fullDate.getString(2)
            needData.productFinishedK = fullDate.getString(3)
            needData.productUnfinishedK = fullDate.getString(4)
            needData.productFinishedR = fullDate.getString(5)
            needData.productUnfinishedR = fullDate.getString(6)
            needData.captain = fullDate.getString(7)
            needData.dateBegin = fullDate.getString(8)
            needData.timeBegin = fullDate.getString(9)
            needData.dateFinish = fullDate.getString(10)
            needData.timeFinish = fullDate.getString(11)
            needData.total = fullDate.getString(12)
            needData.perOne = fullDate.getString(13)
            needData.place = fullDate.getString(14)
            break
        }
    }
    return needData
} // получаем данные из бд (отчет)

fun changeDataForReportExcel(date: String, captain: String, place: String): DataReportExcel {
    val dataReportRequest = getDataReportForNeedDate(date, captain, place)
    return DataReportExcel(
        dataReportRequest.createListForWorkers(),
        dataReportRequest.createListProduct(dataReportRequest.productFinishedK),
        dataReportRequest.createListProduct(dataReportRequest.productUnfinishedK),
        dataReportRequest.createListProduct(dataReportRequest.productFinishedR),
        dataReportRequest.createListProduct(dataReportRequest.productUnfinishedR),
        dataReportRequest.captain,
        dataReportRequest.dateBegin,
        dataReportRequest.timeBegin,
        dataReportRequest.dateFinish,
        dataReportRequest.timeFinish,
        dataReportRequest.total,
        dataReportRequest.perOne,
        dataReportRequest.place
    )
} // преобразуем отчет из бд в класс для отчета в эксель

fun dateEntryCheck(currentDate: String, dateLeft: String, dateRight: String): Boolean {
    val currentDateList = currentDate.split("-")
    val leftDateList = dateLeft.split("-")
    val rightDateList = dateRight.split("-")

    if (currentDateList[0].toInt() == leftDateList[0].toInt() && currentDateList[0].toInt() == rightDateList[0].toInt()) {
        if (currentDateList[1].toInt() == leftDateList[1].toInt() && currentDateList[1].toInt() == rightDateList[1].toInt())
            return currentDateList[2].toInt() in leftDateList[2].toInt()..rightDateList[2].toInt()
        if (currentDateList[1].toInt() in leftDateList[1].toInt()..rightDateList[1].toInt()) return true
    } else if (currentDateList[0].toInt() in leftDateList[0].toInt()..rightDateList[0].toInt()) return true
    return false
} // проверка входит ли полученная дата в заданный промежуток времени

fun contentWorker(name: String, workers: String): Boolean {
    if (workers.contains(name)) return true
    return false
} // содержит ли строка нужного сотрудника

fun createListProd(stringProdKilo: String, stringProdRub: String, size: Double): Pair<MutableList<List<String>>, Double> {
    val listProdKilo = stringProdKilo.split("||")
    val listProdRub = stringProdRub.split("||")
    val resultList = mutableListOf<List<String>>()
    var totalPrice = 0.0
    if (listProdKilo.isNotEmpty()) {
        for (i in listProdKilo.indices) {
            val nameAndTotal = listProdKilo[i].split(", ")
            println(nameAndTotal)
            val nameAndPrice = listProdRub[i].split(", ")
            if (nameAndTotal[0] != "") {
                val name = nameAndTotal[0].replace("(", "")
                val total = nameAndTotal[1].replace(")", "")
                val price = nameAndPrice[1].replace(")", "")
                val currentPrice = price.toDouble() / size
                totalPrice += currentPrice
                resultList.add(listOf(name, (total.toDouble() / size).toString(), currentPrice.toString()))
            }
        }
    }
    return Pair(resultList, totalPrice)
} // создается лист продукции для отчета

fun getSizeWorkers (workers: String): Double {
    return workers.split("||").size.toDouble()
} // получает кол-во сотрудников

fun getDataReportForNeedWorker(dateLeft: String, dateRight: String, name: String): MutableList<DataWorkerPeriod> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val listDate = mutableListOf<DataWorkerPeriod>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(8), dateLeft, dateRight))
            if (contentWorker(name, fullData.getString(2))) {
                val size = getSizeWorkers(fullData.getString(2))
                val finishedPair = createListProd(fullData.getString(3), fullData.getString(5), size)
                val unfinishedPair = createListProd(fullData.getString(4), fullData.getString(6), size)
                val dataWorkerPeriod = DataWorkerPeriod(
                    fullData.getString(8),
                    fullData.getString(9),
                    fullData.getString(11),
                    finishedPair.first,
                    unfinishedPair.first,
                    finishedPair.second.toString(),
                    unfinishedPair.second.toString()
                )
                listDate.add(dataWorkerPeriod)
            }
    }
    return listDate
} // возвращаем список нужных данных для генерации отчета по сотруднику

fun getDataReportForAmountMoney(dateLeft: String, dateRight: String): MutableMap<String, Double> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val dataInPeriod = mutableMapOf<String, Double>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(8), dateLeft, dateRight)) {
            val workers = fullData.getString(2).split("||")
            for (worker in workers) {
                if(dataInPeriod.containsKey(worker)) dataInPeriod[worker] = dataInPeriod[worker]!! + fullData.getString(13).toDouble()
                else dataInPeriod[worker] = fullData.getString(13).toDouble()
            }
        }
    }
    return dataInPeriod
} // возвращаем данные за период по зарплате каждого сотрудника

fun parseDataProduct(productK: String, productR: String): MutableMap<String, Pair<String, String>> {
    val pairProdK = productK.split("||")
    val pairProdR = productR.split("||")
    val nameTotalPrice = mutableMapOf<String, Pair<String, String>>()
    for (pair in pairProdK.indices) {
        val nameAndTotal = pairProdK[pair].split(", ")
        val nameAndPrice = pairProdR[pair].split(", ")
        nameTotalPrice[nameAndTotal[0].replace("(", "")] = Pair(
                nameAndTotal[1].replace(")", ""),
                nameAndPrice[1].replace(")", "")
        )
    }
    return nameTotalPrice
}
fun createMapForReportProd(mapOfResult: MutableMap<String, MutableList<List<String>>>, prodK: String, prodR: String, date: String) {
    //val dataInPeriod = mutableMapOf<String, MutableList<List<String>>>()
    val products = parseDataProduct(prodK, prodR)
    for (product in products) {
        if (mapOfResult.containsKey(product.key)) mapOfResult[product.key]!!.add(listOf(date, product.value.first, product.value.second))
        else mapOfResult[product.key] = mutableListOf(listOf(date, product.value.first, product.value.second))
    }
}
fun getDataForReportProduct(dateLeft: String, dateRight: String, cond: Boolean): MutableMap<String, MutableList<List<String>>> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val result = mutableMapOf<String, MutableList<List<String>>>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(8), dateLeft, dateRight)) {
            val date = "${fullData.getString(8)} ${fullData.getString(9)}-${fullData.getString(11)}"
            if (cond) createMapForReportProd(result, fullData.getString(3), fullData.getString(5), date)
            else createMapForReportProd(result, fullData.getString(4), fullData.getString(6), date)
        }
    }
    return result
}

fun getDataForReportCaptain(dateLeft: String, dateRight: String, place: String): MutableList<List<String>> {
    val dbHandler = DatabaseHandler()
    val captainDatePlace = mutableListOf<List<String>>()
    val fullData = dbHandler.getReport()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(8), dateLeft, dateRight)) {
            if (place == "") {
                val captain = fullData.getString(7)
                val date = fullData.getString(8)
                val realPlace = fullData.getString(14)
                captainDatePlace.add(listOf(captain, date, realPlace))
            } else {
                if (fullData.getString(14) == place) {
                    val captain = fullData.getString(7)
                    val date = fullData.getString(8)
                    captainDatePlace.add(listOf(captain, date, place))
                }
            }
        }
    }
    return captainDatePlace
}
fun getMonth(dateRight: String): Pair<Int, String> {
    val dayMonthYearRight = dateRight.split("-")
    return when (dayMonthYearRight[1].toInt()) {
        1 -> Pair(1, "Январь")
        2 -> Pair(2, "Февраль")
        3 -> Pair(3, "Март")
        4 -> Pair(4, "Апрель")
        5 -> Pair(5, "Май")
        6 -> Pair(6, "Июнь")
        7 -> Pair(7, "Июль")
        8 -> Pair(8, "Август")
        9 -> Pair(9, "Сентябрь")
        10 -> Pair(10, "Октябрь")
        11 -> Pair(11, "Ноябрь")
        else -> Pair(12, "Декабрь")
    }
}

fun getListDays(dateLeft: String, dateRight: String): List<Int> {
    val dayMonthYearLeft = dateLeft.split("-")
    val dayMonthYearRight = dateRight.split("-")
    val listDays = mutableListOf<Int>()
    for (i in dayMonthYearLeft[2].toInt()..dayMonthYearRight[2].toInt()) { listDays.add(i) }
    return listDays
}
fun getTime(timeBegin: String, timeFinished: String): Int {
    val hoursBegin = timeBegin.split(":")
    val hoursFinished = timeFinished.split(":")
    return if (hoursFinished[0].toInt() - hoursBegin[0].toInt() > 0) hoursFinished[0].toInt() - hoursBegin[0].toInt()
           else hoursFinished[0].toInt() - hoursBegin[0].toInt() * -1
}
fun getDataForReportTime(dateLeft: String, dateRight: String, listNames: MutableList<String>, listDays: List<Int>): MutableMap<String, MutableMap<Int, Int>> {
    val mapOfTime = mutableMapOf<String, MutableMap<Int, Int>>()
    val dbHandler = DatabaseHandler()
    val requestReport = dbHandler.getReport()
    println(listDays)
    while (requestReport!!.next()) {
        if (dateEntryCheck(requestReport.getString(8), dateLeft, dateRight)) {
            val currentDay = requestReport.getString(8).split("-")[2].toInt()
            val workers = requestReport.getString(2).split("||")
            val time = getTime(requestReport.getString(9), requestReport.getString(11))
            for (name in listNames) {
                if (workers.contains(name)) {
                    if (mapOfTime[name].isNullOrEmpty()) mapOfTime[name] = mutableMapOf()
                    if (listDays.contains(currentDay)) {
                        mapOfTime[name]!![currentDay] = time
                    }
                }
            }
        }
    }
    return mapOfTime
}

