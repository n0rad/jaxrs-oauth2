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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import lombok.Data;

public class SecuredUtils {
    public static boolean isAuthorized(Method method) {
        return isAuthorized(method, null);
    }

    /**
     * @param method
     * @param scopes null if not authenticated
     * @return
     */
    public static boolean isAuthorized(Method method, Set<String> scopes) {
        Security scoped = findAnnotations(method);
        if (scoped.acceptNonAuthenticated) {
            return true;
        } else {
            return checkSecured(method, scopes, scoped);
        }
    }

    private static Security findAnnotations(Method method) {
        try {
            Security security = new Security();
            security.read(findAnnotation(method, NotSecured.class));
            security.read(findAnnotation(method, SecuredWithScope.class));
            security.read(findAnnotation(method, SecuredWithAnyScopesOf.class));
            security.read(findAnnotation(method, SecuredWithAllScopesOf.class));

            if (security.getProcessed() == null) {
                security.read(findAnnotation(method.getDeclaringClass(), NotSecured.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithScope.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithAnyScopesOf.class));
                security.read(findAnnotation(method.getDeclaringClass(), SecuredWithAllScopesOf.class));
            }

            return security;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid security annotation declaration on method " + method, e);
        }
    }

    private static boolean checkSecured(Method method, Set<String> scopes, Security secured) {
        if (scopes == null) {
            return false;
        }
        if (secured == null || secured.scopes == null || secured.scopes.length == 0) {
            return false;
        }

        if (secured.andAssociation) {
            for (String expectedScope : secured.scopes) {
                if (expectedScope.isEmpty()) {
                    throw new IllegalStateException("Empty scope not allowed on method :" + method);
                }
                if (!scopes.contains(expectedScope)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String expectedScope : secured.scopes) {
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

    @Data
    static class Security {
        private Annotation processed;
        private String[] scopes;
        private boolean andAssociation;
        private boolean acceptNonAuthenticated;

        public void read(NotSecured nonAuthenticated) {
            checkNotFilled(nonAuthenticated);
            acceptNonAuthenticated = nonAuthenticated != null;
        }

        public void read(SecuredWithScope scope) {
            checkNotFilled(scope);
            if (scope != null) {
                scopes = new String[]{scope.value()};
            }
        }

        public void read(SecuredWithAllScopesOf scope) {
            checkNotFilled(scope);
            if (scope != null) {
                scopes = scope.value();
                andAssociation = true;
            }

        }

        public void read(SecuredWithAnyScopesOf scope) {
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
    }

}
