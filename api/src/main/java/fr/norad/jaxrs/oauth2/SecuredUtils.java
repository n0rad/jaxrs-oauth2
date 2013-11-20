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

import static fr.norad.core.lang.reflect.AnnotationUtils.findAnnotation;
import java.lang.reflect.Method;
import java.util.Set;

public class SecuredUtils {
    /**
     * @param method
     * @param scopes null if not authenticated
     * @return
     */
    public static boolean isAuthorized(Method method, Set<String> scopes) {
        Secured scoped = findSecured(method);
        if (scoped != null && scoped.isNotSecured()) {
            return true;
        } else {
            return checkSecured(method, scopes, scoped);
        }
    }

    public static Secured findSecured(Method method) {
        try {
            Secured security = new Secured();
            security.read(findAnnotation(method, NotSecured.class));
            security.read(findAnnotation(method, SecuredWithScope.class));
            security.read(findAnnotation(method, SecuredWithAnyScopesOf.class));
            security.read(findAnnotation(method, SecuredWithAllScopesOf.class));

            if (security.processed == null) {
                security.read(findAnnotation(method.getDeclaringClass(), NotSecured.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithScope.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithAnyScopesOf.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithAllScopesOf.class));
            }

            if (security.processed == null) {
                return null;
            }

            return security;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid security annotation declaration on method " + method, e);
        }
    }

    private static boolean checkSecured(Method method, Set<String> scopes, Secured secured) {
        if (scopes == null) {
            return false;
        }
        if (secured == null || secured.getScopes() == null || secured.getScopes().length == 0) {
            return false;
        }

        if (secured.isAndAssociation()) {
            for (String expectedScope : secured.getScopes()) {
                if (expectedScope.isEmpty()) {
                    throw new IllegalStateException("Empty scope not allowed on method :" + method);
                }
                if (!scopes.contains(expectedScope)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String expectedScope : secured.getScopes()) {
                if (expectedScope.isEmpty()) {
                    throw new IllegalStateException("Empty scope not allowed on method :" + method);
                }
                if (scopes.contains(expectedScope)) {
                    return true;
                }
            }
            return false;
        }
    }

}
