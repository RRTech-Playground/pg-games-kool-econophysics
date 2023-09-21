package th0t_sl4y3r.koolAimTrainer

import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.physics.RigidStatic
import de.fabmax.kool.physics.Shape
import de.fabmax.kool.physics.geometry.BoxGeometry
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.scene.colorMesh
import de.fabmax.kool.util.MdColor
import de.fabmax.kool.util.Time
import kotlin.random.Random

class CubesSpawner{
    var spawnTime=1.5f
    val maxSpawnTime=1.5f


    fun update(scene: Scene){
        spawnTime-= Time.deltaT
        if (spawnTime<=0){
            //spawn a cube
            val boxGeom= BoxGeometry(Vec3f(1f, 1f, 1f))
            //create a box at a random position
            val actor= RigidStatic().apply {
                attachShape(Shape(boxGeom))
                position= Vec3f(
                    Random.nextInt(-10, 10) * 1f,
                    Random.nextInt(-10, 10) * 1f, 0f
                )
            }
            world.addActor(actor)

            val mesh=scene.colorMesh {
                //tags.put("timer", 1f)
                generate {
                    color = MdColor.GREEN
                    boxGeom.generateMesh(this)
                }

                shader = KslPbrShader {
                    color { vertexColor() }
                }

                onUpdate {
                    transform.set(actor.transform)

                }
            }
            //give the actor a reference to its mesh, so that it can be deleted
            //on hit
            actor.tags.put("mesh", mesh)

            spawnTime=maxSpawnTime
        }
    }
}