package model;

import java.sql.Date;
import java.sql.Timestamp;

public class ExportRequestItem {

    private int id;
    private String exportRequestId;
    private String productName;
    private String productCode;
    private String unit;
    private int unitId;
    private double quantity; // Số lượng yêu cầu ban đầu
    private double quantityRequested; // Alias cho quantity
    private double quantityExported; // Số lượng đã xuất tích lũy
    private double quantityPending; // Số lượng còn lại (calculated)
    private String note;
    private int productId;
    private String dayRequest;
    private String status;
    private String rejectReason;
    private String reasonDetail;
    private double exportedQty; // Alias cho quantityExported
    private String lastExportDate;
    private String lastExportedBy;

    // Constructors
    public ExportRequestItem() {
    }

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

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(double quantityRequested) {
        this.quantityRequested = quantityRequested;
        this.quantity = quantityRequested; // Keep sync
    }

    public double getQuantityExported() {
        return quantityExported;
    }

    public void setQuantityExported(double quantityExported) {
        this.quantityExported = quantityExported;
        this.exportedQty = quantityExported; // Keep sync
    }

    public double getQuantityPending() {
        return quantityPending;
    }

    public void setQuantityPending(double quantityPending) {
        this.quantityPending = quantityPending;
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

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }

    public double getExportedQty() {
        return exportedQty;
    }

    public void setExportedQty(double exportedQty) {
        this.exportedQty = exportedQty;
        this.quantityExported = exportedQty; // Keep sync
    }

    public String getLastExportDate() {
        return lastExportDate;
    }

    public void setLastExportDate(String lastExportDate) {
        this.lastExportDate = lastExportDate;
    }

    public String getLastExportedBy() {
        return lastExportedBy;
    }

    public void setLastExportedBy(String lastExportedBy) {
        this.lastExportedBy = lastExportedBy;
    }

    // Utility methods
    public boolean isFullyExported() {
        return quantityExported >= quantity;
    }

    public boolean hasRemainingQuantity() {
        return quantityExported < quantity;
    }

    public double getRemainingQuantity() {
        return Math.max(0, quantity - quantityExported);
    }

    @Override
    public String toString() {
        return "ExportRequestItem{"
                + "id=" + id
                + ", exportRequestId='" + exportRequestId + '\''
                + ", productName='" + productName + '\''
                + ", productCode='" + productCode + '\''
                + ", quantity=" + quantity
                + ", quantityExported=" + quantityExported
                + ", quantityPending=" + quantityPending
                + '}';
    }
}