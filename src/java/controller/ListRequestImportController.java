<<<<<<< HEAD
//package controller;
//
//import dao.ListRequestImportDAO;
//import dao.SupplierDAO;
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import model.ApprovedRequestItem;
//import model.Supplier;
//
//@WebServlet("/request/list")
//public class ListRequestImportController extends HttpServlet {
//
//  @Override
//  protected void doGet(HttpServletRequest request, HttpServletResponse response)
//          throws ServletException, IOException {
//
//      // Tạo instance của DAO
//      ListRequestImportDAO dao = new ListRequestImportDAO();
//
//      // Lấy loại request (purchase hoặc history)
//      String type = request.getParameter("type");
//      if (type == null) {
//          type = "purchase"; // Mặc định là purchase
//      }
//
//      // Lấy tham số tìm kiếm dựa trên loại
//      String searchType = null;
//      String searchValue = null;
//
//      if ("history".equals(type)) {
//          // Tìm kiếm cho lịch sử
//          searchType = request.getParameter("historySearchType");
//          searchValue = request.getParameter("historySearchValue");
//      } else {
//          // Tìm kiếm cho yêu cầu đã duyệt
//          searchType = request.getParameter("searchType");
//          searchValue = request.getParameter("searchValue");
//      }
//
//      System.out.println("Request type: " + type);
//      System.out.println("Search parameters - Type: " + searchType + ", Value: " + searchValue);
//
//      // Lấy dữ liệu dựa trên loại request
//      List<ApprovedRequestItem> approvedItems = new ArrayList<>();
//      List<ApprovedRequestItem> completedItems = new ArrayList<>();
//
//      if ("history".equals(type)) {
//          // Chỉ lấy lịch sử khi cần
//          completedItems = dao.getCompletedRequestItems(searchType, searchValue);
//          // Lấy approved items không có tìm kiếm để hiển thị số lượng
//          approvedItems = dao.getApprovedRequestItems(null, null);
//      } else {
//          // Lấy approved items với tìm kiếm
//          approvedItems = dao.getApprovedRequestItems(searchType, searchValue);
//          // Lấy completed items không có tìm kiếm để hiển thị số lượng
//          completedItems = dao.getCompletedRequestItems(null, null);
//      }
//
//      // Debug logs
//      System.out.println("Approved items size: " + (approvedItems != null ? approvedItems.size() : "null"));
//      System.out.println("Completed items size: " + (completedItems != null ? completedItems.size() : "null"));
//
//      // Lấy thông tin supplier
//      SupplierDAO sd = new SupplierDAO();
//      List<Supplier> listSupplier = new ArrayList<>();
//      for (ApprovedRequestItem item : completedItems) {
//          if (item.getSupplier() != null) {
//              Supplier s = sd.getSupplierByName(item.getSupplier());
//              if (s != null) {
//                  listSupplier.add(s);
//              }
//          }
//      }
//
//      // Gửi dữ liệu sang JSP
//      request.setAttribute("items", approvedItems);
//      request.setAttribute("historyItems", completedItems);
//      request.setAttribute("searchType", searchType);
//      request.setAttribute("searchValue", searchValue);
//      request.setAttribute("supplier", listSupplier);
//      request.setAttribute("requestType", type);
//
//      request.getRequestDispatcher("/ListRequestImport.jsp").forward(request, response);
//  }
//
//  @Override
//  protected void doPost(HttpServletRequest request, HttpServletResponse response)
//          throws ServletException, IOException {
//
//      ListRequestImportDAO dao = new ListRequestImportDAO();
//      String requestId = request.getParameter("id");
//      String note = request.getParameter("note");
//      String approveDate = request.getParameter("approveDate");
//
//      // Validate parameters
//      if (requestId == null || requestId.trim().isEmpty()
//              || approveDate == null || approveDate.trim().isEmpty()) {
//
//          System.err.println("Invalid parameters: requestId=" + requestId + ", approveDate=" + approveDate);
//          response.sendRedirect(request.getContextPath() + "/request/list?error=invalid_data");
//          return;
//      }
//
//      try {
//          // Cập nhật trạng thái yêu cầu thành 'completed'
//          dao.updateRequestStatus(requestId, "completed", approveDate);
//
//          // Chuyển hướng về danh sách với thông báo thành công
//          response.sendRedirect(request.getContextPath() + "/request/list?message=approve_success");
//
//      } catch (SQLException e) {
//          e.printStackTrace();
//          System.err.println("SQLException occurred while updating request status for requestId: "
//                  + requestId + ", Error: " + e.getMessage());
//          response.sendRedirect(request.getContextPath() + "/request/list?error=approve_failed");
//      }
//  }
//}
=======
package controller;

import dao.ListRequestImportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import model.ApprovedRequestItem;

@WebServlet("/request/list")
public class ListRequestImportController extends HttpServlet {

    private static final int PAGE_SIZE = 10; // Số lượng item trên mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            ListRequestImportDAO dao = new ListRequestImportDAO();

            // Lấy tham số tab
            String tab = request.getParameter("tab");
            if (tab == null || tab.isEmpty()) {
                tab = "approved";
            }

            // Lấy tham số page cho từng tab
            int approvedPage = 1;
            int historyPage = 1;

            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    if ("approved".equals(tab)) {
                        approvedPage = Integer.parseInt(pageParam);
                        if (approvedPage < 1) {
                            approvedPage = 1;
                        }
                    } else if ("history".equals(tab)) {
                        historyPage = Integer.parseInt(pageParam);
                        if (historyPage < 1) {
                            historyPage = 1;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // Giữ nguyên giá trị mặc định
            }

            // Khởi tạo danh sách
            List<ApprovedRequestItem> approvedItems = new ArrayList<>();
            List<ApprovedRequestItem> historyItems = new ArrayList<>();
            int approvedPages = 0;
            int historyPages = 0;

            // Lấy tổng số để hiển thị thống kê (đếm từng đơn, không nhóm)
            int totalApproved = dao.countApprovedRequestItems(null, null);
            int totalHistory = dao.countCompletedRequestItems(null, null);

            if ("approved".equals(tab)) {
                // Load dữ liệu cho tab approved
                String searchType = request.getParameter("searchType");
                String searchValue = request.getParameter("searchValue");

                approvedItems = dao.getApprovedRequestItems(searchType, searchValue, approvedPage, PAGE_SIZE);
                int approvedTotal = dao.countApprovedRequestItems(searchType, searchValue);
                approvedPages = (int) Math.ceil((double) approvedTotal / PAGE_SIZE);

                // Set search parameters
                request.setAttribute("searchType", searchType);
                request.setAttribute("searchValue", searchValue);
                request.setAttribute("approvedTotal", approvedTotal);
                request.setAttribute("currentPage", approvedPage);

            } else if ("history".equals(tab)) {
                // Load dữ liệu cho tab history
                String historySearchType = request.getParameter("historySearchType");
                String historySearchValue = request.getParameter("historySearchValue");

                historyItems = dao.getCompletedRequestItems(historySearchType, historySearchValue, historyPage, PAGE_SIZE);
                int historyTotal = dao.countCompletedRequestItems(historySearchType, historySearchValue);
                historyPages = (int) Math.ceil((double) historyTotal / PAGE_SIZE);

                // Set search parameters
                request.setAttribute("historySearchType", historySearchType);
                request.setAttribute("historySearchValue", historySearchValue);
                request.setAttribute("historyTotal", historyTotal);
                request.setAttribute("currentPage", historyPage);
            }

            // Set attributes
            request.setAttribute("approvedItems", approvedItems);
            request.setAttribute("historyItems", historyItems);
            request.setAttribute("currentTab", tab);
            request.setAttribute("approvedPages", approvedPages);
            request.setAttribute("historyPages", historyPages);
            request.setAttribute("approvedPage", approvedPage);
            request.setAttribute("historyPage", historyPage);

            // Thống kê tổng (đếm từng item, không nhóm)
            request.setAttribute("approvedCount", totalApproved);
            request.setAttribute("historyCount", totalHistory);
            request.setAttribute("totalCount", totalApproved + totalHistory);

            request.setAttribute("pageSize", PAGE_SIZE);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("approvedItems", new ArrayList<>());
            request.setAttribute("historyItems", new ArrayList<>());
            request.setAttribute("error", "Có lỗi xảy ra khi tải dữ liệu");
        }

        request.getRequestDispatcher("/ListRequestImport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
>>>>>>> fa762af737b06f7bad2f50ac83eff6970e72fed2
