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
package fr.norad.jaxrs.oauth2.core.tck.test;

import static fr.norad.jaxrs.oauth2.api.spec.domain.GrantType.password;
import static fr.norad.jaxrs.oauth2.core.assertion.Assertions.assertThat;
import static fr.norad.jaxrs.oauth2.core.builder.ClientTestBuilder.client;
import static fr.norad.jaxrs.oauth2.core.builder.UserTestBuilder.user;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.core.tck.Oauth2;
import fr.norad.jaxrs.oauth2.core.tck.spring.SpringConfiguration;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class TokenTest {

    @Rule
    @Autowired
    public Oauth2 oauth2;

    @Test
    public void should() throws Exception {
        oauth2.addClient(client("id").secret("secret").allowedScopes("yop").allowedGrants(password));
        oauth2.addUser(user("username").password("password"));

        Token token = oauth2.passwordGrantResource().requestToken("id", "secret", "username", "password", password, "yop");

        assertThat(token).hasOnlyScopes("yop");
        assertThat(token).hasAccessToken();
        assertThat(token).hasRefreshToken();
        assertThat(token).hasTokenType("bearer");
    }

}
