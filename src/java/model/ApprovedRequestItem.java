package model;

public class ApprovedRequestItem {

    private String requestId;
    private String dayRequest;
    private String status;
    private String supplier;
    private String address;
    private String phone;
    private String email;
    private String productName;
    private String productCode;
    private String unit;
    private double quantity;
    private double price;
    private String note;
    private String reasonDetail;
    private String rejectReason; // Thêm trường mới

    // Các trường cho nhập kho từng phần
    private double quantityOrdered;
    private double quantityImported;
    private double quantityPending;
    private double importProgress;

    // Constructors
    public ApprovedRequestItem() {
        this.quantityImported = 0.0;
        this.quantityPending = 0.0;
        this.importProgress = 0.0;
    }

    // Method tiện ích để lấy lý do phù hợp theo trạng thái
    public String getDisplayReason() {
        if ("rejected".equalsIgnoreCase(this.status) && this.rejectReason != null && !this.rejectReason.trim().isEmpty()) {
            return this.rejectReason;
        } else if (this.reasonDetail != null && !this.reasonDetail.trim().isEmpty()) {
            return this.reasonDetail;
        }
        return null;
    }

    // Getters và Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public double getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
        calculateImportProgress();
    }

    public double getQuantityImported() {
        return quantityImported;
    }

    public void setQuantityImported(double quantityImported) {
        this.quantityImported = quantityImported;
        calculateImportProgress();
    }

    public double getQuantityPending() {
        return quantityPending;
    }

    public void setQuantityPending(double quantityPending) {
        this.quantityPending = quantityPending;
    }

    public double getImportProgress() {
        return importProgress;
    }

    public void setImportProgress(double importProgress) {
        this.importProgress = importProgress;
    }

    private void calculateImportProgress() {
        if (quantityOrdered > 0) {
            this.importProgress = (quantityImported / quantityOrdered) * 100;
            if (this.importProgress > 100) {
                this.importProgress = 100;
            }
        } else {
            this.importProgress = 0;
        }
    }
}
