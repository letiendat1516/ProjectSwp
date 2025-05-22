package model;

import java.io.Serializable;

public class Category implements Serializable {
    private Integer id;
    private String name;
    private Integer parentId;
    private String parentName; // Để hiển thị tên danh mục cha
    private boolean activeFlag;
    
    public Category() {
    }
    
    public Category(Integer id, String name, Integer parentId, boolean activeFlag) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.activeFlag = activeFlag;
    }
    
    // Getters và Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getParentId() {
        return parentId;
    }
    
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getParentName() {
        return parentName;
    }
    
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
    public boolean isActiveFlag() {
        return activeFlag;
    }
    
    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
    
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + ", parentId=" + parentId + ", activeFlag=" + activeFlag + '}';
    }
}
