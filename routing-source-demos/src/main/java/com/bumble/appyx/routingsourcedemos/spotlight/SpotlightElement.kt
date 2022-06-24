package com.bumble.appyx.routingsourcedemos.spotlight

import com.bumble.appyx.core.routing.RoutingElement
import com.bumble.appyx.core.routing.RoutingElements

typealias SpotlightElement<T> = RoutingElement<T, Spotlight.TransitionState>

typealias SpotlightElements<T> = RoutingElements<T, Spotlight.TransitionState>
