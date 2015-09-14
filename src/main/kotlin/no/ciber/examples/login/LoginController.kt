package no.ciber.examples.login

import javafx.fxml.FXML
import javafx.scene.text.Text

class LoginController {
    @FXML
    private var actiontarget: Text? = null

    @FXML
    protected fun handleSubmitButtonAction() {
        actiontarget?.setText("Sign in button pressed")
    }
}