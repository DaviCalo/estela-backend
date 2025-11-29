package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.UserDAO;
import dev.smd.estela.backend.model.User;
import java.util.Optional;

public class AuthService {

        UserDAO userDto = new UserDAO();

        public Optional<User> authenticate(String email, String password) {;
                User user = userDto.login(email, password);

                if (user != null) {
                        return Optional.of(user);
                }
                return Optional.empty();
        }
        
        public Optional<User> register(String username, String password, String name, String nickName) {
                /*if (userDto.findByUsername(username) != null) {
                        return Optional.empty();
                }*/

                User newUser = new User();
                newUser.setEmail(username);
                newUser.setPassword(password);
                newUser.setAdministrator(false);
                newUser.setName(name);
                newUser.setNickname(nickName);

                Boolean isSucess = userDto.save(newUser);

                if (!isSucess) {
                        return Optional.empty();
                }

                newUser.setUserId(userDto.findByUsername(username).getUserId());

                return Optional.of(newUser);
        }
}
