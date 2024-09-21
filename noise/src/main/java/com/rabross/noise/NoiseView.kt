package com.rabross.noise

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.rabross.noise.generator.KotlinRandomNoiseGenerator
import com.rabross.noise.renderer.JVMNoiseRenderer
import com.rabross.noise.renderer.NativeNoiseRenderer

class NoiseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var pelSize: Int = PEL_SIZE_DEFAULT
    private var rendererType = RENDERER_TYPE_DEFAULT

    private var renderThread: RenderThread

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.NoiseView, 0, 0
        ).apply {
            try {
                pelSize = getInteger(R.styleable.NoiseView_pelSize, PEL_SIZE_DEFAULT)
                rendererType = getInt(R.styleable.NoiseView_renderer, 1)
            } finally {
                recycle()
            }
        }

        val renderer = when(rendererType) {
            RENDERER_TYPE_NATIVE -> NativeNoiseRenderer(holder, pelSize)
            else -> JVMNoiseRenderer(holder, KotlinRandomNoiseGenerator(),  pelSize)
        }

        renderThread = RunnableRenderThread(SurfaceHolderAdapterRenderer(holder, renderer))

        holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {
                renderThread.resume()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                renderThread.pause()
                holder.surface.release()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }
        } )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        renderThread.start()
    }

    override fun onDetachedFromWindow() {
        renderThread.stop()
        super.onDetachedFromWindow()
    }

    companion object {
        const val RENDERER_TYPE_JVM = 0
        const val RENDERER_TYPE_NATIVE = 1

        const val PEL_SIZE_DEFAULT = 4
        const val RENDERER_TYPE_DEFAULT = RENDERER_TYPE_NATIVE
    }
}