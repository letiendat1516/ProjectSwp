package model;

public class Category {
    private int id;
    private String name;
    private Integer parentId;
    private boolean activeFlag;
    
    // Constructor mặc định
    public Category() {
    }
    
    // Constructor đầy đủ tham số
    public Category(int id, String name, Integer parentId, boolean activeFlag) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.activeFlag = activeFlag;
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
    
    public Integer getParentId() {
        return parentId;
    }
    
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    
    public boolean isActiveFlag() {
        return activeFlag;
    }
    
    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", activeFlag=" + activeFlag +
                '}';
    }
}
