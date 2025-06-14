/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.MaterialUnitDAO;
import model.MaterialUnit;

public class CreateMaterialUnitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MaterialUnitDAO materialUnitDAO;
    
    @Override
    public void init() {
        materialUnitDAO = new MaterialUnitDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Hiển thị form tạo mới
        request.getRequestDispatcher("/material_unit/createMaterialUnit.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String symbol = request.getParameter("symbol");
            String description = request.getParameter("description");
            String type = request.getParameter("type");

            // Kiểm tra trùng tên hoặc ký hiệu
            if (materialUnitDAO.isDuplicateNameOrSymbol(name, symbol, null)) {
                request.setAttribute("errorMessage", "Tên hoặc ký hiệu đã tồn tại. Vui lòng nhập giá trị khác.");
                // Giữ lại dữ liệu đã nhập
                MaterialUnit unit = new MaterialUnit();
                unit.setName(name);
                unit.setSymbol(symbol);
                unit.setDescription(description);
                unit.setType(type);
                request.setAttribute("unit", unit);
                request.getRequestDispatcher("/material_unit/createMaterialUnit.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng MaterialUnit
            MaterialUnit unit = new MaterialUnit();
            unit.setName(name);
            unit.setSymbol(symbol);
            unit.setDescription(description);
            unit.setType(type);

            // Lưu vào database
            materialUnitDAO.addMaterialUnit(unit);

            // Thông báo thành công
            request.getSession().setAttribute("successMessage", "Thêm đơn vị thành công!");
            response.sendRedirect("materialUnit");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/material_unit/materialUnit");
        }
    }
}
