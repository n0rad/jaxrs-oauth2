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
import javax.validation.ValidationException;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.TargetClassAware;
import com.google.common.collect.Lists;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.resource.Grants;

@RunWith(MockitoJUnitRunner.class)
public class GrantTypeBasedResourceComparatorTest {

    @InjectMocks
    private GrantTypeBasedResourceComparator comparator = new GrantTypeBasedResourceComparator();

    @Mock
    private CXFMessageReader messageReader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        comparator.initGrantToServiceClassMapping(Lists.newArrayList(
                springLikeProxy(TestResourceA.class),
                springLikeProxy(TestResourceB.class),
                springLikeProxy(TestResourceC.class)));
    }

    @Test
    public void should_always_consider_comparator_equal_when_option_request() throws Exception {
        Message message = mock(Message.class);
        when(messageReader.extractGrantTypeParameter(message)).thenThrow(
                new MessageReadException("can't read grant_type !"));
        when(messageReader.isOptionMethod(message)).thenReturn(true);

        ClassResourceInfo criA = new ClassResourceInfo(TestResourceA.class);
        ClassResourceInfo criB = new ClassResourceInfo(TestResourceB.class);
        ClassResourceInfo criC = new ClassResourceInfo(TestResourceC.class);

        assertThat(comparator.compare(criB, criA, message)).isEqualTo(0);
        assertThat(comparator.compare(criA, criB, message)).isEqualTo(0);
        assertThat(comparator.compare(criB, criB, message)).isEqualTo(0);
        assertThat(comparator.compare(criA, criA, message)).isEqualTo(0);
        assertThat(comparator.compare(criB, criC, message)).isEqualTo(0);
    }

    @Test
    public void should_fail_when_no_grant_type_param_cant_be_read() throws Exception {
        Message message = new MessageImpl();
        when(messageReader.extractGrantTypeParameter(message)).thenThrow(
                new MessageReadException("can't read grant_type !"));

        thrown.expect(ValidationException.class);
        comparator.compare(new ClassResourceInfo(TestResourceB.class), new ClassResourceInfo(TestResourceA.class),
                message);
    }

    @Test
    public void should_compare_resource_and_favor_the_one_with_the_requested_grant_type() throws Exception {
        Message message = new MessageImpl();
        when(messageReader.extractGrantTypeParameter(message)).thenReturn(GrantType.password);

        ClassResourceInfo criA = new ClassResourceInfo(TestResourceA.class);
        ClassResourceInfo criB = new ClassResourceInfo(TestResourceB.class);
        ClassResourceInfo criC = new ClassResourceInfo(TestResourceC.class);

        assertThat(comparator.compare(criB, criA, message)).isEqualTo(1);
        assertThat(comparator.compare(criA, criB, message)).isEqualTo(-1);
        assertThat(comparator.compare(criB, criB, message)).isEqualTo(0);
        assertThat(comparator.compare(criA, criA, message)).isEqualTo(0);
        assertThat(comparator.compare(criB, criC, message)).isEqualTo(0);
    }

    @Grants(GrantType.password)
    private static class TestResourceA {

    }

    @Grants(GrantType.client_credentials)
    private static class TestResourceB {

    }

    @Grants(GrantType.refresh_token)
    private static class TestResourceC {

    }

    private TargetClassAware springLikeProxy(final Class<?> clazz) {
        return new TargetClassAware() {
            @Override
            public Class<?> getTargetClass() {
                return clazz;
            }
        };
    }
}
