package model;

import java.math.BigDecimal;

/**
 * Model class for Product In Stock
 * Represents stock information for products stored in product_in_stock table
 */
public class ProductInStock {
    private int id;
    private int productId;
    private BigDecimal qty;
    private String status;

    // Default constructor
    public ProductInStock() {
    }

    // Constructor with all fields
    public ProductInStock(int id, int productId, BigDecimal qty, String status) {
        this.id = id;
        this.productId = productId;
        this.qty = qty;
        this.status = status;
    }

    // Constructor without ID (for new records)
    public ProductInStock(int productId, BigDecimal qty, String status) {
        this.productId = productId;
        this.qty = qty;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setQty(double qty) {
        this.qty = BigDecimal.valueOf(qty);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductInStock{" +
                "id=" + id +
                ", productId=" + productId +
                ", qty=" + qty +
                ", status='" + status + '\'' +
                '}';
    }
}
