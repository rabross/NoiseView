package com.rabross.noise

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.rabross.noise.generator.KotlinRandom

class NoiseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private val noise: Noise by lazy {
        NoiseEngine(holder, pelSize, KotlinRandom())
    }

    private var pelSize: Int = PEL_SIZE_DEFAULT

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.NoiseView, 0, 0
        ).apply {
            try {
                pelSize = getInteger(R.styleable.NoiseView_pelSize, pelSize)
            } finally {
                recycle()
            }
        }
    }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        noise.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        noise.onSizeChanged(p2, p3)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        noise.stop()
    }

    companion object {
        const val PEL_SIZE_DEFAULT = 4
    }
}