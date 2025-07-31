package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Model class for Export Request
 * Represents a warehouse export request with all necessary information
 */
public class ExportRequest {

    private String id;
    private int userId;
    private String role;
    private Date dayRequest;
    private String status; // pending, approved, rejected, completed
    private String reason;
    private String rejectReason;
    private String recipient; // Added recipient field
    private String department;
    private String recipientName;
    private String recipientPhone;
    private String recipientEmail;
    private String approveBy;
    private String warehouse;
    private ArrayList<ExportRequestItem> items;
    private String requesterName;
    private Timestamp createdAt;
    private Timestamp exportAt;
    private Date exportDate;
    private String exportedBy;
    private String exportNote;
    private Timestamp updatedAt;

    // Default constructor
    public ExportRequest() {
    }

    // Constructor with basic parameters
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

    // Full constructor with all parameters including recipient
    public ExportRequest(String id, int userId, String role, Date dayRequest, String status, 
                        String reason, String rejectReason, String recipient, String department, 
                        String recipientName, String recipientPhone, String recipientEmail, 
                        String approveBy, String warehouse, ArrayList<ExportRequestItem> items, 
                        String requesterName, Timestamp createdAt, Timestamp exportAt, 
                        Date exportDate, String exportedBy, String exportNote, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.dayRequest = dayRequest;
        this.status = status;
        this.reason = reason;
        this.rejectReason = rejectReason;
        this.recipient = recipient;
        this.department = department;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientEmail = recipientEmail;
        this.approveBy = approveBy;
        this.warehouse = warehouse;
        this.items = items;
        this.requesterName = requesterName;
        this.createdAt = createdAt;
        this.exportAt = exportAt;
        this.exportDate = exportDate;
        this.exportedBy = exportedBy;
        this.exportNote = exportNote;
        this.updatedAt = updatedAt;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public ArrayList<ExportRequestItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExportRequestItem> items) {
        this.items = items;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExportAt() {
        return exportAt;
    }

    public void setExportAt(Timestamp exportAt) {
        this.exportAt = exportAt;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getExportedBy() {
        return exportedBy;
    }

    public void setExportedBy(String exportedBy) {
        this.exportedBy = exportedBy;
    }

    public String getExportNote() {
        return exportNote;
    }

    public void setExportNote(String exportNote) {
        this.exportNote = exportNote;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public boolean isPending() {
        return "pending".equals(this.status);
    }

    public boolean isApproved() {
        return "approved".equals(this.status);
    }

    public boolean isRejected() {
        return "rejected".equals(this.status);
    }

    public boolean isCompleted() {
        return "completed".equals(this.status);
    }

    public boolean canBeProcessed() {
        return isApproved();
    }

    @Override
    public String toString() {
        return "ExportRequest{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                ", dayRequest=" + dayRequest +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", recipient='" + recipient + '\'' +
                ", department='" + department + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", approveBy='" + approveBy + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", exportAt=" + exportAt +
                ", exportDate=" + exportDate +
                ", exportedBy='" + exportedBy + '\'' +
                ", exportNote='" + exportNote + '\'' +
                ", items=" + items +
                ", requesterName='" + requesterName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
