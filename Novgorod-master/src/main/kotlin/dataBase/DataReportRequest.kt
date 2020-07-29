package dataBase

class DataReportRequest(
    var workers: String,
    var productFinishedK: String,
    var productUnfinishedK: String,
    var productFinishedR: String,
    var productUnfinishedR: String,
    var captain: String,
    var dateBegin: String,
    var timeBegin: String,
    var dateFinish: String,
    var timeFinish: String,
    var total: String,
    var perOne: String,
    var place: String
) {

    fun createListForWorkers (): MutableList<String> {
        return workers.split("||").toMutableList()
    }

    fun createListProduct(product: String): MutableList<Pair<String, String>> {
        val listProdK = product.split("||")
        val resultList = mutableListOf<Pair<String, String>>()
        if (product.isEmpty())  return resultList
        else for (pair in listProdK) {
                val nameAndTotal = pair.split(", ")
                println(listProdK)
                println(nameAndTotal)
                resultList.add(Pair(nameAndTotal[0].replace("(", ""), nameAndTotal[1].replace(")", "")))
            }
        return resultList
    }
}

// Класс которые принимает в себя отчет из бд и методы которые преобразуют нужные строки в листы