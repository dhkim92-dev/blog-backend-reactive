package kr.dhkim92.blog_reactive.config

import com.github.f4b6a3.uuid.UuidCreator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import org.springframework.data.mapping.context.MappingContext
import reactor.core.publisher.Mono
import java.util.UUID

@Configuration
class R2dbcCallbackConfig(
    private val mappingContext: MappingContext<out RelationalPersistentEntity<*>, out RelationalPersistentProperty>
) {

    /**
     * UUID 필드를 가진 모든 엔티티에 대해 UUID V7을 자동으로 생성하는 콜백
     */
    @Bean
    fun idGenerator(): BeforeConvertCallback<Any> {
        return BeforeConvertCallback<Any> { entity, _ ->
            val entityType = entity::class.java
            val persistentEntity = mappingContext.getPersistentEntity(entityType)

            if (persistentEntity != null) {
                val idProperty = persistentEntity.idProperty

                if (idProperty != null && UUID::class.java.isAssignableFrom(idProperty.type)) {
                    val currentId = idProperty.getRequiredGetter().invoke(entity)

                    if (currentId == null) {
                        val uuidV7 = UuidCreator.getTimeOrderedEpoch() // UUID V7 생성
//                        println("new uuid ! : ${uuidV7}")
                        idProperty.getRequiredSetter().invoke(entity, uuidV7)
                    }
                }
            }

            Mono.just(entity)
        }
    }
}
