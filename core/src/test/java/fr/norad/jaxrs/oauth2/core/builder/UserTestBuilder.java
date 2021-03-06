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

import java.util.HashSet;
import java.util.Set;
import fr.norad.jaxrs.oauth2.core.domain.Group;
import fr.norad.jaxrs.oauth2.core.domain.User;

public class UserTestBuilder {

    private String username;
    private String password;
    private Set<Group> groups = new HashSet<>();

    public UserTestBuilder(String username) {
        this.username = username;
    }

    public static UserTestBuilder user(String username) {
        return new UserTestBuilder(username);
    }

    public UserTestBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserTestBuilder allowedGroups(Group... groups) {
        for (Group group : groups) {
            this.groups.add(group);
        }
        return this;
    }

    public User build() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGroups(groups);
        return user;
    }

}
