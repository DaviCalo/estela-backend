package dev.smd.estela.backend.dto;

public class UpdateUserDTO {
        private Long userId;
        private String email;
        private String password;
        private String name;
        private String nickname;

        public Long getUserId() {
                return userId;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
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

        public String getNickname() {
                return nickname;
        }

        public void setNickname(String nickname) {
                this.nickname = nickname;
        }

        @Override
        public String toString() {
                return "UpdateUserDTO{" + "userId=" + userId + ", email=" + email + ", password=" + password + ", name=" + name + ", nickname=" + nickname + '}';
        }
        
}
