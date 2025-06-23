/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

// Model for product information

import java.math.BigDecimal;

public class ProductInfo {

    private int id; // Product ID
    private String name; // Product name
    private String code; // Product code
    private int cate_id; // Category ID
    private int unit_id; // Unit ID
    private BigDecimal price; // Product price
    private String status; // Product status
    private String description; // Product description

    public ProductInfo() {
    }

    public ProductInfo(int id, String name, String code, int cate_id, int unit_id, BigDecimal price, String status, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cate_id = cate_id;
        this.unit_id = unit_id;
        this.price = price;
        this.status = status;
        this.description = description;
    }

    // Getters và Setters
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

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
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
    
    // Additional fields for enhanced product management
    private int supplierId; // Supplier ID
    private java.sql.Date expirationDate; // Expiration date
    private String storageLocation; // Storage location
    private String additionalNotes; // Additional notes
    private int createdBy; // Created by (user ID)
    private java.sql.Date createdDate; // Creation date
    private int updatedBy; // Updated by (user ID)
    private java.sql.Date updatedDate; // Update date

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public java.sql.Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.sql.Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public java.sql.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.sql.Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public java.sql.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.sql.Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
