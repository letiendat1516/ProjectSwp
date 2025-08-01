/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Dell
 */
public class ExportRequest {

  private String id;
  private int userId;
  private String role;
  private Date dayRequest;
  private String status;
  private String reason;
  private String rejectReason;
  private String department;
  private String recipientName;
  private String recipientPhone;
  private String recipientEmail;
  private String approveBy;
  private String warehouse;
  private ArrayList<ExportRequestItem> items;
  private String requesterName;
  private Timestamp createdAt;
  private String rejectReason2;

  // Default constructor
  public ExportRequest() {
  }

    public ExportRequest(String id, int userId, String role, Date dayRequest, String status, String reason, String rejectReason, String department, String recipientName, String recipientPhone, String recipientEmail, String approveBy, String warehouse, ArrayList<ExportRequestItem> items, String requesterName, Timestamp createdAt, String rejectReason2) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.dayRequest = dayRequest;
        this.status = status;
        this.reason = reason;
        this.rejectReason = rejectReason;
        this.department = department;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientEmail = recipientEmail;
        this.approveBy = approveBy;
        this.warehouse = warehouse;
        this.items = items;
        this.requesterName = requesterName;
        this.createdAt = createdAt;
        this.rejectReason2 = rejectReason2;
    }

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

    public String getRejectReason2() {
        return rejectReason2;
    }

    public void setRejectReason2(String rejectReason2) {
        this.rejectReason2 = rejectReason2;
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
              ", department='" + department + '\'' +
              ", recipientName='" + recipientName + '\'' +
              ", recipientPhone='" + recipientPhone + '\'' +
              ", recipientEmail='" + recipientEmail + '\'' +
              ", approveBy='" + approveBy + '\'' +
              ", warehouse='" + warehouse + '\'' +
              ", items=" + items +
              ", requesterName='" + requesterName + '\'' +
              ", createdAt=" + createdAt +
              '}';
  }
}