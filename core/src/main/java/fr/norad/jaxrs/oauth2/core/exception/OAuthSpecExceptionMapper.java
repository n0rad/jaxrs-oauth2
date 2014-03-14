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
package fr.norad.jaxrs.oauth2.core.exception;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.client.server.api.HttpStatus;
import fr.norad.jaxrs.oauth2.api.spec.exception.OauthSpecError;
import fr.norad.jaxrs.oauth2.api.spec.exception.OauthSpecException;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Provider
@Accessors(chain = true, fluent = true)
public class OAuthSpecExceptionMapper implements ExceptionMapper<Exception> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private boolean logRuntimeError = true;
    private boolean logCheckedError = false;

    @Override
    public Response toResponse(Exception exception) {
        logError(exception);

        Status status = findStatus(exception);
        OauthSpecError.Type type;
        if (OauthSpecException.class.isAssignableFrom(exception.getClass())) {
            type = ((OauthSpecException) exception).getErrorType();
        } else {
            type = OauthSpecError.Type.INVALID_REQUEST;
            status = Status.INTERNAL_SERVER_ERROR;
        }

        OauthSpecError error = OauthSpecError.newOauthSpecError(type, "REQ!!!!ID", exception.getMessage()); //TODO requestIdMDC
        return Response.status(status).entity(error).type(APPLICATION_JSON_TYPE).build();
    }

    public Status findStatus(Exception exception) {
        Status status = Status.BAD_REQUEST;
        HttpStatus annotationStatus = AnnotationUtils.findAnnotation(exception.getClass(), HttpStatus.class);
        if (annotationStatus != null) {
            status = annotationStatus.value();
        }
        return status;
    }

    private void logError(Exception exception) {
        if (RuntimeException.class.isAssignableFrom(exception.getClass())) {
            if (logRuntimeError) {
                log.error("Technical exception", exception);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Respond error", exception);
            } else if (logCheckedError) {
                log.info("Respond error : {}", exception.getMessage());
            }
        }
    }


}
