package kr.dhkim92.blog_reactive.application.file.dto

import org.springframework.http.codec.multipart.FilePart

data class UploadFileCommand(
    val file: FilePart
) {
}