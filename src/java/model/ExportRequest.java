package model;

import java.sql.Date;
import java.sql.Timestamp;

public class ExportRequest {
    private String id;
    private int userId;
    private Date dayRequest;
    private String status;
    private String approveBy;
    private String role;
    private String reason;
    private String rejectReason;
    private Timestamp createdAt;

    // Constructors
    public ExportRequest() {}

    public ExportRequest(String id, int userId, Date dayRequest, String status, 
                        String approveBy, String role, String reason) {
        this.id = id;
        this.userId = userId;
        this.dayRequest = dayRequest;
        this.status = status;
        this.approveBy = approveBy;
        this.role = role;
        this.reason = reason;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ExportRequest{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", dayRequest=" + dayRequest +
                ", status='" + status + '\'' +
                ", approveBy='" + approveBy + '\'' +
                ", role='" + role + '\'' +
                ", reason='" + reason + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
