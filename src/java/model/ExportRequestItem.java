package model;

import java.util.Date;

public class ExportRequestItem {

    private int id;
    private String exportRequestId;
    private String productName;
    private String productCode;
    private String unit;
    private int unitId;
    private double quantity;
    private double quantityRequested;
    private double quantityExported;
    private double quantityPending;
    private double exportQuantity;
    private String note;
    private int productId;

    private String dayRequest;
    private String status;
    private double exportedQty;
    private String reasonDetail;
    private String rejectReason;

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
    }

    public double getQuantityExported() {
        return quantityExported;
    }

    public void setQuantityExported(double quantityExported) {
        this.quantityExported = quantityExported;
    }

    public double getQuantityPending() {
        return quantityPending;
    }

    public void setQuantityPending(double quantityPending) {
        this.quantityPending = quantityPending;
    }

    // *** THÊM MỚI ***
    public double getExportQuantity() {
        return exportQuantity;
    }

    public void setExportQuantity(double exportQuantity) {
        this.exportQuantity = exportQuantity;
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

    // Compatibility getters/setters
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

    public double getExportedQty() {
        return exportedQty;
    }

    public void setExportedQty(double exportedQty) {
        this.exportedQty = exportedQty;
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

    @Override
    public String toString() {
        return "ExportRequestItem{"
                + "id=" + id
                + ", exportRequestId='" + exportRequestId + '\''
                + ", productName='" + productName + '\''
                + ", productCode='" + productCode + '\''
                + ", quantityRequested=" + quantityRequested
                + ", quantityExported=" + quantityExported
                + ", quantityPending=" + quantityPending
                + ", exportQuantity=" + exportQuantity
                + '}';
    }
}
