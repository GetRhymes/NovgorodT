package generateReport

import dataBase.*
import generalStyle.*
import javafx.geometry.Pos
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.time.LocalDate

class CenterTimeReport : View("Отчет по времени") {
    override val root = borderpane {
        // окно отчет по сотруднику
        var searchWorker: AnchorPane by singleAssign()
        var boxForSearchWorker: VBox by singleAssign()
        var nameWorkerFd: TextField by singleAssign()
        var selectAll: ToggleButton by singleAssign()
        val dbHandler = DatabaseHandler()
        val listOfWorkers = mutableListOf<Pair<Int, String>>()
        val requestWorker = dbHandler.getWorker()
        while (requestWorker!!.next()) {
            val name = "${requestWorker.getString(2)} ${requestWorker.getString(3)} ${requestWorker.getString(4)} # ${requestWorker.getString(5)}"
            listOfWorkers.add(requestWorker.getInt(1) to name)
        } // запрашиваю из бд список сотрудников
        val finalListWorkers = mutableListOf<Pair<Int, String>>()
        center {
            anchorpane {
                vbox {
                    // входны епараметры
                    layoutX = 215.0
                    layoutY = 10.0
                    val nameReport = label("Табель времени") {
                        alignment = Pos.CENTER
                        style {
                            backgroundColor = multi(Paint.valueOf("#FFFFFF"))
                            fontSize = 28.px
                        }
                    }
                    val dayLeftLb = label("Начальная дата") {
                        translateX = 50.0
                        translateY = 25.0
                        alignment = Pos.BOTTOM_CENTER
                    }
                    val dayLeftFd = datepicker {
                        translateX = 50.0
                        translateY = 25.0
                        value = LocalDate.now()
                    }
                    val dayRightLb = label("Конечная дата") {
                        translateX = 50.0
                        translateY = 50.0
                        alignment = Pos.BOTTOM_CENTER
                    }
                    val dayRightFd = datepicker {
                        translateX = 50.0
                        translateY = 50.0
                        value = LocalDate.now()
                    }

                    val nameWorkerLb = label("Имя сотрудника") {
                        translateX = 25.0
                        translateY = 75.0
                        alignment = Pos.BOTTOM_CENTER
                    }
                    hbox {
                        translateX = 25.0
                        translateY = 75.0
                        nameWorkerFd = textfield {
                            promptText = "Введите имя сотрудника"
                            addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED) {
                                if (boxForSearchWorker.children.size != 0) boxForSearchWorker.children.remove(0, 1)
                                val vBox = VBox()
                                for (worker in listOfWorkers) {
                                    if (worker.second.toLowerCase().contains(text.toString().toLowerCase())) {
                                        val radioButton = RadioButton(worker.second)
                                        if (finalListWorkers.contains(worker)) radioButton.isSelected = true
                                        radioButton.addEventFilter(MouseEvent.MOUSE_CLICKED) {
                                            if (radioButton.isSelected) finalListWorkers.add(worker)
                                            else if (finalListWorkers.contains(worker)) finalListWorkers.remove(worker)
                                            println(finalListWorkers)
                                        }
                                        radioButton.setPrefSize(270.0, 20.0)
                                        radioButton.setMaxSize(270.0, 20.0)
                                        radioButton.setMinSize(270.0, 20.0)
                                        vBox.add(radioButton)
                                    }
                                }
                                boxForSearchWorker.add(vBox)
                            }
                        }

                        selectAll = togglebutton("ВСЕ") {
                            isSelected = false
                            style {
                                padding = box(0.px)
                                textAlignment = TextAlignment.CENTER
                            }
                            addEventFilter(MouseEvent.MOUSE_CLICKED) { println(finalListWorkers) }
                            action {
                                if (isSelected) {
                                    if (boxForSearchWorker.children.size != 0) boxForSearchWorker.children.remove(0, 1)
                                    val vBox = VBox()
                                    for (worker in listOfWorkers) {
                                        val radioButton = RadioButton(worker.second)
                                        radioButton.isSelected = true
                                        radioButton.addEventFilter(MouseEvent.MOUSE_CLICKED) {
                                            if (radioButton.isSelected) finalListWorkers.add(worker)
                                            else if (finalListWorkers.contains(worker)) finalListWorkers.remove(worker)
                                            println(finalListWorkers)
                                        }
                                        finalListWorkers.add(worker)
                                        radioButton.setPrefSize(270.0, 20.0)
                                        radioButton.setMaxSize(270.0, 20.0)
                                        radioButton.setMinSize(270.0, 20.0)
                                        vBox.add(radioButton)
                                    }
                                    boxForSearchWorker.add(vBox)
                                } else {
                                    if (boxForSearchWorker.children.size != 0) boxForSearchWorker.children.remove(0, 1)
                                    finalListWorkers.clear()
                                    val vBox = VBox()
                                    for (worker in listOfWorkers) {
                                        val radioButton = RadioButton(worker.second)
                                        if (finalListWorkers.contains(worker)) radioButton.isSelected = true
                                        radioButton.addEventFilter(MouseEvent.MOUSE_CLICKED) {
                                            if (radioButton.isSelected) finalListWorkers.add(worker)
                                            else if (finalListWorkers.contains(worker)) finalListWorkers.remove(worker)
                                        }
                                        radioButton.setPrefSize(270.0, 20.0)
                                        radioButton.setMaxSize(270.0, 20.0)
                                        radioButton.setMinSize(270.0, 20.0)
                                        vBox.add(radioButton)
                                    }
                                    boxForSearchWorker.add(vBox)
                                }
                            }
                        }
                    }
                    searchWorker = anchorpane {
                        translateX = 15.0
                        translateY = 80.0
                        val scroll = scrollpane {
                            setPrefSize(270.0, 200.0)
                            setMaxSize(270.0, 200.0)
                            setMinSize(270.0, 200.0)
                            boxForSearchWorker = vbox {  }
                        }
                        setStyleForScrollPane(scroll)
                    }
                    val generate = button ("Сгенерировать") {
                        translateY = 150.0
                        action { // генерирую отчет
                            val dayLeft = dayLeftFd.value.toString()
                            val dayRight = dayRightFd.value.toString()
                            val listDays = getListDays(dayLeft, dayRight)
                            val month = getMonth(dayRight)
                            val dataForReport = getDataForReportTime(dayLeftFd.value.toString(), dayRightFd.value.toString(), finalListWorkers, listDays)
                            createReportExcelForTime(dayLeft, dayRight, listDays, month, dataForReport)
                        }
                    }
                    // установка размеров
                    setSizeForButton(300.0, 25.0, generate)
                    setSizeForLabel(200.0, 25.0, dayRightLb)
                    setSizeForLabel(200.0, 25.0, dayLeftLb)
                    setSizeForLabel(250.0, 25.0, nameWorkerLb)
                    setSizeForLabel(300.0, 50.0, nameReport)
                    setSizeForDatePicker(200.0, 25.0, dayLeftFd)
                    setSizeForDatePicker(200.0, 25.0, dayRightFd)
                    setSizeForTextField(250.0, 25.0, nameWorkerFd)
                    setSizeForAnchor(270.0, 200.0, searchWorker)
                    setSizeForVBox(250.0, 1000.0, boxForSearchWorker)
                    setSizeForToggleButton(27.0, 25.0, selectAll)
                }
            }
        }
    }
}
