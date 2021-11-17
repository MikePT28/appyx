package com.github.zsoltk.composeribs.client.backstack

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStack
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStackFader
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStackSlider
import com.github.zsoltk.composeribs.core.routing.source.backstack.operation.Replace
import com.github.zsoltk.composeribs.core.routing.transition.ModifierTransitionHandler
import com.github.zsoltk.composeribs.core.routing.transition.TransitionDescriptor

class BackStackExampleTransitionHandler<T> :
    ModifierTransitionHandler<T, BackStack.TransitionState>(clipToBounds = true) {

    private val slider = BackStackSlider<T>(clipToBounds = clipToBounds)
    private val fader = BackStackFader<T>()

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<BackStack.TransitionState>,
        descriptor: TransitionDescriptor<T, BackStack.TransitionState>
    ): Modifier =
        when (descriptor.operation) {
            is Replace -> fader.createModifier(modifier, transition, descriptor)
            else -> slider.createModifier(modifier, transition, descriptor)
        }
}

@Composable
fun <T> rememberBackStackExampleTransitionHandler(
): ModifierTransitionHandler<T, BackStack.TransitionState> = remember {
    BackStackExampleTransitionHandler()
}
