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
package fr.norad.jaxrs.oauth2.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class SecuredInfo {
    private final ScopeStrategy strategy;

    private final Set<Scope> scopes;

    public SecuredInfo(Scope[] scopes, ScopeStrategy strategy) {
        this.scopes = new HashSet<>(Arrays.asList(scopes));
        this.strategy = strategy;
    }

    private Set<String> asSetOfString(Collection<Scope> scopes) {
        Set<String> scopeStrings = new HashSet<>(scopes.size());
        for (Scope scope : scopes) {
            scopeStrings.add(scope.scopeIdentifier());
        }
        return scopeStrings;
    }

    public boolean isAuthorizingScopes(Set<Scope> scopesToAuthorize) {
        if (scopesToAuthorize == null) {
            return false;
        }

        return isAuthorizingScopeStrings(asSetOfString(scopesToAuthorize));
    }

    public boolean isAuthorizingScopeStrings(Set<String> scopesToAuthorize) {
        if (scopes.isEmpty()) {
            return true;
        }

        if (scopesToAuthorize == null) {
            return false;
        }

        return strategy.isAuthorizingScopes(asSetOfString(scopes), scopesToAuthorize);
    }

}
