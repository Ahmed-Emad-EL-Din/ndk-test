package com.example.cube

import android.content.Context
import android.opengl.GLSurfaceView
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView

/**
 * A Jetpack Compose wrapper around GLSurfaceView that handles user touch gestures
 * and hooks up the Native C++ OpenGL 3D Cube Renderer.
 */
@Composable
fun CubeView(
    modifier: Modifier = Modifier,
    onFpsUpdated: (Float) -> Unit
) {
    // Remember custom surface view instance to ensure rotation gesture inputs aren't recreated
    AndroidView(
        factory = { ctx ->
            CustomGLSurfaceView(ctx, onFpsUpdated)
        },
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Feed raw drag delta values from Compose to NDK logic
                    NativeCubeLib.addRotation(dragAmount.x, -dragAmount.y)
                }
            }
    )
}

/**
 * Underlying standard GLSurfaceView configured to run OpenGL ES 3.0.
 */
class CustomGLSurfaceView(
    context: Context,
    onFpsUpdated: (Float) -> Unit
) : GLSurfaceView(context) {

    init {
        // Request an OpenGL ES 3.0 compatible context
        setEGLContextClientVersion(3)
        
        // Preserve context when paused (prevents shader re-creation overhead on rotate/switch)
        preserveEGLContextOnPause = true

        // Mount our JNI-powered renderer
        setRenderer(NativeCubeRenderer(onFpsUpdated))

        // Render continuously to animate rotating cube states smoothly
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}
