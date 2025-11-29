package dev.smd.estela.backend.config;

public final class Config {

        public static final String DB_DRIVER = "org.postgresql.Driver";
        public static final String DB_URL = "jdbc:postgresql://localhost:5432/estela";
        public static final String DB_USER = "estela";
        public static final String DB_PASSWORD = "smd123";
        public static final String PATH_FILES_GAMES = "C:/Users/maria/Downloads/estela-uploads/games";
        public static final String PATH_FILES_USERS = "C:/Users/maria/Downloads/estela-uploads/users";

        private Config() { }
}
