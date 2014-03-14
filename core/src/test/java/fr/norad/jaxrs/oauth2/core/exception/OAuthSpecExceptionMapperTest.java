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
package fr.norad.jaxrs.oauth2.core.exception;

import static org.assertj.core.api.Assertions.assertThat;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Ignore;
import org.junit.Test;
import fr.norad.jaxrs.oauth2.api.spec.exception.OauthSpecError;

@Ignore
public class OAuthSpecExceptionMapperTest {

    private OAuthSpecExceptionMapper mapper = new OAuthSpecExceptionMapper();

    @Test
    public void should_send_a_invalid_request() throws Exception {
        Response response = mapper.toResponse(new ValidationException("not happy"));
        assertThat(response.getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
        OauthSpecError oAuthError = (OauthSpecError) response.getEntity();
        assertThat(oAuthError.getType()).isEqualTo(OauthSpecError.Type.INVALID_REQUEST);
        assertThat(oAuthError.getDescription()).isEqualTo("not happy");
    }
}
