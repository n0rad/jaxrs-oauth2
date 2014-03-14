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
package fr.norad.jaxrs.oauth2.api.spec.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OauthSpecError {
    @XmlElement(name = "error")
    private Type type;
    @XmlElement(name = "error_description")
    private String description;
    @XmlElement(name = "correlation_id")
    private String correlationId;

    public static OauthSpecError newOauthSpecError(Type type, String correlationId, String description) {
        OauthSpecError error = new OauthSpecError();
        error.setType(type);
        error.setCorrelationId(correlationId);
        error.setDescription(description);
        return error;
    }

    ///////////////////////

    @XmlEnum
    public static enum Type {
        @XmlEnumValue("invalid_request")
        INVALID_REQUEST,
        @XmlEnumValue("invalid_client")
        INVALID_CLIENT,
        @XmlEnumValue("invalid_grant")
        INVALID_GRANT,
        @XmlEnumValue("unauthorized_client")
        UNAUTHORIZED_CLIENT,
        @XmlEnumValue("unsupported_grant_type")
        UNSUPPORTED_GRANT_TYPE,
        @XmlEnumValue("invalid_scope")
        INVALID_SCOPE,
        @XmlEnumValue("server_error")
        SERVER_ERROR;
    }
}
