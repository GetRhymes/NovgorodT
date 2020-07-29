package reportTable

import dataBase.DatabaseHandler
import generalStyle.setSizeForLabel
import generalStyle.setSizeForTextField
import generalStyle.setStyleForLabel
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.*
// метод для формирования 4 колонки таблицы отчета (сотрудники или готовая прод или незавершенная и там ассортимент)
fun addEnumerationOfWorkers(parentToScroll: VBox, anchorPane: AnchorPane, label: String, listOfName: MutableList<String>, boxOfItem: VBox, boxOfItemRub: VBox, cond: Boolean, mapOfAmount: MutableMap<String, Double>, textFieldFirst: TextField, textFieldSecond: TextField, captainField: TextField, hoursBegin: TextField, hoursFinished: TextField) {
    val lb = Label(label)
    setSizeForLabel(300.0, 30.0, lb)
    lb.addEventFilter(MouseEvent.MOUSE_CLICKED) {
        searchField(
            parentToScroll,
            anchorPane,
            listOfName,
            boxOfItem,
            boxOfItemRub,
            cond,
            mapOfAmount,
            textFieldFirst,
            textFieldSecond,
            captainField,
            hoursBegin,
            hoursFinished
        )
    }
    setStyleForLabel(lb, "#e0e0e0") // бежево-серый
    val currentList = filterTrueItem(boxOfItem, cond)
    val list = VBox()
    for (i in listOfName) {
        val radioAndLb = createRadioButton(
            i,
            boxOfItem,
            boxOfItemRub,
            cond,
            mapOfAmount,
            textFieldFirst,
            textFieldSecond,
            captainField,
            hoursBegin,
            hoursFinished
        )
        if (checkCurrentCondition(currentList, i)) radioAndLb.second.isSelected = true
        val hBox = HBox()
        hBox.add(radioAndLb.second)
        hBox.add(radioAndLb.first)
        list.add(hBox)
    }
    parentToScroll.add(lb)
    anchorPane.add(list)
}
// создаю бокс и добавляю в него сотрудников из бд
fun addWorkersInColumn(boxOfItems: VBox, name: String, captainField: TextField) {
    val rb = RadioButton(name)
    rb.addEventFilter(MouseEvent.MOUSE_CLICKED) { if (rb.isSelected) captainField.text = name }
    rb.addEventFilter(KeyEvent.KEY_RELEASED) { if (rb.isSelected) captainField.text = name }
    toggleGroup.toggles += rb
    boxOfItems.add(rb)
}
// создаю бокс и добавляю готовую продукцию
fun addProductsInColumn(boxOfProducts: VBox, boxOfProductsInRub: VBox, name: String, mapOfAmount: MutableMap<String, Double>, textFieldFirst: TextField, textFieldSecond: TextField, hoursBegin: TextField, hoursFinished: TextField) {
    val dbHandler = DatabaseHandler()
    val listOfFinishedProd = mutableMapOf<String, Pair<String, String>>()
    val listOfUnfinishedProd = mutableMapOf<String, Pair<String, String>>()
    val requestFinishedProd = dbHandler.getFinishProd()
    val requestUnfinishedProd = dbHandler.getUnFinishProd()
    while (requestFinishedProd!!.next()) {
        listOfFinishedProd[requestFinishedProd.getString(2)] = Pair(requestFinishedProd.getString(3), requestFinishedProd.getString(4))
    }
    while (requestUnfinishedProd!!.next()) {
        listOfUnfinishedProd[requestUnfinishedProd.getString(2)] = Pair(requestUnfinishedProd.getString(3), requestUnfinishedProd.getString(4))
    }

    mapOfAmount[name] = 0.0
    val hBox = HBox()
    hBox.prefWidth = 225.0
    hBox.prefHeight = hBox.minHeight
    hBox.translateX = 15.0

    val vBoxForName = VBox()
    vBoxForName.prefWidth = 145.0

    val vBoxForNumber = VBox()
    vBoxForNumber.prefWidth = 75.0

    val label = Label(name)
    label.alignment = Pos.CENTER_LEFT
    setSizeForLabel(145.0, 25.0, label)

    val vBoxForNumberSecond = VBox()
    vBoxForNumberSecond.prefWidth = 75.0

    val textFSecond = TextField()
    textFSecond.isEditable = false
    setSizeForTextField(75.0, 25.0, textFSecond)

    val textField = TextField()
    textField.promptText = "Кол-во"
    setSizeForTextField(75.0, 25.0, textField)
    textField.addEventHandler(KeyEvent.KEY_RELEASED) {
        if (textField.text == "") {
            mapOfAmount[name] = 0.0
            textFSecond.text = "0"
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
        else {
            mapOfAmount[name] = textField.text.toDouble()
            if (listOfFinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.second.toDouble()).toString()
            }
            if (listOfUnfinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
            }
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
    }
    hoursBegin.addEventHandler(KeyEvent.KEY_RELEASED) {
        if (textField.text == "") {
            mapOfAmount[name] = 0.0
            textFSecond.text = "0"
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
        else {
            mapOfAmount[name] = textField.text.toDouble()
            if (listOfFinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.second.toDouble()).toString()
            }
            if (listOfUnfinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
            }
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
    }
    hoursFinished.addEventHandler(KeyEvent.KEY_RELEASED) {
        if (textField.text == "") {
            mapOfAmount[name] = 0.0
            textFSecond.text = "0"
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
        else {
            mapOfAmount[name] = textField.text.toDouble()
            if (listOfFinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfFinishedProd[name]!!.second.toDouble()).toString()
            }
            if (listOfUnfinishedProd.containsKey(name)) {
                if (checkTimeEntry(hoursBegin, hoursFinished)!!) textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
                else textFSecond.text = (mapOfAmount[name]!! * listOfUnfinishedProd[name]!!.first.toDouble()).toString()
            }
            changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
        }
    }
    textField.filterInput {
        it.controlNewText.matches(Regex("""\d+"""))  || it.controlNewText.matches(Regex("""\d+\.""")) || it.controlNewText.matches(Regex("""\d+\.\d+"""))
    }

    val labelForSize = Label("кг.")
    labelForSize.alignment = Pos.CENTER_LEFT
    setSizeForLabel(20.0, 25.0, labelForSize)

    vBoxForName.add(label)
    vBoxForNumber.add(textField)

    hBox.add(vBoxForName)
    hBox.add(vBoxForNumber)
    hBox.add(labelForSize)

    boxOfProducts.add(hBox)

    val hBoxSecond = HBox()
    hBoxSecond.prefWidth = 250.0
    hBoxSecond.prefHeight = hBox.minHeight
    hBoxSecond.translateX = 15.0

    val vBoxForNameSecond = VBox()
    vBoxForNameSecond.prefWidth = 150.0

    val labelSecond = Label(name)
    labelSecond.alignment = Pos.CENTER_LEFT
    setSizeForLabel(150.0, 25.0, labelSecond)

    vBoxForNameSecond.add(labelSecond)

    vBoxForNumberSecond.add(textFSecond)
    vBoxForNumberSecond.translateX = 25.0

    val labelForSizeSecond = Label("руб.")
    labelForSizeSecond.translateX = 25.0
    labelForSizeSecond.alignment = Pos.CENTER_LEFT
    setSizeForLabel(70.0, 25.0, labelForSizeSecond)

    hBoxSecond.add(vBoxForNameSecond)
    hBoxSecond.add(vBoxForNumberSecond)
    hBoxSecond.add(labelForSizeSecond)
    boxOfProductsInRub.add(hBoxSecond)
}
// парсер для удаления сотрудников из колонки или продукта
fun parse(str: String): String {

    val parts = str.split("]")
    return parts[1].replace("\'", "")
}
// парсер для удаления сотрудников из колонки или продукта
fun parse2(str: String): String {
    val parts = str.split(",")
    return parse(parts[0])
}
// удаление сотрудника из колонки
fun deleteWorkersInColumn(boxOfWorkers: VBox, name: String) {
    boxOfWorkers.children.removeIf {
        parse(it.toString()) == name
    }
    toggleGroup.toggles.removeIf { parse(it.toString()) == name }
}
// удаление продукта из колонки
fun deleteProductInColumn(boxOfProduct: VBox, boxOfProductsInRub: VBox, name: String, mapOfAmount: MutableMap<String, Double>) {
    mapOfAmount.remove(name)
    boxOfProduct.children.removeIf {
        val parts = it.getChildList()
        parse2(parts!![0].getChildList().toString()) == name
    }
    boxOfProductsInRub.children.removeIf {
        val parts = it.getChildList()
        parse2(parts!![0].getChildList().toString()) == name
    }

}
// добавляет событие при нажатии на кнопку чтоб стиль сохранялся
fun addEventForLabel(label: Label) {
    label.addEventFilter(MouseEvent.MOUSE_PRESSED) {
        label.style {
            backgroundColor = multi(Paint.valueOf("#bdbdbd"))
            borderColor += box(
                top = Color.TRANSPARENT,
                right = Color.TRANSPARENT,
                left = Color.TRANSPARENT,
                bottom = Color.BLACK
            )
        }
    }
    label.addEventFilter(MouseEvent.MOUSE_ENTERED) {
        label.style {
            backgroundColor = multi(Paint.valueOf("#eeeeee"))
            borderColor += box(
                top = Color.TRANSPARENT,
                right = Color.TRANSPARENT,
                left = Color.TRANSPARENT,
                bottom = Color.BLACK
            )
        }
    }
    label.addEventFilter(MouseEvent.MOUSE_EXITED) {
        label.style {
            backgroundColor = multi(Paint.valueOf("#e0e0e0"))
            borderColor += box(
                top = Color.TRANSPARENT,
                right = Color.TRANSPARENT,
                left = Color.TRANSPARENT,
                bottom = Color.BLACK
            )
        }
    }
    label.addEventFilter(MouseEvent.MOUSE_RELEASED) {
        label.style {
            backgroundColor = multi(Paint.valueOf("#eeeeee"))
            borderColor += box(
                top = Color.TRANSPARENT,
                right = Color.TRANSPARENT,
                left = Color.TRANSPARENT,
                bottom = Color.BLACK
            )
        }
    }
}
// проверяет те поля из 4 колонки что были выбраны заранее
fun filterTrueItem(boxOfItem: VBox, cond: Boolean): MutableList<String> {
    val listOfTrueItem = mutableListOf<String>()
    if (boxOfItem.children.size > 0) {
        if (cond) {
            boxOfItem.children.forEach { listOfTrueItem.add(parse(it.toString())) }
        } else {
            boxOfItem.children.forEach {
                val parts = it.getChildList()
                listOfTrueItem.add(parse2(parts!![0].getChildList().toString())) }
        }
    }

    return listOfTrueItem
}
// возвращает тру или фолс на тех полях которые были выбраны
fun checkCurrentCondition(listOfTrueItem: MutableList<String>, name: String): Boolean {
    listOfTrueItem.forEach { if(it == name) return true }
    return false
}
// подсчитывает стоимость продукта исходя из кол-ва кг
fun calculateSummarize(mapOfAmountForFinished: MutableMap<String, Double>, mapOfAmountForUnfinished: MutableMap<String, Double>, hoursBegin: TextField, hoursFinished: TextField): List<Double> {
    val listOfFinishedProd = mutableMapOf<String, Pair<String, String>>()
    val listOfUnfinishedProd = mutableMapOf<String, Pair<String, String>>()
    val dbHandler = DatabaseHandler()
    val requestFinishedProd = dbHandler.getFinishProd()
    val requestUnfinishedProd = dbHandler.getUnFinishProd()
    while (requestFinishedProd!!.next()) {
        listOfFinishedProd[requestFinishedProd.getString(2)] = Pair(requestFinishedProd.getString(3), requestFinishedProd.getString(4))
    }
    while (requestUnfinishedProd!!.next()) {
        listOfUnfinishedProd[requestUnfinishedProd.getString(2)] = Pair(requestUnfinishedProd.getString(3), requestUnfinishedProd.getString(4))
    }
    var firstSum = 0.0
    var secondSum = 0.0
    if (mapOfAmountForFinished.isNotEmpty()) {
        if (checkTimeEntry(hoursBegin, hoursFinished)!!)
            mapOfAmountForFinished.forEach { firstSum += it.value * listOfFinishedProd[it.key]!!.first.toDouble() }
        else mapOfAmountForFinished.forEach { firstSum += it.value * listOfFinishedProd[it.key]!!.second.toDouble() }
    }
    if (mapOfAmountForUnfinished.isNotEmpty()) {
        if (checkTimeEntry(hoursBegin, hoursFinished)!!)
            mapOfAmountForUnfinished.forEach { secondSum += it.value * listOfUnfinishedProd[it.key]!!.first.toDouble() }
        else mapOfAmountForUnfinished.forEach { secondSum += it.value * listOfUnfinishedProd[it.key]!!.second.toDouble() }
    }
    return listOf(firstSum, secondSum, firstSum + secondSum)
}
// меняет значение стоимости при изменении параметров
fun changeDataInSummarize(textFieldFirst: TextField, textFieldSecond: TextField, hoursBegin: TextField, hoursFinished: TextField) {
    val result = calculateSummarize(
        mapOfAmountForFinished,
        mapOfAmountForUnfinished,
        hoursBegin,
        hoursFinished
    )
    textFieldFirst.text = result[2].toString()
    if (boxOfWorkers.children.size == 0) textFieldSecond.text = "0"
    else textFieldSecond.text = (result[2] / boxOfWorkers.children.size).toString()
}
// создает радиокнопки для выбора полей (сотрудники продукт)
fun createRadioButton(name: String, boxOfItem: VBox, boxOfItemRub: VBox, cond: Boolean, mapOfAmount: MutableMap<String, Double>, textFieldFirst: TextField, textFieldSecond: TextField, captainField: TextField, hoursBegin: TextField, hoursFinished: TextField): Pair<Label, RadioButton> {
    val newLb = Label(name)
    newLb.alignment = Pos.CENTER

    val radioButton = RadioButton()
    radioButton.addEventFilter(MouseEvent.MOUSE_CLICKED) {
        if (radioButton.isSelected)  {
            if (cond) {
                addWorkersInColumn(boxOfItem, name, captainField)
                changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
            }
            else {
                addProductsInColumn(
                    boxOfItem,
                    boxOfItemRub,
                    name,
                    mapOfAmount,
                    textFieldFirst,
                    textFieldSecond,
                    hoursBegin,
                    hoursFinished
                )
                changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
            }
        }
        if (!radioButton.isSelected) {
            if (cond) {
                deleteWorkersInColumn(boxOfItem, name)
                changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)

            }
            else {
                deleteProductInColumn(boxOfItem, boxOfItemRub, name, mapOfAmount)
                changeDataInSummarize(textFieldFirst, textFieldSecond, hoursBegin, hoursFinished)
            }
        }

    }
    return Pair(newLb, radioButton)
}
// метод для создания текстфилда благодаря которому можно искать по названию поля из 4 колонки (сотрудник продукт)
fun searchField(parentToScroll: VBox, anchorPane: AnchorPane, listOfName: MutableList<String>, boxOfItem: VBox, boxOfItemRub: VBox, cond: Boolean, mapOfAmount: MutableMap<String, Double>, textFieldFirst: TextField, textFieldSecond: TextField, captainField: TextField, hoursBegin: TextField, hoursFinished: TextField) {
    val items = VBox()
    val searchField = TextField()
    var currentList = filterTrueItem(boxOfItem, cond)
    setSizeForTextField(290.0, 25.0, searchField)
    searchField.translateX = 5.0
    searchField.promptText = "Введите наименование"
    searchField.addEventHandler(KeyEvent.KEY_RELEASED) {
        currentList = filterTrueItem(boxOfItem, cond)
        val newBoxForItem = VBox()
        anchorPane.children.removeAt(0)
        for (i in listOfName)  {
            val radioAndLb = createRadioButton(
                i,
                boxOfItem,
                boxOfItemRub,
                cond,
                mapOfAmount,
                textFieldFirst,
                textFieldSecond,
                captainField,
                hoursBegin,
                hoursFinished
            )
            if (checkCurrentCondition(currentList, i)) radioAndLb.second.isSelected = true
            if (i.toLowerCase().contains(searchField.text.toLowerCase())) {
                val hBox = HBox()
                hBox.add(radioAndLb.second)
                hBox.add(radioAndLb.first)
                newBoxForItem.add(hBox)
            }
        }
        anchorPane.add(newBoxForItem)
    }

    for (i in listOfName) {
        val radioAndLb = createRadioButton(
            i,
            boxOfItem,
            boxOfItemRub,
            cond,
            mapOfAmount,
            textFieldFirst,
            textFieldSecond,
            captainField,
            hoursBegin,
            hoursFinished
        )
        if (checkCurrentCondition(currentList, i)) radioAndLb.second.isSelected = true
        val hBox = HBox()
        hBox.add(radioAndLb.second)
        hBox.add(radioAndLb.first)
        items.add(hBox)
    }
    if (parentToScroll.children.size == 1) {
        anchorPane.children.removeAt(0)
        parentToScroll.add(searchField)
        anchorPane.add(items)

    }
    if (parentToScroll.children.size == 2)
    parentToScroll.children.removeAt(1)
    anchorPane.children.removeAt(0)
    parentToScroll.add(searchField)
    anchorPane.add(items)

}
// считываю инфомрацию для отправки бд по сотрудникам
fun readDataForWorkers(): MutableList<String> {
    val listOfWorkers = mutableListOf<String>()
    for (workers in boxOfWorkers.children) {
        listOfWorkers.add(parse(workers.toString()))
    }
    return listOfWorkers
}
// инф для отправки бд по продуктам в кг
fun readDataForProdK(): Pair<MutableList<Pair<String, String>>, MutableList<Pair<String, String>>> {
    val listOfFinishProdK = mutableListOf<Pair<String, String>>()
    val listOfUnfinishedProdK = mutableListOf<Pair<String, String>>()
    mapOfAmountForFinished.forEach { listOfFinishProdK.add(Pair(it.key, it.value.toString())) }
    mapOfAmountForUnfinished.forEach { listOfUnfinishedProdK.add(Pair(it.key, it.value.toString())) }
    return Pair(listOfFinishProdK, listOfUnfinishedProdK)
}
// инф для отпраки бд по продуктам в руб
fun readDataForProdR(hoursBegin: TextField, hoursFinished: TextField): Pair<MutableList<Pair<String, String>>, MutableList<Pair<String, String>>> {
    val dbHandler = DatabaseHandler()
    val listOfFinishedProd = mutableMapOf<String, Pair<String, String>>()
    val listOfUnfinishedProd = mutableMapOf<String, Pair<String, String>>()
    val requestFinishedProd = dbHandler.getFinishProd()
    val requestUnfinishedProd = dbHandler.getUnFinishProd()
    while (requestFinishedProd!!.next()) {
        listOfFinishedProd[requestFinishedProd.getString(2)] = Pair(requestFinishedProd.getString(3), requestFinishedProd.getString(4))
    }
    while (requestUnfinishedProd!!.next()) {
        listOfUnfinishedProd[requestUnfinishedProd.getString(2)] = Pair(requestUnfinishedProd.getString(3), requestUnfinishedProd.getString(4))
    }
    val listOfFinishProdR = mutableListOf<Pair<String, String>>()
    val listOfUnfinishedProdR = mutableListOf<Pair<String, String>>()
    if (checkTimeEntry(hoursBegin, hoursFinished)!!) {
        mapOfAmountForFinished.forEach {
            listOfFinishProdR.add(Pair(it.key, (it.value * listOfFinishedProd[it.key]!!.first.toDouble()).toString()))
        }
        mapOfAmountForUnfinished.forEach {
            listOfUnfinishedProdR.add(Pair(it.key, (it.value * listOfUnfinishedProd[it.key]!!.first.toDouble()).toString()))
        }
    }
    else {
        mapOfAmountForFinished.forEach {
            listOfFinishProdR.add(Pair(it.key, (it.value * listOfFinishedProd[it.key]!!.second.toDouble()).toString()))
        }
        mapOfAmountForUnfinished.forEach {
            listOfUnfinishedProdR.add(Pair(it.key, (it.value * listOfUnfinishedProd[it.key]!!.second.toDouble()).toString()))
        }
    }
    return Pair(listOfFinishProdR, listOfUnfinishedProdR)
}
// проверка времени на день ночь 19*60 = 1140 часы в минутах
fun checkTimeEntry(hoursBegin: TextField, hoursFinished: TextField): Boolean? {
    if (hoursBegin.text != "" && hoursFinished.text != "") {
        if (hoursFinished.text.toInt() in 19..21 && hoursBegin.text.toInt() >= 7) return true

        if ((hoursFinished.text.toInt() <= 7 || hoursFinished.text.toInt() >= 19)  && (hoursBegin.text.toInt() >= 18 || hoursBegin.text.toInt() <= 7)) return false
    } else return true
    return true
}
// добавляю или изменяю участок
fun addPlace(anchorPane: AnchorPane, placeField: TextField, parentToScroll: VBox) {
    if (anchorPane.children.size != 0) anchorPane.children.remove(0, 1)
    parentToScroll.children.remove(0, 1)
    if (parentToScroll.children.isNotEmpty()) parentToScroll.children.remove(0, 1)
    val dbHandler = DatabaseHandler()
    val listOfPlace = mutableListOf<String>()
    val placeRequest = dbHandler.getPlace()
    while (placeRequest!!.next()) {
        listOfPlace.add(placeRequest.getString(2))
    }
    val toggleGroupOfPlace = ToggleGroup()
    val vBox = VBox()
    for (place in listOfPlace) {
        val radioButton = RadioButton(place)
        toggleGroupOfPlace.toggles.add(radioButton)
        radioButton.addEventFilter(MouseEvent.MOUSE_CLICKED) { if (radioButton.isSelected) placeField.text = radioButton.text }
        vBox.add(radioButton)
    }
    val label = Label("Участок")
    setStyleForLabel(label, "#e0e0e0")
    setSizeForLabel(300.0, 30.0, label)
    anchorPane.add(vBox)
    parentToScroll.add(label)
}


