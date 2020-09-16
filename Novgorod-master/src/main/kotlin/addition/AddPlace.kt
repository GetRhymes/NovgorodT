package addition

import dataBase.DatabaseHandler
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generalStyle.setSizeForTextField
import javafx.geometry.Pos
import javafx.scene.paint.Paint
import tornadofx.*

class AddPlace : Fragment("Участок") {
    override val root = borderpane {
        center {
            anchorpane {
                vbox {
                    layoutY = 20.0
                    layoutX = 115.0
                    val addProdLb = label("Добавить участок") {
                        alignment = Pos.CENTER
                        style {
                            backgroundColor = multi(Paint.valueOf("#FFFFFF"))
                            fontSize = 24.px
                        }
                    }
                    vbox {
                        translateX = 50.0
                        translateY = 45.0
                        val nameProdLb = label("Название участка") { alignment = Pos.BOTTOM_CENTER }
                        val nameProdFd = textfield {
                            promptText = "Название участка"
                            filterInput { it.controlNewText.matches(Regex("""[а-яА-ЯёЁ]+""")) || it.controlNewText.matches(Regex("""[а-яА-ЯёЁ]+\s""")) || it.controlNewText.matches(Regex("""[а-яА-ЯёЁ]+\s[а-яА-ЯёЁ]+"""))}
                        }
                        val addB = button("Добавить") {
                            translateY = 185.0
                            translateX = -50.0
                            action {
                                if (nameProdFd.text != "") {
                                    val dbHandler = DatabaseHandler()
                                    val answer = dbHandler.addPlace(nameProdFd.text.trim())
                                    nameProdFd.text = ""
                                    if (answer > 0) style { borderColor = multi(box(Paint.valueOf("#4caf50"))) }
                                    else style { borderColor = multi(box(Paint.valueOf("#f44336"))) }
                                } else style { borderColor = multi(box(Paint.valueOf("#f44336"))) }
                            }
                        }
                        // Label
                        setSizeForLabel(200.0, 25.0, nameProdLb)
                        // Text_Field
                        setSizeForTextField(200.0, 25.0, nameProdFd)
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
