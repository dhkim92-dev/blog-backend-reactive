package kr.dhkim92.blog_reactive.application.file.impl

import kr.dhkim92.blog_reactive.application.file.ImageUploadService
import kr.dhkim92.blog_reactive.application.file.dto.FileDto
import kr.dhkim92.blog_reactive.application.file.dto.UploadFileCommand
import kr.dhkim92.blog_reactive.application.file.usecases.UploadImageUseCase
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class UploadImageUseCaseImpl(
    private val imageUploadService: ImageUploadService
): UploadImageUseCase {

    override fun uploadImage(command: UploadFileCommand): Mono<FileDto> {
        return imageUploadService.uploadImage(command.file)
            .subscribeOn(Schedulers.boundedElastic())
    }
}