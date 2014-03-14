/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.oauth2.core.assertion;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Set;
import org.assertj.core.api.AbstractAssert;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;

public class TokenAssert extends AbstractAssert<TokenAssert, Token> {

    protected TokenAssert(Token actual) {
        super(actual, TokenAssert.class);
    }

    public TokenAssert hasSameClientId(Token token) {
        isNotNull();
        assertThat(actual.getClientId())
                .overridingErrorMessage("Expected token client id <%s> is equal to token info client id <%s>",
                        token.getClientId(), actual.getClientId())
                .isEqualTo(token.getClientId());
        return this;
    }

    public TokenAssert hasSameUserId(Token token) {
        isNotNull();
        assertThat(actual.getUsername())
                .overridingErrorMessage("Expected token user id <%s> is equal to token info user id <%s>",
                        token.getUsername(), actual.getUsername())
                .isEqualTo(token.getUsername());
        return this;
    }

    public TokenAssert hasSameScopes(Token token) {
        isNotNull();
        assertThat(actual.getScopes())
                .overridingErrorMessage("Expected token scopes <%s> are same as token info scopes <%s>",
                        token.getScopes(), actual.getScopes())
                .containsExactly(token.getScopes().toArray(new String[token.getScopes().size()]));
        return this;
    }

    public TokenAssert hasSameTokenType(Token token) {
        isNotNull();
        assertThat(actual.getTokenType())
                .overridingErrorMessage("Expected token type <%s> is equal to token info type <%s>",
                        token.getTokenType(), actual.getTokenType())
                .isEqualTo(token.getTokenType());
        return this;
    }

    public TokenAssert hasSameExpirationDuration(Token token) {
        isNotNull();
        assertThat(actual.getExpiresIn())
                .overridingErrorMessage("Expected token expiration duration <%s> is equal to token info expiration duration<%s>",
                        token.getExpiresIn(), actual.getExpiresIn())
                .isEqualTo(token.getExpiresIn());
        return this;
    }

    public TokenAssert hasSameUnixCreationDate(Token token) {
        isNotNull();
        assertThat(actual.getIssuedAt())
                .overridingErrorMessage("Expected token Unix creation date <%s> is equal to token info Unix creation date<%s>",
                        token.getIssuedAt(), actual.getIssuedAt())
                .isEqualTo(token.getIssuedAt());
        return this;
    }

    public TokenAssert hasAccessToken() {
        isNotNull();
        assertThat(actual.getAccessToken())
                .overridingErrorMessage("Expected token to have an access token")
                .isNotEmpty();
        return this;
    }

    public TokenAssert accessToken(String accessToken) {
        isNotNull();
        assertThat(actual.getAccessToken())
                .overridingErrorMessage("Expected token to have an access token <%s>", accessToken)
                .isEqualTo(accessToken);
        return this;
    }

    public TokenAssert hasScope(String scope) {
        isNotNull();
        assertThat(actual.getScopes())
                .overridingErrorMessage("Expected token to have scope <%s>", scope)
                .isEqualTo(scope);
        return this;
    }

    public TokenAssert hasExactlyScopes(Set<String> scopes) {
        return hasExactlyScopes(scopes.toArray(new String[scopes.size()]));
    }

    public TokenAssert hasExactlyScopes(String... scopes) {
        isNotNull();
        assertThat(actual.getScopes())
                .overridingErrorMessage("Expected token to contain exactly the scopes <%s>", Arrays.asList(scopes))
                .containsExactly(scopes);
        return this;
    }

    public TokenAssert hasOnlyScopes(String... scopes) {
        isNotNull();
        assertThat(actual.getScopes())
                .overridingErrorMessage("Expected token to contain only the scopes <%s>", Arrays.asList(scopes))
                .containsOnly(scopes);
        return this;
    }

    public TokenAssert expiresInSeconds(int seconds) {
        isNotNull();
        assertThat(actual.getExpiresIn())
                .overridingErrorMessage("Expected token to expire in <%s> seconds", seconds)
                .isEqualTo(seconds);
        return this;
    }

    public TokenAssert hasTokenType(String tokenType) {
        isNotNull();
        assertThat(actual.getTokenType())
                .overridingErrorMessage("Expected token to have token type <%s>", tokenType)
                .isEqualTo(tokenType);
        return this;
    }

    public TokenAssert hasRefreshToken() {
        isNotNull();
        assertThat(actual.getRefreshToken())
                .overridingErrorMessage("Expected token to have a refresh token")
                .isNotEmpty();
        return this;
    }

    public TokenAssert hasNoRefreshToken() {
        isNotNull();
        assertThat(actual.getRefreshToken())
                .overridingErrorMessage("Expected token to have no refresh token")
                .isNullOrEmpty();
        return this;
    }
}
