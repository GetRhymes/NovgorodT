import addition.NewAddition
import reportTable.WorkTable
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generateReport.GenerateReport
import javafx.geometry.Pos
import tornadofx.*

class MainMenu : View("Главное меню") {
    override val root = borderpane {
        // окно ГЛАВНОЕ МЕНЮ
        center {
            anchorpane {
                setPrefSize(600.0, 400.0) // уникальные размеры окна 600 ширина и 400 высота (по числа аналогично как и в LogIn классе)
                setMaxSize(600.0, 400.0)
                setMinSize(600.0, 400.0)
                vbox {
                    layoutX = 150.0 // перемещение бокса в рамках окна по горизонтали (по числа аналогично как и в LogIn классе)
                    layoutY = 20.0 // по вертикали  (по числа аналогично как и в LogIn классе)
                    val mainMenuLb = label("Главное меню") {
                        alignment = Pos.CENTER
                        style { fontSize = 36.px }
                    }
                    setSizeForLabel(300.0, 50.0, mainMenuLb) // установка размеров лейбла (по числа аналогично как и в LogIn классе)
                    vbox {
                        translateX = 50.0
                        translateY = 25.0
                        val createReportB = button("Создать отчет") {
                            action { // кнопка которая перебрасывает нас на создание отчета
                                val window = find<WorkTable>()
                                window.openModal()
                                window.currentStage!!.isResizable = false
                            }
                        }
                        val addB = button("Добавить") {
                            translateY = 15.0
                            action { // кнопка которая перебрасывает нас на добавить (сотрудников и продукт)
                                close()
                                val window = find<NewAddition>()
                                window.openWindow()
                                window.currentStage!!.isResizable = false
                            }
                        }
                        val generateReportB = button("Сгенерировать отчет") {
                            translateY = 30.0
                            action { // кнопка перебрасывает на меню генератора отчетов в эксель
                                close()
                                val window = find<GenerateReport>()
                                window.openWindow()
                                window.currentStage!!.isResizable = false
                            }
                        }
                        val exitB = button("Выйти") {
                            translateY = 45.0
                            action { // выйти к окну авторизации пользователя
                                close()
                                val window = find<LogIn>()
                                window.openWindow()
                                window.currentStage!!.isResizable = false
                            }
                        }
                        setSizeForButton(200.0, 50.0, createReportB) // установка размеров кнопки (по числа аналогично как и в LogIn классе)
                        setSizeForButton(200.0, 50.0, addB)
                        setSizeForButton(200.0, 50.0, generateReportB)
                        setSizeForButton(200.0, 50.0, exitB)
                    }
                }
            }
        }
    }
}
