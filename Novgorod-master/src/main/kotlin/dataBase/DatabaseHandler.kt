package dataBase

import java.sql.*
import java.sql.Statement.RETURN_GENERATED_KEYS

class DatabaseHandler {
    // методы для связи с бд

    // константы бд
    private val dbHost = "localhost"
    private val dbPort = "5432"
    private val dbUser = "nikitascorp"
    private val dbPass = "613970tt"
    private val dbName = "novgorod"

    private fun getDbConnection(): Connection {
        val connect = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
        return DriverManager.getConnection(connect, dbUser, dbPass)
    } // метод для подключения

    fun signUpUser(lastName: String, firstName: String, fatherName: String, code: String): Int {
        val insert = "INSERT INTO " + UserConst().table + "(" + UserConst().lastName + "," + UserConst().firstName  + "," + UserConst().fatherName + "," + UserConst().code + ")" + "VALUES(?,?,?,?)"
        val connection = getDbConnection()
        val prString = connection.prepareStatement(insert)
        prString.setString(1, lastName)
        prString.setString(2, firstName)
        prString.setString(3, fatherName)
        prString.setInt(4, code.toInt())
        val answer = prString.executeUpdate()
        connection.close()
        return answer
    } // метод для регистрации сотрудника

    fun addFinishProduct(name: String, priceDay: String, priceNight: String): Int {
        val insert = "INSERT INTO " + ProductConst().tableFinish + "(" + ProductConst().name + "," + ProductConst().priceDay  + "," + ProductConst().priceNight + ")" + "VALUES(?,?,?)"
        val connection = getDbConnection()
        val prString = connection.prepareStatement(insert)
        prString.setString(1, name)
        prString.setString(2, priceDay)
        prString.setString(3, priceNight)
        val answer = prString.executeUpdate()
        connection.close()
        return answer
    } // метод для регистрации продукта

    fun addUnFinishProduct(name: String, priceDay: String, priceNight: String): Int {
        val insert = "INSERT INTO " + ProductConst().tableUnFinish + "(" + ProductConst().name + "," + ProductConst().priceDay  + "," + ProductConst().priceNight + ")" + "VALUES(?,?,?)"
        val connection = getDbConnection()
        val prString = connection.prepareStatement(insert)
        prString.setString(1, name)
        prString.setString(2, priceDay)
        prString.setString(3, priceNight)
        val answer = prString.executeUpdate()
        connection.close()
        return answer
    } // метод для регистрации незавершенного продукта

    fun addReport(report: DataReportRequest): Int {
        val insertToReports = "INSERT INTO " + ReportConst().table + "(" +
                ReportConst().dateBegin + "," +
                ReportConst().timeBegin + "," +
                ReportConst().dateFinish + "," +
                ReportConst().timeFinish + "," +
                ReportConst().total + "," +
                ReportConst().salary + ")" + "VALUES(?,?,?,?,?,?)"
        val connection = getDbConnection()
        val prString = connection.prepareStatement(insertToReports, PreparedStatement.RETURN_GENERATED_KEYS)
        prString.setString(1, report.dateBegin)
        prString.setString(2, report.timeBegin)
        prString.setString(3, report.dateFinish)
        prString.setString(4, report.timeFinish)
        prString.setString(5, report.total)
        prString.setString(6, report.salary)
        val answer = prString.executeUpdate()

        val resGeneratedKeys = prString.generatedKeys
        resGeneratedKeys.next()
        val key = resGeneratedKeys.getInt(1)
        connection.close()

        val insertToWorkers = "INSERT INTO reports_to_workers (id_report, id_worker) VALUES(?,?)"
        for (worker in report.workers) {
            val connectionForWorker = getDbConnection()
            val workerString = connectionForWorker.prepareStatement(insertToWorkers)
            workerString.setInt(1, key)
            workerString.setInt(2, worker.key)
            workerString.executeUpdate()
            connectionForWorker.close()
        }
        if (report.productsFinished.isNotEmpty()) {
            val insertToFinishedProducts = "INSERT INTO reports_to_finishedproducts (id_report, id_product, total) VALUES(?,?,?)"
            for (products in report.productsFinished) {
                val connectionForProducts = getDbConnection()
                val finishedProductString = connectionForProducts.prepareStatement(insertToFinishedProducts)
                finishedProductString.setInt(1, key)
                finishedProductString.setInt(2, products.key)
                finishedProductString.setString(3, products.value[1])
                finishedProductString.executeUpdate()
                connectionForProducts.close()
            }
        }
        if (report.productsUnfinished.isNotEmpty()) {
            val insertToUnfinishedProducts = "INSERT INTO reports_to_unfinishedproducts (id_report, id_product, total) VALUES(?,?,?)"
            for (products in report.productsUnfinished) {
                val connectionForProducts = getDbConnection()
                val unfinishedProductString = connectionForProducts.prepareStatement(insertToUnfinishedProducts)
                unfinishedProductString.setInt(1, key)
                unfinishedProductString.setInt(2, products.key)
                unfinishedProductString.setString(3, products.value[1])
                unfinishedProductString.executeUpdate()
                connectionForProducts.close()
            }
        }
        val insertToCaptain = "INSERT INTO reports_to_captain (id_report, id_worker) VALUES(?,?)"
        val connectionForCaptain = getDbConnection()
        val captainString = connectionForCaptain.prepareStatement(insertToCaptain)
        captainString.setInt(1, key)
        captainString.setInt(2, report.captain.first)
        captainString.executeUpdate()
        connectionForCaptain.close()

        val insertToPlace = "INSERT INTO reports_to_place (id_report, id_place) VALUES(?,?)"
        val connectionForPlace = getDbConnection()
        val placeString = connectionForPlace.prepareStatement(insertToPlace)
        placeString.setInt(1, key)
        placeString.setInt(2, report.place.first)
        placeString.executeUpdate()
        connectionForPlace.close()
        return answer
    }// отправка отчета в бд

    fun addPlace(name: String): Int {
        val insert = "INSERT INTO " + PlaceConst().table + "(" + ProductConst().name + ")" + "VALUES(?)"
        val connection = getDbConnection()
        val prString = connection.prepareStatement(insert)
        prString.setString(1, name)
        val answer = prString.executeUpdate()
        connection.close()
        return answer
    }

    fun getWorker(): ResultSet? {
        val request = "SELECT * FROM " + UserConst().table
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить сотрудников из бд

    fun getFinishProd(): ResultSet? {
        val request = "SELECT * FROM " + ProductConst().tableFinish
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить готовую продукцию

    fun getUnFinishProd(): ResultSet? {
        val request = "SELECT * FROM " + ProductConst().tableUnFinish
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить незавершенную продукцию

    fun getReport():ResultSet? {
        val request = "SELECT * FROM " + ReportConst().table
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить отчет из бд

    fun getReportForNeedDate(date: String):ResultSet? {
        val request = "SELECT * FROM reports WHERE datebegin = '$date'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    }

    fun getCaptainForNeedReport(idReport: Int): Int {
        val request = "SELECT * FROM reports_to_captain WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        answer.next()
        return answer.getInt(2)
    }

    fun getPlaceForNeedReport(idReport: Int): Int {
        val request = "SELECT * FROM reports_to_place WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        answer.next()
        return answer.getInt(2)
    }

    fun getNeedWorkers(idReport: Int): MutableMap<Int, String> {
        val mapOfWorkers = mutableMapOf<Int, String>()
        val request = "SELECT * FROM reports_to_workers WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        while (answer.next()) {
            val requestForWorker = "SELECT * FROM workers WHERE id_worker = '${answer.getInt(2)}'"
            val connectionForWorker = getDbConnection()
            val answerForWorker = connectionForWorker.prepareStatement(requestForWorker).executeQuery()
            connectionForWorker.close()
            answerForWorker.next()
            mapOfWorkers[answer.getInt(2)] = "${answerForWorker.getString(2)} ${answerForWorker.getString(3)} ${answerForWorker.getString(4)} # ${answerForWorker.getInt(5)}"
        }
        return mapOfWorkers
    }

    fun getNeedFinishedProducts(idReport: Int, time: Boolean): MutableMap<Int, List<String>> {
        val mapOfFinishedProducts = mutableMapOf<Int, List<String>>()
        val request = "SELECT * FROM reports_to_finishedproducts WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        while (answer.next()) {
            val requestForFP = "SELECT * FROM finishedproducts WHERE id_fp = '${answer.getInt(2)}'"
            val connectionForFP = getDbConnection()
            val answerForFP = connectionForFP.prepareStatement(requestForFP).executeQuery()
            connectionForFP.close()
            answerForFP.next()
            mapOfFinishedProducts[answer.getInt(2)] = listOf(
                    answerForFP.getString(2),
                    answer.getString(3),
                    if (time) (answer.getString(3).toDouble() * answerForFP.getString(3).toDouble()).toString()
                    else (answer.getString(3).toDouble() * answerForFP.getString(4).toDouble()).toString()
            )
        }
        return mapOfFinishedProducts
    }

    fun getNeedUnfinishedProducts(idReport: Int, time: Boolean): MutableMap<Int, List<String>> {
        val mapOfUnfinishedProducts = mutableMapOf<Int, List<String>>()
        val request = "SELECT * FROM reports_to_unfinishedproducts WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        while (answer.next()) {
            val requestForFP = "SELECT * FROM unfinishedproducts WHERE id_unfp = '${answer.getInt(2)}'"
            val connectionForUnfP = getDbConnection()
            val answerForUnfP = connectionForUnfP.prepareStatement(requestForFP).executeQuery()
            connectionForUnfP.close()
            answerForUnfP.next()
            mapOfUnfinishedProducts[answer.getInt(2)] = listOf(
                    answerForUnfP.getString(2),
                    answer.getString(3),
                    if (time) (answer.getString(3).toDouble() * answerForUnfP.getString(3).toDouble()).toString()
                    else (answer.getString(3).toDouble() * answerForUnfP.getString(4).toDouble()).toString()
            )
        }
        return mapOfUnfinishedProducts
    }

    fun getNeedCaptain(idReport: Int): Pair<Int, String> {
        val request = "SELECT * FROM reports_to_captain WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        answer.next()
        val requestForWorkers = "SELECT * FROM workers WHERE id_worker = '${answer.getInt(2)}'"
        val connectionForWorker = getDbConnection()
        val answerForWorker = connectionForWorker.prepareStatement(requestForWorkers).executeQuery()
        connectionForWorker.close()
        answerForWorker.next()
        val id = answer.getInt(2)
        val dataWorker = "${answerForWorker.getString(2)} ${answerForWorker.getString(3)} ${answerForWorker.getString(4)} # ${answerForWorker.getInt(5)}"
        return Pair(id, dataWorker)
    }

    fun getNeedPlace(idReport: Int): Pair<Int, String> {
        val request = "SELECT * FROM reports_to_place WHERE id_report = '$idReport'"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        answer.next()
        val requestForPlace = "SELECT * FROM place WHERE id_place = '${answer.getInt(2)}'"
        val connectionForPlace = getDbConnection()
        val answerForPlace = connectionForPlace.prepareStatement(requestForPlace).executeQuery()
        connectionForPlace.close()
        answerForPlace.next()
        val id = answer.getInt(2)
        val placeName = answerForPlace.getString(2)
        return Pair(id, placeName)
    }

    fun getAuth():ResultSet? {
        val request = "SELECT * FROM " + "login"
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить пользователей

    fun getPlace():ResultSet? {
        val request = "SELECT * FROM " + PlaceConst().table
        val connection = getDbConnection()
        val answer = connection.prepareStatement(request).executeQuery()
        connection.close()
        return answer
    } // получить участок
}
