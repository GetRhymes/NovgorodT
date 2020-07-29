package addition

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

class NewAddition : Fragment("Добавить") {
    override val root = borderpane {
        setPrefSize(600.0, 400.0) // установка размеров окна (по числа аналогично как и в LogIn классе)
        setMaxSize(600.0, 400.0)
        setMinSize(600.0, 400.0)
        left {
            vbox {
                val worker = button ("Сотрудника") {
                    translateX = 2.0
                    translateY = 5.0
                    action { center<AddWorkers>() }
                }
                val product = button ("Продукт") {
                    translateX = 2.0
                    translateY = 15.0
                    action { center<AddProduct>() }
                }
                val place = button("Участок") {
                    translateX = 2.0
                    translateY = 25.0
                    action { center<AddPlace>() }
                }
                val delete = button ("Удалить") {
                    isDisable = true
                    translateX = 2.0
                    translateY = 35.0
                    action { center<DeleteAny>() }
                }
                val back = button ("Назад") {
                    translateX = 2.0
                    translateY = 45.0
                    action { // в главное меню возвращаемся
                        close()
                        val window = find<MainMenu>()
                        window.openWindow()
                        window.currentStage!!.isResizable = false
                    }
                }
                // Размеры
                setSizeForButton( 70.0, 70.0, worker)
                setSizeForButton( 70.0, 70.0, product)
                setSizeForButton( 70.0, 70.0, place)
                setSizeForButton( 70.0, 70.0, delete)
                setSizeForButton( 70.0, 70.0, back)
                // Стиль
                setStyleForButtonAdd(worker)
                setStyleForButtonAdd(product)
                setStyleForButtonAdd(place)
                setStyleForButtonAdd(delete)
                setStyleForButtonAdd(back)
            }
        }
    }
}
