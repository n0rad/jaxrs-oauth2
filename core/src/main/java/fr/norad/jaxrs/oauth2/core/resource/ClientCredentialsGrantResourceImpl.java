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
package fr.norad.jaxrs.oauth2.core.resource;

import static com.google.common.collect.ImmutableSet.copyOf;
import static fr.norad.core.util.MetaMessageBuilder.metaMsg;
import static fr.norad.jaxrs.oauth2.api.spec.domain.GrantType.client_credentials;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.CLIENT_ID;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.SCOPE;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.google.common.base.Splitter;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.resource.ClientCredentialsGrantResource;
import fr.norad.jaxrs.oauth2.core.domain.Client;
import fr.norad.jaxrs.oauth2.core.service.ClientSpecService;
import fr.norad.jaxrs.oauth2.core.service.TokenSpecService;

@OauthResource
@Validated
class ClientCredentialsGrantResourceImpl implements ClientCredentialsGrantResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ClientSpecService clientService;
    @Autowired
    private TokenSpecService tokenService;

    @Override
    public Token requestToken(String clientId, String clientSecret, GrantType type, String scopes)
            throws InvalidClientOauthException, InvalidGrantOauthException, UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException, InvalidScopeOauthException {
        logger.info(metaMsg("Creating client token").meta(CLIENT_ID, clientId).meta(SCOPE, scopes).toString());
        Set<String> requestedScopes = copyOf(Splitter.on(' ').split(scopes));
        Client client = clientService.getAuthenticated(clientId, clientSecret);
        return tokenService.createClientCredentialsToken(client.checkAllowed(client_credentials), requestedScopes);
    }
}
