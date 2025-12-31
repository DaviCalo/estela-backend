package dev.smd.estela.backend.model;

import java.io.Serializable;

public class Media implements Serializable {

    private Long id;
    private String url;
    private MediaType mediaType;
    private Long gameId;

    public Media() {
    }

    public Media(Long id, String url, MediaType mediaType, Long gameId) {
        this.id = id;
        this.url = url;
        this.mediaType = mediaType;
        this.gameId = gameId;
    }

    public Media(String url, MediaType mediaType, Long gameId) {
        this.url = url;
        this.mediaType = mediaType;
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
