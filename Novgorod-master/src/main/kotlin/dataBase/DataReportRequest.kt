package dataBase

class DataReportRequest {
    var workers =  mutableMapOf<Int, String>()
    var productsFinished = mutableMapOf<Int, List<String>>()
    var productsUnfinished = mutableMapOf<Int, List<String>>()
    var captain = Pair(0, "")
    var dateBegin = String()
    var timeBegin = String()
    var dateFinish = String()
    var timeFinish = String()
    var total = String()
    var salary = String()
    var place = Pair(0, "")
}

// Класс которые принимает в себя отчет из бд и методы которые преобразуют нужные строки в листы