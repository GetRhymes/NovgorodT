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
                    var name = ""
                    var priceDay = ""
                    var priceNight = ""
                    vbox {
                        translateX = 75.0
                        translateY = 45.0
                        val toggleGroup = ToggleGroup()
                        val finishedProd = radiobutton("Готова продукция", toggleGroup) { translateY = -30.0 }
                        val unfinishedProd = radiobutton("Незавершеная продукция", toggleGroup) { translateY = -15.0 }
                        val nameProdLb = label("Название продукта") { alignment = Pos.BOTTOM_CENTER }
                        val nameProdFd = textfield {
                            promptText = "Название продукта"
                            addEventHandler(KeyEvent.KEY_RELEASED) { name = text }
                        }
                        val priceKgDayLb = label("Цена за кг / день") { alignment = Pos.BOTTOM_CENTER }
                        val priceKgDayFd = textfield {
                            promptText = "Цена за кг / день"
                            addEventHandler(KeyEvent.KEY_RELEASED) { priceDay = text }
                        }
                        val priceKgNightLb = label("Цена за кг / ночь") { alignment = Pos.BOTTOM_CENTER }
                        val priceKgNightFd =textfield {
                            promptText = "Цена за кг / ночь"
                            addEventHandler(KeyEvent.KEY_RELEASED) { priceNight = text }
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
                    }
                    val addB = button("Добавить") {
                        translateY = 85.0
                        action { // Отправляем в бд данные о новом продукте
                            val dbHandler = DatabaseHandler()
                            if (cond != null) {
                                if (cond as Boolean) dbHandler.addUnFinishProduct(name, priceDay, priceNight)
                                else dbHandler.addFinishProduct(name, priceDay, priceNight)
                            }
                        }
                    }
                    // Label
                    setSizeForLabel(300.0, 50.0, addProdLb)
                    // Button
                    setSizeForButton(300.0, 25.0, addB)
                }
            }
        }
    }
}
