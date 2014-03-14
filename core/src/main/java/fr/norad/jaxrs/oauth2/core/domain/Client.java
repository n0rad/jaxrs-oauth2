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
package fr.norad.jaxrs.oauth2.core.domain;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.exception.UnauthorizedClientOauthException;
import lombok.Data;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Client {

    private String id;
    private String secret;
    private String description;
    private Integer tokenLifetimeSecond;
    private Integer refreshTokenLifetimeSecond;
    private Set<GrantType> allowedGrantTypes = new HashSet<>();
    private Set<String> allowedScopes = new HashSet<>();

    public Client() {
    }

    public Client checkAllowed(GrantType grantType) throws UnauthorizedClientOauthException {
        if (!allowedGrantTypes.contains(this)) {
            throw new UnauthorizedClientOauthException("client not allowed to use grant type: " + grantType);
        }
        return this;
    }

    public Client(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    public int tokenLifetime(Integer defaultLifetime) {
        return defaultIfNotPositive(tokenLifetimeSecond, defaultLifetime);
    }

    public int refreshTokenLifetime(Integer defaultLifetime) {
        return defaultIfNotPositive(refreshTokenLifetimeSecond, defaultLifetime);
    }

    private int defaultIfNotPositive(Integer value, int fallback) {
        if (value != null && value > 0) {
            return value;
        } else {
            return fallback;
        }
    }

}
