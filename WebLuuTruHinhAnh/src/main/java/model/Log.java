package model;

import java.util.Date;

public class Log {
    private int id;
    private int userId;
    private String action;
    private Date createdAt;

    public Log() {}

    public Log(int id, int userId, String action, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}