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
package fr.norad.jaxrs.oauth2.api;

import static fr.norad.jaxrs.oauth2.api.ScopeStrategy.ALL;
import static fr.norad.jaxrs.oauth2.api.ScopeStrategy.ANY;
import static fr.norad.jaxrs.oauth2.api.SecuringSomething.TheScopes.SCOPE_A;
import static fr.norad.jaxrs.oauth2.api.SecuringSomething.TheScopes.SCOPE_B;
import static fr.norad.jaxrs.oauth2.api.SecuringSomething.TheScopes.SCOPE_C;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SecuredInfoTest {

    private static Set<Scope> IGNORED = Collections.emptySet();

    private static Scope[] EMPTY_ARRAY = asArray();

    private static Set<Scope> setOf(Scope... scopes) {
        return new HashSet<>(Arrays.asList(scopes));
    }

    private static Scope[] asArray(Scope... scopes) {
        return scopes;
    }

    @Test
    public void should_authorize_all_for_empty_required_scope() {
        assertThat(new SecuredInfo(EMPTY_ARRAY, ALL).isAuthorizingScopes(IGNORED)).isTrue();
    }

    @Test
    public void should_NOT_authorize_on_null_current_scope() {
        assertThat(new SecuredInfo(asArray(SCOPE_A), ALL).isAuthorizingScopes(null)).isFalse();
    }

    @Test
    public void should_authorize_using_ALL_strategy_when_scope_matches_the_annotated_scope() {
        assertThat(new SecuredInfo(asArray(SCOPE_A), ALL).isAuthorizingScopes(setOf(SCOPE_A))).isTrue();
    }

    @Test
    public void should_authorize_using_ANY_strategy_when_scope_matches_the_annotated_scope() {
        assertThat(new SecuredInfo(asArray(SCOPE_A), ANY).isAuthorizingScopes(setOf(SCOPE_A))).isTrue();
    }

    @Test
    public void should_NOT_authorize_when_scope_DOESNT_match_the_annotated_scope() {
        assertThat(new SecuredInfo(asArray(SCOPE_A), ALL).isAuthorizingScopes(setOf(SCOPE_B))).isFalse();
    }

    @Test
    public void should_authorize_using_ALL_strategy_when_two_scopes_required_and_both_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ALL).isAuthorizingScopes(setOf(SCOPE_A, SCOPE_B)))
                .isTrue();
    }

    @Test
    public void should_NOT_authorize_using_ALL_strategy_when_two_scopes_required_but_only_first_one_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ALL).isAuthorizingScopes(setOf(SCOPE_A))).isFalse();
    }

    @Test
    public void should_NOT_authorize_using_ALL_strategy_when_two_scopes_required_but_only_last_one_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ALL).isAuthorizingScopes(setOf(SCOPE_B))).isFalse();
    }

    @Test
    public void should_authorize_using_ANY_strategy_when_two_scopes_defined_and_only_first_one_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ANY).isAuthorizingScopes(setOf(SCOPE_A))).isTrue();
    }

    @Test
    public void should_authorize_using_ANY_strategy_when_two_scopes_defined_and_only_last_one_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ANY).isAuthorizingScopes(setOf(SCOPE_B))).isTrue();
    }

    @Test
    public void should_NOT_authorize_using_ANY_strategy_when_two_scopes_defined_but_none_given() {
        assertThat(new SecuredInfo(asArray(SCOPE_A, SCOPE_B), ANY).isAuthorizingScopes(setOf(SCOPE_C))).isFalse();
    }
}
