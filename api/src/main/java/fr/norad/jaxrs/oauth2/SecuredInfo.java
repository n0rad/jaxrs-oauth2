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

import java.lang.annotation.Annotation;
import java.util.Set;
import lombok.Getter;

public class SecuredInfo {
    Annotation processed;
    @Getter
    private String[] scopes;
    @Getter
    private boolean andAssociation;
    @Getter
    private boolean notSecured;

    void read(NotSecured notSecured) {
        checkNotFilled(notSecured);
        this.notSecured = notSecured != null;
    }

    void read(SecuredWithScope scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = new String[]{scope.value()};
        }
    }

    void read(SecuredWithAllScopesOf scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = scope.value();
            andAssociation = true;
        }

    }

    void read(SecuredWithAnyScopesOf scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = scope.value();
            andAssociation = false;
        }
    }

    private void checkNotFilled(Annotation a) {
        if (a == null) {
            return;
        }
        if (processed != null) {
            throw new IllegalStateException("only one accept annotation is allowed. found " + a + " and "
                                                    + processed);
        }
        processed = a;
    }

    public boolean isAuthorizingScopes(Set<String> scopesToAuthorize) {
        if (scopesToAuthorize == null) {
            return false;
        }
        if (scopes == null || scopes.length == 0) {
            return false;
        }

        if (isAndAssociation()) {
            for (String expectedScope : scopes) {
                if (!scopesToAuthorize.contains(expectedScope)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String expectedScope : scopes) {
                if (scopesToAuthorize.contains(expectedScope)) {
                    return true;
                }
            }
            return false;
        }
    }
}
