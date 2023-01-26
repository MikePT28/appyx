package com.bumble.appyx.interactions.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.lerp
import com.bumble.appyx.interactions.core.TransitionModel

interface Interpolator<Target, ModelState> : VisibilityInterpolator<Target, ModelState> {


    // TODO produce only visible frames?!
    fun map(
        segment: TransitionModel.Segment<ModelState>
    ): List<FrameModel<Target>>

    // TODO extract along with other interpolation helpers
    companion object {
        fun lerpFloat(start: Float, end: Float, progress: Float): Float =
            start + progress * (end - start)

        fun lerpDpOffset(start: DpOffset, end: DpOffset, progress: Float): DpOffset =
            DpOffset(lerp(start.x, end.x, progress), lerp(start.y, end.y, progress))

        fun lerpDp(start: Dp, end: Dp, progress: Float): Dp =
            Dp(lerpFloat(start.value, end.value, progress))
    }
}
