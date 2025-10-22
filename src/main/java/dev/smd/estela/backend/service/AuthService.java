/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dto.UserDAO;
import dev.smd.estela.backend.model.User;
import java.util.Optional;

public class AuthService {
    UserDAO userDto = new UserDAO();
    
    public Optional<User> authenticate(String username, String password) {
        User user = userDto.login(username, password);
        
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
    
    public Optional<User> register(String username, String password, String name, String nickName) {
        if (userDto.findByUsername(username) != null) {
            return Optional.empty();
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setAdministrator(false);
        newUser.setName(name);
        newUser.setNickname(nickName);
                
        Boolean isSucess = userDto.save(newUser);
        
        if(!isSucess){
            return Optional.empty();
        }
        
         newUser.setId(userDto.findByUsername(username).getId());
        
        return Optional.of(newUser); 
    }
}
