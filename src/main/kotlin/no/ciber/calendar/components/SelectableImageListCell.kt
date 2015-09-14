package no.ciber.calendar.components

import javafx.beans.property.BooleanProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.control.cell.CheckBoxListCell
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority

/**
 * This software is written by Michael on 01/05/2015.
 */
public class SelectableImageListCell<T>(
        val url: (T) -> ObservableValue<String>,
        val selected: (T) -> BooleanProperty,
        val text: (T) -> ObservableValue<String>
) : ListCell<T>() {
    val checkbox: CheckBox = CheckBox()
    val image: ImageView = ImageView()
    val label: Label = Label()
    val group = HBox()

    override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            setGraphic(null);
        } else {
            checkbox.selectedProperty().bindBidirectional(selected.invoke(item!!))
            image.setImage(Image(url.invoke(item).getValue()))
            label.textProperty().bind(text.invoke(item))
            group.getChildren().clear()
            group.getChildren().addAll(arrayOf(checkbox, label, createFillPane(), image))
            this.setGraphic(group);
        }
    }

    private fun createFillPane(): Pane {
        val pane = Pane()
        HBox.setHgrow(pane, Priority.ALWAYS)
        return pane
    }
}
