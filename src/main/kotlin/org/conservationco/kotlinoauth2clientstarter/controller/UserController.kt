package org.conservationco.kotlinoauth2clientstarter.controller

import org.conservationco.kotlinoauth2clientstarter.user.Name
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/user")
class UserController {

    @GetMapping("/me")
    fun getCurrentUsersName(@AuthenticationPrincipal user: OidcUser) =
        Mono.just(
            ResponseEntity.ok(
                Name(user.idToken.claims["name"].toString())
            )
        )

}
