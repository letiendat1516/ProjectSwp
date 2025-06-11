package model;

import java.sql.Timestamp;

public class CategoryProduct {
  private int id;
  private String name;
  private Integer parentId;
  private boolean activeFlag;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  
  // ✅ THÊM FIELD MỚI cho tên danh mục cha
  private String parentName;

  // Constructors
  public CategoryProduct() {}

  public CategoryProduct(int id, String name, Integer parentId, boolean activeFlag) {
      this.id = id;
      this.name = name;
      this.parentId = parentId;
      this.activeFlag = activeFlag;
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

  public Timestamp getCreatedAt() {
      return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
      this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
      return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
      this.updatedAt = updatedAt;
  }

  // ✅ GETTER/SETTER MỚI cho parentName
  public String getParentName() {
      return parentName;
  }

  public void setParentName(String parentName) {
      this.parentName = parentName;
  }

  @Override
  public String toString() {
      return "CategoryProduct{" +
              "id=" + id +
              ", name='" + name + '\'' +
              ", parentId=" + parentId +
              ", parentName='" + parentName + '\'' +
              ", activeFlag=" + activeFlag +
              ", createdAt=" + createdAt +
              ", updatedAt=" + updatedAt +
              '}';
  }
}