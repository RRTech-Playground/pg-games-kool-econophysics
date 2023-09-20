package ch.rrte.pg.games.kool.playground.app.desktop

import ch.rrte.pg.games.kool.playground.compose.desktop.app.RRTechDesktopApp
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RRTechVrAppDesktopTest : FunSpec({

    test("should welcome the future") {
        val app = RRTechDesktopApp()
        app.greet shouldBe "Hello Future from RRTech Desktop App!"
    }

    xtest("should fail") {
        1+2 shouldBe 4
    }

    xtest("should not run") {
        1+2 shouldBe 3
    }
})
