package dev.smd.estela.backend.model;

import dev.smd.estela.backend.dto.UpdateUserDTO;
import java.io.Serializable;

public class User implements Serializable {
        private Long userId;
        private String name;
        private String email;
        private String password;
        private Boolean administrator;
        private String nickname;
        private String fileName;

        public User() {
        }
        
         public User(UpdateUserDTO updateUserDTO) {
                 this.userId = updateUserDTO.getUserId();
                 this.name = updateUserDTO.getName();
                 this.email = updateUserDTO.getEmail();
                 this.password = updateUserDTO.getPassword();
                 this.nickname = updateUserDTO.getNickname();
        }

        public User(Long userId, String name, String email, String password, Boolean administrator, String nickname, String fileName) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.password = password;
            this.administrator = administrator;
            this.nickname = nickname;
            this.fileName = fileName;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Boolean getAdministrator() {
            return administrator;
        }

        public void setAdministrator(Boolean administrator) {
            this.administrator = administrator;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFileName() {
                return fileName;
        }

        public void setFileName(String fileName) {
                this.fileName = fileName;
        }
}