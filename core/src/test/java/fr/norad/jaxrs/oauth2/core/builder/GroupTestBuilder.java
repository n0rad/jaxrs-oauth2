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
package fr.norad.jaxrs.oauth2.core.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import fr.norad.jaxrs.oauth2.core.domain.Group;

public class GroupTestBuilder {

    private String id;
    private Set<String> allowedScopes = new HashSet<>();
    private Set<String> userIds = new HashSet<>();

    public static GroupTestBuilder group() {
        return new GroupTestBuilder();
    }

    public GroupTestBuilder id(String id) {
        this.id = id;
        return this;
    }

    public GroupTestBuilder allowedScopes(String... scopes) {
        this.allowedScopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public GroupTestBuilder userIds(String... userIds) {
        this.userIds.addAll(Arrays.asList(userIds));
        return this;
    }

    public Group build() {
        Group group = new Group();
        if (StringUtils.isBlank(id)) {
            id = RandomStringUtils.randomAlphabetic(5);
        }
        group.setId(id);
        group.getAllowedScopes().addAll(allowedScopes);
        group.getUserIds().addAll(userIds);

        return group;
    }
}
