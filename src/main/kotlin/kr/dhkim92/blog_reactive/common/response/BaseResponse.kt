package kr.dhkim92.blog_reactive.common.response

import io.swagger.v3.oas.annotations.media.Schema

data class Href (
    val href: String?
)

abstract class BaseResponse(
    @field: Schema(name="_links", description = "HATEOAS links", example = "{\"self\": {\"href\": \"/api/resource/1\"}}")
    val _links: MutableMap<String, Href> = mutableMapOf()
) {
}