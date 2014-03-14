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
package fr.norad.jaxrs.oauth2.core.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.base.Preconditions;

public class PasswordHasher {

    private static final String SHA512 = "SHA-512";

    private static final String UTF8 = "UTF-8";

    private final byte[] globalSalt;

    public PasswordHasher(String globalSalt) {
        Preconditions.checkNotNull(globalSalt);
        digester();
        this.globalSalt = bytes(globalSalt);
    }

    public String hash(String password, String oneTimeSalt) {
        return hash(bytes(password), bytes(oneTimeSalt));
    }

    private MessageDigest digester() {
        try {
            return MessageDigest.getInstance(SHA512);
        } catch (NoSuchAlgorithmException e1) {
            throw new IllegalStateException(SHA512 + "not supported", e1);
        }
    }

    private String hash(byte[] password, byte[] oneTimeSalt) {
        byte[] sentence = join(password, oneTimeSalt, globalSalt);
        return string(digester().digest(sentence));
    }

    private byte[] bytes(String string) {
        try {
            return string.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Please add support for " + UTF8, e);
        }
    }

    private String string(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    private byte[] join(byte[]... arrays) {
        byte[] total = new byte[0];
        for (byte[] array : arrays) {
            total = ArrayUtils.addAll(total, array);
        }
        return total;
    }
}
