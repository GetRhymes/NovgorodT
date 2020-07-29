package dataBase

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class DatabaseHandler {
    // методы для связи с бд

    // константы бд
    private val dbHost = "localhost"
    private val dbPort = "3306"
    private val dbUser = "root"
    private val dbPass = "613970"
    private val dbName = "novgorod"

    private fun getDbConnection(): Connection {
        val connect = "jdbc:mysql://$dbHost:$dbPort/$dbName?useUnicode=true&serverTimezone=UTC&useSSL=false"
        return DriverManager.getConnection(connect, dbUser, dbPass)
    } // метод для подключения

    fun signUpUser(lastName: String, firstName: String, fatherName: String, code: String) {
        val insert = "INSERT INTO " + UserConst().table + "(" + UserConst().lastName + "," + UserConst().firstName  + "," + UserConst().fatherName + "," + UserConst().code + ")" + "VALUES(?,?,?,?)"
        val prString = getDbConnection().prepareStatement(insert)
        prString.setString(1, lastName)
        prString.setString(2, firstName)
        prString.setString(3, fatherName)
        prString.setString(4, code)
        prString.executeUpdate()
    } // метод для регистрации сотрудника

    fun addFinishProduct(name: String, priceDay: String, priceNight: String) {
        val insert = "INSERT INTO " + ProductConst().tableFinish + "(" + ProductConst().name + "," + ProductConst().priceDay  + "," + ProductConst().priceNight + ")" + "VALUES(?,?,?)"
        val prString = getDbConnection().prepareStatement(insert)
        prString.setString(1, name)
        prString.setString(2, priceDay)
        prString.setString(3, priceNight)
        prString.executeUpdate()
    } // метод для регистрации продукта

    fun addUnFinishProduct(name: String, priceDay: String, priceNight: String) {
        val insert = "INSERT INTO " + ProductConst().tableUnFinish + "(" + ProductConst().name + "," + ProductConst().priceDay  + "," + ProductConst().priceNight + ")" + "VALUES(?,?,?)"
        val prString = getDbConnection().prepareStatement(insert)
        prString.setString(1, name)
        prString.setString(2, priceDay)
        prString.setString(3, priceNight)
        prString.executeUpdate()
    } // метод для регистрации незавершенного продукта

    fun sendReport(report: DataReportRequest) {
        val insert = "INSERT INTO " + ReportConst().table + "(" +
                ReportConst().workers + "," +
                ReportConst().productFinishedK + "," +
                ReportConst().productUnfinishedK + "," +
                ReportConst().productFinishedR + "," +
                ReportConst().productUnfinishedR + "," +
                ReportConst().captain + "," +
                ReportConst().dateBegin + "," +
                ReportConst().timeBegin + "," +
                ReportConst().dateFinish + "," +
                ReportConst().timeFinish + "," +
                ReportConst().total + "," +
                ReportConst().perOne + "," +
                ReportConst().place + ")" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)"
        val prString = getDbConnection().prepareStatement(insert)
        prString.setString(1, report.workers)
        prString.setString(2, report.productFinishedK)
        prString.setString(3, report.productUnfinishedK)
        prString.setString(4, report.productFinishedR)
        prString.setString(5, report.productUnfinishedR)
        prString.setString(6, report.captain)
        prString.setString(7, report.dateBegin)
        prString.setString(8, report.timeBegin)
        prString.setString(9, report.dateFinish)
        prString.setString(10, report.timeFinish)
        prString.setString(11, report.total)
        prString.setString(12, report.perOne)
        prString.setString(13, report.place)
        prString.executeUpdate()
    } // отправка отчета в бд

    fun addPlace(name: String) {
        val insert = "INSERT INTO " + PlaceConst().table + "(" + ProductConst().name + ")" + "VALUES(?)"
        val prString = getDbConnection().prepareStatement(insert)
        prString.setString(1, name)
        prString.executeUpdate()
    }

    fun getWorker(): ResultSet? {
        val request = "SELECT * FROM " + UserConst().table
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить сотрудников из бд

    fun getFinishProd(): ResultSet? {
        val request = "SELECT * FROM " + ProductConst().tableFinish
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить готовую продукцию

    fun getUnFinishProd(): ResultSet? {
        val request = "SELECT * FROM " + ProductConst().tableUnFinish
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить незавершенную продукцию

    fun getReport():ResultSet? {
        val request = "SELECT * FROM " + ReportConst().table
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить отчет из бд
    fun getAuth():ResultSet? {
        val request = "SELECT * FROM " + "login"
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить пользователей
    fun getPlace():ResultSet? {
        val request = "SELECT * FROM " + PlaceConst().table
        return getDbConnection().prepareStatement(request).executeQuery()
    } // получить участок
}
