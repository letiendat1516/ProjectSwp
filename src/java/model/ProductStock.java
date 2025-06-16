package model;

import java.math.BigDecimal;
import java.util.Date;

public class ProductStock {
    private int id;
    private String name;
    private String code;
    private int cateId;
    private int unitId;
    private BigDecimal price;
    private String status;
    private String description;
    private BigDecimal stockQuantity;
    private String stockStatus;
    private String categoryName;
    private String unitName;
    private String unitSymbol;
    private Date expirationDate;
    private boolean isLowStock;
    private boolean isNearExpiration;

    public ProductStock() {
    }    public ProductStock(int id, String name, String code, BigDecimal stockQuantity) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isLowStock() {
        return isLowStock;
    }

    public void setLowStock(boolean lowStock) {
        isLowStock = lowStock;
    }

    public boolean isNearExpiration() {
        return isNearExpiration;
    }

    public void setNearExpiration(boolean nearExpiration) {
        isNearExpiration = nearExpiration;
    }

    // Helper method to check if stock is low (less than 10 units)
    public void checkLowStock() {
        this.isLowStock = stockQuantity != null && stockQuantity.compareTo(new BigDecimal("10")) < 0;
    }

    // Helper method to check if product is near expiration (within 30 days)
    public void checkNearExpiration() {
        if (expirationDate != null) {
            Date now = new Date();
            long diffTime = expirationDate.getTime() - now.getTime();
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            this.isNearExpiration = diffDays <= 30 && diffDays >= 0;
        }
    }
}
