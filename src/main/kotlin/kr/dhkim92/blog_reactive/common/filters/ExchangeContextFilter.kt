package kr.dhkim92.blog_reactive.common.filters

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ExchangeContextFilter() : WebFilter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        // Reactor Context에 ServerWebExchange를 저장
        return chain.filter(exchange)
            .contextWrite { ctx -> ctx.put(ServerWebExchange::class.java, exchange) }
    }
}