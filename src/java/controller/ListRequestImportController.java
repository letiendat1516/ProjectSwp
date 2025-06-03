package controller;

import dao.ListRequestImportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.ApprovedRequestItem;

public class ListRequestImportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạo instance của DAO
        ListRequestImportDAO dao = new ListRequestImportDAO();

        // Lấy tham số tìm kiếm từ request
        String search = request.getParameter("search");

        // Lấy danh sách RequestItem với tham số tìm kiếm
        List<ApprovedRequestItem> approvedItems = dao.getApprovedRequestItems(search);

        if (approvedItems == null || approvedItems.isEmpty()) {
            System.out.println("Không có yêu cầu nào đã được duyệt.");
        } else {
            System.out.println("Số lượng yêu cầu đã duyệt: " + approvedItems.size());
        }

        // Gửi danh sách và tham số tìm kiếm sang JSP
        request.setAttribute("items", approvedItems);
        request.setAttribute("search", search);
        request.getRequestDispatcher("ListRequestImport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiện tại để trống, bạn có thể thêm logic nếu cần
    }
}