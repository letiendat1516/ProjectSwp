package model;

import java.util.Date;

public class ExportRequestItem {
    private int id;
    private String exportRequestId;
    private String productName;
    private String productCode;
    private String unit;
    private double quantity;
    private String note;
    private int productId;
    private int unitId;
    
    // Các trường khác
    private String dayRequest;
    private String status;
    private String reasonDetail;
    private String rejectReason;
    private double exportedQty; // Số lượng đã xuất (cho lịch sử)
    private double quantityExported; // Alias cho exportedQty
    private String recipient; // Thêm field recipient

    // Constructors
    public ExportRequestItem() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExportRequestId() {
        return exportRequestId;
    }

    public void setExportRequestId(String exportRequestId) {
        this.exportRequestId = exportRequestId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getDayRequest() {
        return dayRequest;
    }

    public void setDayRequest(String dayRequest) {
        this.dayRequest = dayRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public double getExportedQty() {
        return exportedQty;
    }

    public void setExportedQty(double exportedQty) {
        this.exportedQty = exportedQty;
    }

    public double getQuantityExported() {
        return quantityExported;
    }

    public void setQuantityExported(double quantityExported) {
        this.quantityExported = quantityExported;
    }

    // Getter và Setter cho recipient
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    // Utility methods
    public boolean isApproved() {
        return "approved".equals(this.status);
    }

    public boolean isCompleted() {
        return "completed".equals(this.status);
    }

    public boolean isRejected() {
        return "rejected".equals(this.status);
    }

    @Override
    public String toString() {
        return "ExportRequestItem{" +
                "id=" + id +
                ", exportRequestId='" + exportRequestId + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", status='" + status + '\'' +
                ", quantity=" + quantity +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
