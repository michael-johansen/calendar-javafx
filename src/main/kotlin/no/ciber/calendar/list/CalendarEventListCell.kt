package no.ciber.calendar.list

import javafx.scene.control.ListCell
import no.ciber.calendar.model.CalendarEvent

class CalendarEventListCell : ListCell<CalendarEvent>(){
    override fun updateItem(item: CalendarEvent?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item != null && !empty) {
            textProperty().bind(item.nameProperty)
        } else{
            textProperty().unbind()
            textProperty().set("");
        }
    }
}