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
package fr.norad.jaxrs.oauth2.client.interception.resourceinput;

import static fr.norad.core.util.MetaMessageBuilder.metaMsg;
import static org.assertj.core.util.Preconditions.checkNotNull;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.oauth2.api.SecuredInfo;
import fr.norad.jaxrs.oauth2.api.SecuredUtils;
import fr.norad.jaxrs.oauth2.api.SecurityDeclarationException;
import fr.norad.jaxrs.oauth2.api.SecurityForbiddenException;
import fr.norad.jaxrs.oauth2.api.SecurityUnauthorizedException;
import fr.norad.jaxrs.oauth2.api.TokenFetcher;
import fr.norad.jaxrs.oauth2.api.TokenNotFoundException;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;

public class SecuredAnnotationChecker {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private TokenFetcher tokenFetcher;

    public SecuredAnnotationChecker(TokenFetcher tokenFetcher) {
        this.tokenFetcher = checkNotNull(tokenFetcher);
    }

    public void authorize(Method calledMethod, AuthorizationHeader authorizationHeader) {
        SecuredInfo calledMethodSecuredInfo = SecuredUtils.findSecuredInfo(calledMethod);

        if (calledMethodSecuredInfo == null) {
            throw new SecurityDeclarationException("Resource should declare securization details");
        }

        if (authorizationProvided(authorizationHeader)) {
            Token authorization = fetchToken(authorizationHeader);

            if (calledMethodSecuredInfo.isAuthorizingScopeStrings(authorization.getScopes())) {
                return;
            } else {
                logger.info(metaMsg("Not allowed to access this URI.")
                        .meta("header", authorizationHeader.getAccessToken())
                        .toString());
                throw new SecurityForbiddenException("Insufficient authorization provided !");
            }
        } else {
            if (calledMethodSecuredInfo.getScopes().isEmpty()) {
                return;
            } else {
                logger.info(metaMsg("No authorization header provided to access a secured URI").toString());
                throw new SecurityUnauthorizedException("No authentication and resource is not public !");
            }
        }
    }

    private boolean authorizationProvided(AuthorizationHeader authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.isBearer();
    }

    private Token fetchToken(AuthorizationHeader authorizationHeader) {
        try {
            return tokenFetcher.findToken(authorizationHeader.getAccessToken());
        } catch (TokenNotFoundException e) {
            logger.info(metaMsg("No access token found in database matching Authorization header")
                    .meta("authorizationHeader", authorizationHeader.getAccessToken()).toString());
            throw new SecurityForbiddenException("Invalid token : Unauthorized");
        }
    }
}
