package dev.smd.estela.backend.dto;

public class NewUserDTO {
        private String email;
        private String password;
        private String name;
        private String nickname;
        private boolean administrator;

        public NewUserDTO() {
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

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getNickName() {
                return nickname;
        }

        public void setNickname(String nickName) {
                this.nickname = nickName;
        }

        public boolean isAdministrator() {
                return administrator;
        }

        public void setAdministrator(boolean administrator) {
                this.administrator = administrator;
        }
        
}
