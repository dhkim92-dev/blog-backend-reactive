package kr.dhkim92.blog_reactive.interfaces.file

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dhkim92.blog_reactive.application.file.FileServiceFacade
import kr.dhkim92.blog_reactive.application.file.dto.UploadFileCommand
import kr.dhkim92.blog_reactive.common.annotations.Envelope
import kr.dhkim92.blog_reactive.interfaces.file.dto.FileResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Mono

@RestController
@Tag(name = "File", description = "File API")
@RequestMapping("/api/v1/files")
class FileController(private val fileUploadServiceFacade: FileServiceFacade) {

    @Operation(summary = "이미지 업로드", description = "JPEG, PNG 형식의 이미지를 업로드합니다.")
    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
        ]
    )
    @Envelope(status = HttpStatus.OK, message = "Image uploaded successfully", code = "FI001")
    fun uploadImage(
        @RequestPart("file") file: FilePart
    ): Mono<FileResponse> {
        return fileUploadServiceFacade.uploadImage(UploadFileCommand(file = file))
            .map { fileDto -> FileResponse.from(fileDto) }
    }
}