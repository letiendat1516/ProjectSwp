
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Request {

    private String id;
    private int user_id;
    private String role;
    private Date day_request;
    private String status;
    private String reason;
    private String supplier;
    private String address;
    private String phone;
    private String email;
    private String approve_by;
    private String warehouse;
    private ArrayList<RequestItem> items;

    public Request() {
    }

    public Request(String id, int user_id, String role, Date day_request, String status, String reason,
            String supplier, String address, String phone, String email,
            String approve_by, String warehouse, ArrayList<RequestItem> items) {
        this.id = id;
        this.user_id = user_id;
        this.role = role;
        this.day_request = day_request;
        this.status = status;
        this.reason = reason;
        this.supplier = supplier;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.approve_by = approve_by;
        this.warehouse = warehouse;
        this.items = items;
    }

    public String getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(String approve_by) {
        this.approve_by = approve_by;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
 
    public ArrayList<RequestItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RequestItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDay_request() {
        return day_request;
    }

    public void setDay_request(Date day_request) {
        this.day_request = day_request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

}
