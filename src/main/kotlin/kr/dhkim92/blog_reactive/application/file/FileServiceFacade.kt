package kr.dhkim92.blog_reactive.application.file

import kr.dhkim92.blog_reactive.application.file.dto.FileDto
import kr.dhkim92.blog_reactive.application.file.dto.UploadFileCommand
import kr.dhkim92.blog_reactive.application.file.usecases.UploadImageUseCase
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class FileServiceFacade(
    private val uploadImageUseCase: UploadImageUseCase
) {

    fun uploadImage(command: UploadFileCommand): Mono<FileDto> {
        return uploadImageUseCase.uploadImage(command)
    }
}