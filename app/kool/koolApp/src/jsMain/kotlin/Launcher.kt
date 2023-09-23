import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfig
import de.fabmax.kool.demos.demo
import kotlinx.browser.window
import template.launchApp
//import th0t_sl4y3r.koolAimTrainer.launchApp

/**
 * JS main function / app entry point: Creates a new KoolContext (with optional platform-specific configuration) and
 * forwards it to the common-code launcher.
 */
fun main() = KoolApplication(
    config = KoolConfig(
        canvasName = "glCanvas",
        isGlobalKeyEventGrabbing = true
    )
) { ctx ->
    //launchApp(ctx)

    // launch demo
    demo("demo", ctx)
    //demo(getParams()["demo"], ctx)
}

// I have absolutely no clue what following code does - for demo
fun getParams(): Map<String, String> {
    val params: MutableMap<String, String> = mutableMapOf()
    if (window.location.search.length > 1) {
        val vars = window.location.search.substring(1).split("&").filter { it.isNotBlank() }
        for (pair in vars) {
            val keyVal = pair.split("=")
            val keyEnc = keyVal[0]
            val key = js("decodeURIComponent(keyEnc)").toString()
            val value = if (keyVal.size == 2) {
                val valEnc = keyVal[1]
                js("decodeURIComponent(valEnc)").toString()
            } else {
                ""
            }
        }
    }
    return params
}