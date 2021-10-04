package com.github.zsoltk.composeribs.core.routing.source.backstack

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.zsoltk.composeribs.core.routing.RoutingElement
import com.github.zsoltk.composeribs.core.routing.RoutingKey
import com.github.zsoltk.composeribs.core.routing.RoutingSource
import com.github.zsoltk.composeribs.core.routing.source.backstack.BackStack.TransitionState.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableSource
import java.util.concurrent.atomic.AtomicInteger

open class BackStack<T>(
    initialElement: T,
) : RoutingSource<T, BackStack.TransitionState> {

    data class LocalRoutingKey<T>(
        override val routing: T,
        val uuid: Int,
    ) : RoutingKey<T>

    enum class TransitionState {
        CREATED, ON_SCREEN, STASHED_IN_BACK_STACK, DESTROYED
    }

    private val tmpCounter = AtomicInteger(1)

//    private lateinit var onRemoved: (RoutingKey<T>) -> Unit

//    override fun onRemoved(block: (RoutingKey<T>) -> Unit) {
//        onRemoved = block
//    }

    private val elements = mutableListOf(
        BackStackElement(
            key = LocalRoutingKey(initialElement, tmpCounter.incrementAndGet()),
            fromState = ON_SCREEN,
            targetState = ON_SCREEN,
            onScreen = true
        )
    )

    private val elementsRelay: BehaviorRelay<List<RoutingElement<T, TransitionState>>> = BehaviorRelay.createDefault(elements)
    override val elementsObservable: ObservableSource<List<RoutingElement<T, TransitionState>>> = elementsRelay

    val pendingRemoval: SnapshotStateList<RoutingElement<T, TransitionState>> =
        mutableStateListOf()

    val currentRouting: T
        get() = elements.last().key.routing

    override val all: List<RoutingElement<T, TransitionState>>
        get() = elements

    override val offScreen: List<BackStackElement<T>>
        get() = elements.filter { !it.onScreen }

    override val onScreen: List<BackStackElement<T>>
        get() = elements.filter { it.onScreen }

    fun push(element: T) {
        with(elements) {
            elements[lastIndex] = elements[lastIndex].copy(
                targetState = STASHED_IN_BACK_STACK
            )
            elements += BackStackElement(
                key = LocalRoutingKey(element, tmpCounter.incrementAndGet()),
                fromState = CREATED,
                targetState = ON_SCREEN,
                onScreen = true
            )
        }

        elementsRelay.accept(elements)
    }

    fun pop() {
        with(elements) {
            if (size > 1) {
                val popped = elements.removeLast()
                pendingRemoval.add(
                    popped.copy(
                        targetState = DESTROYED
                    )
                )
                elements[lastIndex] = elements[lastIndex].copy(
                    fromState = STASHED_IN_BACK_STACK,
                    targetState = ON_SCREEN,
                    onScreen = true
                )
            }
        }

        elementsRelay.accept(elements)
    }

    override fun onTransitionFinished(key: RoutingKey<T>) {
        elements.toList().forEachIndexed { index, routingElement ->
            if (routingElement.key == key) {
                val targetState = routingElement.targetState
                elements[index] = routingElement.copy(
                    fromState = targetState
                )

                when (targetState) {
                    STASHED_IN_BACK_STACK -> markOffScreen(key)
                    DESTROYED -> remove(key)
                    else -> {
                    }
                }
            }
        }
    }

    private fun markOffScreen(key: RoutingKey<T>) {
        elements.toList().forEachIndexed { index, routingElement ->
            if (routingElement.key == key) {
                elements[index] = routingElement.copy(
                    onScreen = false
                )
            }
        }
    }

    private fun remove(key: RoutingKey<T>) {
        pendingRemoval.removeAll { it.key == key }
//        onRemoved(key)
    }

    override fun canHandleBackPress(): Boolean =
        elements.size > 1

    override fun onBackPressed() {
        pop()
    }
}
