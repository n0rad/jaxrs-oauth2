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
package fr.norad.jaxrs.oauth2.api.spec.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Token {

    private static final String BEARER_TOKEN_TYPE = "bearer";

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private Integer lifetime;

    private Long issuedAt;

    private GrantType grantType;

    private String clientId;

    private String username;

    private Set<String> scopes = new HashSet<>();

    public Token() {
    }

    public Token(String clientId, Integer lifetime, Set<String> scopes, GrantType grantType) {
        this.accessToken = UUID.randomUUID().toString();
        this.tokenType = BEARER_TOKEN_TYPE;
        this.lifetime = lifetime;
        issuedNow();
        this.clientId = clientId;
        this.grantType = grantType;
        this.scopes = new HashSet<>(scopes);
    }

    public void issuedNow() {
        this.issuedAt = System.currentTimeMillis() / 1000;
    }

    @XmlElement(name = "expires_in")
    public Integer getExpiresIn() {
        if (issuedAt == null || lifetime == null) {
            return null;
        }
        return (int) (issuedAt + lifetime - System.currentTimeMillis() / 1000);
    }

    public void setExpiresIn(Integer expiresIn) {
    }


    @XmlTransient
    public boolean isExpired() {
        Integer expiresIn = getExpiresIn();
        if (expiresIn == null) {
            return true;
        }
        return expiresIn < 0;
    }

    @XmlElement(name = "access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @XmlElement(name = "token_type")
    public String getTokenType() {
        return tokenType;
    }

    @XmlElement(name = "refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    @XmlElement(name = "lifetime")
    public Integer getLifetime() {
        return lifetime;
    }

    @XmlElement(name = "issued_at")
    public Long getIssuedAt() {
        return issuedAt;
    }

    @XmlElement(name = "client_id")
    public String getClientId() {
        return clientId;
    }

}
