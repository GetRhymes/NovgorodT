import dataBase.DatabaseHandler
import generalStyle.setSizeForButton
import generalStyle.setSizeForLabel
import generalStyle.setSizeForPasswordField
import generalStyle.setSizeForTextField
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class LogIn : Fragment("Введите имя и пароль") {
    override val root = borderpane {
// Окно авторизации
        val listUsers = mutableListOf<String>() // лист пользователей
        val dbHandler = DatabaseHandler()
        val requestAuth = dbHandler.getAuth()
        while (requestAuth!!.next()) {
            listUsers.add(requestAuth.getString(2))
        } // делаю вызов с бд , запрос на пользователей, и закидываю в лист
        center {
            anchorpane {
                setPrefSize(600.0, 400.0) // 600 и 400 размеры окна авторизации, не завожу константы потому что нет смысла выделять память на эти значения так как часто могу менять их
                setMaxSize(600.0, 400.0)
                setMinSize(600.0, 400.0)
                translateY = 130.0 // перемещение объекта в рамках окна по оси Y, не завожу в переменную потому что часто использую этот параметр в разных фреймах и под каждую заводить переменную нет смысла
                translateX = 200.0 // аналогично комменту выше, но здесь перемещение по оси X
                vbox {
                    val loginLb = label("Логин") { alignment = Pos.BOTTOM_LEFT }
                    val loginFd = textfield { promptText = "Введите логин" }
                    val passLb = label("Пароль") { alignment = Pos.BOTTOM_LEFT }
                    val passFd = passwordfield { promptText = "Введите пароль" }
                    val logIn = button("Войти") {
                        translateY = 10.0 // аналогично комменту выше,  перемещение по оси Y
                        style { fontWeight = FontWeight.BOLD }
                        action {
                            if (loginFd.text == listUsers[0] || loginFd.text == listUsers[1]) { // делаю проверку на юзера
                                close() // закрываю окно авторизации
                                val window = find<MainMenu>() // ищу главное меню
                                window.openWindow() // Открываю его
                                window.currentStage!!.isResizable = false // запрещаю изменение размера окна, так как работаю с фиксированной величиной, чтобы не делать оптимизацию под разные окна
                            }
                        }
                    }
                    // Label
                    setSizeForLabel(200.0, 25.0, loginLb) // установка размера лейба 200 и 25 ширина и высота, не завожу константы, так как частая корректировка размеров и они уникальны
                    setSizeForLabel(200.0, 25.0, passLb) // установка размера лейба 200 и 25 ширина и высота, не завожу константы, так как частая корректировка размеров и они уникальны
                    // Text_Field
                    setSizeForTextField(200.0, 25.0, loginFd) // установка размера текстфилда 200 и 25 ширина и высота, не завожу константы, так как частая корректировка размеров и они уникальны
                    // Password_Field
                    setSizeForPasswordField(200.0, 25.0, passFd) // установка размера пассвордфилда 200 и 25 ширина и высота, не завожу константы, так как частая корректировка размеров и они уникальны
                    // Button
                    setSizeForButton(200.0, 25.0, logIn) // установка размера кнопки 200 и 25 ширина и высота, не завожу константы, так как частая корректировка размеров и они уникальны
                }
            }
        }
    }
}

