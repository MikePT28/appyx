package com.bumble.appyx.interactions.core.inputsource

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.Operation
import com.bumble.appyx.interactions.core.TransitionModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class DebugProgressInputSource<NavTarget, ModelState>(
    private val transitionModel: TransitionModel<NavTarget, ModelState>,
    private val coroutineScope: CoroutineScope,
) : InputSource<NavTarget, ModelState> {
    private val animatable = Animatable(0f)
    private lateinit var result: AnimationResult<Float, AnimationVector1D>
    private var progress: Float = 1f

    override fun operation(operation: Operation<ModelState>) {
        transitionModel.enqueue(operation)
    }

    fun setNormalisedProgress(progress: Float) {
        this.progress = progress.coerceIn(0f, 1f)
        // TODO enforce min 1f in NavModel as a hidden detail rather than here:
        transitionModel.setProgress(1f + this.progress * (transitionModel.maxProgress - 1f))
    }

    fun settle() {
        Logger.log(TAG, "Settle ${progress} to: ${progress.roundToInt().toFloat()}")
        coroutineScope.launch {
            animatable.snapTo(progress)
            result = animatable.animateTo(progress.roundToInt().toFloat(), spring()) {
                setNormalisedProgress(this.value)
            }
        }
    }

    fun stopModel() {
        coroutineScope.launch(Dispatchers.Main) {
            animatable.snapTo(transitionModel.currentProgress)
        }
    }

    private companion object {
        private val TAG = DebugProgressInputSource::class.java.name
    }
}
