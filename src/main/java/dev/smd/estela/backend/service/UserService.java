package dev.smd.estela.backend.service;

import static dev.smd.estela.backend.config.Config.PATH_FILES_USERS;
import dev.smd.estela.backend.dao.UserDAO;
import dev.smd.estela.backend.dto.UpdateUserDTO;
import dev.smd.estela.backend.model.User;
import java.io.File;

public class UserService {

    private final UserDAO userDao = new UserDAO();

    public String getProfilePhotoById(Long userId, String applicationPath) {
        String fileName = userDao.getById(userId).getFileName();
        String filePath = PATH_FILES_USERS + File.separator + fileName;
        return filePath;
    }

    public Boolean saveNewProfilePhoto(String fileName, Long userId) {
        if (userExists(userId) == false) {
            return false;
        }
        return userDao.updateProfilePhoto(fileName, userId);
    }

    public Boolean userExists(Long userId) {
        User user = userDao.getById(userId);
        return (user != null);
    }

    public Boolean updateUser(UpdateUserDTO updateUserDTO) {
        User user = new User(updateUserDTO);
        return userDao.update(user);
    }

    public User getUserById(Long userId) {
        return userDao.getById(userId);
    }

    public boolean deleteUser(Long userId) {
        return userDao.deleteById(userId);
    }
}
