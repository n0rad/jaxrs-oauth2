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
package fr.norad.jaxrs.oauth2.core.tck.spring;


import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.ResourceComparator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.jaxrs.oauth2.core.application.GrantTypeBasedResourceComparator;
import fr.norad.jaxrs.oauth2.core.exception.OAuthSpecExceptionMapper;
import fr.norad.jaxrs.oauth2.core.resource.OauthResource;
import fr.norad.jaxrs.oauth2.core.service.PasswordHasher;

@Configuration
@ComponentScan(basePackages = "fr.norad.jaxrs.oauth2.core")
public class SpringConfiguration {
    private static final String CXF_BUS_BEAN = "cxf";
    public static final String LOCAL_ADDRESS = "local://test";

    @Bean(destroyMethod = "shutdown", name = CXF_BUS_BEAN)
    SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    PasswordHasher passwordHasher() {
        return new PasswordHasher("VERYSECUREDGLOBALSALT");
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer props = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();
        properties.setProperty("token.lifetime.second.default", "42");
        properties.setProperty("refresh_token.lifetime.second.default", "42");
        props.setProperties(properties);
        return props;
    }

    @DependsOn(CXF_BUS_BEAN)
    @Bean
    Server oauthServer(@OauthResource List<Object> oauthServices) {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBeans(oauthServices);
        sf.setAddress(LOCAL_ADDRESS);
        sf.setResourceComparator(tokenResourceComparator());
        List<Object> providers = Arrays.asList(
                new JacksonJaxbJsonProvider(),
                new OAuthSpecExceptionMapper());
        sf.setProviders(providers);
        return sf.create();
    }

    @Bean
    ResourceComparator tokenResourceComparator() {
        return new GrantTypeBasedResourceComparator();
    }

    @Bean
    public Hibernate5MethodValidationPostProcessor methodValidationPostProcessor() {
        Hibernate5MethodValidationPostProcessor processor = new Hibernate5MethodValidationPostProcessor();
        processor.setBeforeExistingAdvisors(true);
        return processor;
    }

}
