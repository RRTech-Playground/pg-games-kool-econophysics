package de.fabmax.kool.demos.ui

import de.fabmax.kool.demos.config.Settings
import de.fabmax.kool.modules.ui2.*

object UiSizes {
    val hGap: Dp get() = Settings.uiSize.value.sizes.gap * 1.25f
    val vGap: Dp get() = Settings.uiSize.value.sizes.gap

    val baseSize: Dp get() = Settings.uiSize.value.sizes.gap * 4f
    val menuWidth: Dp get() = baseSize * 7f
}

fun UiScope.MenuRow(vGap: Dp = UiSizes.vGap, block: UiScope.() -> Unit) {
    Row(width = Grow.Std) {
        modifier.margin(horizontal = UiSizes.hGap, vertical = vGap)
        block()
    }
}
//Todo: implement menu
//fun UiScope.MenuSlider
//fun UiScope.MenuSlider2

fun UiScope.LabeledSwitch(label: String, toggleState: MutableStateValue<Boolean>, onToggle: ((Boolean) -> Unit)? = null) {
    MenuRow {
        Text(label) {
            labelStyle(Grow.Std)
            modifier.onClick {
                toggleState.toggle()
                onToggle?.invoke(toggleState.value)
            }
        }
        Switch(toggleState.use()) {
            modifier
                .alignY(AlignmentY.Center)
                .onToggle {
                    toggleState.set(it)
                    onToggle?.invoke(toggleState.value)
                }
        }
    }
}

//Todo: implement TextScope
//Todo: implement menu
fun TextScope.sectionTitleStyle() {
    modifier
        .width(Grow.Std)
        .margin(vertical = UiSizes.hGap)    // hGap is intentional, since we want a little more spacing around titles
        .padding(vertical = sizes.smallGap)
        .textColor(colors.primary)
        .backgroundColor(colors.primaryVariant.withAlpha(0.2f))
        .font(sizes.largeText)
        .textAlignX(AlignmentX.Center)
}

fun TextScope.labelStyle(width: Dimension = FitContent) {
    modifier
        .width(width)
        .align(yAlignment = AlignmentY.Center)
        .padding(vertical = sizes.smallGap * 0.5f)
}