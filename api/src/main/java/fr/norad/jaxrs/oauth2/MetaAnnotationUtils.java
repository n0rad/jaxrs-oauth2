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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

abstract class MetaAnnotationUtils {

    private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new WeakHashMap<Class<?>, Boolean>();

    public static Annotation findAnnotationWithMetaAnnotation(Method method,
            Class<? extends Annotation> metaAnnotationType) {
        Annotation annotation = getAnnotationWithMetaAnnotation(method, metaAnnotationType);
        Class<?> clazz = method.getDeclaringClass();
        if (annotation == null) {
            annotation = searchOnInterfaces(method, metaAnnotationType, clazz.getInterfaces());
        }
        while (annotation == null) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz.equals(Object.class)) {
                break;
            }
            try {
                Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getAnnotationWithMetaAnnotation(equivalentMethod, metaAnnotationType);
            } catch (NoSuchMethodException ex) {
                // No equivalent method found
            }
            if (annotation == null) {
                annotation = searchOnInterfaces(method, metaAnnotationType, clazz.getInterfaces());
            }
        }
        if (annotation == null) {
            annotation = findAnnotationWithMetaAnnotation(method.getDeclaringClass(), metaAnnotationType);
        }
        return annotation;
    }

    private static Annotation getAnnotationWithMetaAnnotation(AnnotatedElement annotatedElement,
            Class<? extends Annotation> metaAnnotationType) {
        Annotation[] annotations = annotatedElement.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (annotationClass.isAnnotationPresent(metaAnnotationType)) {
                return annotation;
            }
        }
        return null;
    }

    private static Annotation searchOnInterfaces(Method method, Class<? extends Annotation> annotationType,
            Class<?>[] ifcs) {
        Annotation annotation = null;
        for (Class<?> iface : ifcs) {
            if (isInterfaceWithAnnotatedMethods(iface)) {
                try {
                    Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                    annotation = getAnnotationWithMetaAnnotation(equivalentMethod, annotationType);
                } catch (NoSuchMethodException ex) {
                    // Skip this interface - it doesn't have the method...
                }
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

    private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
        synchronized (annotatedInterfaceCache) {
            Boolean flag = annotatedInterfaceCache.get(iface);
            if (flag != null) {
                return flag;
            }
            boolean found = false;
            for (Method ifcMethod : iface.getMethods()) {
                if (ifcMethod.getAnnotations().length > 0) {
                    found = true;
                    break;
                }
            }
            annotatedInterfaceCache.put(iface, found);
            return found;
        }
    }

    private static Annotation findAnnotationWithMetaAnnotation(Class<?> clazz,
            Class<? extends Annotation> metaAnnotationType) {
        //        Assert.notNull(clazz, "Class must not be null");
        Annotation annotation = getAnnotationWithMetaAnnotation(clazz, metaAnnotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotationWithMetaAnnotation(ifc, metaAnnotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotationWithMetaAnnotation(ann.annotationType(), metaAnnotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass.equals(Object.class)) {
            return null;
        }
        return findAnnotationWithMetaAnnotation(superClass, metaAnnotationType);
    }
}
