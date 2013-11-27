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

import static fr.norad.jaxrs.oauth2.SecuredUtils.findSecuredInfo;
import static fr.norad.jaxrs.oauth2.SecuredUtils.isAuthorized;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SecuredUtilsTest {
    public static Set<String> set(String... scopes) {
        return new HashSet<>(Arrays.asList(scopes));
    }

    @Test
    public void should_find_secured_return_null_on_non_annotated() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(findSecuredInfo(method)).isNull();
    }

    @Test
    public void should_not_authorize_non_secured_method() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isFalse();
    }

    @Test
    public void should_not_authorize_for_empty_current_scope() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set())).isFalse();
    }

    @Test
    public void should_not_authorize_for_empty_current_scope_or() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set())).isFalse();
    }

    @Test
    public void should_not_fail_on_null_current_scope() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isFalse();
    }

    @Test
    public void should_authorize_single_scope() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_authorize_empty_scope() throws Exception {
        class Test {
            @SecuredWithScope("")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        isAuthorized(method, set("scope1"));
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_authorize_empty_scope_or() throws Exception {
        class Test {
            @SecuredWithScope(value = "")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        isAuthorized(method, set("scope1"));
    }

    @Test
    public void should_authorize_single_scope_and() throws Exception {
        class Test {
            @SecuredWithScope(value = "scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isTrue();
    }

    @Test
    public void should_authorize_single_scope_or() throws Exception {
        class Test {
            @SecuredWithScope(value = "scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isTrue();
    }

    @Test
    public void should_not_authorize_single_scope() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isFalse();
    }

    @Test
    public void should_not_authorize_2_and_scope_missing() throws Exception {
        class Test {
            @SecuredWithAllScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isFalse();
    }

    @Test
    public void should_not_authorize_2_and_scope_missing2() throws Exception {
        class Test {
            @SecuredWithAllScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isFalse();
    }

    @Test
    public void should_authorize_2_or_scope() throws Exception {
        class Test {
            @SecuredWithAnyScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isTrue();
    }

    @Test
    public void should_authorize_2_or_scope2() throws Exception {
        class Test {
            @SecuredWithAnyScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_not_authorize_or_scope2() throws Exception {
        class Test {
            @SecuredWithAnyScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_find_secured_on_class() throws Exception {
        @SecuredWithAnyScopesOf({"scope1", "scope2"})
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_find_secured_on_interface() throws Exception {
        class Test implements NotSecuredInterface {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_find_secured_on_extends() throws Exception {
        @SecuredWithAnyScopesOf({"scope1", "scope2"})
        class NotSecuredExtends {
        }
        class Test extends NotSecuredExtends {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_find_secured_on_override() throws Exception {
        class NotSecuredExtends {
            @SecuredWithAnyScopesOf({"scope1", "scope2"})
            public void genre() {
            }
        }
        class Test extends NotSecuredExtends {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_find_secured_on_method_interface() throws Exception {
        class Test implements NotSecuredMethodInterface {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @SecuredWithAnyScopesOf({"scope1", "scope2"})
    interface NotSecuredInterface {
    }

    interface NotSecuredMethodInterface {
        @SecuredWithAnyScopesOf({"scope1", "scope2"})
        public void genre();
    }


}
