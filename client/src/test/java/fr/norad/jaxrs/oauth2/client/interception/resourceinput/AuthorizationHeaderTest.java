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

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.junit.Test;
import fr.norad.jaxrs.oauth2.api.SecurityForbiddenException;

public class AuthorizationHeaderTest {
    @Test
    public void should_identify_bearer_type() throws Exception {
        assertThat(new AuthorizationHeader("Bearer", "blabla").isBearer()).isTrue();
        assertThat(new AuthorizationHeader("bearer", "blabla").isBearer()).isFalse();
        assertThat(new AuthorizationHeader("", "blabla").isBearer()).isFalse();
        assertThat(new AuthorizationHeader(null, "blabla").isBearer()).isFalse();
    }

    @Test
    public void should_return_null_if_no_auth_header_found() throws Exception {
        MultivaluedMap<String, String> empty_headers = new MetadataMap<>();
        assertThat(AuthorizationHeader.authorizationHeaderFrom(empty_headers)).isNull();
    }

    @Test(expected = SecurityForbiddenException.class)
    public void should_fail_if_malformed_auth_header_found() throws Exception {
        Map<String, List<String>> headers = headers("Authorization", "not a valid header value");

        AuthorizationHeader.authorizationHeaderFrom(headers);
    }

    @Test(expected = SecurityForbiddenException.class)
    public void should_fail_if_empty_auth_header_found() throws Exception {
        Map<String, List<String>> headers = headers("Authorization", "");

        AuthorizationHeader.authorizationHeaderFrom(headers);
    }

    @Test(expected = SecurityForbiddenException.class)
    public void should_fail_if_several_auth_headers_found() throws Exception {
        Map<String, List<String>> headers = headers("Authorization", "Bearer token", "Bearer anothertoken");

        AuthorizationHeader.authorizationHeaderFrom(headers);
    }

    @Test
    public void should_create_an_AuthorizationHeader_if_header_is_correct() throws Exception {
        Map<String, List<String>> headers = headers("Authorization", "Bearer token000df0sg0d0fg0df0g");

        AuthorizationHeader.authorizationHeaderFrom(headers);
    }

    private Map<String, List<String>> headers(String header, String... values) {
        HashMap<String, List<String>> headers = new HashMap<>();
        headers.put(header, Arrays.asList(values));
        return headers;
    }
}
