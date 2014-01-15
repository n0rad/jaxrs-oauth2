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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import fr.norad.core.lang.reflect.AnnotationUtils;

public class SecuredUtils {

    private static final SecuredInfo NOT_SECURED = new SecuredInfo(new Scope[] {}, ScopeStrategy.ALL);

    public static SecuredInfo findSecuredInfo(Method method) {
        Annotation annotation = MetaAnnotationUtils.findAnnotationWithMetaAnnotation(method, Secured.class);
        if (annotation != null) {
            Scope[] scopes = readAttribute("value", annotation);
            ScopeStrategy strategy = readAttribute("strategy", annotation);
            if (isEmpty(scopes)) {
                throw new IllegalStateException("Empty scopes defined on method '" + method
                        + "'. Please use @NotSecured if it was desired");
            }
            return new SecuredInfo(scopes, strategy);
        }
        annotation = findNotSecuredAnnotation(method);
        if (annotation != null) {
            return NOT_SECURED;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T readAttribute(String attribute, Annotation annotation) {
        try {
            return (T) annotation.annotationType().getDeclaredMethod(attribute).invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException reflectionException) {
            throw new IllegalStateException("Cannot access annotation attribute, \n" +
                    "is the attribute '" + attribute + "' declared in the annotation '" + annotation + "' annotated "
                    +
                    "by @Secured",
                    reflectionException);
        }
    }

    private static Annotation findNotSecuredAnnotation(Method method) {
        Annotation annotation = AnnotationUtils.findAnnotation(method, NotSecured.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), NotSecured.class);
        }
        return annotation;
    }

    private static boolean isEmpty(Object[] array) {
        return array.length == 0;
    }
}
