package generalStyle

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.box
import tornadofx.multi
import tornadofx.px
import tornadofx.style
// методы для установки некоторых стилей для неокторых компонентов (из названия следует название компонента)
fun setStyleForLabel(label: Label, hexSecond: String) {
    label.alignment = Pos.CENTER
    label.style {
        borderColor += box(
            top = Color.TRANSPARENT, // прозрачный цвет
            right = Color.TRANSPARENT,
            left = Color.TRANSPARENT,
            bottom = Color.BLACK
        )
        backgroundColor = multi(Paint.valueOf(hexSecond))
    }
}

fun setStyleForScrollPane(scrollPane: ScrollPane) {
    scrollPane.style {
        backgroundColor = multi(Color.TRANSPARENT) // фон прозрачный
        padding = box(0.px)
    }
    scrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER // использовать горизонтольный скролл == никогда
}

fun setStyleForButtonAdd(button: Button) {
    button.style {
        padding = box(2.px) // отступ от краев
        fontSize = 10.px
        fontWeight = FontWeight.BOLD
        wrapText = true // перенос текста
        alignment = Pos.CENTER
        textAlignment = TextAlignment.CENTER
    }
}