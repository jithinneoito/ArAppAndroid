package com.example.myarapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.material.setExternalTexture
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.VideoNode

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    lateinit var placeSofaButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode
    private lateinit var modelSofNode: ArModelNode
    private lateinit var videoNode: VideoNode
    private lateinit var mediaPlayer:MediaPlayer


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.ad)

        placeButton = findViewById(R.id.place)
        placeSofaButton = findViewById(R.id.placeSofa)

        placeButton.setOnClickListener {
            placeModel()
        }
        placeSofaButton.setOnClickListener {
            placeSofaModel()
        }

//        videoNode = VideoNode(sceneView.engine, scaleToUnits = 0.7f, centerOrigin = Position(y=-4f), glbFileLocation = "models/plane.glb", player = mediaPlayer, onLoaded = {_,_ ->
//            mediaPlayer.start()
//        })

        modelNode = ArModelNode(sceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/spiderthing_take_3.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            )
            {
                sceneView.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
//            onAnchorChanged = {
//                placeButton.isGone = it != null
//            }

        }

        modelSofNode = ArModelNode(sceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/sofa_single.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)
            )
            {
                sceneView.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
//            onAnchorChanged = {
//                placeSofaButton.isGone = it != null
//            }

        }

//        modelNode.addChild(videoNode)

    }

    private fun placeModel(){
        modelNode.anchor()
        sceneView.addChild(modelNode)
        sceneView.removeChild(modelSofNode)
        sceneView.planeRenderer.isVisible = false
        placeSofaButton.isGone = false

    }

    private fun placeSofaModel(){
        modelSofNode.anchor()
        sceneView.addChild(modelSofNode)
        sceneView.removeChild(modelNode)
        sceneView.planeRenderer.isVisible = false
        placeButton.isGone = false

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}