/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phucn
 */
public class AuthorizationFilter implements Filter {

    private static final Map<String, String> urlPermissionMap = new HashMap<>();

    static {
        // USERS
        urlPermissionMap.put("/usermanager", "USER_VIEW");
        urlPermissionMap.put("/adduser", "USER_ADD");
        urlPermissionMap.put("/edituser", "USER_EDIT");
        urlPermissionMap.put("/role-permission", "ROLE_ASSIGN");
        // CATEGORY
        urlPermissionMap.put("/categories", "CATEGORY_VIEW");
        urlPermissionMap.put("/addcategory", "CATEGORY_ADD");
        urlPermissionMap.put("/editcategory", "CATEGORY_EDIT");
        urlPermissionMap.put("/deletecategory", "CATEGORY_DELETE");
        // SUPPLIER
        urlPermissionMap.put("/suppliers", "SUPPLIER_VIEW");
        urlPermissionMap.put("/addsupplier", "SUPPLIER_ADD");
        urlPermissionMap.put("/editsupplier", "SUPPLIER_EDIT");
        urlPermissionMap.put("/evaluateSupplier", "SUPPLIER_EVALUATE");
        // PRODUCT
        urlPermissionMap.put("/products", "PRODUCT_VIEW");
        urlPermissionMap.put("/addproduct", "PRODUCT_ADD");
        urlPermissionMap.put("/editproduct", "PRODUCT_EDIT");
        // REQUESTS
        urlPermissionMap.put("/createPurchaseRequest", "REQUEST_PURCHASE_CREATE");
        urlPermissionMap.put("/approvePurchaseRequest", "REQUEST_PURCHASE_APPROVE");
        // QUOTE
        urlPermissionMap.put("/createQuote", "QUOTE_CREATE");
        urlPermissionMap.put("/approveQuote", "QUOTE_APPROVE");
        // IMPORT/EXPORT
        urlPermissionMap.put("/confirmImport", "IMPORT_CONFIRM");
        urlPermissionMap.put("/createExport", "EXPORT_CREATE");
        // UNIT
        urlPermissionMap.put("/units", "UNIT_VIEW");
        urlPermissionMap.put("/addunit", "UNIT_ADD");
        urlPermissionMap.put("/editunit", "UNIT_EDIT");
        urlPermissionMap.put("/deleteunit", "UNIT_DELETE");
        // ... bạn có thể bổ sung các chức năng khác theo docx/database
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        // Lấy danh sách quyền từ session (set sau khi login thành công)
        List<String> userPermissions = null;
        if (session != null) {
            Object permObj = session.getAttribute("userPermissions");
            if (permObj instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> checkedList = (List<String>) permObj;
                userPermissions = checkedList;
            }
        }

        // Lấy servletPath, vd: /adduser, /editproduct, ...
        String path = request.getServletPath();

        // Kiểm tra path này có yêu cầu phân quyền không
        String permissionRequired = urlPermissionMap.get(path);

        System.out.println("====== AUTH FILTER DEBUG ======");
        System.out.println("Filter path: " + path);
        System.out.println("permissionRequired: " + permissionRequired);
        System.out.println("userPermissions: " + userPermissions);

        if (permissionRequired != null) {
            // Nếu không login hoặc không đủ quyền
            if (userPermissions == null || !userPermissions.contains(permissionRequired)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Bạn không đủ quyền truy cập chức năng này!");
                return;
            }
        }

        // Nếu pass, cho đi tiếp (gọi filter hoặc servlet tiếp theo)
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
