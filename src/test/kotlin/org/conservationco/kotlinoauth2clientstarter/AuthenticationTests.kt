package org.conservationco.kotlinoauth2clientstarter

import org.conservationco.kotlinoauth2clientstarter.user.Name
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
@AutoConfigureWebTestClient
internal class AuthenticationTests(
    @Autowired private val client: WebTestClient,
) {

    @Test
    internal fun `should redirect unauthenticated access to protected endpoint`() {
        client
            .get()
            .uri("/user/me")
            .exchange()
            .expectStatus().is3xxRedirection
    }

    @Test
    internal fun `should redirect unauthenticated access to Google login page`() {
        client
            .get()
            .uri("/user/me")
            .exchange()
            .expectHeader().valueEquals("Location", "/oauth2/authorization/google");
    }

    @Test
    internal fun `should allow authenticated access to protected endpoint`() {
        client
            .mutateWith(mockOidcUser())
            .get()
            .uri("/user/me")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    internal fun `should return authenticated user's full name`() {
        val expected = MOCK_USER_FULL_NAME
        val actual = client
            .mutateWith(mockOidcUser())
            .get()
            .uri("/user/me")
            .exchange()
            .expectBody<Name>()
            .returnResult()
            .responseBody
            ?.data
        assertEquals(expected, actual)
    }

}

internal const val MOCK_USER_FULL_NAME = "Perry The Platypus"

internal fun mockOidcUser() =
    SecurityMockServerConfigurers
        .mockOidcLogin()
        .idToken { it.claim("name", MOCK_USER_FULL_NAME) }
