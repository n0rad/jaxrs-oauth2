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
package fr.norad.jaxrs.oauth2.client.interception.toresource;

import static fr.norad.jaxrs.oauth2.api.ScopeStrategy.ALL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import fr.norad.jaxrs.oauth2.api.Scope;
import fr.norad.jaxrs.oauth2.api.ScopeStrategy;
import fr.norad.jaxrs.oauth2.api.Secured;

@Secured
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SecuredResource {
    Scopes[] value();

    ScopeStrategy strategy() default ALL;

    public enum Scopes implements Scope {
        SCOPE1,
        SCOPE2,;

        @Override
        public String scopeIdentifier() {
            return name();
        }
    }
}
