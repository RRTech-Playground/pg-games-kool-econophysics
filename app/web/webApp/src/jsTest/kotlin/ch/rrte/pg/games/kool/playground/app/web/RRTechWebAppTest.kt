package ch.rrte.pg.games.kool.playground.app.web

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RRTechWebAppTest : FunSpec({

    test("should welcome the future") {
        greet() shouldBe "Hello Future from the RRTech WebApp!"
    }

    xtest("should fail") {
        1+2 shouldBe 4
    }

    xtest("should not run") {
        1+2 shouldBe 3
    }
})