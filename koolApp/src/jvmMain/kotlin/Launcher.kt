import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.GL_TRUE
import template.launchApp

/**
 * JVM main function / app entry point: Creates a new KoolContext (with optional platform-specific configuration) and
 * forwards it to the common-code launcher.
 */
fun main() = KoolApplication(
    config = KoolConfigJvm(
        windowTitle = "kool Template App",

    )
) {
    launchApp(ctx)
}