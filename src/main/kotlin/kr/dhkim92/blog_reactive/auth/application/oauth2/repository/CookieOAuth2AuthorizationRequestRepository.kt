package kr.dhkim92.blog_reactive.auth.application.oauth2.repository

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository
import org.springframework.web.server.ServerWebExchange
import kr.dhkim92.blog_reactive.common.util.CookieUtil
import reactor.core.publisher.Mono
import java.util.Base64
import java.nio.charset.StandardCharsets
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.core.type.TypeReference
import kr.dhkim92.blog_reactive.config.CookieConfig
import kr.dhkim92.blog_reactive.config.OAuth2Config

@Repository
class CookieOAuth2AuthorizationRequestRepository(
    private val cookieConfig: CookieConfig,
    private val oauth2Config: OAuth2Config
) : ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private val httpOnly: Boolean by lazy { cookieConfig.httpOnly }

    private val secure: Boolean by lazy { cookieConfig.secure }

    private val sameSite: String by lazy { cookieConfig.sameSite }

    private val OAUTH2_AUTH_REQUEST_COOKIE_NAME: String by lazy { oauth2Config.authorizationRequestCookieName }

    private val COOKIE_EXPIRE_SECONDS: Int by lazy { oauth2Config.authCookieExpireSeconds }

    init {
        logger.info("CookieOAuth2AuthorizationRequestRepository initialized with httpOnly=$httpOnly, secure=$secure, sameSite=$sameSite")
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, exchange: ServerWebExchange): Mono<Void> {
        logger.debug("Saving OAuth2AuthorizationRequest to cookies")
        if (authorizationRequest == null) {
            return CookieUtil.deleteCookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        }

        return try {
            val serializedData = serialize(authorizationRequest)
            CookieUtil.setCookie(
                key = OAUTH2_AUTH_REQUEST_COOKIE_NAME,
                value = serializedData,
                maxAge = COOKIE_EXPIRE_SECONDS,
                httpOnly = httpOnly,
                secure = secure,
                sameSite = sameSite
            )
        } catch (e: Exception) {
            logger.error("Failed to serialize OAuth2AuthorizationRequest: ${e.message}", e)
            Mono.error(e)
        }
    }

    override fun removeAuthorizationRequest(exchange: ServerWebExchange): Mono<OAuth2AuthorizationRequest> {
        return CookieUtil.getCookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME)
            .map { cookieValue -> deserialize(cookieValue) }
            .flatMap { authRequest ->
                CookieUtil.deleteCookie(
                    OAUTH2_AUTH_REQUEST_COOKIE_NAME,
                    httpOnly = httpOnly,
                    secure = secure,
                    sameSite = sameSite
                )
                .then(Mono.just(authRequest))
            }
            .doOnError { e -> logger.error("Failed to remove OAuth2AuthorizationRequest: ${e.message}", e) }
    }

    override fun loadAuthorizationRequest(exchange: ServerWebExchange): Mono<OAuth2AuthorizationRequest> {
        return CookieUtil.getCookie(OAUTH2_AUTH_REQUEST_COOKIE_NAME)
            .map { cookieValue -> deserialize(cookieValue) }
            .doOnError { e -> logger.error("Failed to load authorization request: {}", e.message) }
    }

    private fun serialize(authRequest: OAuth2AuthorizationRequest): String {
        val authRequestMap = mapOf(
            "authorizationUri" to authRequest.authorizationUri,
            "clientId" to authRequest.clientId,
            "redirectUri" to authRequest.redirectUri,
            "scopes" to authRequest.scopes,
            "state" to authRequest.state,
            "additionalParameters" to authRequest.additionalParameters,
            "authorizationRequestUri" to authRequest.authorizationRequestUri,
            "attributes" to authRequest.attributes
        )
        val json = objectMapper.writeValueAsString(authRequestMap)
        return Base64.getEncoder().encodeToString(json.toByteArray(StandardCharsets.UTF_8))
    }

    private fun deserialize(data: String): OAuth2AuthorizationRequest {
        val json = String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8)
        val typeRef = object : TypeReference<Map<String, Any>>() {}
        val authRequestMap = objectMapper.readValue(json, typeRef)

        return OAuth2AuthorizationRequest.authorizationCode()
            .authorizationUri(authRequestMap["authorizationUri"] as String)
            .clientId(authRequestMap["clientId"] as String)
            .redirectUri(authRequestMap["redirectUri"] as String?)
            .scopes(convertToStringSet(authRequestMap["scopes"]))
            .state(authRequestMap["state"] as String?)
            .additionalParameters(convertToStringAnyMap(authRequestMap["additionalParameters"]))
            .authorizationRequestUri(authRequestMap["authorizationRequestUri"] as String?)
            .attributes(convertToStringAnyMap(authRequestMap["attributes"]))
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertToStringSet(value: Any?): Set<String> {
        return when (value) {
            is Collection<*> -> value.filterIsInstance<String>().toSet()
            else -> emptySet()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertToStringAnyMap(value: Any?): Map<String, Any> {
        return when (value) {
            is Map<*, *> -> value.entries.associate {
                (it.key as? String ?: it.key.toString()) to (it.value ?: "")
            }
            else -> emptyMap()
        }
    }
}
