package de.fabmax.kool.demos.ui.menu.components

import de.fabmax.kool.demos.config.Settings
import de.fabmax.kool.demos.ui.LabeledSwitch
import de.fabmax.kool.demos.ui.MenuRow
import de.fabmax.kool.demos.ui.UiSizes
import de.fabmax.kool.demos.ui.labelStyle
import de.fabmax.kool.demos.ui.menu.DemoMenu
import de.fabmax.kool.demos.ui.menu.renderer.TitleBgRenderer
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.MsdfFont

class SettingsContent(val menu: DemoMenu) : Composable {
    override fun UiScope.compose() = Column {
        modifier
            .height(Grow.Std)
            .width(Grow.Std)

        Text("Settings") {
            modifier
                .width(Grow.Std)
                .height(UiSizes.baseSize)
                .padding(horizontal = UiSizes.hGap)
                .textAlignY(AlignmentY.Center)
                .background(TitleBgRenderer(DemoMenu.titleBgMesh, 0.75f, 0.95f))
                .font((sizes.largeText as MsdfFont).copy(glowColor = DemoMenu.titleTextGlowColor))
                .textColor(Color.WHITE)
        }

        MenuRow {
            Text("UI size") { labelStyle() }
            ComboBox {
                val uiSizeOptions = Settings.defaultUiSizes.values.toList()
                modifier
                    .width(Grow.Std)
                    .margin(start = sizes.largeGap)
                    .items(uiSizeOptions)
                    .selectedIndex(uiSizeOptions.indexOf(Settings.uiSize.use()))
                    .onItemSelected {
                        Settings.uiSize.set(uiSizeOptions[it])
                    }
            }
        }
        LabeledSwitch("Menu initially expanded", Settings.showMenuOnStartup)
        LabeledSwitch("Debug overlay", Settings.showDebugOverlay)
        LabeledSwitch("Fullscreen", Settings.isFullscreen)
        LabeledSwitch("Hidden demos", Settings.showHiddenDemos)

        menu.demoLoader.activeDemo?.let {
            LabeledSwitch("Show demo menu", it.isMenu)
        }
    }
}