/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class ParentCategoryDTO extends Category {
    private int childCount;
    
    public ParentCategoryDTO() {
        super();
    }
    
    public int getChildCount() {
        return childCount;
    }
    
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}
