/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import java.util.List;
import java.util.Map;
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
            /* TODO output your page here. You may use following sample code. */
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
        List<Role> roles = dao.getAllRoles();
        List<Permission> permissions = dao.getAllPermissions();

        // Tạo map roleId -> List<permissionId>
        Map<Integer, List<Integer>> rolePermissions = new HashMap<>();
        for (Role role : roles) {
            List<Integer> perms = dao.getPermissionIdsByRoleId(role.getId());
            rolePermissions.put(role.getId(), perms);
        }

        request.setAttribute("roles", roles);
        request.setAttribute("permissions", permissions);
        request.setAttribute("rolePermissions", rolePermissions);

        request.getRequestDispatcher("permission.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roles = dao.getAllRoles();
        List<Permission> permissions = dao.getAllPermissions();

        // Xử lý từng role: xóa quyền cũ, thêm quyền mới được check
        for (Role role : roles) {
            List<Integer> checkedPerms = new ArrayList<>();
            for (Permission perm : permissions) {
                String param = "perm_" + perm.getId() + "_role_" + role.getId();
                if (request.getParameter(param) != null) {
                    checkedPerms.add(perm.getId());
                }
            }
            dao.deletePermissionsByRoleId(role.getId());
            if (!checkedPerms.isEmpty()) {
                dao.addPermissionsToRole(role.getId(), checkedPerms);
            }
        }

        response.sendRedirect("role-permission");
    }
}
