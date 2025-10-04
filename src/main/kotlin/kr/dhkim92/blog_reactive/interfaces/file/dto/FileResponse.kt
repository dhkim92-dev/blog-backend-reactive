package kr.dhkim92.blog_reactive.interfaces.file.dto

import kr.dhkim92.blog_reactive.application.file.dto.FileDto

data class FileResponse(
    val url: String
) {

    companion object {
        fun from(dto: FileDto): FileResponse {
            return FileResponse(
                url = dto.url
            )
        }
    }
}