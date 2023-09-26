package de.fabmax.kool.demos.demos.physics.vehicle.ui

import de.fabmax.kool.demos.config.Settings
import de.fabmax.kool.demos.demos.physics.vehicle.game.DemoVehicle
import de.fabmax.kool.demos.demos.physics.vehicle.ui.components.Dashboard
import de.fabmax.kool.demos.demos.physics.vehicle.ui.components.Timer
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.MdColor
import de.fabmax.kool.util.MsdfFont

class VehicleUi(val vehicle: DemoVehicle) {

    var onToggleSound: (Boolean) -> Unit = { }

    val dashboard = Dashboard()
    val timerUi = Timer(this)

    private val menuColors = Colors.singleColorDark(
        accent = MdColor.ORANGE,
        background = Color("00000070")
    )

    val uiSurface = UiSurface(colors = menuColors).apply {
        content = {
            Box {
                modifier
                    .backgroundColor(colors.background)
                    .layout(ColumnLayout)

                surface.sizes = getSizes(Settings.uiSize.use().sizes)

                modifier
                    .background(null)
                    .layout(CellLayout)
                    .width(FitContent)
                    .height(FitContent)
                    .align(AlignmentX.Start, AlignmentY.Bottom)
                dashboard()

                surface.popup().apply {
                    modifier
                        .width(FitContent)
                        .height(FitContent)
                        .align(AlignmentX.Center, AlignmentY.Top)
                    timerUi()
                }
            }
        }
    }

    class VehicleUiFonts(baseFontSize: Float) {
        val normalFont = MsdfFont(sizePts = baseFontSize * 1.1f, italic = 0.25f)
        val largeFont = MsdfFont(sizePts = baseFontSize * 3.5f, italic = 0.25f)
        val speedFont = MsdfFont(sizePts = baseFontSize * 5.5f, italic = 0.25f)
    }

    companion object {
        private val sizes = mutableMapOf<Sizes, Sizes>()
        private val fonts = mutableMapOf<Float, VehicleUiFonts>()

        fun getUiFonts(baseFontSize: Float): VehicleUiFonts {
            return fonts.getOrPut(baseFontSize) { VehicleUiFonts(baseFontSize) }
        }

        fun getSizes(globalSizes: Sizes): Sizes {
            return sizes.getOrPut(globalSizes) {
                val fonts = getUiFonts(globalSizes.normalText.sizePts)
                globalSizes.copy(normalText = fonts.normalFont, largeText = fonts.largeFont)
            }
        }
    }
}