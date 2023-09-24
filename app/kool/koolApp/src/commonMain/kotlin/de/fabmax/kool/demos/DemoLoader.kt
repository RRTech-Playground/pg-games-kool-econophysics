package de.fabmax.kool.demos

import de.fabmax.kool.KoolContext
import de.fabmax.kool.demos.config.Settings
import de.fabmax.kool.demos.demos.DemoScene
import de.fabmax.kool.demos.demos.Demos
import de.fabmax.kool.input.PointerInput
import de.fabmax.kool.physics.Physics
import de.fabmax.kool.util.DebugOverlay
import de.fabmax.kool.util.Time
import de.fabmax.kool.demos.ui.LoadingScreen
import de.fabmax.kool.demos.ui.UiSizes
import de.fabmax.kool.demos.ui.menu.DemoMenu

fun demo(startScene: String? = null, ctx: KoolContext) {
    // launch demo
    var demo = startScene
    if (demo != null) {
        demo = demo.lowercase()
        if (demo.endsWith("demo")) {
            demo = demo.substring(0, demo.length - 4)
        }
    }
    DemoLoader(ctx, demo)
}


class DemoLoader(ctx: KoolContext, startScene: String? = null) {

    val dbgOverlay = DebugOverlay(DebugOverlay.Position.LOWER_RIGHT)
    val menu = DemoMenu(this)

    private val loadingScreen = LoadingScreen(ctx)
    private var currentDemo: Pair<String, DemoScene>? = null
    private var switchDemo: Demos.Entry? = null

    private var initShownMenu = false
    private var shouldAutoHideMenu = 2.5f

    val activeDemo: DemoScene?
        get() = currentDemo?.second

    init {
        Physics.loadPhysics()
        Settings.loadSettings()

        ctx.scenes += dbgOverlay.ui
        ctx.scenes += menu.ui
        ctx.onRender += this::onRender

        val loadScene = startScene ?: Settings.selectedDemo.value
        val loadDemo = Demos.demos[loadScene] ?: Demos.demos[Demos.defaultDemo]!!
        switchDemo = loadDemo
    }

    fun loadDemo(demo: Demos.Entry) {
        if (demo.id != currentDemo?.first) {
            switchDemo = demo
        }
    }

    private fun onRender(ctx: KoolContext) {

        applySetting(ctx)

        switchDemo?.let { newDemo ->
            Settings.selectedDemo.set(newDemo.id)

            // release old demo
            currentDemo?.second?.let { demo ->
                demo.scenes.forEach {
                    ctx.scenes -= it
                    it.dispose(ctx)
                }
                demo.menuUi?.let {
                    menu.ui -= it
                    it.dispose(ctx)
                }
            }
            ctx.scenes.add(0, loadingScreen)

            // set new demo
            currentDemo = newDemo.id to newDemo.newInstance(ctx).also {
                it.demoEntry = newDemo
                it.demoLoader = this
                it.loadingScreen = loadingScreen
            }
            switchDemo = null
        }

        currentDemo?.second?.let {
            if (it.demoState != DemoScene.State.RUNING) {
                it.checkDemoState(this, ctx)
                if (it.demoState == DemoScene.State.RUNING) {
                    // demo setup complete -> add scenes
                    ctx.scenes -= loadingScreen
                    it.scenes.forEachIndexed { i, s -> ctx.scenes.add(i, s)}
                }
            } else {
                // demo fully loaded - handle Menu state
                if (shouldAutoHideMenu > 0f) {
                    shouldAutoHideMenu -= Time.deltaT
                    if (Settings.showMenuOnStartup.value) {
                        if (!initShownMenu) {
                            menu.isExpanded = true
                            initShownMenu = true
                        }
                        val ptr = PointerInput.primaryPointer
                        if (shouldAutoHideMenu <= 0f && (!ptr.isValid || ptr.x > UiSizes.menuWidth.px)) {
                            menu.isExpanded = false
                        }
                    }
                }
            }
        }
    }

    private fun applySetting(ctx: KoolContext) {
        if (Settings.isFullscreen.value != ctx.isFullscreen) {
            ctx.isFullscreen = Settings.isFullscreen.value
        }
        dbgOverlay.ui.isVisible = Settings.showDebugOverlay.value
    }

    companion object {

        val demoProps = mutableMapOf<String, Any>()

        val assetStorageBase: String
            get() = getProperty("asset.base", "https://kool.blob.core.windows.net/kool-demo")

        val hdriPath: String
            get() = getProperty("assets.hdri", "$assetStorageBase/hdri")

        val materialPath: String
            get() = getProperty("assets.materials", "$assetStorageBase/materials")

        val modelPath: String
            get() = getProperty("assets.models", "$assetStorageBase/models")

        val heightMapPath: String
            get() = getProperty("assets.heightmaps", "$assetStorageBase/heightmaps")

        val soundPath: String
            get() = getProperty("sounds", "$assetStorageBase/sounds")

        fun setProperty(key: String, value: Any) {
            demoProps[key] = value
        }

        inline fun <reified T> getProperty(key: String, default: T): T {
            return demoProps[key] as? T ?: default
        }
    }
}