package addition

import dataBase.DatabaseHandler
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generalStyle.setSizeForTextField
import javafx.geometry.Pos
import javafx.scene.paint.Paint
import tornadofx.*

class AddWorkers : Fragment("Сотрудника") {
    override val root = borderpane {
        center {
            anchorpane {
                vbox {
                    layoutY = 20.0
                    layoutX = 115.0
                    val addWorker = label("Добавить сотрудника") {
                        alignment = Pos.CENTER
                        style {
                            backgroundColor = multi(Paint.valueOf("#FFFFFF"))
                            fontSize = 24.px
                        }
                    }
                    vbox {
                        translateX = 75.0
                        val lastNameLb = label("Фамилия") { alignment = Pos.BOTTOM_CENTER }
                        val lastNameFd = textfield { promptText = "Фамилия" }
                        val nameLb = label("Имя") { alignment = Pos.BOTTOM_CENTER }
                        val nameFd = textfield { promptText = "Имя" }
                        val fatherNameLb = label("Отчество") { alignment = Pos.BOTTOM_CENTER }
                        val fatherNameFd = textfield { promptText = "Отчетсво" }
                        val individualCodeLb = label("Индивидуальный код") { alignment = Pos.BOTTOM_CENTER }
                        val individualCodeFd = textfield { promptText = "Индивидуальный код" }
                        //Label
                        setSizeForLabel(150.0, 25.0, lastNameLb) // установка размеров (по числа аналогично как и в LogIn классе)
                        setSizeForLabel(150.0, 25.0, nameLb)
                        setSizeForLabel(150.0, 25.0, fatherNameLb)
                        setSizeForLabel(150.0, 25.0, individualCodeLb)
                        //Text_Field
                        setSizeForTextField(150.0, 25.0, lastNameFd)
                        setSizeForTextField(150.0, 25.0, nameFd)
                        setSizeForTextField(150.0, 25.0, fatherNameFd)
                        setSizeForTextField(150.0, 25.0, individualCodeFd)

                        val addB = button("Добавить") {
                            translateY = 75.0
                            translateX = -75.0
                            action { // Отправляем в бд данные о новом сотруднике
                                val dbHandler = DatabaseHandler()
                                dbHandler.signUpUser(lastNameFd.text, nameFd.text, fatherNameFd.text, individualCodeFd.text)
                            }
                        }
                        // Button
                        setSizeForButton(300.0, 25.0, addB)
                    }
                    // Label
                    setSizeForLabel(300.0, 50.0, addWorker)
                }
            }
        }
    }
}
