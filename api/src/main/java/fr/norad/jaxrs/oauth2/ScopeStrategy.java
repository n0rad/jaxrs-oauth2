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
package fr.norad.jaxrs.oauth2;


import java.util.Set;

public enum ScopeStrategy {
    /**
     * Any scope present will allow the access.
     */
    ANY {
        @Override
        public boolean isAuthorizingScopes(Set<Scope> resourceDefinedScopes, Set<Scope> scopesToAuthorize) {
            for (Scope definedScope : resourceDefinedScopes) {
                if (scopesToAuthorize.contains(definedScope)) {
                    return true;
                }
            }
            return false;

        }
    },

    /**
     * All scopes must be present to allow the access.
     */
    ALL {
        @Override
        public boolean isAuthorizingScopes(Set<Scope> resourceDefinedScopes, Set<Scope> scopesToAuthorize) {
            for (Scope definedScope : resourceDefinedScopes) {
                if (!scopesToAuthorize.contains(definedScope)) {
                    return false;
                }
            }
            return true;

        }
    };

    public abstract boolean isAuthorizingScopes(Set<Scope> scopes, Set<Scope> scopesToAuthorize);
}
