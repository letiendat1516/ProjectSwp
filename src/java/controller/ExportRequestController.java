package controller;

import dao.*;
import model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ExportRequestController extends HttpServlet {

    private ExportRequestDAO exportRequestDAO;
    private ExportRequestItemsDAO exportRequestItemsDAO;
    private ProductInfoDAO productInfoDAO;
    private UnitDAO unitDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        exportRequestDAO = new ExportRequestDAO();
        exportRequestItemsDAO = new ExportRequestItemsDAO();
        productInfoDAO = new ProductInfoDAO();
        unitDAO = new UnitDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== ExportRequestController doGet() ===");

        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("Session không hợp lệ, redirect về login");
            response.sendRedirect("login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");
        System.out.println("Current user ID: " + currentUser.getId());

        try {
            // Lấy thông tin user
            String fullname = userDAO.getFullName(currentUser.getId());
            Date dob = userDAO.getDoB(currentUser.getId());

            // Tính tuổi
            int age = calculateAge(dob);

            // Lấy ID preview cho form
            String nextExportID = exportRequestDAO.generateNextExportRequestId();

            // Lấy danh sách sản phẩm và đơn vị
            List<ProductInfo> productsList = productInfoDAO.getAllProducts();
            List<Unit> unitsList = unitDAO.getAllUnits();

            System.out.println("Next Export ID: " + nextExportID);
            System.out.println("Products count: " + (productsList != null ? productsList.size() : 0));
            System.out.println("Units count: " + (unitsList != null ? unitsList.size() : 0));

            // Set attributes
            request.setAttribute("nextExportID", nextExportID);
            request.setAttribute("productsList", productsList);
            request.setAttribute("unitsList", unitsList);
            request.setAttribute("currentUser", fullname);
            request.setAttribute("age", age);
            request.setAttribute("dob", dob);
            Users u = (Users) session.getAttribute("user");
            request.setAttribute("roleID", u.id);
            

            // Forward đến JSP
            System.out.println("Forward to ExportRequest.jsp");
            request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Lỗi trong doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải trang: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== ExportRequestController doPost() BẮT ĐẦU ===");

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("Session không hợp lệ, redirect về login");
            response.sendRedirect("login.jsp");
            return;
        }

        Users currentUser = (Users) session.getAttribute("user");
        System.out.println("Current user ID: " + currentUser.getId());

        try {
            // Lấy dữ liệu từ form
            String role = request.getParameter("role");
            System.out.println("Role nhận được: " + role);

            // Validate dữ liệu cơ bản
            if (role == null || role.trim().isEmpty()) {
                System.out.println("Role trống, trả về form với lỗi");
                request.setAttribute("error", "Vui lòng chọn vai trò");
                doGet(request, response);
                return;
            }

            // Lấy dữ liệu items từ form
            String[] productIds = request.getParameterValues("product_id");
            String[] productNames = request.getParameterValues("product_name");
            String[] productCodes = request.getParameterValues("product_code");
            String[] units = request.getParameterValues("unit");
            String[] quantities = request.getParameterValues("quantity");
            String[] notes = request.getParameterValues("note");

            // Debug log chi tiết
            System.out.println("=== DEBUG FORM DATA ===");
            System.out.println("Product IDs: " + java.util.Arrays.toString(productIds));
            System.out.println("Product Names: " + java.util.Arrays.toString(productNames));
            System.out.println("Product Codes: " + java.util.Arrays.toString(productCodes));
            System.out.println("Units: " + java.util.Arrays.toString(units));
            System.out.println("Quantities: " + java.util.Arrays.toString(quantities));
            System.out.println("Notes: " + java.util.Arrays.toString(notes));

            // Validate items
            if (productIds == null || productIds.length == 0) {
                System.out.println("Không có sản phẩm nào được chọn");
                request.setAttribute("error", "Vui lòng thêm ít nhất một sản phẩm");
                doGet(request, response);
                return;
            }

            // Kiểm tra có ít nhất 1 item hợp lệ
            boolean hasValidItem = false;
            int validItemCount = 0;
            for (int i = 0; i < productIds.length; i++) {
                System.out.println("Checking item " + i + ": productId=" + productIds[i] + ", quantity=" + quantities[i]);
                if (productIds[i] != null && !productIds[i].trim().isEmpty()
                        && quantities[i] != null && !quantities[i].trim().isEmpty()) {
                    try {
                        double qty = Double.parseDouble(quantities[i]);
                        if (qty > 0) {
                            hasValidItem = true;
                            validItemCount++;
                            System.out.println("Item " + i + " hợp lệ với quantity: " + qty);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi parse quantity tại item " + i + ": " + quantities[i]);
                    }
                }
            }

            System.out.println("Số item hợp lệ: " + validItemCount);

            if (!hasValidItem) {
                System.out.println("Không có item hợp lệ nào");
                request.setAttribute("error", "Vui lòng nhập ít nhất một sản phẩm với số lượng hợp lệ");
                doGet(request, response);
                return;
            }

            System.out.println("Bắt đầu thêm export request vào database...");

            // Tạo đối tượng ExportRequest
            ExportRequest exportRequest = new ExportRequest();
            exportRequest.setUserId(currentUser.getId());
            exportRequest.setRole(role);
            exportRequest.setDayRequest(new java.sql.Date(System.currentTimeMillis()));
            exportRequest.setStatus("pending");

            // Thêm đơn xuất kho
            String exportRequestId = exportRequestDAO.addExportRequest(exportRequest);
            System.out.println("Export Request ID được tạo: " + exportRequestId);

            if (exportRequestId != null && !exportRequestId.trim().isEmpty()) {
                System.out.println("Export request được tạo thành công với ID: " + exportRequestId);

                // Tạo danh sách items
                List<ExportRequestItem> items = new ArrayList<>();

                for (int i = 0; i < productIds.length; i++) {
                    if (productIds[i] != null && !productIds[i].trim().isEmpty()
                            && quantities[i] != null && !quantities[i].trim().isEmpty()) {

                        try {
                            double quantity = Double.parseDouble(quantities[i]);
                            if (quantity > 0) {
                                ExportRequestItem item = new ExportRequestItem();
                                item.setExportRequestId(exportRequestId);
                                item.setProductId(Integer.parseInt(productIds[i]));

                                // Sửa: Kiểm tra null trước khi set
                                item.setProductName(productNames != null && i < productNames.length && productNames[i] != null ? productNames[i] : "");
                                item.setProductCode(productCodes != null && i < productCodes.length && productCodes[i] != null ? productCodes[i] : "");
                                item.setUnit(units != null && i < units.length && units[i] != null ? units[i] : "");
                                item.setQuantity(quantity);
                                item.setNote(notes != null && i < notes.length && notes[i] != null ? notes[i] : "");

                                items.add(item);
                                System.out.println("Đã thêm item: " + item.getProductName() + " - Qty: " + quantity);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Lỗi parse số lượng tại index " + i + ": " + quantities[i]);
                        }
                    }
                }

                System.out.println("Tổng số items chuẩn bị thêm: " + items.size());

                // Thêm items vào database
                if (!items.isEmpty()) {
                    boolean itemsAdded = exportRequestItemsDAO.addExportRequestItems(items);
                    System.out.println("Kết quả thêm items: " + itemsAdded);

                    if (itemsAdded) {
                        System.out.println("THÀNH CÔNG! Chuẩn bị redirect về trang form mới...");

                        // Lưu thông báo thành công vào session
                        session.setAttribute("successMessage",
                                "Đã gửi yêu cầu xuất kho thành công! Mã đơn: " + exportRequestId
                                + " (Số lượng sản phẩm: " + items.size() + ")");

                        // Redirect về chính controller này để tạo form mới
                        response.sendRedirect(request.getContextPath() + "/exportRequest");
                        return;

                    } else {
                        System.out.println("LỖI: Không thể thêm items vào database");
                        request.setAttribute("error", "Không thể thêm chi tiết sản phẩm vào hệ thống");
                        doGet(request, response);
                        return;
                    }
                } else {
                    System.out.println("LỖI: Danh sách items trống");
                    request.setAttribute("error", "Không có sản phẩm hợp lệ để thêm");
                    doGet(request, response);
                    return;
                }
            } else {
                System.out.println("LỖI: Không thể tạo export request (ID null hoặc empty)");
                request.setAttribute("error", "Không thể tạo đơn xuất kho trong hệ thống");
                doGet(request, response);
                return;
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION trong doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi hệ thống xảy ra: " + e.getMessage());
            doGet(request, response);
        }

        System.out.println("=== ExportRequestController doPost() KẾT THÚC ===");
    }

    /**
     * Tính tuổi từ ngày sinh
     */
    private int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(birthDate);
        int yearOfBirth = cal.get(java.util.Calendar.YEAR);
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        return currentYear - yearOfBirth;
    }

    @Override
    public String getServletInfo() {
        return "Export Request Controller - Modified for Continuous Form";
    }
}