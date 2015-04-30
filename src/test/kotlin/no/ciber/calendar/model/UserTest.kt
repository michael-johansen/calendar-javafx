package no.ciber.calendar.model

import javafx.collections.FXCollections
import no.ciber.util.liveFiltered
import org.junit.Assert
import org.junit.Test

class UserTest {


    Test fun canFilterList() {
        val one = getUser("1", "Donald", "Duck", false)
        val two = getUser("2", "Dolly", "Duck", false)

        val masterList = FXCollections.observableArrayList(one, two)
        val filteredList = masterList.liveFiltered({ it.selectedProperty }, { it.selected })

        Assert.assertEquals(filteredList.size(), 0)
        one.selected = true
        Assert.assertEquals(filteredList.size(), 1)
    }

    private fun getUser(id: String, firstName: String, lastName: String, selected: Boolean): User {
        val user = User()
        user.id = id
        user.firstname = firstName
        user.lastname = lastName
        user.selected = selected
        return user
    }

}