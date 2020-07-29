package reportTable

import dataBase.DataReportExcel
import dataBase.DataReportRequest
import dataBase.DatabaseHandler
import generalStyle.*
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import tornadofx.*
import java.time.LocalDate
import java.time.LocalTime


val mapOfAmountForFinished = mutableMapOf<String, Double>() // имя и кол-во продукта
val mapOfAmountForUnfinished = mutableMapOf<String, Double>() // имя и кол-во незаверш продукта
var boxOfWorkers: VBox by singleAssign()
var toggleGroup = ToggleGroup() // группа для кнопок, чтобы можно было выбрать лишь одну из нескольких
// Окно отчета
class WorkTable : View("Отчетность") {
    override val root = borderpane {
        //ANCHOR_PANE
        var callListOfColumn: AnchorPane by singleAssign()
        var indentionFirst: AnchorPane by singleAssign()
        var indentionSecond: AnchorPane by singleAssign()
        var indentionThird: AnchorPane by singleAssign()
        var paddingForSend: AnchorPane by singleAssign()
        // LABELS
        var workers: Label by singleAssign()
        var finishedProducts: Label by singleAssign()
        var unfinishedProducts: Label by singleAssign()
        var summarizeLabel: Label by singleAssign()
        var reportLabel: Label by singleAssign()
        var separatorForTimeBegin: Label by singleAssign()
        var separatorForTimeFinished: Label by singleAssign()
        var dateOfFinished: Label by singleAssign()
        var dateOfBegin: Label by singleAssign()
        var fullSumLabel: Label by singleAssign()
        var amountPerWorkerLabel: Label by singleAssign()
        var generalInformation: Label by singleAssign()
        var lbFinishedProdInRub: Label by singleAssign()
        var lbUnfinishedProdInRub: Label by singleAssign()
        var captainLabel: Label by singleAssign()
        var placeLabel: Label by singleAssign()
        //V_BOX
        var boxOfFinishedProduct: VBox by singleAssign()
        var boxOfUnfinishedProduct: VBox by singleAssign()
        var finishedProdInRub: VBox by singleAssign()
        var unfinishedProdInRub: VBox by singleAssign()
        var summarizeFirstColumn: VBox by singleAssign()
        var parentToScroll: VBox by singleAssign()
        //DATA_PICKER
        var dateOfBeginInput: DatePicker by singleAssign()
        var dateOfFinishedInput: DatePicker by singleAssign()
        //TEXT_FIELD
        var fullSumInput: TextField by singleAssign()
        var amountPerWorkerInput: TextField by singleAssign()
        var hoursForBegin: TextField by singleAssign()
        var minutesForBegin: TextField by singleAssign()
        var hoursForFinished: TextField by singleAssign()
        var minutesForFinished: TextField by singleAssign()
        var captainField: TextField by singleAssign()
        var placeField: TextField by singleAssign()
        //SCROLL_PANE
        var scrollForWorkers: ScrollPane by singleAssign()
        var scrollForFinishedProd: ScrollPane by singleAssign()
        var scrollForUnfinishedProd: ScrollPane by singleAssign()
        var scrollForFinishedProdInRub: ScrollPane by singleAssign()
        var scrollForUnfinishedProdInRub: ScrollPane by singleAssign()
        var scrollForCallList: ScrollPane by singleAssign()
        //BUTTON
        var send: Button by singleAssign()
        var addPlace: Button by singleAssign()
        //DATA BASE
        val dbHandler = DatabaseHandler()
        val requestName = dbHandler.getWorker()
        val requestFinishedProd = dbHandler.getFinishProd()
        val requestUnfinishedProd = dbHandler.getUnFinishProd()
        val names = mutableListOf<String>()
        val finishedProd = mutableListOf<String>()
        val unfinishedProd = mutableListOf<String>()

        while (requestName!!.next()) { // из бд заполняю сотрудников
            val name = "${requestName.getString(2)} ${requestName.getString(3)} ${requestName.getString(4)} # ${requestName.getString(5)}"
            names.add(name)
        }
        while (requestFinishedProd!!.next()) { // из бд заполняю продукт
            val prodName = requestFinishedProd.getString(2)
            finishedProd.add(prodName)
        }
        while (requestUnfinishedProd!!.next()) { // из бд заполняю незавершенный продукт
            val prodName = requestUnfinishedProd.getString(2)
            unfinishedProd.add(prodName)
        }
        center {
            anchorpane {
                setPrefSize(1200.0, 755.0) // устанавливаю размеры окна  (по числа аналогично как и в LogIn классе)
                setMaxSize(1200.0, 755.0)
                setMinSize(1200.0, 755.0)

                vbox {
                    reportLabel = label("ОТЧЕТ") {
                        alignment = Pos.TOP_CENTER
                        style {
                            fontSize = 18.px
                            borderColor = multi(
                                box(
                                    bottom = Paint.valueOf("000000"), // черный
                                    top = Paint.valueOf("000000"),
                                    left = Paint.valueOf("FFFFFF"), // белый
                                    right = Paint.valueOf("FFFFFF")
                                )
                            )
                        }
                    }
                    hbox {
                        setPrefSize(1200.0, 355.0) // устанавливаю размер внутри окна для бокса (по числа аналогично как и в LogIn классе)
                        setMinSize(1200.0, 355.0)
                        setMaxSize(1200.0, 355.0)
                        vbox {
                            hbox {
                                workers = label("Бригада") {
                                    addEventFilter(MouseEvent.MOUSE_CLICKED) { // слушателей который отслеживает клик и в случае нажатия запускает метод
                                        callListOfColumn.children.removeAt(0)
                                        if (parentToScroll.children.size == 1) parentToScroll.children.remove(0, 1)
                                        else parentToScroll.children.remove(0, 2)
                                        addEnumerationOfWorkers(
                                            parentToScroll,
                                            callListOfColumn,
                                            "Сотрудники",
                                            names,
                                            boxOfWorkers,
                                            finishedProdInRub,
                                            true,
                                            mapOfAmountForFinished,
                                            fullSumInput,
                                            amountPerWorkerInput,
                                            captainField,
                                            hoursForBegin,
                                            hoursForFinished
                                        )
                                    }
                                }
                                addEventForLabel(workers)

                                finishedProducts = label("Готовая продукция") {

                                    addEventFilter(MouseEvent.MOUSE_CLICKED) { // слушателей который отслеживает клик и в случае нажатия запускает метод
                                        callListOfColumn.children.removeAt(0)
                                        if (parentToScroll.children.size == 1) parentToScroll.children.remove(0, 1)
                                        else parentToScroll.children.remove(0, 2)
                                        addEnumerationOfWorkers(
                                            parentToScroll,
                                            callListOfColumn,
                                            "Готовая продукция",
                                            finishedProd,
                                            boxOfFinishedProduct,
                                            finishedProdInRub,
                                            false,
                                            mapOfAmountForFinished,
                                            fullSumInput,
                                            amountPerWorkerInput,
                                            captainField,
                                            hoursForBegin,
                                            hoursForFinished
                                        )
                                    }
                                }
                                addEventForLabel(finishedProducts)

                                unfinishedProducts = label("Незавершенная продукция") { // слушателей который отслеживает клик и в случае нажатия запускает метод
                                    addEventFilter(MouseEvent.MOUSE_CLICKED) {
                                        callListOfColumn.children.removeAt(0)
                                        if (parentToScroll.children.size == 1) parentToScroll.children.remove(0, 1)
                                        else parentToScroll.children.remove(0, 2)
                                        addEnumerationOfWorkers(
                                            parentToScroll,
                                            callListOfColumn,
                                            "Незавершенная продукция",
                                            unfinishedProd,
                                            boxOfUnfinishedProduct,
                                            unfinishedProdInRub,
                                            false,
                                            mapOfAmountForUnfinished,
                                            fullSumInput,
                                            amountPerWorkerInput,
                                            captainField,
                                            hoursForBegin,
                                            hoursForFinished
                                        )
                                    }
                                }
                                addEventForLabel(unfinishedProducts)
                            }

                            hbox { // бокс для скролл пейнов у них стоят стили (ниже) которые их активирует при переполнении видимого окна
                                scrollForWorkers = scrollpane { boxOfWorkers = vbox { layoutY = 5.0 } }
                                scrollForFinishedProd = scrollpane { boxOfFinishedProduct = vbox { layoutY = 5.0 } }
                                scrollForUnfinishedProd = scrollpane { boxOfUnfinishedProduct = vbox { layoutY = 5.0 } }
                            }

                        }
                        vbox {
                            parentToScroll = vbox { }
                            scrollForCallList = scrollpane { callListOfColumn = anchorpane { } }
                        }
                    }

                    summarizeLabel = label("ИТОГ") {
                        alignment = Pos.TOP_CENTER
                        style {
                            fontSize = 18.px
                            borderColor = multi(box(bottom = Paint.valueOf("000000"), top = Paint.valueOf("000000"), left = Paint.valueOf("FFFFFF"), right = Paint.valueOf("FFFFFF")))
                        }
                    }

                    hbox {
                        style { borderColor = multi(box(top = Color.TRANSPARENT, bottom = Color.BLACK, right = Color.TRANSPARENT, left = Color.TRANSPARENT)) } // прозрачный)
                        setPrefSize(1200.0, 310.0)
                        setMinSize(1200.0, 310.0)
                        setMaxSize(1200.0, 310.0)

                        summarizeFirstColumn = vbox {// колонка общие сведения
                            translateX = 5.0
                            generalInformation = label("Общие сведения") { alignment = Pos.CENTER }
                            dateOfBegin = label("Дата начала работы:") { alignment = Pos.BOTTOM_LEFT }
                            hbox {
                                dateOfBeginInput = datepicker(SimpleObjectProperty(LocalDate.now()))
                                indentionSecond = anchorpane { }
                                hoursForBegin = textfield((LocalTime.now().hour - 12).toString()) {
                                    promptText = "ЧЧ"
                                    filterInput { it.controlNewText.matches(Regex("""\d{0,2}""")) } // фильтр вводимого текста так как это время ЧЧ и ММ он смотрит чтобы 2 символа и числа
                                    addEventFilter(KeyEvent.ANY) { if (it.code == KeyCode.BACK_SPACE || it.code == KeyCode.DELETE) text = "" }
                                    addEventHandler(KeyEvent.ANY) { if (text.length == 2) minutesForBegin.requestFocus() }
                                }
                                separatorForTimeBegin = label(":") {
                                    alignment = Pos.CENTER
                                    style {
                                        fontWeight = FontWeight.BOLD
                                        fontSize = 20.px
                                    }
                                }
                                minutesForBegin = textfield((LocalTime.now().minute).toString()) {
                                    promptText = "ММ"
                                    filterInput { it.controlNewText.matches(Regex("""\d{0,2}""")) }
                                    addEventFilter(KeyEvent.ANY) { if ((it.code == KeyCode.BACK_SPACE || it.code == KeyCode.DELETE) && text == "") hoursForBegin.requestFocus() }
                                }
                            }
                            dateOfFinished = label("Дата окончания работы:") { alignment = Pos.BOTTOM_LEFT }
                            hbox {
                                dateOfFinishedInput = datepicker(SimpleObjectProperty(LocalDate.now()))
                                indentionThird = anchorpane { }
                                hoursForFinished = textfield((LocalTime.now().hour).toString()) {
                                    promptText = "ЧЧ"
                                    filterInput { it.controlNewText.matches(Regex("""\d{0,2}""")) }
                                    addEventFilter(KeyEvent.ANY) { if (it.code == KeyCode.BACK_SPACE || it.code == KeyCode.DELETE) text = "" }
                                    addEventHandler(KeyEvent.ANY) { if (text.length == 2) minutesForFinished.requestFocus() }
                                }
                                separatorForTimeFinished = label(":") {
                                    alignment = Pos.CENTER
                                    style {
                                        fontWeight = FontWeight.BOLD
                                        fontSize = 20.px
                                    }
                                }
                                minutesForFinished = textfield((LocalTime.now().minute).toString()) {
                                    promptText = "ММ"
                                    filterInput { it.controlNewText.matches(Regex("""\d{0,2}""")) }
                                    addEventFilter(KeyEvent.ANY) { if ((it.code == KeyCode.BACK_SPACE || it.code == KeyCode.DELETE) && text == "") hoursForFinished.requestFocus() }
                                }
                            }
                            indentionFirst = anchorpane { }
                            fullSumLabel = label("Общая сумма:") { alignment = Pos.BOTTOM_LEFT }
                            fullSumInput = textfield { isEditable = false }
                            amountPerWorkerLabel = label("Сумма на одного сотрудника:") { alignment = Pos.BOTTOM_LEFT }
                            amountPerWorkerInput = textfield { isEditable = false }
                            captainLabel = label("Бригадир:") { alignment = Pos.BOTTOM_LEFT }
                            captainField = textfield { isEditable = false }
                            placeLabel = label("Участок:") {
                                alignment = Pos.BOTTOM_LEFT
                            }
                            hbox {
                                placeField = textfield { isEditable = false }
                                addPlace = button("+") {
                                    style { padding = box(0.px) }
                                    action { addPlace(callListOfColumn, placeField, parentToScroll) }
                                }
                            }


                        }
                        vbox {
                            lbFinishedProdInRub = label("Готовая продукция") { alignment = Pos.CENTER }
                            scrollForFinishedProdInRub = scrollpane { finishedProdInRub = vbox { } }
                        }
                        vbox {
                            lbUnfinishedProdInRub = label("Незавершенная продукция") { alignment = Pos.CENTER }
                            scrollForUnfinishedProdInRub = scrollpane { unfinishedProdInRub = vbox { } }
                        }
                    }
                    hbox {
                        paddingForSend = anchorpane { }
                        hbox {
                            checkbox("Подтверждаю правильность данных") { // проверка данных сработает если необходимые поля заполнены
                                translateY = 10.0
                                addEventFilter(MouseEvent.ANY) { send.isDisable = !(captainField.text != "" && amountPerWorkerInput.text != "" && fullSumInput.text != "" && hoursForBegin.text != "" && minutesForBegin.text != "" && hoursForFinished.text != "" && minutesForFinished.text != "" && isSelected) }
                            }
                            send = button("Отправить") { // отправка отчета в бд и генерация эксель файла, что его распечатать
                                isDisable = true
                                translateX = 20.0
                                translateY = 6.5
                                action {
                                    val wks = readDataForWorkers()
                                    val prodsK = readDataForProdK()
                                    val prodsR = readDataForProdR(hoursForBegin, hoursForFinished)
                                    val data = DataReportExcel(
                                        wks,
                                        prodsK.first,
                                        prodsK.second,
                                        prodsR.first,
                                        prodsR.second,
                                        captainField.text,
                                        dateOfBeginInput.value.toString(),
                                        "${hoursForBegin.text}:${minutesForBegin.text}",
                                        dateOfFinishedInput.value.toString(),
                                        "${hoursForFinished.text}:${minutesForFinished.text}",
                                        fullSumInput.text,
                                        amountPerWorkerInput.text,
                                        placeField.text
                                    )
                                    val work = wks.joinToString(separator = "||")
                                    val str1 = prodsK.first.joinToString(separator = "||")
                                    val str2 = prodsK.second.joinToString(separator = "||")
                                    val str3 = prodsR.first.joinToString(separator = "||")
                                    val str4 = prodsR.second.joinToString(separator = "||")
                                    val dataReportRequest = DataReportRequest(
                                        work,
                                        str1,
                                        str2,
                                        str3,
                                        str4,
                                        captainField.text,
                                        dateOfBeginInput.value.toString(),
                                        "${hoursForBegin.text}:${minutesForBegin.text}",
                                        dateOfFinishedInput.value.toString(),
                                        "${hoursForFinished.text}:${minutesForFinished.text}",
                                        fullSumInput.text,
                                        amountPerWorkerInput.text,
                                        placeField.text
                                    )
                                    dbHandler.sendReport(dataReportRequest)
                                    createReportExcel(data)
                                }
                            }
                        }
                    }
                }
            }
        }
        // Установка размеров для Label
        setSizeForLabel(316.5, 30.0, workers)
        setSizeForLabel(291.5, 30.0, finishedProducts)
        setSizeForLabel(291.5, 30.0, unfinishedProducts)
        setSizeForLabel(250.0, 20.0, dateOfBegin)
        setSizeForLabel(250.0, 20.0, dateOfFinished)
        setSizeForLabel(250.0, 20.0, fullSumLabel)
        setSizeForLabel(250.0, 20.0, amountPerWorkerLabel)
        setSizeForLabel(1200.0, 25.0, summarizeLabel)
        setSizeForLabel(1200.0, 25.0, reportLabel)
        setSizeForLabel(10.0, 25.0, separatorForTimeBegin)
        setSizeForLabel(10.0, 25.0, separatorForTimeFinished)
        setSizeForLabel(400.0, 25.0, generalInformation)
        setSizeForLabel(400.0, 25.0, lbFinishedProdInRub)
        setSizeForLabel(400.0, 25.0, lbUnfinishedProdInRub)
        setSizeForLabel(250.0, 20.0, captainLabel)
        setSizeForLabel(250.0, 20.0, placeLabel)
        // Установка размеров для VBox
        setSizeForVBox(316.5, 700.0, boxOfWorkers)
        setSizeForVBox(291.5, 700.0, boxOfFinishedProduct)
        setSizeForVBox(291.5, 700.0, boxOfUnfinishedProduct)
        setSizeForVBox(400.0, 295.0, summarizeFirstColumn)
        setSizeForVBox(400.0, 265.0, finishedProdInRub)
        setSizeForVBox(400.0, 265.0, unfinishedProdInRub)
        // Установка размеров для AnchorPane
        setSizeForAnchor(298.0, 1000.0, callListOfColumn)
        setSizeForAnchor(250.0, 15.0, indentionFirst)
        setSizeForAnchor(5.0, 25.0, indentionSecond)
        setSizeForAnchor(5.0, 25.0, indentionThird)
        setSizeForAnchor(815.0, 90.0, paddingForSend)
        // Установка размеров для TextField
        setSizeForTextField(200.0, 25.0, fullSumInput)
        setSizeForTextField(200.0, 25.0, amountPerWorkerInput)
        setSizeForTextField(35.0, 25.0, hoursForBegin)
        setSizeForTextField(40.0, 25.0, minutesForBegin)
        setSizeForTextField(35.0, 25.0, hoursForFinished)
        setSizeForTextField(40.0, 25.0, minutesForFinished)
        setSizeForTextField(250.0, 25.0, captainField)
        setSizeForTextField(250.0, 25.0, placeField)
        // Установка размеров для DatePicker
        setSizeForDatePicker(200.0, 25.0, dateOfBeginInput)
        setSizeForDatePicker(200.0, 25.0, dateOfFinishedInput)
        // Установка размеров для Button
        setSizeForButton(25.0, 25.0, addPlace)
        // Установка единого стиля для Label
        setStyleForLabel(workers, "#e0e0e0")
        setStyleForLabel(finishedProducts, "#e0e0e0")
        setStyleForLabel(unfinishedProducts, "#e0e0e0")
        // Установка внешнего вида ScrollPane
        setStyleForScrollPane(scrollForWorkers)
        setStyleForScrollPane(scrollForFinishedProd)
        setStyleForScrollPane(scrollForUnfinishedProd)
        setStyleForScrollPane(scrollForCallList)
        setStyleForScrollPane(scrollForFinishedProdInRub)
        setStyleForScrollPane(scrollForUnfinishedProdInRub)
        // Начальная установка
        addEnumerationOfWorkers(
            parentToScroll,
            callListOfColumn,
            "Сотрудники",
            names,
            boxOfWorkers,
            finishedProdInRub,
            true,
            mapOfAmountForFinished,
            fullSumInput,
            amountPerWorkerInput,
            captainField,
            hoursForBegin,
            hoursForFinished
        )
    }
}
