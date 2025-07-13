package model;

import java.util.ArrayList;
import java.util.Date;

public class ExportRequest {

    private String id;
    private int userId;
    private String role;
    private Date dayRequest;
    private String status;
    private String approveBy;
    private Date createdAt;
    private ArrayList<ExportRequestItem> items;

    // Constructors
    public ExportRequest() {
        this.items = new ArrayList<>();
    }

    public ExportRequest(String id, int userId, String role, Date dayRequest, String status) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.dayRequest = dayRequest;
        this.status = status;
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDayRequest() {
        return dayRequest;
    }

    public void setDayRequest(Date dayRequest) {
        this.dayRequest = dayRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<ExportRequestItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExportRequestItem> items) {
        this.items = items;
    }
}
