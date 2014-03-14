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

import static fr.norad.jaxrs.oauth2.api.spec.resource.OAuthParams.GRANT_TYPE;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.HttpMethod;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;

class CXFMessageReader {

    public boolean isOptionMethod(Message message) {
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        return HttpMethod.OPTIONS.equals(httpMethod);
    }

    public GrantType extractGrantTypeParameter(Message message) throws MessageReadException {
        Map<String, List<String>> params = extractFormParams(message);
        List<String> grantTypes = params.get(GRANT_TYPE);
        if (grantTypes == null) {
            throw new MessageReadException(GRANT_TYPE + " is mandatory");
        } else if (grantTypes.size() != 1) {
            throw new MessageReadException(GRANT_TYPE + " must be unique");
        } else {
            return valueOf(grantTypes.get(0));
        }
    }

    private GrantType valueOf(String string) throws MessageReadException {
        for (GrantType type : GrantType.values()) {
            if (type.name().equals(string)) {
                return type;
            }
        }
        throw new MessageReadException(string + " is not a valid grant type");
    }

    private Map<String, List<String>> extractFormParams(Message message) throws MessageReadException {
        InputStream is = message.getContent(InputStream.class);
        StringBuilder buf = new StringBuilder();
        if (is != null) {
            try (CachedOutputStream bos = new CachedOutputStream(); InputStream stream = is) {
                IOUtils.copy(stream, bos);

                bos.flush();

                message.setContent(InputStream.class, bos.getInputStream());
                bos.writeCacheTo(buf);
            } catch (IOException e) {
                throw new MessageReadException(e);
            }
        }
        return JAXRSUtils.getStructuredParams(buf.toString(), "&", false, false);
    }
}
