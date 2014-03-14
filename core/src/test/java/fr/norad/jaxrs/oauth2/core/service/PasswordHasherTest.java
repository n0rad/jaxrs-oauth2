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
package fr.norad.jaxrs.oauth2.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class PasswordHasherTest {

    @Test
    public void should_find_same_hash_when_same_input() throws Exception {
        String first = new PasswordHasher("GLOBAL").hash("password", "passwordSalt");
        String second = new PasswordHasher("GLOBAL").hash("password", "passwordSalt");

        assertThat(first).isEqualTo(second);
    }

    @Test
    public void should_not_find_same_hash_when_global_salt_differs() throws Exception {
        String first = new PasswordHasher("GLOBAL").hash("password", "passwordSalt");
        String second = new PasswordHasher("GLOBAL2").hash("password", "passwordSalt");

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void should_not_find_same_hash_when_password_differs() throws Exception {
        String first = new PasswordHasher("GLOBAL").hash("password", "passwordSalt");
        String second = new PasswordHasher("GLOBAL").hash("password2", "passwordSalt");

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void should_not_find_same_hash_when_password_salt_differs() throws Exception {
        String first = new PasswordHasher("GLOBAL").hash("password", "passwordSalt");
        String second = new PasswordHasher("GLOBAL").hash("password", "passwordSalt2");

        assertThat(first).isNotEqualTo(second);
    }
}
