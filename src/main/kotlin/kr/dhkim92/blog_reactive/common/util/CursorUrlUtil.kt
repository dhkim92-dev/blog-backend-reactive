package kr.dhkim92.blog_reactive.common.util

import reactor.core.publisher.Mono
import kotlin.collections.forEach

object CursorUrlUtil {

    /**
     * 커서 기반 페이지네이션을 위한 다음 페이지 URL 생성
     * @param pageSize 페이지 크기
     * @param items 현재 페이지 아이템 리스트
     * @param extractors 커서로 사용할 필드 추출 함수 맵 (필드명 -> 추출 함수)
     * @return 다음 페이지 URL (Mono)
     */
    fun <T> buildNextCursorUrl(
        pageSize: Int,
        items: List<T>,
        extractors: Map<String, (T) -> Any?>
    ): Mono<String> {
        if (items.size != pageSize + 1) return Mono.justOrEmpty(null)
        if (items.isEmpty()) return Mono.empty()

        return RequestUrlUtil.currentRequestInfo().map { info ->
            val lastItem = items.last()
            val cursorParams = extractors.mapValues { it.value(lastItem) }
            // 기존 쿼리 파라미터 복사 (pageSize, 커서 파라미터는 덮어씀)
            val query = info.queryParams.toMutableMap()
            query["size"] = pageSize.toString()

            // 커서 파라미터 중 value가 null이 아닌 것만 쿼리에 추가 (String 변환)
            cursorParams.forEach { (k, v) -> if (v != null) query[k] = v.toString() }
            // 쿼리스트링 빌드 (value가 null인 경우 제외)
            val queryString =
                if (query.isNotEmpty())
                    query.entries
                        .filter { it.value != null }
                        .joinToString("&") { "${it.key}=${it.value}" }
                else ""
            // 프로토콜 결정
            val protocol = if (info.host == "localhost" || info.host == "127.0.0.1") "http" else "https"
            val url = buildString {
                append(protocol)
                append("://")
                append(info.host)
                if (info.host == "localhost" || info.host == "127.0.0.1") {
                    if (info.port != -1) {
                        append(":")
                        append(info.port)
                    }
                }
                append(info.path)
                if (queryString.isNotBlank()) {
                    append("?")
                    append(queryString)
                }
            }
            url
        }
    }
}