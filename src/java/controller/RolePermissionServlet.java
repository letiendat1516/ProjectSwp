package controller;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import model.Permission;
import model.Role;
import model.Users;

/**
 *
 * @author phucn
 */
public class RolePermissionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RolePermissionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RolePermissionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Role> allRoles = dao.getAllRoles();
        List<Permission> permissions = dao.getAllPermissions();

        // Gộp tìm kiếm theo keyword (tên quyền và mã quyền)
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.trim().toLowerCase();
            permissions = permissions.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) || 
                                 p.getCode().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }

        // Lọc theo vai trò
        String roleIdStr = request.getParameter("role");
        Integer filterRoleId = null;
        List<Role> filteredRoles = allRoles;
        
        if (roleIdStr != null && !roleIdStr.isEmpty()) {
            try {
                filterRoleId = Integer.parseInt(roleIdStr);
                final Integer selectedRoleId = filterRoleId;
                filteredRoles = allRoles.stream()
                        .filter(r -> r.getId() == selectedRoleId)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Ignore invalid role ID
            }
        }

        // Map roleId -> List<permissionId>
        Map<Integer, List<Integer>> rolePermissions = new HashMap<>();
        for (Role role : allRoles) {
            List<Integer> perms = dao.getPermissionIdsByRoleId(role.getId());
            rolePermissions.put(role.getId(), perms);
        }

        // Set attributes
        request.setAttribute("allRoles", allRoles);
        request.setAttribute("roles", filteredRoles);
        request.setAttribute("permissions", permissions);
        request.setAttribute("rolePermissions", rolePermissions);
        request.setAttribute("filterKeyword", keyword);
        request.setAttribute("filterRole", roleIdStr);

        request.getRequestDispatcher("permission.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        String role = request.getParameter("role");

        try {
            if ("reset".equals(action)) {
                // Reset toàn bộ phân quyền
                handleResetAll();
                redirectWithMessage(response, keyword, role, "reset=1");
            } else {
                // Lưu phân quyền (action = "save" hoặc null)
                handleSavePermissions(request, keyword, role);
                redirectWithMessage(response, keyword, role, "success=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithMessage(response, keyword, role, "error=1");
        }
    }

    private void handleResetAll() {
    boolean success = dao.deleteAllRolePermissions();
    System.out.println("Reset all permissions result: " + success);
}

    private void handleSavePermissions(HttpServletRequest request, String keyword, String role) {
        List<Role> allRoles = dao.getAllRoles();
        List<Permission> allPermissions = dao.getAllPermissions();

        // Áp dụng filter để lấy permissions hiện tại
        List<Permission> filteredPermissions = allPermissions;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.trim().toLowerCase();
            filteredPermissions = filteredPermissions.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) || 
                                 p.getCode().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }

        // Xử lý cho từng role
        for (Role roleObj : allRoles) {
            // Lấy danh sách quyền được check
            List<Integer> checkedPerms = new ArrayList<>();
            for (Permission perm : filteredPermissions) {
                String param = "perm_" + perm.getId() + "_role_" + roleObj.getId();
                if (request.getParameter(param) != null) {
                    checkedPerms.add(perm.getId());
                }
            }
            
            // Lấy danh sách ID của permissions hiện tại
            List<Integer> currentPermIds = filteredPermissions.stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());
            
            // Xóa các quyền hiện tại cho vai trò đó
            if (!currentPermIds.isEmpty()) {
                dao.deletePermissionsOfRoleForPermissions(roleObj.getId(), currentPermIds);
            }
            
            // Thêm các quyền được chọn
            if (!checkedPerms.isEmpty()) {
                dao.addPermissionsToRole(roleObj.getId(), checkedPerms);
            }
        }
    }

    private void redirectWithMessage(HttpServletResponse response, String keyword, String role, 
                                     String message) throws IOException {
        StringBuilder sb = new StringBuilder("role-permission?" + message);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("&keyword=").append(java.net.URLEncoder.encode(keyword, "UTF-8"));
        }
        if (role != null && !role.isEmpty()) {
            sb.append("&role=").append(java.net.URLEncoder.encode(role, "UTF-8"));
        }

        response.sendRedirect(sb.toString());
    }
}