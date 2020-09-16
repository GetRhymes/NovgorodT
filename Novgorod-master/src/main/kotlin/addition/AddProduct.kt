package addition

import dataBase.DatabaseHandler
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generalStyle.setSizeForTextField
import javafx.geometry.Pos
import javafx.scene.control.ToggleGroup
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Paint
import tornadofx.*

class AddProduct : Fragment("Продукт") {
    override val root = borderpane {
        center {
            anchorpane {
                vbox {
                    layoutY = 20.0
                    layoutX = 115.0
                    val addProdLb = label("Добавить продукт") {
                        alignment = Pos.CENTER
                        style {
                            backgroundColor = multi(Paint.valueOf("#FFFFFF"))
                            fontSize = 24.px
                        }
                    }
                    var cond: Boolean? = null
                    vbox {
                        translateX = 75.0
                        translateY = 45.0
                        val toggleGroup = ToggleGroup()
                        val finishedProd = radiobutton("Готова продукция", toggleGroup) { translateY = -30.0 }
                        val unfinishedProd = radiobutton("Незавершеная продукция", toggleGroup) { translateY = -15.0 }
                        val nameProdLb = label("Название продукта") { alignment = Pos.BOTTOM_CENTER }
                        val nameProdFd = textfield {
                            promptText = "Название продукта"
                            filterInput { it.controlNewText.matches(Regex("""[а-яА-ЯёЁ./]+""")) }
                        }
                        val priceKgDayLb = label("Цена за кг / день") { alignment = Pos.BOTTOM_CENTER }
                        val priceKgDayFd = textfield {
                            promptText = "Цена за кг / день"
                            filterInput { it.controlNewText.matches(Regex("""\d+"""))  || it.controlNewText.matches(Regex("""\d+\.""")) || it.controlNewText.matches(Regex("""\d+\.\d+""")) }
                        }
                        val priceKgNightLb = label("Цена за кг / ночь") { alignment = Pos.BOTTOM_CENTER }
                        val priceKgNightFd =textfield {
                            promptText = "Цена за кг / ночь"
                            filterInput { it.controlNewText.matches(Regex("""\d+"""))  || it.controlNewText.matches(Regex("""\d+\.""")) || it.controlNewText.matches(Regex("""\d+\.\d+""")) }
                        }
                        val addB = button("Добавить") {
                            translateY = 40.0
                            translateX = -75.0
                            action { // Отправляем в бд данные о новом продукте
                                if (cond != null && nameProdFd.text != "" && priceKgDayFd.text != "" && priceKgNightFd.text != "") {
                                    val dbHandler = DatabaseHandler()
                                    val answer = if (cond as Boolean) dbHandler.addUnFinishProduct(nameProdFd.text.trim(), priceKgDayFd.text.trim(), priceKgNightFd.text.trim())
                                    else dbHandler.addFinishProduct(nameProdFd.text.trim(), priceKgDayFd.text.trim(), priceKgNightFd.text.trim())
                                    if (answer > 0) style { borderColor = multi(box(Paint.valueOf("#4caf50"))) }
                                    else style { borderColor = multi(box(Paint.valueOf("#f44336"))) }
                                    cond = null
                                    nameProdFd.text = ""
                                    priceKgDayFd.text = ""
                                    priceKgNightFd.text = ""
                                    finishedProd.isSelected = false
                                    unfinishedProd.isSelected = false
                                }  else style { borderColor = multi(box(Paint.valueOf("#f44336"))) }
                            }
                        }
                        // Label
                        setSizeForLabel(150.0, 25.0, nameProdLb)
                        setSizeForLabel(150.0, 25.0, priceKgDayLb)
                        setSizeForLabel(150.0, 25.0, priceKgNightLb)
                        // Text_Field
                        setSizeForTextField(150.0, 25.0, nameProdFd)
                        setSizeForTextField(150.0, 25.0, priceKgDayFd)
                        setSizeForTextField(150.0, 25.0, priceKgNightFd)
                        finishedProd.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { if (finishedProd.isSelected) { cond = false } }
                        unfinishedProd.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { if (unfinishedProd.isSelected) { cond = true } }
                        // Button
                        setSizeForButton(300.0, 25.0, addB)
                    }
                    // Label
                    setSizeForLabel(300.0, 50.0, addProdLb)

                }
            }
        }
    }
}
