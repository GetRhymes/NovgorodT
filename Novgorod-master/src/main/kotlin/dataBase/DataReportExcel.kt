package dataBase

data class DataReportExcel(
    var listOfWorkers: MutableList<String>,
    var listFinishedProdK: MutableList<Pair<String, String>>,
    var listUnfinishedProdK: MutableList<Pair<String, String>>,
    var listFinishedProdR: MutableList<Pair<String, String>>,
    var listUnfinishedProdR: MutableList<Pair<String, String>>,
    var captain: String,
    var dateBegin: String,
    var timeBegin: String,
    var dateFinish: String,
    var timeFinish: String,
    var total: String,
    var onePart: String,
    var place: String
) // данные для отчета в эксель