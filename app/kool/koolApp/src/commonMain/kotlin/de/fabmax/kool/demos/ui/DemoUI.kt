package de.fabmax.kool.demos.ui

import de.fabmax.kool.demos.config.Settings
import de.fabmax.kool.modules.ui2.Dp

object UiSizes {
    val hGap: Dp get() = Settings.uiSize.value.sizes.gap * 1.25f
    val vGap: Dp get() = Settings.uiSize.value.sizes.gap

    val baseSize: Dp get() = Settings.uiSize.value.sizes.gap * 4f
    val menuWidth: Dp get() = baseSize * 7f
}

//Todo: implement menu
//fun UiScope.MenuRow
//fun UiScope.MenuSlider
//fun UiScope.MenuSlider2
//fun UiScope.LabeledSwitch

//Todo: implement TextScope
//fun TextScope.selectionTitleStyle
//fun TextScope.labelStyle