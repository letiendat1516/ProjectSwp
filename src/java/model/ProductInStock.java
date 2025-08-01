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
    private BigDecimal minStockThreshold;

    // Default constructor
    public ProductInStock() {
    }

    // Constructor with all fields
    public ProductInStock(int id, int productId, BigDecimal qty, BigDecimal minStockThreshold) {
        this.id = id;
        this.productId = productId;
        this.qty = qty;
        this.minStockThreshold = minStockThreshold;
    }

    // Constructor without ID (for new records)
    public ProductInStock(int productId, BigDecimal qty, BigDecimal minStockThreshold) {
        this.productId = productId;
        this.qty = qty;
        this.minStockThreshold = minStockThreshold;
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

    public BigDecimal getMinStockThreshold() {
        return minStockThreshold;
    }

    public void setMinStockThreshold(BigDecimal minStockThreshold) {
        this.minStockThreshold = minStockThreshold;
    }

    @Override
    public String toString() {
        return "ProductInStock{" +
                "id=" + id +
                ", productId=" + productId +
                ", qty=" + qty +
                ", minStockThreshold=" + minStockThreshold +
                '}';
    }
}
