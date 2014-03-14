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
package fr.norad.jaxrs.oauth2.core.builder;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import java.util.HashSet;
import java.util.Set;
import com.google.common.collect.Sets;
import fr.norad.jaxrs.oauth2.core.domain.Client;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;

public class ClientTestBuilder {

    private String id;

    private String secret;

    private Set<String> allowedScopes = new HashSet<>();

    private Set<GrantType> allowedGrants = new HashSet<>();

    private int tokenLifetime;

    private int refreshTokenLifetime;

    public ClientTestBuilder secret(String secret) {
        this.secret = secret;
        return this;
    }

    public ClientTestBuilder allowedScopes(String... allowedScopes) {
        this.allowedScopes = Sets.newHashSet(allowedScopes);
        return this;
    }

    public ClientTestBuilder allowedGrants(GrantType... allowedGrants) {
        this.allowedGrants = Sets.newHashSet(allowedGrants);
        return this;
    }

    public ClientTestBuilder tokenLifetime(int tokenLifetime) {
        this.tokenLifetime = tokenLifetime;
        return this;
    }

    public ClientTestBuilder refreshTokenLifetime(int lifetime) {
        this.refreshTokenLifetime = lifetime;
        return this;
    }

    public ClientTestBuilder(String id) {
        this.id = id;
    }

    public static ClientTestBuilder client() {
        return client("client_" + randomAlphanumeric(10));
    }

    public static ClientTestBuilder client(String id) {
        return new ClientTestBuilder(id);
    }

    public Client build() {
        Client client = new Client(id, secret);
        client.setAllowedGrantTypes(allowedGrants);
        client.setAllowedScopes(allowedScopes);
        client.setTokenLifetimeSecond(tokenLifetime);
        client.setRefreshTokenLifetimeSecond(refreshTokenLifetime);
        return client;
    }
}
