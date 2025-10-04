package kr.dhkim92.blog_reactive.config

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    private val flyway: Flyway
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnProperty(value = ["spring.profiles.active"], havingValue = "local")
    fun flywayResetRunner() = ApplicationRunner {
        logger.info("Running Flyway clean and migrate for local profile")
        logger.info("    is clean disabled? : {}", flyway.configuration.isCleanDisabled)
        flyway.clean()
        flyway.migrate()
    }
}