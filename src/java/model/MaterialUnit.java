/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author tunga
 */

import java.sql.Timestamp;

public class MaterialUnit {
    private int id;
    private String name;
    private String symbol;
    private String description;
    private String status;
    private Timestamp createdAt;
    
    public MaterialUnit() {
    }
    
    public MaterialUnit(int id, String name, String symbol, String description, String status, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
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
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFormattedCreatedDate() {
        // Format date as MM/dd/yyyy
        if (createdAt != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            return sdf.format(createdAt);
        }
        return "";
    }
}

