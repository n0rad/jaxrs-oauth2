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

import static fr.norad.jaxrs.oauth2.SecuredAnnotationReader.notSecuredReader;
import static fr.norad.jaxrs.oauth2.SecuredAnnotationReader.securedWithAllScopeReader;
import static fr.norad.jaxrs.oauth2.SecuredAnnotationReader.securedWithAnyScopeReader;
import static fr.norad.jaxrs.oauth2.SecuredAnnotationReader.securedWithScopeReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SecuredUtils {
    /**
     * @param method method
     * @param scopes null if not authenticated
     * @return true if scopes match the secured info on the method, false otherwise
     */
    public static boolean isAuthorized(Method method, Set<String> scopes) {
        SecuredInfo securedInfo = findSecuredInfo(method);
        return isAuthorized(securedInfo, scopes);
    }

    /**
     * @param securedInfo Secured info
     * @param scopes null if not authenticated
     * @return true if scopes match the passed secured info
     */
    public static boolean isAuthorized(SecuredInfo securedInfo, Set<String> scopes) {
        return securedInfo != null
                && (securedInfo.isNotSecured() || securedInfo.isAuthorizingScopes(scopes));
    }

    public static boolean isNotSecured(Method method) {
        SecuredInfo securedInfo = findSecuredInfo(method);
        return securedInfo != null && securedInfo.isNotSecured();
    }

    public static Set<String> fromSpaceDelimitedString(String scopes) {
        if (scopes == null || scopes.trim().isEmpty()) {
            return Collections.unmodifiableSet(Collections.EMPTY_SET);
        }
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(scopes.split(" "))));
    }

    public static SecuredInfo findSecuredInfo(Method method) {
        try {
            SecuredInfo security = new SecuredInfo();
            security.read(notSecuredReader().scopes(method));
            security.read(securedWithScopeReader().scopes(method));
            security.read(securedWithAnyScopeReader().scopes(method));
            security.read(securedWithAllScopeReader().scopes(method));

            if (security.processed == null) {
                security.read(notSecuredReader().scopes(method.getDeclaringClass()));
                security.read(securedWithScopeReader().scopes(method.getDeclaringClass()));
                security.read(securedWithAnyScopeReader().scopes(method.getDeclaringClass()));
                security.read(securedWithAllScopeReader().scopes(method.getDeclaringClass()));
            }

            if (security.processed == null) {
                return null;
            }

            return security;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid security annotation declaration on method " + method, e);
        }
    }

}
