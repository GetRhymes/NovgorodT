package generateReport

data class DataWorkerPeriod( // дата класс для облегчения читаемости кода который хранит информацию для генератор экселя по сотруднику за период
    var date: String,
    var timeBegin: String,
    var timeFinish: String,
    var listFinishedProd: MutableList<List<String>>,
    var listUnfinishedProd: MutableList<List<String>>,
    var totalFinish: Double,
    var totalUnfinished: Double
)