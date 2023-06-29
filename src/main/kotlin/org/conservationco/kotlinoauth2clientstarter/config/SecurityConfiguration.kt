package org.conservationco.kotlinoauth2clientstarter.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    internal fun securityWebFilterChain(http: ServerHttpSecurity) =
        http
            .authorizeExchange { it.anyExchange().authenticated() }
            .oauth2Login { }
            .build()

}
