/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.MaterialUnitDAO;
import model.MaterialUnit;

@WebServlet("/createMaterialUnit")
public class CreateMaterialUnitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MaterialUnitDAO materialUnitDAO;
    
    public void init() {
        materialUnitDAO = new MaterialUnitDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Hiển thị form tạo mới
        request.getRequestDispatcher("/createMaterialUnit.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String symbol = request.getParameter("symbol");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        
        // Tạo đối tượng MaterialUnit
        MaterialUnit unit = new MaterialUnit();
        unit.setName(name);
        unit.setSymbol(symbol);
        unit.setDescription(description);
        unit.setStatus(status);
        
        // Lưu vào database
        materialUnitDAO.addMaterialUnit(unit);
        
        // Chuyển hướng về trang danh sách
        response.sendRedirect("materialUnit");
    }
}
