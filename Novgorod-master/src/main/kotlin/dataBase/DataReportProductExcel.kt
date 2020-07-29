package dataBase

data class DataReportProductExcel(
        var name: String,
        var mapOfDateToPair: MutableMap<String, Pair<String, String>>
)