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


import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;

//@Grants(GrantType.authorization_code)
@Path("/token")
public interface ImplicitGrantResource {

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    Token requestToken()
            throws
            InvalidClientOauthException,
            InvalidGrantOauthException,
            UnauthorizedClientOauthException,
            UnsupportedGrantTypeOauthException,
            InvalidScopeOauthException;


//    response_type
//    client_id
//    redirect_uri
//    scope
//    state
}
