package kr.dhkim92.blog_reactive.common.util

import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

object RequestUrlUtil {
    fun currentRequestUrl(): Mono<String> =
        Mono.deferContextual { ctx ->
            val exchange = try { ctx.get(ServerWebExchange::class.java) } catch (e: Exception) { return@deferContextual Mono.error(IllegalStateException("No ServerWebExchange in context")) }
            Mono.just(exchange.request.uri.toString())
        }

    fun currentRequestInfo(): Mono<RequestInfo> =
        Mono.deferContextual { ctx ->
            val exchange = try { ctx.get(ServerWebExchange::class.java) } catch (e: Exception) { return@deferContextual Mono.error(IllegalStateException("No ServerWebExchange in context")) }
            val request = exchange.request
            val host = request.uri.host ?: request.headers["host"]?.firstOrNull() ?: ""
            val port = request.uri.port.takeIf { it != -1 } ?: (request.headers["host"]?.firstOrNull()?.split(":")?.getOrNull(1)?.toIntOrNull() ?: -1)
            val info = RequestInfo(
                url = request.uri.toString(),
                host = host,
                port = port,
                path = request.path.value(),
                method = request.method?.toString() ?: "",
                queryParams = request.queryParams.toSingleValueMap(),
                headers = request.headers.toSingleValueMap()
            )
            Mono.just(info)
        }
}

data class RequestInfo(
    val url: String,
    val host: String,
    val port: Int,
    val path: String,
    val method: String,
    val queryParams: Map<String, String>,
    val headers: Map<String, String>
)
