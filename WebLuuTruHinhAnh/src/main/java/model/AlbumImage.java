package model;

public class AlbumImage {
    private int albumId;
    private int imageId;

    public AlbumImage() {}

    public AlbumImage(int albumId, int imageId) {
        this.albumId = albumId;
        this.imageId = imageId;
    }

    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }

    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }
}