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
package fr.norad.jaxrs.oauth2.api.spec.resource;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;

@Grants(GrantType.refresh_token)
@Path("/token")
public interface RefreshTokenGrantResource {

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    Token requestToken(
            @FormParam(OAuthParams.CLIENT_ID) @NotNull(message = "client_id should not be null") String clientId,
            @FormParam(OAuthParams.CLIENT_SECRET) @NotNull(message = "client_secret should not be null") String clientSecret,
            @FormParam(OAuthParams.GRANT_TYPE) @NotNull(message = "grant_type should not be null") GrantType type,
            @FormParam(OAuthParams.REFRESH_TOKEN) @NotNull(message = "refresh_token should not be null") String refreshToken)
            throws
            InvalidClientOauthException,
            InvalidGrantOauthException,
            UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException,
            InvalidScopeOauthException;

}
