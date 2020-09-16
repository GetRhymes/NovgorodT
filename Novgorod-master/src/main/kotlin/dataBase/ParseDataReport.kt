package dataBase

import generateReport.DataWorkerPeriod
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.contains
import kotlin.collections.isNullOrEmpty
import kotlin.collections.listOf
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set

fun checkDataForReport(captainId: Int, placeId: Int, reportId: Int): Boolean {
    val dbHandler = DatabaseHandler()
    val needIdCaptain = dbHandler.getCaptainForNeedReport(reportId)
    val needIdPlace = dbHandler.getPlaceForNeedReport(reportId)
    if (captainId == needIdCaptain && placeId == needIdPlace) return true
    return false
}

fun checkTime(timeBegin: String, timeFinished: String): Boolean {
    val hoursBegin = timeBegin.split(":")[0].toInt()
    val hoursFinished = timeFinished.split(":")[0].toInt()
    if (hoursFinished in 19..21 && hoursBegin >= 7) return true
    if ((hoursFinished <= 7 || hoursFinished >= 19)  && (hoursBegin >= 18 || hoursBegin <= 7)) return false
    return true
}

fun getDataReportForNeedDate(date: String, captain: Pair<Int, String>, place: Pair<Int, String>): DataReportRequest {
    val dbHandler = DatabaseHandler()
    val fullDate = dbHandler.getReportForNeedDate(date)
    val needData = DataReportRequest()
    while (fullDate!!.next()) {
        val idReport = fullDate.getInt(1)
        if (checkDataForReport(captain.first, place.first, idReport)) {
            val time = Pair(fullDate.getString(3), fullDate.getString(5))
            needData.workers = dbHandler.getNeedWorkers(idReport)
            needData.productsFinished = dbHandler.getNeedFinishedProducts(idReport, checkTime(time.first, time.second))
            needData.productsUnfinished = dbHandler.getNeedUnfinishedProducts(idReport, checkTime(time.first, time.second))
            needData.captain = captain
            needData.dateBegin = fullDate.getString(2)
            needData.timeBegin = fullDate.getString(3)
            needData.dateFinish = fullDate.getString(4)
            needData.timeFinish = fullDate.getString(5)
            needData.total = fullDate.getString(6)
            needData.salary = fullDate.getString(7)
            needData.place = place
        }
    }
    return needData
} // получаем данные из бд (отчет)

fun dateEntryCheck(currentDate: String, dateLeft: String, dateRight: String): Boolean {
    val currentDateList = currentDate.split("-")
    val leftDateList = dateLeft.split("-")
    val rightDateList = dateRight.split("-")

    val currentDateYear = currentDateList[0].toInt()
    val currentDateMonth = currentDateList[1].toInt()
    val currentDateDay = currentDateList[2].toInt()

    val leftDateYear = leftDateList[0].toInt()
    val leftDateMonth = leftDateList[1].toInt()
    val leftDateDay = leftDateList[2].toInt()

    val rightDateYear = rightDateList[0].toInt()
    val rightDateMonth = rightDateList[0].toInt()
    val rightDateDay = rightDateList[0].toInt()

    if (currentDateYear == leftDateYear && currentDateYear == rightDateYear) {
        if (currentDateMonth == leftDateMonth && currentDateMonth == rightDateMonth)
            return currentDateDay in leftDateDay..rightDateDay
        if (currentDateMonth in leftDateMonth..rightDateMonth) return true
    } else if (currentDateYear in leftDateYear..rightDateYear) return true
    return false
} // проверка входит ли полученная дата в заданный промежуток времени

fun createListProd(listProd: MutableMap<Int, List<String>>, size: Double): Pair<MutableList<List<String>>, Double> {
    var totalPrice = 0.0
    val listProducts = mutableListOf<List<String>>()
    for (prod in listProd) {
        totalPrice += prod.value[2].toDouble() / size
        listProducts.add(prod.value)
    }
    return Pair(listProducts, totalPrice)
} // создается лист продукции для отчета

fun getDataReportForNeedWorker(dateLeft: String, dateRight: String, name: Pair<Int, String>): MutableList<DataWorkerPeriod> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val listDate = mutableListOf<DataWorkerPeriod>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(2), dateLeft, dateRight)) {
            val workers = dbHandler.getNeedWorkers(fullData.getInt(1))
            if (workers.containsKey(name.first)) {
                val time = checkTime(fullData.getString(3), fullData.getString(5))
                val size = workers.size.toDouble()
                val listFinishedProducts = createListProd(dbHandler.getNeedFinishedProducts(fullData.getInt(1), time), size)
                val listUnfinishedProducts = createListProd(dbHandler.getNeedUnfinishedProducts(fullData.getInt(1), time), size)
                val dataWorkerPeriod = DataWorkerPeriod(
                    fullData.getString(2),
                    fullData.getString(3),
                    fullData.getString(5),
                    listFinishedProducts.first,
                        listUnfinishedProducts.first,
                        listFinishedProducts.second,
                        listUnfinishedProducts.second
                )
                listDate.add(dataWorkerPeriod)
            }
        }

    }
    return listDate
} // возвращаем список нужных данных для генерации отчета по сотруднику

fun getDataReportForAmountMoney(dateLeft: String, dateRight: String): MutableMap<Int, Pair<String, Double>> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val dataInPeriod = mutableMapOf<Int, Pair<String, Double>>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(2), dateLeft, dateRight)) {
            val workers = dbHandler.getNeedWorkers(fullData.getInt(1))
            for (worker in workers) {
                if(dataInPeriod.containsKey(worker.key)) dataInPeriod[worker.key] = Pair(worker.value , dataInPeriod[worker.key]!!.second + fullData.getString(7).toDouble())
                else dataInPeriod[worker.key] = Pair(worker.value, fullData.getString(7).toDouble())
            }
        }
    }
    return dataInPeriod
} // возвращаем данные за период по зарплате каждого сотрудника

fun createMapForReportProd(mapOfResult: MutableMap<Int, MutableList<List<String>>>, products: Map<Int, List<String>>, date: String) {
    for (product in products) {
        if (mapOfResult.containsKey(product.key)) mapOfResult[product.key]!!.add(listOf(date, product.value[0], product.value[1], product.value[2]))
        else mapOfResult[product.key] = mutableListOf(listOf(date, product.value[0], product.value[1], product.value[2]))
    }
}

fun getDataForReportProduct(dateLeft: String, dateRight: String, cond: Boolean): MutableMap<Int, MutableList<List<String>>> {
    val dbHandler = DatabaseHandler()
    val fullData = dbHandler.getReport()
    val result = mutableMapOf<Int, MutableList<List<String>>>()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(2), dateLeft, dateRight)) {
            val date = "${fullData.getString(2)} ${fullData.getString(3)}-${fullData.getString(5)}"
            val time = checkTime(fullData.getString(3), fullData.getString(5))
            if (cond) createMapForReportProd(result, dbHandler.getNeedFinishedProducts(fullData.getInt(1), time) , date)
            else createMapForReportProd(result, dbHandler.getNeedUnfinishedProducts(fullData.getInt(1), time), date)
        }
    }
    return result
}

fun getDataForReportCaptain(dateLeft: String, dateRight: String, place: Pair<Int, String>): MutableList<List<Pair<Int, String>>> {
    val dbHandlerForReport = DatabaseHandler()
    val captainDatePlace = mutableListOf<List<Pair<Int, String>>>()
    val fullData = dbHandlerForReport.getReport()
    while (fullData!!.next()) {
        if (dateEntryCheck(fullData.getString(2), dateLeft, dateRight)) {
            val dbHandler = DatabaseHandler()
            if (place.first == 0) {
                val captain = dbHandler.getNeedCaptain(fullData.getInt(1))
                val date = fullData.getString(2)
                val realPlace = dbHandler.getNeedPlace(fullData.getInt(1))
                captainDatePlace.add(listOf(captain, Pair(0, date), realPlace))
            } else {
                if (dbHandler.getNeedPlace(fullData.getInt(1)).first == place.first) {
                    val captain = dbHandler.getNeedCaptain(fullData.getInt(1))
                    val date = fullData.getString(2)
                    captainDatePlace.add(listOf(captain, Pair(0, date), place))
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

fun getDataForReportTime(dateLeft: String, dateRight: String, listNames: MutableList<Pair<Int, String>>, listDays: List<Int>): MutableMap<String, MutableMap<Int, Int>> {
    val mapOfTime = mutableMapOf<String, MutableMap<Int, Int>>()
    val dbHandler = DatabaseHandler()
    val requestReport = dbHandler.getReport()
    while (requestReport!!.next()) {
        if (dateEntryCheck(requestReport.getString(2), dateLeft, dateRight)) {
            val currentDay = requestReport.getString(2).split("-")[2].toInt()
            val workers = dbHandler.getNeedWorkers(requestReport.getInt(1))
            val time = getTime(requestReport.getString(3), requestReport.getString(5))
            for (name in listNames) {
                if (workers.contains(name.first)) {
                    if (mapOfTime[name.second].isNullOrEmpty()) mapOfTime[name.second] = mutableMapOf()
                    if (listDays.contains(currentDay)) {
                        mapOfTime[name.second]!![currentDay] = time
                    }
                }
            }
        }
    }
    return mapOfTime
}

