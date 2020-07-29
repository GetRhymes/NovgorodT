package generateReport

import dataBase.DatabaseHandler
import dataBase.getDataReportForAmountMoney
import dataBase.getDataReportForNeedWorker
import generalStyle.*
import javafx.geometry.Pos
import javafx.scene.control.DatePicker
import javafx.scene.control.RadioButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import tornadofx.*
import java.time.LocalDate

class CenterMoneyReport : View("Общий отчет по ЗП") {
    override val root = borderpane {
        // окно отчет по ЗП
        var dayRightFd: DatePicker by singleAssign()
        var dayLeftFd: DatePicker by singleAssign()
        center {
            anchorpane {
                vbox {
                    // входны епараметры
                    layoutX = 215.0
                    layoutY = 10.0
                    val nameReport = label("Общий отчет по ЗП") {
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
                    dayLeftFd = datepicker {
                        translateX = 50.0
                        translateY = 25.0
                        value = LocalDate.now()
                    }
                    val dayRightLb = label("Конечная дата") {
                        translateX = 50.0
                        translateY = 50.0

                        alignment = Pos.BOTTOM_CENTER
                    }
                    dayRightFd = datepicker {
                        translateX = 50.0
                        translateY = 50.0
                        value = LocalDate.now()
                    }
                    val generate = button ("Сгенерировать") {
                        translateY = 400.0
                        action { // генерирую отчет
                            createReportForAmountMoney(dayLeftFd.value.toString(), dayRightFd.value.toString(), getDataReportForAmountMoney(dayLeftFd.value.toString(), dayRightFd.value.toString()))
                        }
                    }
                    // установка размеров
                    setSizeForButton(300.0, 25.0, generate)
                    setSizeForLabel(200.0, 25.0, dayRightLb)
                    setSizeForLabel(200.0, 25.0, dayLeftLb)
                    setSizeForLabel(300.0, 50.0, nameReport)
                }
            }
        }
        setSizeForDatePicker(200.0, 25.0, dayLeftFd)
        setSizeForDatePicker(200.0, 25.0, dayRightFd)
    }
}
