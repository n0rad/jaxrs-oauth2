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
package fr.norad.jaxrs.oauth2.core.tck;


import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import fr.norad.jaxrs.oauth2.api.TokenNotFoundException;
import fr.norad.jaxrs.oauth2.api.spec.domain.Token;
import fr.norad.jaxrs.oauth2.core.domain.Client;
import fr.norad.jaxrs.oauth2.core.domain.RefreshToken;
import fr.norad.jaxrs.oauth2.core.domain.User;
import fr.norad.jaxrs.oauth2.core.persistence.ClientNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.ClientRepository;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.RefreshTokenRepository;
import fr.norad.jaxrs.oauth2.core.persistence.TokenRepository;
import fr.norad.jaxrs.oauth2.core.persistence.UserNotFoundException;
import fr.norad.jaxrs.oauth2.core.persistence.UserRepository;
import fr.norad.jaxrs.oauth2.core.service.PasswordHasher;

@Component
public class TestRepositories implements UserRepository, ClientRepository, TokenRepository, RefreshTokenRepository {

    @Autowired
    private PasswordHasher passwordHasher;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();

    public void clear() {
        users.clear();
        clients.clear();
    }

    public void createUser(User user) {
        user.setSalt("securedUserSalt");
        user.setHashedPassword(passwordHasher.hash(user.getPassword(), user.getSalt()));
        user.setPassword(null);
        users.put(user.getUsername(), user);
    }

    public void createClient(Client client) {
        this.clients.put(client.getId(), client);
    }

    ////////////////////////////////////////////////////////////////////////

    @Override
    public Client findClient(String clientId) throws ClientNotFoundException {
        Client client = clients.get(clientId);
        if (client == null) {
            throw new ClientNotFoundException("client not found");
        }
        return client;
    }

    @Override
    public void saveToken(Token token) {

    }

    @Override
    public void deleteToken(String accessToken) {

    }

    @Override
    public Token findToken(String accessToken) throws TokenNotFoundException {
        throw new TokenNotFoundException("not found");
    }

    @Override
    public User findUser(String username) throws UserNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException("not found");
        }
        return user;
    }

    @Override
    public void increaseFailedLoginAttempts(User user) {

    }

    @Override
    public RefreshToken findRefreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        return null;
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {

    }

    @Override
    public void deleteRefreshToken(String refreshToken) {

    }

}
