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

import java.lang.reflect.Method;
import java.util.Set;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.oauth2.api.Scope;
import fr.norad.jaxrs.oauth2.api.SecuredInfo;
import fr.norad.jaxrs.oauth2.api.SecuredUtils;

public class AuthorizationInterceptor extends AbstractPhaseInterceptor<Message> {

    private final AccessTokenProvider accessTokenProvider;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public AuthorizationInterceptor(AccessTokenProvider accessTokenProvider) {
        super(Phase.POST_STREAM);
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Method method = message.getExchange().get(Method.class);
        SecuredInfo securedInfo = SecuredUtils.findSecuredInfo(method);
        if (securedInfo == null || securedInfo.getScopes().isEmpty()) {
            logger.debug("No authorization requirement found, doing nothing");
            return;
        }
        attachAuthorizationToRequest(message, requestTokenGranting(securedInfo.getScopes()));
    }

    private void attachAuthorizationToRequest(Message message, String accessToken) {
        logger.debug("Adding Authorization header to request");
        getHeaders(message.getExchange()).putSingle("Authorization", "Bearer " + accessToken);
    }

    private MetadataMap<String, String> getHeaders(Exchange exchange) {
        return (MetadataMap<String, String>) exchange.get(Message.PROTOCOL_HEADERS);
    }

    private String requestTokenGranting(Set<Scope> requiredScopes) {
        return accessTokenProvider.retrieveAccessTokenWith(requiredScopes);
    }
}
