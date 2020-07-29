package generateReport

import MainMenu
import dataBase.DatabaseHandler
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generalStyle.setSizeForTextField
import generalStyle.setStyleForButtonAdd
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.paint.Paint
import tornadofx.*

class GenerateReport : View("Генерация Отчета") {
    override val root = borderpane {
        setPrefSize(800.0, 600.0) // фиксированный размер окна
        setMaxSize(800.0, 600.0)
        setMinSize(800.0, 600.0)
        left {
            //style { backgroundColor = multi(Paint.valueOf("#9ccc65"))}
            vbox {
                val reportDay = button ("Дневной") {
                    translateX = 2.0
                    translateY = 3.0
                    action { center<CenterDayReport>() } // переход на генератор дневного отчета
                }
                val reportWorker = button ("Сотрудник") {
                    translateX = 2.0
                    translateY = 17.0
                    action { center<CenterWorkerReport>() } // переход на генератор отчета по сотруднику за период
                }
                val reportTotalPrice = button ("Зарплата") {
                    translateX = 2.0
                    translateY = 34.0
                    action { center<CenterMoneyReport>() }
                }
                val reportFinishedProd = button ("Продукция") {
                    translateX = 2.0
                    translateY = 51.0
                    action { center<CenterProductReport>() }
                }
                val reportTimeSheet = button ("Рабочее время") {
                    translateX = 2.0
                    translateY = 68.0
                    action { center<CenterTimeReport>() } // для перспективы
                }
                val reportCaptainSheet = button ("Бригадиры") {
                    translateX = 2.0
                    translateY = 85.0
                    action { center<CenterCaptainReport>() } // для перспективы
                }
                val back = button ("Назад") {
                    translateX = 2.0
                    translateY = 102.0
                    action { // возвращаемся  в главное меню
                        close()
                        val window = find<MainMenu>()
                        window.openWindow()
                        window.currentStage!!.isResizable = false
                    }
                }
                // Размеры
                setSizeForButton( 70.0, 70.0, reportDay) // установка размеров для кнопок
                setSizeForButton( 70.0, 70.0, reportWorker)
                setSizeForButton( 70.0, 70.0, reportTotalPrice)
                setSizeForButton( 70.0, 70.0, reportFinishedProd)
                setSizeForButton( 70.0, 70.0, reportTimeSheet)
                setSizeForButton( 70.0, 70.0, reportCaptainSheet)
                setSizeForButton( 70.0, 70.0, back)
                // Стиль
                setStyleForButtonAdd(reportDay)
                setStyleForButtonAdd(reportWorker)
                setStyleForButtonAdd(reportTotalPrice)
                setStyleForButtonAdd(reportFinishedProd)
                setStyleForButtonAdd(reportTimeSheet)
                setStyleForButtonAdd(reportCaptainSheet)
                setStyleForButtonAdd(back)
            }
        }
    }
}
