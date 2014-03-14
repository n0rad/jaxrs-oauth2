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
package fr.norad.jaxrs.oauth2.api.spec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import java.util.Set;
import org.junit.Test;

public class TokenTest {

    @Test
    public void should_return_null_for_exires_in_when_some_null() {

        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", null, scopes, grantType);

        assertThat(token.getExpiresIn()).isNull();
    }

    @Test
    public void should_return_expired_for_some_null() {
        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", null, scopes, grantType);

        assertThat(token.isExpired()).isTrue();
    }

    @Test
    public void should_construct_a_fully_initialized_token() throws Exception {
        int lifetime = 95849;
        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", lifetime, scopes, grantType);

        assertThat(token.getAccessToken()).isNotEmpty();
        assertThat(token.getClientId()).isEqualTo("client id");
        assertThat(token.getLifetime()).isEqualTo(lifetime);
        assertThat(token.getGrantType()).isEqualTo(GrantType.refresh_token);
        assertThat(token.getIssuedAt()).isNotNull();
        assertThat(token.getScopes()).containsOnly("CONTACT_READ", "CONTACT_WRITE");
    }

    @Test
    public void should_count_down_expire_in() throws Exception {
        int lifetime = 5;
        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", lifetime, scopes, grantType);

        Thread.sleep(1000);

        assertThat(token.getExpiresIn()).isEqualTo(4);
    }

    @Test
    public void should_find_not_expired_token_at_0_expires() throws Exception {
        int lifetime = 1;
        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", lifetime, scopes, grantType);

        Thread.sleep(1000);

        assertThat(token.isExpired()).isFalse();
    }

    @Test
    public void should_find_expired_token_at_minus_1_expires() throws Exception {
        int lifetime = 0;
        Set<String> scopes = newLinkedHashSet("CONTACT_READ", "CONTACT_WRITE");
        GrantType grantType = GrantType.refresh_token;

        Token token = new Token("client id", lifetime, scopes, grantType);

        Thread.sleep(1000);

        assertThat(token.isExpired()).isTrue();
    }

}
