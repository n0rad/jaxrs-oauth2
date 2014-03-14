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
package fr.norad.jaxrs.oauth2.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Sets;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;

@Ignore
public class ClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_fail_if_client_not_allowed_to_use_grant_type() throws Exception {
        Client client = new Client("iphone", "secret");
        client.setAllowedGrantTypes(Sets.newLinkedHashSet(GrantType.password));

        thrown.expect(UnauthorizedClientOauthException.class);
        client.checkAllowed(GrantType.client_credentials);
    }

    @Test
    public void should_return_client_that_is_allowed_to_use_grant_type() throws Exception {
        Client client = new Client("iphone", "secret");
        client.setAllowedGrantTypes(Sets.newLinkedHashSet(GrantType.password));

        assertThat(client.checkAllowed(GrantType.password)).isSameAs(client);
    }

    @Test
    public void should_provide_default_token_lifetime_if_none_specified() throws Exception {
        assertThat(new Client("id", "secret").tokenLifetime(666)).isEqualTo(666);
        assertThat(new Client("id", "secret").refreshTokenLifetime(888)).isEqualTo(888);
    }

    @Test
    public void should_provide_custom_token_lifetime_if_specified() throws Exception {
        Client client = new Client("id", "secret");
        client.setTokenLifetimeSecond(123);
        assertThat(client.tokenLifetime(666)).isEqualTo(123);
        assertThat(client.refreshTokenLifetime(888)).isEqualTo(888);
    }

    @Test
    public void should_provide_custom_refresh_token_lifetime_if_specified() throws Exception {
        Client client = new Client("id", "secret");
        client.setRefreshTokenLifetimeSecond(123);
        assertThat(client.tokenLifetime(666)).isEqualTo(666);
        assertThat(client.refreshTokenLifetime(888)).isEqualTo(123);
    }
}
