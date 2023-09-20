package ch.rrte.pg.games.kool.playground.app.web

import ch.rrte.pg.games.kool.playground.app.web.utils.canvas.BrowserViewportWindow
import ch.rrte.pg.games.kool.playground.core.ui.components.example.HelloFutureComposable
import org.jetbrains.skiko.wasm.onWasmReady

fun greet() = "Hello Future from the RRTech WebApp!"

fun main() {

    console.log(greet())

    onWasmReady {
        BrowserViewportWindow(title = "RRTech WebApp") {
            HelloFutureComposable()
        }
    }
}
