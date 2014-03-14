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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.norad.jaxrs.oauth2.api.spec.exception.InvalidGrantOauthException;
import fr.norad.jaxrs.oauth2.core.domain.User;
import fr.norad.jaxrs.oauth2.core.persistence.UserNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.UserRepository;

@Service
public abstract class UserSpecService {

    @Autowired
    private PasswordHasher hasher;

    @Autowired
    private UserRepository userRepository;

    protected abstract int getMaxFailedLoginAttempt();

    public User authenticate(String username, String password) throws InvalidGrantOauthException, UserNotFoundException {
        User user = userRepository.findUser(username);
        if (user.getFailedLoginAttempt() >= getMaxFailedLoginAttempt()) {
            throw new InvalidGrantOauthException("Too many failed login attempt");
        }

        String actual = hasher.hash(password, user.getSalt());
        String expected = user.getHashedPassword();
        if (actual.equals(expected)) {
            return user;
        } else {
            userRepository.increaseFailedLoginAttempts(user);
            throw new InvalidGrantOauthException("User password mismatch");
        }
    }
}
