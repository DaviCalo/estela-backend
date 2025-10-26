package dev.smd.estela.backend.service;

import dev.smd.estela.backend.dao.UserDAO;
import dev.smd.estela.backend.model.User;
import java.io.File;

public class UserService {

        private static final String UPLOAD_DIRECTORY = "uploads";
        UserDAO userDao = new UserDAO();

        public String getProfilePhotoById(Long userId, String applicationPath) {
                String fileName = userDao.getById(userId).getFileName();
                String uploadPath = applicationPath + UPLOAD_DIRECTORY;
                String filePath = uploadPath + File.separator + fileName;
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
}
