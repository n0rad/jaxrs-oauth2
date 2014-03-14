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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Group {

    @NotNull
    private String id;
    @Size(min = 1)
    private Set<String> allowedScopes = new HashSet<>();
    private Set<String> userIds = new HashSet<>();

    public Group() {
    }

    public Group(String id, Set<String> allowedScopes, Set<String> userIds) {
        this.id = id;
        this.allowedScopes = allowedScopes;
        this.userIds = userIds;
    }

    public Group(String id, String... scopes) {
        this.id = id;
        if (scopes != null) {
            for (String scope : scopes) {
                allowedScopes.add(scope);
            }
        }
    }

    public Group(String id, Set<String> allowedScopes) {
        this.id = id;
        this.allowedScopes = allowedScopes;
    }

}
