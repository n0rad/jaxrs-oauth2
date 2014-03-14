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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.cxf.jaxrs.ext.ResourceComparator;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.ClassResourceInfoComparator;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetClassAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.api.spec.resource.Grants;
import fr.norad.jaxrs.oauth2.core.resource.OauthResource;

public class GrantTypeBasedResourceComparator extends ClassResourceInfoComparator implements ResourceComparator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<GrantType, Class<?>> grantHandlerClasses = new HashMap<>();

    private CXFMessageReader messageReader = new CXFMessageReader();

    @Autowired
    @OauthResource
    void initGrantToServiceClassMapping(List<TargetClassAware> services) {
        for (TargetClassAware service : services) {
            Grants annotation = AnnotationUtils.findAnnotation(service.getTargetClass(), Grants.class);
            if (annotation != null) {
                grantHandlerClasses.put(annotation.value(), service.getTargetClass());
            }
        }
    }

    public GrantTypeBasedResourceComparator() {
        super(null);
    }

    @Override
    public int compare(ClassResourceInfo cri1, ClassResourceInfo cri2, Message message) {
        if (messageReader.isOptionMethod(message)) {
            return 0;
        }
        try {
            Class<?> wanted = grantHandlerClasses.get(messageReader.extractGrantTypeParameter(message));
            if (cri1.getServiceClass().equals(cri2.getServiceClass())) {
                return 0;
            } else if (cri1.getServiceClass().equals(wanted)) {
                return -1;
            } else if (cri2.getServiceClass().equals(wanted)) {
                return 1;
            } else {
                return 0;
            }
        } catch (MessageReadException e) {
            logger.debug("error while extracting grant_type_from request", e);
            throw new ValidationException(e.getMessage(), e);
        }
    }

    @Override
    public int compare(OperationResourceInfo oper1, OperationResourceInfo oper2, Message message) {
        return 0;
    }
}
