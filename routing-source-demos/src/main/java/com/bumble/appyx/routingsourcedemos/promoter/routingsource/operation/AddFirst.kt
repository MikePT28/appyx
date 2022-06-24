package com.bumble.appyx.routingsourcedemos.promoter.routingsource.operation

import android.system.Os.accept
import com.bumble.appyx.routingsourcedemos.promoter.routingsource.Promoter
import com.bumble.appyx.routingsourcedemos.promoter.routingsource.Promoter.TransitionState.CREATED
import com.bumble.appyx.routingsourcedemos.promoter.routingsource.PromoterElement
import com.bumble.appyx.routingsourcedemos.promoter.routingsource.PromoterElements
import com.bumble.appyx.core.routing.RoutingElements
import com.bumble.appyx.core.routing.RoutingKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class AddFirst<T : Any>(
    private val element: @RawValue T
) : PromoterOperation<T> {

    override fun isApplicable(elements: PromoterElements<T>): Boolean =
        true

    override fun invoke(
        elements: PromoterElements<T>,
    ): RoutingElements<T, Promoter.TransitionState> {
        val new = PromoterElement(
            key = RoutingKey(element),
            fromState = CREATED,
            targetState = CREATED,
            operation = this
        )

        return listOf(new) + elements
    }
}

fun <T : Any> Promoter<T>.addFirst(element: T) {
    accept(AddFirst(element))
}
