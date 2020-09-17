package com.rabross.noise

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import com.rabross.noise.generator.KotlinRandomNoiseGenerator

class NoiseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var pelSize: Int = PEL_SIZE_DEFAULT

    private val noiseEngine: NoiseEngine

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

        noiseEngine = NoiseEngine(holder, pelSize, KotlinRandomNoiseGenerator())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        noiseEngine.start()
    }

    override fun onDetachedFromWindow() {
        noiseEngine.stop()
        super.onDetachedFromWindow()
    }

    companion object {
        const val PEL_SIZE_DEFAULT = 4
    }
}