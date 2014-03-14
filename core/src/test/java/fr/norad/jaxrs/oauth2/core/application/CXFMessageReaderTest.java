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
package fr.norad.jaxrs.oauth2.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.HttpMethod;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;

public class CXFMessageReaderTest {

    private CXFMessageReader reader = new CXFMessageReader();

    private Message message = new MessageImpl();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_return_true_if_option_method() throws Exception {
        message.put(Message.HTTP_REQUEST_METHOD, HttpMethod.OPTIONS);

        assertThat(reader.isOptionMethod(message)).isTrue();
    }

    @Test
    public void should_return_true_if_post_method() throws Exception {
        message.put(Message.HTTP_REQUEST_METHOD, HttpMethod.POST);

        assertThat(reader.isOptionMethod(message)).isFalse();
    }

    @Test
    public void should_find_valid_grant_type_in_message_body() throws Exception {
        String messageBody = "param1=toto&grant_type=password&param2=titi";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        assertThat(reader.extractGrantTypeParameter(message)).isEqualTo(GrantType.password);
    }

    @Test
    public void should_fail_when_grant_type_param_is_present_but_invalid() throws Exception {
        String messageBody = "param1=toto&grant_type=hiya&param2=titi";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_grant_type_param_is_absent() throws Exception {
        String messageBody = "param1=toto&param2=titi";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_grant_type_param_is_present_but_empty() throws Exception {
        String messageBody = "param1=toto&grant_type=&param2=titi";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_several_grant_type_params_are_present() throws Exception {
        String messageBody = "param1=toto&grant_type=hiya&grant_type=password&param2=titi";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_message_has_no_body() throws Exception {
        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_body_is_empty() throws Exception {
        String messageBody = "";
        message.setContent(InputStream.class, new ByteArrayInputStream(messageBody.getBytes()));

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }

    @Test
    public void should_fail_when_body_cannot_be_read() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        message.setContent(InputStream.class, inputStream);
        when(inputStream.read()).thenThrow(new IOException());

        thrown.expect(MessageReadException.class);
        reader.extractGrantTypeParameter(message);
    }
}
