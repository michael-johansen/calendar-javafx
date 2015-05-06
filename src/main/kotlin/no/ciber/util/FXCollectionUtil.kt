package no.ciber.util

import javafx.beans.property.BooleanProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList

public fun <E, S : ObservableValue<*>> ObservableList<E>.liveFiltered(accessor: (E) -> S, predicate: (E) -> Boolean): FilteredList<E> {
    val filteredList = this.filtered(predicate)
    this.map {
        accessor.invoke(it)
    }.forEach {
        it.addListener {
            o, v1, v2 -> filteredList.setPredicate(predicate)
        }
    }
    return filteredList
}