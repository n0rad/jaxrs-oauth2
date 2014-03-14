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
package fr.norad.jaxrs.oauth2.client;

import static com.google.common.base.Joiner.on;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Joiner;
import fr.norad.jaxrs.oauth2.api.Scope;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.exception.OauthSpecException;
import fr.norad.jaxrs.oauth2.api.spec.resource.ClientCredentialsGrantResource;

public class ClientGrantTokenProvider extends AbstractAccessTokenProvider {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ClientCredentialsGrantResource resource;

    public ClientGrantTokenProvider(String clientId, String clientSecret, String url) {
        super(clientId, clientSecret);
        resource = restBuilder.buildClient(ClientCredentialsGrantResource.class, url);
    }

    @Override
    public String retrieveAccessTokenWith(Set<Scope> scopes) {
        logger.debug("About to request token with scopes '{}'", on(", ").join(scopes));
        try {
            return resource.requestToken(clientId, clientSecret, GrantType.client_credentials,
                    Joiner.on(" ").join(scopes)).getAccessToken();
        } catch (OauthSpecException e) {
            throw new IllegalStateException("error while requesting access token with scopes " + scopes, e);
        }
    }
}
