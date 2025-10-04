package kr.dhkim92.blog_reactive.application.file

import kr.dhkim92.blog_reactive.application.file.dto.FileDto
import kr.dhkim92.blog_reactive.common.error.InternalServerError
import kr.dhkim92.blog_reactive.config.FileStorageConfig
import net.coobird.thumbnailator.Thumbnails
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

@Service
class ImageUploadService(
    private val fileStorageConfig: FileStorageConfig,
    private val imageTypeValidator: ImageTypeValidator
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun uploadImage(file: FilePart): Mono<FileDto> {
        return DataBufferUtils.join(file.content())
            .map { dataBuffer ->
                val bytes = ByteArray(dataBuffer.readableByteCount())
                dataBuffer.read(bytes)
                DataBufferUtils.release(dataBuffer)
                bytes
            }
            .map { bytes ->
                imageTypeValidator.validate(bytes)
                val fileExt = file.filename()
                    .substringAfterLast('.', "")
                    .lowercase()
                val randomFileName = UUID.randomUUID().toString() + if (fileExt.isNotEmpty()) ".${fileExt}" else ""
                val fileAccessPath = "images/${randomFileName[0]}/${randomFileName[1]}/$randomFileName"
                val fullPath = fileStorageConfig.storagePath + fileAccessPath
                checkParentDirOrCreate(fullPath)

                val inputStream = ByteArrayInputStream(bytes)
                val outputStream = ByteArrayOutputStream()
                Thumbnails.of(inputStream)
                    .size(1280, 720)
                    .keepAspectRatio(true)
                    .toOutputStream(outputStream)

                try {
                    val savedFile = File(fullPath)
                    savedFile.writeBytes(outputStream.toByteArray())
                } catch (e: Exception) {
                    logger.error("파일 쓰기 실패 {}", e.message)
                    throw InternalServerError(message = "파일 쓰기 실패")
                }

                FileDto(
                    url = "${fileStorageConfig.host}/media/${fileAccessPath}"
                )
            }
    }

    private fun checkParentDirOrCreate(dirPath: String) {
        val parents = dirPath.substringBeforeLast('/')
        val dir = File(parents)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }
}