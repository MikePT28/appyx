package com.bumble.appyx.routingsourcedemos.tiles.operation

import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.routingsourcedemos.tiles.Tiles
import com.bumble.appyx.routingsourcedemos.tiles.TilesElements
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deselect<T : Any>(
    private val key: RoutingKey<T>
) : TilesOperation<T> {

    override fun isApplicable(elements: TilesElements<T>): Boolean = true

    override fun invoke(
        elements: TilesElements<T>
    ): RoutingElements<T, Tiles.TransitionState> =
        elements.map {
            if (it.key == key && it.targetState == Tiles.TransitionState.SELECTED) {
                it.transitionTo(
                    targetState = Tiles.TransitionState.STANDARD,
                    operation = this
                )
            } else {
                it
            }
        }
}

fun <T : Any> Tiles<T>.deselect(key: RoutingKey<T>) {
    accept(Deselect(key))
}
