package generateReport

import dataBase.getDataForReportProduct
import generalStyle.setSizeForButton
import generalStyle.setSizeForDatePicker
import generalStyle.setSizeForLabel
import generalStyle.setSizeForToggleButton
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Paint
import tornadofx.*
import java.awt.event.MouseEvent
import java.time.LocalDate

class CenterProductReport : View("Отчет по продукции") {
    override val root = borderpane {
        center {
            anchorpane {
                vbox {
                    val toggleGr = ToggleGroup()
                    var product: Boolean? = null
                    var generate: Button by singleAssign()
                    var finished: ToggleButton by singleAssign()
                    var unfinished: ToggleButton by singleAssign()
                    // входны епараметры
                    layoutX = 215.0
                    layoutY = 10.0
                    val nameReport = label("Отчет по продукции") {
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
                    hbox {
                        translateY = 75.0
                        translateX = -55.0
                        finished = togglebutton("Готовая продукция", toggleGr) {
                            isSelected = false
                            addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { generate.isDisable = !isSelected }
                            addEventFilter(KeyEvent.KEY_RELEASED) { generate.isDisable = !isSelected }
                            action {
                                if (isSelected) { product = true }
                            }
                        }
                        unfinished = togglebutton("Незавершенная продукция", toggleGr) {
                            isSelected = false
                            addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { generate.isDisable = !isSelected }
                            addEventFilter(KeyEvent.KEY_RELEASED) { generate.isDisable = !isSelected }
                            action {
                                if (isSelected) { product = false }
                            }
                        }
                        setSizeForToggleButton(200.0, 25.0, finished)
                        setSizeForToggleButton(200.0, 25.0, unfinished)
                    }
                    generate = button ("Сгенерировать") {
                        translateY = 380.0
                        isDisable = true
                        action { // генерирую отчет
                            if(product!!) {
                                isDisable = true
                                finished.isSelected = false
                            } else {
                                unfinished.isSelected = false
                                isDisable = true
                            }
                            createReportExcelForProducts(dayLeftFd.value.toString(), dayRightFd.value.toString(), getDataForReportProduct(dayLeftFd.value.toString(), dayRightFd.value.toString(), product!!), product!!)
                        }
                    }
                    // установка размеров
                    setSizeForButton(300.0, 25.0, generate)
                    setSizeForLabel(200.0, 25.0, dayRightLb)
                    setSizeForLabel(200.0, 25.0, dayLeftLb)
                    setSizeForLabel(300.0, 50.0, nameReport)
                    setSizeForDatePicker(200.0, 25.0, dayLeftFd)
                    setSizeForDatePicker(200.0, 25.0, dayRightFd)
                }
            }
        }
    }
}
