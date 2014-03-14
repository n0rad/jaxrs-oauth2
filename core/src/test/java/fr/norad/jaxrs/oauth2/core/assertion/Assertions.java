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
package fr.norad.jaxrs.oauth2.core.assertion;

import fr.norad.jaxrs.oauth2.api.spec.domain.Token;

public class Assertions extends org.assertj.core.api.Assertions {
//    public static UserAssert assertThat(User user){
//        return new UserAssert(user);
//    }
//
//    public static ResponseAssert assertThat(Response response){
//        return new ResponseAssert(response);
//    }

    public static TokenAssert assertThat(Token token) {
        return new TokenAssert(token);
    }
}
