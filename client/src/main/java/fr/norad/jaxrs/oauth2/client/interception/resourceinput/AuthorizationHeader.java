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

import java.util.List;
import java.util.Map;
import fr.norad.jaxrs.oauth2.api.SecurityForbiddenException;
import lombok.Data;

@Data
public class AuthorizationHeader {
    public static final String BEARER = "Bearer";
    private final String type;
    private final String accessToken;

    public AuthorizationHeader(String type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public boolean isBearer() {
        return type != null && type.equals(BEARER);
    }

    public static AuthorizationHeader authorizationHeaderFrom(Map<String, List<String>> headers) {
        List<String> authHeaders = headers.get("Authorization");
        if (authHeaders != null) {
            if (authHeaders.size() == 1) {
                return asAuthorization(authHeaders.get(0));
            } else {
                throw new SecurityForbiddenException("Multiple auth headers : Unauthorized");
            }
        }
        return null;
    }

    private static AuthorizationHeader asAuthorization(String header) {
        String[] headerParts = header.split(" ");
        if (headerParts.length == 2) {
            return new AuthorizationHeader(headerParts[0], headerParts[1]);
        } else {
            throw new SecurityForbiddenException("Invalid header format : Unauthorized");
        }
    }
}
