package kr.dhkim92.blog_reactive.common.response

import kr.dhkim92.blog_reactive.common.annotations.Envelope
import org.springframework.http.HttpStatus
import java.lang.reflect.Method
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class EnvelopResponseHandler(
    writers: MutableList<HttpMessageWriter<*>>,
    resolver: RequestedContentTypeResolver
) : ResponseBodyResultHandler(writers, resolver) {

    override fun supports(result: HandlerResult): Boolean {
        val handler = result.handler
        val method: Method? = when (handler) {
            is HandlerMethod -> handler.method
            is Method -> handler
            else -> null
        }
        val hasEnvelope = method?.getAnnotation(Envelope::class.java) != null
        return hasEnvelope
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        // Envelope 어노테이션 값 추출
        val (status, message, code) = extractEnvelopeInfo(result)
        exchange.response.statusCode = status
        val body: Mono<EnvelopResponse<Any?>> = when (val value = result.returnValue) {
            is Mono<*> -> value.map { EnvelopResponse(data = it, status = status, message = message, code = code) as EnvelopResponse<Any?> }
            is Flux<*> -> value.collectList().map { EnvelopResponse(data = it, status = status, message = message, code = code) as EnvelopResponse<Any?> }
            else -> return Mono.error(ClassCastException("The \"body\" should be Mono<*> or Flux<*>!"))
        }
        val returnTypeSource = result.returnTypeSource
        return writeBody(body, returnTypeSource, exchange)
    }

    private fun extractEnvelopeInfo(result: HandlerResult): Triple<HttpStatus, String, String> {
        val handler = result.handler
        val method: Method? = when (handler) {
            is HandlerMethod -> handler.method
            is Method -> handler
            else -> null
        }
        val envelope = method?.getAnnotation(Envelope::class.java)
        return if (envelope != null) Triple(envelope.status, envelope.message, envelope.code) else Triple(HttpStatus.OK, "success", "G001")
    }
}