package th0t_sl4y3r.koolAimTrainer

import de.fabmax.kool.KoolContext
import de.fabmax.kool.input.InputStack
import de.fabmax.kool.input.PointerState
import de.fabmax.kool.math.Ray
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.physics.ConstantPhysicsStepperSync
import de.fabmax.kool.physics.HitResult
import de.fabmax.kool.physics.PhysicsWorld
import de.fabmax.kool.scene.*
import de.fabmax.kool.util.*

/**
 * Main application entry. This demo creates a small example scene, which you probably want to replace by your actual
 * game / application content.
 */

lateinit var world:PhysicsWorld
var stepper=ConstantPhysicsStepperSync()
//ui stuff
var score=0
var cubesSpawner= CubesSpawner()

fun launchApp(ctx: KoolContext) {
    // add a hello-world demo scene
    ctx.scenes += scene {
        //init the world
        world =PhysicsWorld(this).apply {
            simStepper= stepper
            gravity= Vec3f.ZERO
        }

        camera.apply {
            position.set(0f, 0f, 35f)
            clipNear = 0.5f
            clipFar = 500f
            lookAt.set(Vec3f(0f, 0f, 0f))
        }
        //lighting.singleLight {
        //    setDirectional(Vec3f(-1f, -1f, -1f))
        //    setColor(Color.WHITE, 5f)
        //}
        // Kool version 0.12.1-SNAPSHOT
        lighting.singleDirectionalLight {
            setup(Vec3f(-1f, -1f, -1f))
            setColor(Color.WHITE, 5f)
        }
        //this should work for mouse input
        val mouseListener= MyMouseListener(this)
        InputStack.defaultInputHandler.pointerListeners+=mouseListener
        onUpdate{
            cubesSpawner.update(this)
        }
    }

    ctx.scenes+= UiScene{
        setupUiScene()

        Panel {
            //simple ui to display the score
            val font = MsdfFont(sizePts = 20f,
                glowColor = colors.secondary.withAlpha(0.5f))
            var scoreText by remember(0)

            modifier.size(150.dp, 50.dp).align(AlignmentX.Start, AlignmentY.Top)

            Text("Score: $scoreText") {
                modifier.backgroundColor(MdColor.LIGHT_GREEN)
                modifier.alignX(AlignmentX.Center)
                modifier.textAlign(AlignmentX.Center, AlignmentY.Center)
                modifier.font(font)
                modifier.size(250.dp, 80.dp)
                modifier.textColor(MdColor.DEEP_ORANGE)
                onUpdate {
                    scoreText= score
                }
            }
        }
    }

}

private class MyMouseListener(var scene:Scene):InputStack.PointerListener{
    //ray that will shoot from th camera
    val pickRay=Ray()
    //hit result to check the raycasted actor
    var hitResult=HitResult()

    override fun handlePointer(pointerState: PointerState, ctx: KoolContext) {
        val shootPtr=pointerState.primaryPointer
        if(shootPtr.isLeftButtonClicked){
            scene.camera.computePickRay(pickRay, shootPtr,
                scene.mainRenderPass.viewport,ctx)
            //now raycast it inside the physics world
            world.raycast(pickRay, 1000f, hitResult)
            //watch out, for trigger actors it won't detect them, there must be another way
            //for this example I'll just set them to non trigger
            if (hitResult.isHit){
                //works
                val actorMesh= hitResult.nearestActor!!.tags["mesh"] as Mesh
                //whether you switch these two's order or not, it doesn't change,
                //still works
                actorMesh.dispose(ctx)
                scene.removeNode(actorMesh)

                world.removeActor(hitResult.nearestActor!!)
                score++
            }
        }
    }

}