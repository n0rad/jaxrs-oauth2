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
package fr.norad.jaxrs.oauth2.core.tck.spring;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.annotation.Validated;

public class Hibernate5MethodValidationInterceptor implements MethodInterceptor {

    private final ExecutableValidator validator;

    public Hibernate5MethodValidationInterceptor() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator().forExecutables();
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class[] groups = determineValidationGroups(invocation);
        Set<ConstraintViolation<Object>> result = this.validator.validateParameters(invocation.getThis(),
                invocation.getMethod(), invocation.getArguments(), groups);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException("invalid parameter(s)", result);
        }
        Object returnValue = invocation.proceed();
        result = this.validator
                .validateReturnValue(invocation.getThis(), invocation.getMethod(), returnValue, groups);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException("invalid return value(s)", result);
        }
        return returnValue;
    }

    protected Class[] determineValidationGroups(MethodInvocation invocation) {
        Validated valid = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
        return (valid != null ? valid.value() : new Class[0]);
    }

}
