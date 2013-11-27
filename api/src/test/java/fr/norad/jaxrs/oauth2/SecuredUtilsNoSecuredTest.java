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
import static fr.norad.jaxrs.oauth2.SecuredUtils.isNotSecured;
import static fr.norad.jaxrs.oauth2.SecuredUtilsTest.set;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import org.junit.Test;

public class SecuredUtilsNoSecuredTest {

    @Test
    public void should_find_if_not_secured() throws Exception {
        class Test {
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isNotSecured(method)).isTrue();
    }

    @Test
    public void should_not_find_if_not_secured() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isNotSecured(method)).isFalse();
    }

    @Test
    public void should_authorize_not_authenticated_for_authenticated() throws Exception {
        class Test {
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isTrue();
    }

    @Test
    public void should_not_authorize_not_annotated_method_for_non_authenticated() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isFalse();
    }

    @Test
    public void should_not_authorize_not_annotated_method_for_authenticated() throws Exception {
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, set("scope1"))).isFalse();
    }

    @Test
    public void should_authorize_not_authenticated_for_non_authenticated() throws Exception {
        class Test {
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void should_fail_on_secured_and_not_secured() throws Exception {
        class Test {
            @SecuredWithScope("scope1")
            @NotSecured
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        isAuthorized(method, set("scope1"));
    }

    @Test
    public void should_find_not_secured_on_class() throws Exception {
        @NotSecured
        class Test {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @Test
    public void should_find_not_secured_on_interface() throws Exception {
        class Test implements NotSecuredInterface {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @Test
    public void should_find_not_secured_on_extends() throws Exception {
        @NotSecured
        class NotSecuredExtends {
        }
        class Test extends NotSecuredExtends {
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @Test
    public void should_find_not_secured_on_override() throws Exception {
        class NotSecuredExtends {
            @NotSecured
            public void genre() {
            }
        }
        class Test extends NotSecuredExtends {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @Test
    public void should_find_not_secured_on_method_interface() throws Exception {
        class Test implements NotSecuredMethodInterface {
            @Override
            public void genre() {
            }
        }
        Method method = Test.class.getMethod("genre");

        assertThat(isAuthorized(method, null)).isTrue();
    }

    @NotSecured
    interface NotSecuredInterface {
    }

    interface NotSecuredMethodInterface {
        @NotSecured
        public void genre();
    }


}
