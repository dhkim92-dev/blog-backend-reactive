package kr.dhkim92.blog_reactive.interfaces

import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.common.util.RequestInfo
import kr.dhkim92.blog_reactive.common.util.RequestUrlUtil
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class HealthCheckController {

    @GetMapping("/health-check")
    @Envelope(status = HttpStatus.OK, message = "Health check successful", code = "H001")
    fun healthCheck(): Mono<Long> {
        return Mono.just(System.currentTimeMillis())
    }

    @GetMapping("/health-check/{memberId}")
    fun healthCheckMember(): Mono<RequestInfo> {
        return RequestUrlUtil.currentRequestInfo()
    }
}