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

import static fr.norad.jaxrs.oauth2.ScopeStrategy.ALL;
import static fr.norad.jaxrs.oauth2.ScopeStrategy.ANY;
import static fr.norad.jaxrs.oauth2.SecuredUtils.findSecuredInfo;
import static fr.norad.jaxrs.oauth2.SecuringSomething.TheScopes.SCOPE_A;
import static fr.norad.jaxrs.oauth2.SecuringSomething.TheScopes.SCOPE_B;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import org.junit.Test;

public class SecuredUtilsTest {

    @Test
    public void should_return_null_on_non_annotated() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(findSecuredInfo(method)).isNull();
    }

    @Test
    public void should_find_empty_scopes_when_method_annotated_with_not_secured() throws Exception {
        class Test {
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).isEmpty();
        assertThat(securedInfo.getStrategy()).isEqualTo(ALL);
    }

    @Test
    public void should_find_empty_scopes_when_class_annotated_with_not_secured() throws Exception {
        @NotSecured
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).isEmpty();
        assertThat(securedInfo.getStrategy()).isEqualTo(ALL);
    }

    @Test(expected = IllegalStateException.class)
    public void should_fail_when_method_annotated_with_empty_scope() throws Exception {
        class Test {
            @SecuringSomething({})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        findSecuredInfo(method);
    }

    @Test
    public void should_find_scopes_when_method_annotated() throws Exception {
        class Test {
            @SecuringSomething({ SCOPE_A, SCOPE_B })
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ALL);
    }

    @Test
    public void should_find_given_strategy() throws Exception {
        class Test {
            @SecuringSomething(value = { SCOPE_A, SCOPE_B }, strategy = ANY)
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ANY);
    }

    @Test
    public void should_find_scopes_on_interface() throws Exception {
        class Test implements SecuredInterface {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ANY);
    }

    @Test
    public void should_find_secured_on_extends() throws Exception {
        @SecuringSomething(value = { SCOPE_A, SCOPE_B }, strategy = ANY)
        class SecuredExtends {
        }
        class Test extends SecuredExtends {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ANY);
    }

    @Test
    public void should_find_secured_on_overridden_method() throws Exception {
        class SecuredExtends {
            @SecuringSomething(value = { SCOPE_A, SCOPE_B }, strategy = ANY)
            public void genre() {
            }
        }
        class Test extends SecuredExtends {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ANY);
    }

    @Test
    public void should_find_secured_on_method_interface() throws Exception {
        class Test implements SecuredMethodInterface {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        SecuredInfo securedInfo = findSecuredInfo(method);
        assertThat(securedInfo.getScopes()).containsOnly(SCOPE_A, SCOPE_B);
        assertThat(securedInfo.getStrategy()).isEqualTo(ANY);
    }

    @SecuringSomething(value = { SCOPE_A, SCOPE_B }, strategy = ANY)
    interface SecuredInterface {
    }

    interface SecuredMethodInterface {
        @SecuringSomething(value = { SCOPE_A, SCOPE_B }, strategy = ANY)
        public void genre();
    }
}
