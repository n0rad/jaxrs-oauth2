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

import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.CLIENT_ID;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.CLIENT_SECRET;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.GRANT_TYPE;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.RESOURCE_OWNER_NAME;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.RESOURCE_OWNER_PASSWORD;
import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.SCOPE;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
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
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidRequestOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;

@Grants(GrantType.password)
@Path("/token")
public interface ResourceOwnerPasswordGrantResource {

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    Token requestToken(
            @FormParam(CLIENT_ID) @NotNull(message = "client_id should not be null") String clientId,
            @FormParam(CLIENT_SECRET) @NotNull(message = "client_secret should not be null") String clientSecret,
            @FormParam(RESOURCE_OWNER_NAME) @NotNull(message = "username should not be null") String username,
            @FormParam(RESOURCE_OWNER_PASSWORD) @NotNull(message = "password should not be null") String password,
            @FormParam(GRANT_TYPE) @NotNull(message = "grant_type should not be null") GrantType type,
            @FormParam(SCOPE) @NotNull(message = "scope should not be null") String scope)
            throws
            InvalidClientOauthException,
            InvalidGrantOauthException,
            UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException,
            InvalidScopeOauthException,
            InvalidRequestOauthException;
}
