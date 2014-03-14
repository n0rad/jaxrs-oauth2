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
package fr.norad.jaxrs.oauth2.client;


import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.client.server.rest.RestBuilder.Generic;
import fr.norad.jaxrs.oauth2.client.interception.toresource.AccessTokenProvider;

abstract class AbstractAccessTokenProvider implements AccessTokenProvider {

    private boolean verbose;
    protected final String clientId;
    protected final String clientSecret;
    protected final RestBuilder restBuilder;

    protected AbstractAccessTokenProvider(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        restBuilder = new RestBuilder().threadSafe(true)
                .addProvider(new JacksonJaxbJsonProvider())
                .addProvider(new OauthSpecResponseExceptionMapper());
    }

    public AccessTokenProvider verbose() {
        restBuilder.addOutInterceptor(Generic.outStdoutLogger);
        restBuilder.addInInterceptor(Generic.inStdoutLogger);
        return this;
    }
}
