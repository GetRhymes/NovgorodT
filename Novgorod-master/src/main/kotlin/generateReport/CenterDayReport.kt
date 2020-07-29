package generateReport

import dataBase.changeDataForReportExcel
import dataBase.getDataForReportCaptain
import generalStyle.*
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import reportTable.createReportExcel
import tornadofx.*
import java.time.LocalDate

class CenterDayReport : View("Отчет за день") {
    override val root = borderpane {
        center {// окно отчет за день
            anchorpane {
                var boxOfCaptainPlace: AnchorPane by singleAssign()
                var namePLaceFd: TextField by singleAssign()
                var nameCaptainFd: TextField by singleAssign()
                vbox {
                    var dayOrNight = ""
                    val tgg = ToggleGroup()
                    translateX = 215.0
                    translateY = 10.0
                    val nameReport = label("Отчет за день") {
                        alignment = Pos.CENTER
                        style {
                            backgroundColor = multi(Paint.valueOf("#FFFFFF"))
                            fontSize = 32.px
                        }
                    }
                    val dayReport = label("Дата отчета") {
                        translateX = 50.0 // сдвиг (объяснение значений в LogIn)
                        translateY = 25.0

                        alignment = Pos.BOTTOM_CENTER
                    }
                    val dayReportFd = datepicker {
                        translateX = 50.0
                        translateY = 25.0
                        value = LocalDate.now()
                    }
                    // Входные параметры для отчета
                    vbox {
                        translateY = 60.0
                        translateX = -77.5
                        hbox {
                            translateX = 2.0
                            val nameCaptainLb = label("ФИО Бригадира") {
                                alignment = Pos.BOTTOM_LEFT
                            }
                            val namePlaceLb = label("Название участка") {
                                translateX = 5.0
                                alignment = Pos.BOTTOM_LEFT
                            }
                            setSizeForLabel(250.0, 25.0, nameCaptainLb)
                            setSizeForLabel(200.0, 25.0, namePlaceLb)
                        }
                        hbox {
                            nameCaptainFd = textfield {
                                promptText = "Введите имя"
                                addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED) {
                                    val listOfItems = getDataForReportCaptain(dayReportFd.value.toString(), dayReportFd.value.toString(), "")
                                    val toggleGroup = ToggleGroup()
                                    if (boxOfCaptainPlace.children.size != 0) boxOfCaptainPlace.children.remove(0, 1)
                                    val vBox = VBox()
                                    for (place in listOfItems) {
                                        if (place[0].toLowerCase().contains(text.toString().toLowerCase())) {
                                            val radioButton = RadioButton("${place[0]} ${place[2]}")
                                            toggleGroup.toggles.add(radioButton)
                                            radioButton.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
                                                if (radioButton.isSelected) {
                                                    text = place[0]
                                                    namePLaceFd.text = place[2]
                                                }
                                            }
                                            radioButton.setPrefSize(455.0, 20.0)
                                            radioButton.setMaxSize(455.0, 20.0)
                                            radioButton.setMinSize(455.0, 20.0)
                                            vBox.add(radioButton)
                                        }
                                    }
                                    boxOfCaptainPlace.add(vBox)
                                }
                            }
                            namePLaceFd = textfield {
                                promptText = "Название учатска"
                                translateX = 5.0
                                addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED) {
                                    val listOfItems = getDataForReportCaptain(dayReportFd.value.toString(), dayReportFd.value.toString(), "")
                                    val toggleGroup = ToggleGroup()
                                    if (boxOfCaptainPlace.children.size != 0) boxOfCaptainPlace.children.remove(0, 1)
                                    val vBox = VBox()
                                    for (place in listOfItems) {
                                        if (place[2].toLowerCase().contains(text.toString().toLowerCase())) {
                                            val radioButton = RadioButton()
                                            radioButton.text = "${place[0]} ${place[2]}"
                                            toggleGroup.toggles.add(radioButton)
                                            radioButton.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
                                                if (radioButton.isSelected) {
                                                    text = place[2]
                                                    nameCaptainFd.text = place[0]
                                                }
                                            }
                                            println(radioButton.style.length)
                                            radioButton.setPrefSize(455.0, 20.0)
                                            radioButton.setMaxSize(455.0, 20.0)
                                            radioButton.setMinSize(455.0, 20.0)
                                            vBox.add(radioButton)
                                        }
                                    }
                                    boxOfCaptainPlace.add(vBox)
                                }
                            }

                        }
                    }
                    val captainPlace = scrollpane {
                        translateY = 65.0
                        translateX = -77.5
                        setMaxSize(455.0, 300.0)
                        setPrefSize(455.0, 300.0)
                        setMinSize(455.0, 300.0)

                        boxOfCaptainPlace = anchorpane {}
                        setSizeForAnchor(455.0, 700.0, boxOfCaptainPlace)
                    }
                    setStyleForScrollPane(captainPlace)

                    val generate = button ("Сгенерировать") {
                        translateY = 70.0
                        action { createReportExcel(changeDataForReportExcel(dayReportFd.value.toString(), nameCaptainFd.text, namePLaceFd.text)) } // сам отчет
                    }
                    // Установка размеров
                    setSizeForButton(300.0, 25.0, generate)
                    setSizeForLabel(200.0, 25.0, dayReport)
                    setSizeForLabel(300.0, 50.0, nameReport)
                    setSizeForDatePicker(200.0, 25.0, dayReportFd)
                    setSizeForTextField(200.0, 25.0, namePLaceFd)
                    setSizeForTextField(250.0, 25.0, nameCaptainFd)
                }
            }
        }
    }
}
