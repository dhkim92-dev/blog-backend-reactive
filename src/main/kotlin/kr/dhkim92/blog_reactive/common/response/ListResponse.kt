package kr.dhkim92.blog_reactive.common.response

import kr.dhkim92.blog_reactive.common.util.CursorUrlUtil
import reactor.core.publisher.Mono

class ListResponse<T>(
    val count: Int,
    val items: List<T>,
): BaseResponse() {

    companion object {

        /**
         * @param size: requested size
         * @param items: actual items
         * @param extractors: map of fields to make next cursor link
         */
        fun <T> ofAsync(
            size: Int,
            items: List<T>,
            extractors: Map<String, (T) -> Any?> = emptyMap()
        ): Mono<ListResponse<T>> {
            require(size > 0) { throw IllegalArgumentException("size must be positive.") }

            val response = ListResponse(
                count = items.size,
                items = items.take(size)
            )

            return CursorUrlUtil.buildNextCursorUrl(
                pageSize = size,
                items = items,
                extractors = extractors
            ).map { nextUrl ->
                response._links["next"] = Href(nextUrl)
                response
            }.defaultIfEmpty(run {
                response._links["next"] = Href(null)
                response
            })
        }
        /**
         * @param items: actual items
         */
        fun <T> from(
            items: List<T>
        ): ListResponse<T> {
            return ListResponse(
                count = items.size,
                items = items
            )
        }
    }
}