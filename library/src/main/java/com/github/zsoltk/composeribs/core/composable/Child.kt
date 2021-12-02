package com.github.zsoltk.composeribs.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import com.github.zsoltk.composeribs.core.node.ParentNode
import com.github.zsoltk.composeribs.core.routing.RoutingElement
import com.github.zsoltk.composeribs.core.routing.RoutingElements
import com.github.zsoltk.composeribs.core.routing.RoutingSource
import com.github.zsoltk.composeribs.core.routing.transition.TransitionDescriptor
import com.github.zsoltk.composeribs.core.routing.transition.TransitionHandler
import com.github.zsoltk.composeribs.core.routing.transition.TransitionParams

@Composable
fun <Routing : Any, State> ParentNode<Routing>.Child(
    routingElement: RoutingElement<Routing, out State>,
    saveableStateHolder: SaveableStateHolder,
    transitionParams: TransitionParams,
    transitionHandler: TransitionHandler<Routing, State>,
    decorator: @Composable (transitionScope: ChildTransitionScope<State>, child: @Composable () -> Unit, transitionDescriptor: TransitionDescriptor<Routing, State>) -> Unit
) {
    val childEntry = childOrCreate(routingElement.key)
    saveableStateHolder.SaveableStateProvider(key = routingElement.key) {
        val descriptor = routingElement.createDescriptor(transitionParams)
        val transitionScope =
            transitionHandler.handle(
                descriptor = descriptor,
                onTransitionFinished = {
                    routingSource.onTransitionFinished(
                        childEntry.key
                    )
                })

        decorator(
            transitionDescriptor = descriptor,
            transitionScope = transitionScope,
            child = {
                CompositionLocalProvider(LocalTransitionModifier provides transitionScope.transitionModifier) {
                    childEntry.node.Compose()
                }
            },
        )
    }
}

fun <Routing : Any, State> RoutingElement<Routing, State>.createDescriptor(
    transitionParams: TransitionParams
) =
    TransitionDescriptor(
        params = transitionParams,
        operation = operation,
        element = key.routing,
        fromState = fromState,
        toState = targetState
    )

@Composable
fun <R, S> RoutingSource<R, S>?.childrenAsState(): State<RoutingElements<R, out S>> =
    if (this != null) {
        elements.collectAsState()
    } else {
        remember { mutableStateOf(emptyList()) }
    }

