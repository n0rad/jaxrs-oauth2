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
package fr.norad.jaxrs.oauth2.core.tck;


import static fr.norad.jaxrs.oauth2.core.tck.spring.SpringConfiguration.LOCAL_ADDRESS;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.client.server.rest.RestBuilder.Generic;
import fr.norad.jaxrs.oauth2.api.spec.resource.ResourceOwnerPasswordGrantResource;
import fr.norad.jaxrs.oauth2.client.OauthSpecResponseExceptionMapper;
import fr.norad.jaxrs.oauth2.core.builder.ClientTestBuilder;
import fr.norad.jaxrs.oauth2.core.builder.UserTestBuilder;

@Component
public class Oauth2 extends ExternalResource {

    @Autowired
    private TestRepositories repositories;

    private RestBuilder builder = new RestBuilder()
            .addInInterceptor(Generic.inStdoutLogger)
            .addOutInterceptor(Generic.outStdoutLogger)
            .addProvider(new OauthSpecResponseExceptionMapper())
            .addProvider(new JacksonJaxbJsonProvider());


    public ResourceOwnerPasswordGrantResource passwordGrantResource() {
        return builder.buildClient(ResourceOwnerPasswordGrantResource.class, LOCAL_ADDRESS);
    }

    @Override
    protected void after() {
        super.after();
    }

    public void addClient(ClientTestBuilder clientBuilder) throws Exception {
        repositories.createClient(clientBuilder.build());
    }

    public void addUser(UserTestBuilder userBuilder) {
        repositories.createUser(userBuilder.build());
    }
}
