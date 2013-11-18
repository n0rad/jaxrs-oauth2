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

import static fr.norad.jaxrs.oauth2.SecuredUtils.isAuthorized;
import static fr.norad.jaxrs.oauth2.SecuredUtilsTest.set;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import org.junit.Test;

public class SecuredUtilsMultiTest {
    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi() throws Exception {
        class Test {
            @SecuredWithAnyScopesOf({"scope1", "scope2"})
            @SecuredWithAllScopesOf({"scope2", "scope3"})
            @SecuredWithScope("genre")
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi2() throws Exception {
        class Test {
            @NotSecured
            @SecuredWithScope("genre")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi3() throws Exception {
        class Test {
            @SecuredWithAllScopesOf({"scope2", "scope3"})
            @SecuredWithScope("genre")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi4() throws Exception {
        class Test {
            @SecuredWithAllScopesOf({"scope2", "scope3"})
            @SecuredWithScope("genre")
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi_on_class() throws Exception {
        @SecuredWithAllScopesOf({"scope2", "scope3"})
        @SecuredWithScope("genre")
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi_on_class2() throws Exception {
        @NotSecured
        @SecuredWithScope("genre")
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi_on_class_inherited() throws Exception {
        @SecuredWithScope("genre")
        class superClass {
        }
        @NotSecured
        class Test extends superClass {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_support_multi_on_interface() throws Exception {
        @NotSecured
        class Test implements NotSecuredInterface {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isTrue();
    }

    @Test
    public void should_allow_override_for_method() throws Exception {
        class Test implements NotSecuredInterface {
            @SecuredWithAnyScopesOf({"scope42", "scope42"})
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope2"))).isFalse();
        assertThat(isAuthorized(method, set("scope42"))).isTrue();
    }

    @SecuredWithAnyScopesOf({"scope1", "scope2"})
    interface NotSecuredInterface {
    }

    interface NotSecuredMethodInterface {
        @SecuredWithAnyScopesOf({"scope1", "scope2"})
        public void genre();
    }

}
