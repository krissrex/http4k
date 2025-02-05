package org.http4k.routing

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.routing.RouterDescription.Companion.unavailable
import org.http4k.routing.RouterMatch.MatchedWithoutHandler
import org.http4k.routing.RouterMatch.MethodNotMatched
import org.http4k.routing.RouterMatch.Unmatched

/**
 * Convert any predicate on a request into a router
 */
fun ((Request) -> Boolean).asRouter(description: String = unavailable.description): Router = object : Router {
    override val description = RouterDescription(description)
    override fun match(request: Request): RouterMatch =
        if (this@asRouter(request)) MatchedWithoutHandler(this.description) else Unmatched(this.description)
}

fun Request.path(name: String): String? = when (this) {
    is RequestWithRoute -> xUriTemplate.extract(uri.path)[name]
    else -> throw IllegalStateException("Request was not routed, so no uri-template present")
}

fun Method.asRouter() = object : Router {
    override fun match(request: Request): RouterMatch =
        if (this@asRouter == request.method) MatchedWithoutHandler(description) else MethodNotMatched(
            description
        )

    override val description = RouterDescription("method == ${this@asRouter}")

    override fun withBasePath(new: String): Router = this
}

fun Method.and(that: Router) = asRouter().and(that)

infix fun Method.bind(routingHandler: RoutingHttpHandler) = asRouter() bind routingHandler
infix fun Method.bind(httpHandler: HttpHandler) = asRouter() bind httpHandler
