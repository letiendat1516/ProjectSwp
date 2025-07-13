package model;

public class ExportRequestItem {

    private int id;
    private String exportRequestId;
    private String productName;
    private String productCode;
    private String unit;
    private double quantity;
    private String note;
    private int productId;
    private double exportedQty;

    // Constructors
    public ExportRequestItem() {
    }

    public ExportRequestItem(String exportRequestId, String productName,
            String productCode, String unit, double quantity,
            String note, int productId) {
        this.exportRequestId = exportRequestId;
        this.productName = productName;
        this.productCode = productCode;
        this.unit = unit;
        this.quantity = quantity;
        this.note = note;
        this.productId = productId;
        this.exportedQty = 0.0;
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

    public double getExportedQty() {
        return exportedQty;
    }

    public void setExportedQty(double exportedQty) {
        this.exportedQty = exportedQty;
    }
}
