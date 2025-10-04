package kr.dhkim92.blog_reactive.application.file.usecases

import kr.dhkim92.blog_reactive.application.file.dto.FileDto
import kr.dhkim92.blog_reactive.application.file.dto.UploadFileCommand
import reactor.core.publisher.Mono

interface UploadImageUseCase {

    fun uploadImage(command: UploadFileCommand): Mono<FileDto>
}