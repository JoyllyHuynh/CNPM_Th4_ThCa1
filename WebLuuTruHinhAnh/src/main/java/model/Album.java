package model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Album {
    private int id;
    private int userId;
    private String albumName;
    private LocalDate createdAt;
    private String coverUrl;
    private int itemCount;
    private List<Image> images;

    public Album() {}

    public Album(int id, int userId, String albumName, LocalDate createdAt, String coverUrl, int itemCount, List<Image> images) {
        this.id = id;
        this.userId = userId;
        this.albumName = albumName;
        this.createdAt = createdAt;
        this.coverUrl = coverUrl;
        this.itemCount = itemCount;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}