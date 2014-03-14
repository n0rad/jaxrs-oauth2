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
package fr.norad.jaxrs.oauth2.core.service;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.core.domain.Client;
import fr.norad.jaxrs.oauth2.core.domain.Group;
import fr.norad.jaxrs.oauth2.core.domain.RefreshToken;
import fr.norad.jaxrs.oauth2.core.domain.User;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenRepository;
import fr.norad.jaxrs.oauth2.core.persistence.TokenRepository;
import fr.norad.jaxrs.oauth2.core.persistence.UserNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.UserRepository;

@Service
public abstract class TokenSpecService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    protected abstract int getDefaultRefreshTokenLifetimeSeconds();

    protected abstract int getDefaultTokenLifetimeSeconds();

    public Token createRefreshableToken(Client client, Set<String> scopes, GrantType grantType, String username)
            throws InvalidScopeOauthException, UnauthorizedClientOauthException, InvalidGrantOauthException {
        try {
            return createRefreshableToken(client, scopes, grantType, userRepository.findUser(username));
        } catch (UserNotFoundException e) {
            throw new InvalidGrantOauthException("No user found with username '" + username + "'");
        }
    }

    public Token createRefreshableToken(Client client, Set<String> scopes, GrantType grantType, User user)
            throws InvalidScopeOauthException, UnauthorizedClientOauthException {
        client.checkAllowed(grantType);

        Token token = buildToken(client, scopes, grantType, user);
        RefreshToken refreshToken = createRefreshToken(client, token);
        tokenRepository.saveToken(token);
        refreshTokenRepository.saveRefreshToken(refreshToken);
        return token;
    }

    public RefreshToken createRefreshToken(Client client, Token token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setLifetime(client.refreshTokenLifetime(getDefaultRefreshTokenLifetimeSeconds()));
        refreshToken.setUsername(token.getUsername());
        refreshToken.setScopes(new HashSet<>(token.getScopes()));
        refreshToken.setGrantType(token.getGrantType());
        token.setRefreshToken(refreshToken.getRefreshToken());
        refreshToken.setGrantType(token.getGrantType());
        return refreshToken;
    }


    public Token createClientCredentialsToken(Client client, Set<String> requestedScopes)
            throws InvalidScopeOauthException, UnauthorizedClientOauthException {
        Token token = buildClientCredentialsToken(client, requestedScopes);
        tokenRepository.saveToken(token);
        return token;
    }

    public Token buildClientCredentialsToken(Client client, Set<String> requestedScopes)
            throws InvalidScopeOauthException, UnauthorizedClientOauthException {
        return buildToken(client, requestedScopes, GrantType.client_credentials, null);
    }

    ////////////////////////////////////////

    private Token buildToken(Client client, Set<String> requestedScopes, GrantType grantType, User user)
            throws InvalidScopeOauthException, UnauthorizedClientOauthException {
        Token token = new Token(client.getId(), client.tokenLifetime(getDefaultTokenLifetimeSeconds()),
                checkScopes(requestedScopes, client.checkAllowed(grantType), user), grantType);
        if (user != null && StringUtils.isNotBlank(user.getUsername())) {
            token.setUsername(user.getUsername());
        }
        return token;
    }

    private Set<String> checkScopes(Set<String> requestedScopes, Client client, User user)
            throws InvalidScopeOauthException {
        Set<String> allowedScopes = client.getAllowedScopes();
        if (user != null) {
            for (Group group : user.getGroups()) {
                allowedScopes = Sets.union(allowedScopes, group.getAllowedScopes());
            }
        }
        Sets.SetView<String> intersection = Sets.intersection(allowedScopes, requestedScopes);
        if (intersection.isEmpty()) {
            throw new InvalidScopeOauthException("No scope allowed requested");
        }
        return intersection;
    }

}
