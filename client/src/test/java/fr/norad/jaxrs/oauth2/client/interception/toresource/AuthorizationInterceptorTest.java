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

import static fr.norad.jaxrs.oauth2.client.interception.toresource.SecuredResource.Scopes.SCOPE1;
import static fr.norad.jaxrs.oauth2.client.interception.toresource.SecuredResource.Scopes.SCOPE2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.common.collect.ImmutableSet;
import fr.norad.jaxrs.oauth2.api.Scope;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationInterceptorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private Message message;
    @Mock
    private Exchange exchange;
    @Mock
    private AccessTokenProvider provider;

    @InjectMocks
    private AuthorizationInterceptor interceptor;

    private MetadataMap<String, String> headers = new MetadataMap<>();
    private String accessToken = UUID.randomUUID().toString();
    private Set<Scope> scopes = ImmutableSet.of((Scope) SCOPE2, (Scope) SCOPE1);

    public class Resource {
        @SecuredResource(SCOPE1)
        public void res() {

        }
    }

    @Before
    public void setUp() throws Exception {
        when(message.getExchange()).thenReturn(exchange);
        when(exchange.get(Message.PROTOCOL_HEADERS)).thenReturn(headers);
        when(exchange.get(Method.class)).thenReturn(Resource.class.getMethod("res"));
    }

    @Test
    public void should_add_authorization_header_with_token_when_method__and_class_are_annotated() throws Exception {
        when(provider.retrieveAccessTokenWith(scopes)).thenReturn(accessToken);

        interceptor.handleMessage(message);

        assertThat(headers.get("Authorization")).containsExactly("Bearer " + accessToken);
    }

    @Test
    public void should_add_authorization_header_with_token_when_class_is_annotated() throws Exception {
        when(provider.retrieveAccessTokenWith(scopes)).thenReturn(accessToken);

        interceptor.handleMessage(message);

        assertThat(headers.get("Authorization")).containsExactly("Bearer " + accessToken);
    }

    @Test
    public void should_not_do_anything_when_class_and_method_not_annotated() throws Exception {

        interceptor.handleMessage(message);

        assertThat(headers).isEmpty();
        verifyZeroInteractions(provider);
    }
}
