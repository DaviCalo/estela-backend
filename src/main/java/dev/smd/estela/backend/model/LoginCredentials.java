/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.smd.estela.backend.model;

/**
 *
 * @author maria
 */
public class LoginCredentials {
        // Os nomes das variáveis DEVEM ser iguais aos campos JSON (case-sensitive)

        private String username;
        private String password;

        // Construtor vazio (necessário para o Gson)
        public LoginCredentials() {
        }

        // Getters e Setters (necessários para acesso, embora o Gson possa dispensá-los na leitura)
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
}
