package generateReport

import dataBase.DatabaseHandler
import dataBase.getDataForReportCaptain
import generalStyle.*
import javafx.geometry.Pos
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import tornadofx.*
import java.time.LocalDate

class CenterCaptainReport : View("Отчет по бригадирам") {
    override val root = borderpane {
        var searchPlace: AnchorPane by singleAssign()
        var boxForSearchPlace: VBox by singleAssign()
        val listOfPlace = mutableListOf<Pair<Int, String>>()
        val dbHandler = DatabaseHandler()
        val placeRequest = dbHandler.getPlace()
        while (placeRequest!!.next()) {
            listOfPlace.add(placeRequest.getInt(1) to placeRequest.getString(2))
        }
        var currentPlace = Pair(0, "")
        center {
            anchorpane {
                vbox {
                    layoutX = 215.0
                    layoutY = 10.0
                    val nameReport = label("Отчет по бригадирам") {
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
                    val placeLb = label("Название участка") {
                        translateX = 25.0
                        translateY = 75.0
                        alignment = Pos.BOTTOM_CENTER
                    }
                    val placeFd = textfield {
                        translateX = 25.0
                        translateY = 75.0
                        promptText = "Название участка"
                        addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED) {
                            val toggleGroup = ToggleGroup()
                            if (boxForSearchPlace.children.size != 0) boxForSearchPlace.children.remove(0, 1)
                            val vBox = VBox()
                            for (place in listOfPlace) {
                                if (place.second.toLowerCase().contains(text.toString().toLowerCase())) {
                                    val radioButton = RadioButton(place.second)
                                    toggleGroup.toggles.add(radioButton)
                                    radioButton.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
                                        if (radioButton.isSelected) {
                                            text = radioButton.text
                                            currentPlace = place
                                        }
                                    }
                                    radioButton.setPrefSize(270.0, 20.0)
                                    radioButton.setMaxSize(270.0, 20.0)
                                    radioButton.setMinSize(270.0, 20.0)
                                    vBox.add(radioButton)
                                }
                            }
                            boxForSearchPlace.add(vBox)
                        }
                    }
                    searchPlace = anchorpane {
                        translateX = 15.0
                        translateY = 80.0
                        val scroll = scrollpane {
                            setPrefSize(270.0, 200.0)
                            setMaxSize(270.0, 200.0)
                            setMinSize(270.0, 200.0)
                            boxForSearchPlace = vbox {  }
                        }
                        setStyleForScrollPane(scroll)
                    }
                    val generate = button ("Сгенерировать") {
                        translateY = 150.0
                        action {
                            if (placeFd.text == "" || placeFd.text.isNotEmpty())
                                createReportForCaptain(
                                        dayLeftFd.value.toString(),
                                        dayRightFd.value.toString(),
                                        getDataForReportCaptain(dayLeftFd.value.toString(), dayRightFd.value.toString(), currentPlace)
                                )
                        }
                    }
                    // button
                    setSizeForButton(300.0, 25.0, generate)
                    // label
                    setSizeForLabel(200.0, 25.0, dayRightLb)
                    setSizeForLabel(200.0, 25.0, dayLeftLb)
                    setSizeForLabel(300.0, 50.0, nameReport)
                    setSizeForLabel(250.0, 25.0, placeLb)
                    // datepicker
                    setSizeForDatePicker(200.0, 25.0, dayLeftFd)
                    setSizeForDatePicker(200.0, 25.0, dayRightFd)
                    // textfield
                    setSizeForTextField(250.0, 25.0, placeFd)
                    // anchor
                    setSizeForAnchor(270.0, 200.0, searchPlace)
                    // vBox
                    setSizeForVBox(250.0, 1000.0, boxForSearchPlace)
                }
            }
        }
    }
}
