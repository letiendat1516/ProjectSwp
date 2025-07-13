package model;

import java.math.BigDecimal;

public class PurchaseOrderItems {

    private int id;
    private String purchaseId;
    private String productName;
    private String productCode;
    private String unit;
    private BigDecimal quantity; // Số lượng gốc từ đơn hàng
    private BigDecimal quantityOrdered; // Số lượng đặt hàng
    private BigDecimal quantityImported; // Số lượng đã nhập
    private BigDecimal quantityPending; // Số lượng còn lại
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;
    private String note;

    // Constructor
    public PurchaseOrderItems() {
        this.quantityImported = BigDecimal.ZERO;
        this.quantityPending = BigDecimal.ZERO;
    }

    // Getters và Setters
    public BigDecimal getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(BigDecimal quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
        // Tự động tính số lượng còn lại
        if (this.quantityImported != null) {
            this.quantityPending = quantityOrdered.subtract(this.quantityImported);
        } else {
            this.quantityPending = quantityOrdered;
        }
    }

    public BigDecimal getQuantityImported() {
        return quantityImported != null ? quantityImported : BigDecimal.ZERO;
    }

    public void setQuantityImported(BigDecimal quantityImported) {
        this.quantityImported = quantityImported != null ? quantityImported : BigDecimal.ZERO;
        // Tự động tính số lượng còn lại
        if (this.quantityOrdered != null) {
            this.quantityPending = this.quantityOrdered.subtract(this.quantityImported);
        }
    }

    public BigDecimal getQuantityPending() {
        return quantityPending != null ? quantityPending : BigDecimal.ZERO;
    }

    public void setQuantityPending(BigDecimal quantityPending) {
        this.quantityPending = quantityPending;
    }

    // Các getter/setter khác giữ nguyên...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
