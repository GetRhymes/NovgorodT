package generalStyle

import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
// методы для установки размеров для различнвх компонентов, из названия следует какому компоненту соответвует
fun setSizeForLabel(width: Double, height: Double, label: Label) {
    label.minWidth = width
    label.prefWidth = width
    label.maxWidth = width

    label.minHeight = height
    label.maxHeight = height
    label.prefHeight = height
}

fun setSizeForVBox(width: Double, height: Double, vBox: VBox) {
    if (height != 0.0) {
        vBox.minWidth = width
        vBox.prefWidth = width
        vBox.maxWidth = width

        vBox.maxHeight = vBox.minHeight
        vBox.prefHeight = vBox.minHeight
    } else {
        vBox.minWidth = width
        vBox.prefWidth = width
        vBox.maxWidth = width

        vBox.maxHeight = height
        vBox.prefHeight = height
        vBox.minHeight = height
    }
}

fun setSizeForAnchor(width: Double, height: Double, anchorPane: AnchorPane) {
    if (height != 0.0) {
        anchorPane.minWidth = width
        anchorPane.prefWidth = width
        anchorPane.maxWidth = width

        anchorPane.maxHeight = anchorPane.minHeight
        anchorPane.prefHeight = anchorPane.minHeight
    } else {
        anchorPane.minWidth = width
        anchorPane.prefWidth = width
        anchorPane.maxWidth = width

        anchorPane.maxHeight = height
        anchorPane.prefHeight = height
        anchorPane.minHeight = height
    }

}

fun setSizeForTextField(width: Double, height: Double, textField: TextField) {
    textField.prefWidth = width
    textField.maxWidth = width
    textField.minWidth = width

    textField.prefHeight = height
    textField.maxHeight = height
    textField.minHeight = height
}
fun setSizeForToggleButton(width: Double, height: Double, toggleButton: ToggleButton) {
    toggleButton.prefWidth = width
    toggleButton.maxWidth = width
    toggleButton.minWidth = width

    toggleButton.prefHeight = height
    toggleButton.maxHeight = height
    toggleButton.minHeight = height
}

fun setSizeForDatePicker(width: Double, height: Double, datePicker: DatePicker) {
    datePicker.prefWidth = width
    datePicker.maxWidth = width
    datePicker.minWidth = width

    datePicker.prefHeight = height
    datePicker.maxHeight = height
    datePicker.minHeight = height
}

fun setSizeForButton(width: Double, height: Double, button: Button) {
    button.prefWidth = width
    button.maxWidth = width
    button.minWidth = width

    button.prefHeight = height
    button.maxHeight = height
    button.minHeight = height
}

fun setSizeForPasswordField(width: Double, height: Double, passwordField: PasswordField) {
    passwordField.prefWidth = width
    passwordField.maxWidth = width
    passwordField.minWidth = width

    passwordField.prefHeight = height
    passwordField.maxHeight = height
    passwordField.minHeight = height
}