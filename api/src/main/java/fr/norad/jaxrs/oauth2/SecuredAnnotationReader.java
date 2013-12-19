package fr.norad.jaxrs.oauth2;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import fr.norad.core.lang.reflect.AnnotationUtils;

public abstract class SecuredAnnotationReader<A extends Annotation> {

    public abstract A scopes(AnnotatedElement element);

    protected static void checkScopeNotEmpty(AnnotatedElement annotatedElement, String... scopeValues) {
        for (String scopeValue : scopeValues) {
            if (scopeValue.isEmpty()) {
                throw new IllegalStateException("Empty scope not allowed on element : " + annotatedElement);
            }
        }
    }

    private static <A extends Annotation> A findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        if (element instanceof Class) {
            return AnnotationUtils.findAnnotation((Class) element, annotationType);
        }
        if (element instanceof Method) {
            return AnnotationUtils.findAnnotation((Method) element, annotationType);
        }
        throw new IllegalArgumentException("Element '" + element + "' is not supported by this class ");
    }

    public static SecuredAnnotationReader<NotSecured> notSecuredReader() {
        return new SecuredAnnotationReader<NotSecured>() {
            public NotSecured scopes(AnnotatedElement element) {
                NotSecured annotation = findAnnotation(element, NotSecured.class);
                return annotation;
            }
        };
    }

    public static SecuredAnnotationReader<SecuredWithScope> securedWithScopeReader() {
        return new SecuredAnnotationReader<SecuredWithScope>() {
            public SecuredWithScope scopes(AnnotatedElement element) {
                SecuredWithScope annotation = findAnnotation(element, SecuredWithScope.class);
                if(annotation != null) checkScopeNotEmpty(element, annotation.value());
                return annotation;
            }
        };
    }

    public static SecuredAnnotationReader<SecuredWithAllScopesOf> securedWithAllScopeReader() {
        return new SecuredAnnotationReader<SecuredWithAllScopesOf>() {
            public SecuredWithAllScopesOf scopes(AnnotatedElement element) {
                SecuredWithAllScopesOf annotation = findAnnotation(element, SecuredWithAllScopesOf.class);
                if(annotation != null) checkScopeNotEmpty(element, annotation.value());
                return annotation;
            }
        };
    }

    public static SecuredAnnotationReader<SecuredWithAnyScopesOf> securedWithAnyScopeReader() {
        return new SecuredAnnotationReader<SecuredWithAnyScopesOf>() {
            public SecuredWithAnyScopesOf scopes(AnnotatedElement element) {
                SecuredWithAnyScopesOf annotation = findAnnotation(element, SecuredWithAnyScopesOf.class);
                if(annotation != null) checkScopeNotEmpty(element, annotation.value());
                return annotation;
            }
        };
    }


}
