/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Date;
/**
 *
 * @author Fpt06
 */
public class StatisticExport {
    
    private String id;
    private Date export_at;
    private String product_name;
    private double quantity;

    public StatisticExport() {
    }

    public StatisticExport(String id, Date export_at, String product_name, double quantity) {
        this.id = id;
        this.export_at = export_at;
        this.product_name = product_name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExport_at() {
        return export_at;
    }

    public void setExport_at(Date export_at) {
        this.export_at = export_at;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    
    
}
