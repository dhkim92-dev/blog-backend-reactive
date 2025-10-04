package kr.dhkim92.blog_reactive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FileStorageConfig(
    @Value("\${server.media.path}")
    val storagePath: String,
    @Value("\${server.media.host}")
    val host: String
) {
}