package dev.smd.estela.backend.model;

import java.io.Serializable;

public class User implements Serializable {

        private long id;
        private String username;
        private String password;
        private String name;
        private String nickname;
        private boolean administrator;

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
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

        public boolean isAdministrator() {
                return administrator;
        }

        public void setAdministrator(boolean administrator) {
                this.administrator = administrator;
        }

        @Override
        public String toString() {
                return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", nickname=" + nickname + ", administrator=" + administrator + '}';
        }
}
