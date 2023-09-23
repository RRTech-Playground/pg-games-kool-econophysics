package de.fabmax.kool.demos.demos.hello

import de.fabmax.kool.KoolContext
import de.fabmax.kool.demos.demos.DemoScene
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.scene.addColorMesh
import de.fabmax.kool.scene.defaultOrbitCamera
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.Time

class HelloWorldDemo : DemoScene("Hello World") {

    override fun Scene.setupMainScene(ctx: KoolContext) {

        // enable simple camera mouse control
        defaultOrbitCamera()

        // add a spinning color cube to the scene
        addColorMesh {
            generate {
                // called once on init: generates a cube with different (vertex-)colors assigned to each side
                cube {
                    colored()
                }
            }

            // assign a shader, which uses the vertex color info
            shader = KslPbrShader {
                color { vertexColor() }
                metallic(0f)
                roughness(0.25f)
            }

            onUpdate {
                transform.rotate(Time.deltaT * 45f, Vec3f.X_AXIS)
            }
        }

        // set up a single light source
        lighting.singleDirectionalLight {
            setup(Vec3f(-1f, -1f, -1f))
            setColor(Color.WHITE, 5f)
        }
    }
}