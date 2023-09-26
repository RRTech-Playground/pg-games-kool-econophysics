package de.fabmax.kool.demos.demos.creative.creativecoding.content

import de.fabmax.kool.modules.ui2.UiScope
import de.fabmax.kool.scene.Node

abstract class CreativeContent(name: String) : Node(name) {

      abstract fun UiScope.settingsMenu()

      override fun toString(): String {
          return name
      }
}