package ch.rrte.pg.games.kool.playground.compose.desktop.app

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import ch.rrte.pg.games.kool.playground.core.ui.components.example.HelloFutureComposable

class RRTechDesktopApp {
    val greet: String
        get() {
            return "Hello Future from RRTech Desktop App!"
        }
}

fun main() {

   singleWindowApplication(
        title = "RRTech Desktop App",
        state = WindowState(size = DpSize(1200.dp, 800.dp))
        //state = WindowState(size = DpSize.Unspecified)
    ) {
       HelloFutureComposable()
    }
}
