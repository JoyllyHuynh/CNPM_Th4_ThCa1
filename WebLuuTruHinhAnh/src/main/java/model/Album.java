package model;

import java.util.Date;
import java.util.List;

public class Album {
    private int id;
    private int userId;
    private String albumName;
    private Date createdAt;

    // UI support (không bắt buộc trong DB)
    private String coverUrl;
    private int itemCount;
    private List<Image> images;

    public Album() {}

    public Album(int id, int userId, String albumName, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.albumName = albumName;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public List<Image> getImages() { return images; }
    public void setImages(List<Image> images) { this.images = images; }
}