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

import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidRequestOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidScopeOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.OauthSpecError;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnsupportedGrantTypeOauthException;

public class OauthSpecResponseExceptionMapper implements ResponseExceptionMapper<Exception> {

    private OauthSpecError findError(Response r) {
        return r.readEntity(OauthSpecError.class);
    }

    @Override
    public Exception fromResponse(Response r) {
        OauthSpecError error = findError(r);
        if (error.getType() == null) {
            throw new RuntimeException("Cannot Create Exception from Error : " + error);
        }
        return buildException(error.getType(), error.getDescription());
    }

    private Exception buildException(OauthSpecError.Type error, String message) {
        switch (error) {
            case INVALID_CLIENT:
                return new InvalidClientOauthException(message);
            case INVALID_GRANT:
                return new InvalidGrantOauthException(message);
            case INVALID_REQUEST:
                return new InvalidRequestOauthException(message);
            case INVALID_SCOPE:
                return new InvalidScopeOauthException(message);
            case UNAUTHORIZED_CLIENT:
                return new UnauthorizedClientOauthException(message);
            case UNSUPPORTED_GRANT_TYPE:
                return new UnsupportedGrantTypeOauthException(message);
            default:
                throw new IllegalStateException("not implemented !");
        }
    }
}
