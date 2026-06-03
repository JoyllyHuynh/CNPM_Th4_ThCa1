package model;

import java.time.LocalDate;

public class Imagee {
    private int id;
    private int userId;
    private String fileName;
    private String filePath;
    private String description;
    private long fileSize;
    private LocalDate uploadDate;
    private boolean isDeleted;

    public Imagee() {}


    public Imagee(int id, int userId, String fileName, String filePath, String description, long fileSize, LocalDate uploadDate, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.description = description;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.isDeleted = isDeleted;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", userId=" + userId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                ", fileSize=" + fileSize +
                ", uploadDate=" + uploadDate +
                ", isDeleted=" + isDeleted +
                '}';
    }
}