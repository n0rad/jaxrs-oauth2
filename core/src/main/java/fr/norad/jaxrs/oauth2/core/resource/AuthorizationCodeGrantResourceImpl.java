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

import static fr.norad.core.util.MetaMessageBuilder.metaMsg;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.CLIENT_ID;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.SCOPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.resource.AuthorizationCodeGrantResource;
import fr.norad.jaxrs.oauth2.core.persistence.AuthorizationCodeRepository;
import fr.norad.jaxrs.oauth2.core.service.ClientSpecService;
import fr.norad.jaxrs.oauth2.core.service.TokenSpecService;

@OauthResource
@Validated
class AuthorizationCodeGrantResourceImpl implements AuthorizationCodeGrantResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ClientSpecService clientService;
    @Autowired
    private AuthorizationCodeRepository authorizationRepository;
    @Autowired
    private TokenSpecService tokenService;

    @Override
    public Token requestToken(String clientId, String clientSecret, String userId, String code, String type,
                              String scopeString)
            throws InvalidClientOauthException, InvalidGrantOauthException, UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException, InvalidScopeOauthException {
        logger.info(metaMsg("Redeeming authorization code for token").meta(CLIENT_ID, clientId).meta(SCOPE, scopeString).toString());
/*

        Set<String> scopes = copyOf(Splitter.on(CharMatcher.anyOf(" ,:;|")).omitEmptyStrings().split(scopeString));

        Client client = clientService.getAuthenticated(clientId, clientSecret);
        Authorization authorization = AuthorizationCodeRepository.findByUserIdAndCode(userId, code);

        if (authorization.isConsumed()) {
            throw new InvalidGrantOauthException("Code already consumed");
        }
        Token token = tokenService.createRefreshableToken(client, scopes, authorization_code, authorization.getUserId());
        authorization.setConsumed(true);
        AuthorizationCodeRepository.update(authorization);
*/

        return null;
    }
}
