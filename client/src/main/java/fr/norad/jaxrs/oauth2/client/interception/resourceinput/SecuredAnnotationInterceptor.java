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
package fr.norad.jaxrs.oauth2.client.interception.resourceinput;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.WebApplicationExceptionMapper;
import fr.norad.jaxrs.oauth2.api.SecurityDeclarationException;
import fr.norad.jaxrs.oauth2.api.SecurityForbiddenException;
import fr.norad.jaxrs.oauth2.api.SecurityUnauthorizedException;
import fr.norad.jaxrs.oauth2.api.TokenFetcher;
import lombok.Data;

@Data
public class SecuredAnnotationInterceptor extends AbstractPhaseInterceptor<Message> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExceptionMapper<Exception> exceptionMapper = new ErrorExceptionMapper();
    private ExceptionMapper<WebApplicationException> notFoundExceptionMapper = new WebApplicationExceptionMapper();
    private SecuredAnnotationChecker securedAnnotationChecker;

    public SecuredAnnotationInterceptor(TokenFetcher tokenFetcher) {
        super(Phase.PRE_INVOKE);
        securedAnnotationChecker = new SecuredAnnotationChecker(tokenFetcher);
    }

    @Override
    public void handleMessage(Message message) {
        try {
            authorize(message);
        } catch (SecurityForbiddenException | SecurityUnauthorizedException | SecurityDeclarationException e) {
            message.getExchange().put(Response.class, exceptionMapper.toResponse(e));
        } catch (NotFoundException e) {
            message.getExchange().put(Response.class, notFoundExceptionMapper.toResponse(e));
        }
    }

    void authorize(Message message) {
        securedAnnotationChecker.authorize(getServiceMethod(message), searchAuthHeader(message));
    }

    public AuthorizationHeader searchAuthHeader(Message message) {
        Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
        return AuthorizationHeader.authorizationHeaderFrom(headers);
    }

    public Method getServiceMethod(Message m) {
        Method method = (Method) m.get("org.apache.cxf.resource.method");
        if (method == null) {
            throw new NotFoundException();
        }
        return method;
    }

}
