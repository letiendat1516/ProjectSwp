package model;

public class RequestItem {

    private String id;
    private String requestId;
    private String productName;
    private String productCode;
    private String unit;
    private double quantity; // Sửa từ int thành double
    private double importedQty; // mới

    private String note;
    private String reasonDetail;

    public RequestItem() {
    }

    public RequestItem(String id, String requestId, String productName, String productCode, String unit,
            double quantity, double importedQty, String note, String reasonDetail) {
        this.id = id;
        this.requestId = requestId;
        this.productName = productName;
        this.productCode = productCode;
        this.unit = unit;
        this.quantity = quantity;
        this.importedQty = importedQty;
        this.note = note;
        this.reasonDetail = reasonDetail;
    }

    public double getImportedQty() {
        return importedQty;
    }

    public void setImportedQty(double importedQty) {
        this.importedQty = importedQty;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getReasonDetail() {
        return reasonDetail;
    }

    public void setReasonDetail(String reasonDetail) {
        this.reasonDetail = reasonDetail;
    }
}
