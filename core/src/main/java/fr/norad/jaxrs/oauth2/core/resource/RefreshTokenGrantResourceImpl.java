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
import static fr.norad.jaxrs.oauth2.api.spec.domain.GrantType.refresh_token;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.CLIENT_ID;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.REFRESH_TOKEN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.resource.RefreshTokenGrantResource;
import fr.norad.jaxrs.oauth2.core.domain.Client;
import fr.norad.jaxrs.oauth2.core.domain.RefreshToken;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenRepository;
import fr.norad.jaxrs.oauth2.core.persistence.TokenRepository;
import fr.norad.jaxrs.oauth2.core.service.ClientSpecService;
import fr.norad.jaxrs.oauth2.core.service.TokenSpecService;

@OauthResource
@Validated
class RefreshTokenGrantResourceImpl implements RefreshTokenGrantResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ClientSpecService clientService;
    @Autowired
    private TokenSpecService tokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token requestToken(String clientId, String clientSecret, GrantType type, String refreshToken)
            throws InvalidClientOauthException, InvalidGrantOauthException, UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException, InvalidScopeOauthException {
        logger.info(metaMsg("Refreshing client token").meta(CLIENT_ID, clientId).meta(REFRESH_TOKEN, refreshToken).toString());
        try {
            RefreshToken rToken = refreshTokenRepository.findRefreshToken(refreshToken);
            tokenRepository.deleteToken(rToken.getSourceAccessToken());
            refreshTokenRepository.deleteRefreshToken(rToken.getRefreshToken());

            Client client = clientService.getAuthenticated(clientId, clientSecret).checkAllowed(refresh_token);
            if (!rToken.wasIssuedForClient(client)) {
                logger.warn("Trying to refreshing token with another client : " + rToken);
                throw new InvalidGrantOauthException("token was issued for another client");
            }

            return tokenService.createRefreshableToken(client, rToken.getScopes(), rToken.getGrantType(), rToken.getUsername());
        } catch (RefreshTokenNotFoundException e) {
            throw new InvalidGrantOauthException("Cannot find refresh token : " + refresh_token);
        }

    }
}
