package no.ciber.calendar

import javafx.scene.control.ListCell

class CalendarEventListCell : ListCell<CalendarEvent>(){
    override fun updateItem(item: CalendarEvent?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item != null && !empty) {
            textProperty().bind(item.nameProperty)
        }
    }
}