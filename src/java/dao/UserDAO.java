package dao;

import DBContext.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Users;

public class UserDAO extends Context {

    public List<Users> getAllUsers() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE r.role_name != 'Admin'";

        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setDob(rs.getDate("dob"));
                user.setPhone(rs.getString("phone"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setActiveFlag(rs.getInt("active_flag"));
                user.setCreateDate(rs.getTimestamp("create_date"));
                user.setRoleName(rs.getString("role_name"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Users> getAllUsersExcludeAdmin() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE r.role_name != 'Admin'";

        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDob(rs.getDate("dob"));
                user.setActiveFlag(rs.getInt("active_flag"));
                user.setCreateDate(rs.getTimestamp("create_date"));
                user.setRoleName(rs.getString("role_name"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateUserRole(int userId, int roleId) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM user_role WHERE user_id = ?";
        try (Connection connection = Context.getJDBCConnection(); PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String updateSql = "UPDATE user_role SET role_id = ? WHERE user_id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, roleId);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, roleId);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    public void addUser(Users user, int roleId) throws SQLException {
        Connection connection = Context.getJDBCConnection();
        connection.setAutoCommit(false);
        try {
            String sqlUser = "INSERT INTO users(username, password, email, fullname, phone, dob, active_flag) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, user.getUsername());
                stmtUser.setString(2, user.getPassword());
                stmtUser.setString(3, user.getEmail());
                stmtUser.setString(4, user.getFullname());
                stmtUser.setString(5, user.getPhone());
                if (user.getDob() != null) {
                    stmtUser.setDate(6, new java.sql.Date(user.getDob().getTime()));
                } else {
                    stmtUser.setNull(6, java.sql.Types.DATE);
                }
                stmtUser.setInt(7, user.getActiveFlag());
                stmtUser.executeUpdate();

                try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);

                        String sqlUserRole = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
                        try (PreparedStatement stmtUserRole = connection.prepareStatement(sqlUserRole)) {
                            stmtUserRole.setInt(1, userId);
                            stmtUserRole.setInt(2, roleId);
                            stmtUserRole.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Failed to get user ID.");
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Users getUserById(int id) {
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE u.id = ?";
        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setDob(rs.getDate("dob"));
                    user.setPhone(rs.getString("phone"));
                    user.setFullname(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
                    user.setActiveFlag(rs.getInt("active_flag"));
                    user.setCreateDate(rs.getTimestamp("create_date"));
                    user.setRoleName(rs.getString("role_name"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(Users user, int roleId) throws SQLException {
        Connection connection = Context.getJDBCConnection();
        connection.setAutoCommit(false);
        try {
            String sqlUser = "UPDATE users SET username=?, fullname=?, email=?, phone=?, dob=?, active_flag=? WHERE id=?";
            try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser)) {
                stmtUser.setString(1, user.getUsername());
                stmtUser.setString(2, user.getFullname());
                stmtUser.setString(3, user.getEmail());
                stmtUser.setString(4, user.getPhone());
                if (user.getDob() != null) {
                    stmtUser.setDate(5, new java.sql.Date(user.getDob().getTime()));
                } else {
                    stmtUser.setNull(5, java.sql.Types.DATE);
                }
                stmtUser.setInt(6, user.getActiveFlag());
                stmtUser.setInt(7, user.getId());
                stmtUser.executeUpdate();
            }

            String sqlDelete = "DELETE FROM user_role WHERE user_id=?";
            try (PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete)) {
                stmtDelete.setInt(1, user.getId());
                stmtDelete.executeUpdate();
            }

            String sqlInsert = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, user.getId());
                stmtInsert.setInt(2, roleId);
                stmtInsert.executeUpdate();
            }

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public void updateUserProfile(Users user) throws SQLException {
    Connection connection = Context.getJDBCConnection();
    String sql = "UPDATE users SET fullname=?, email=?, phone=?, dob=? WHERE id=?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, user.getFullname());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPhone());
        stmt.setDate(4, user.getDob() == null ? null : new java.sql.Date(user.getDob().getTime()));
        stmt.setInt(5, user.getId());
        stmt.executeUpdate();
    }
}


    public List<Users> filterUsers(Integer roleId, String status, String keyword, boolean includeAdmin) {
        List<Users> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT u.*, r.role_name FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE 1=1 "
                + "AND r.role_name != 'Admin'");

        if (!includeAdmin) {
            sql.append(" AND r.role_name != 'Admin' ");
        }

        if (roleId != null) {
            sql.append(" AND r.id = ? ");
        }

        if (status != null && !status.isEmpty()) {
            if ("active".equalsIgnoreCase(status)) {
                sql.append(" AND u.active_flag = 1 ");
            } else if ("inactive".equalsIgnoreCase(status)) {
                sql.append(" AND u.active_flag = 0 ");
            }
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (u.username LIKE ? OR u.fullname LIKE ? OR u.email LIKE ?) ");
        }

        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (!includeAdmin) {
                // no param
            }

            if (roleId != null) {
                stmt.setInt(paramIndex++, roleId);
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim() + "%";
                stmt.setString(paramIndex++, kw);
                stmt.setString(paramIndex++, kw);
                stmt.setString(paramIndex++, kw);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setDob(rs.getDate("dob"));
                    user.setActiveFlag(rs.getInt("active_flag"));
                    user.setCreateDate(rs.getTimestamp("create_date"));

                    String roleName = rs.getString("role_name");
                    if (roleName == null) {
                        roleName = "No Role";
                    }
                    user.setRoleName(roleName);

                    list.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Users> getUsersByPage(int pageIndex, int pageSize) {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE r.role_name != 'Admin'"
                + "ORDER BY u.id "
                + "LIMIT ? OFFSET ?";

        int offset = (pageIndex - 1) * pageSize;

        try (
                Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setDob(rs.getDate("dob"));
                    user.setActiveFlag(rs.getInt("active_flag"));
                    user.setCreateDate(rs.getTimestamp("create_date"));
                    user.setRoleName(rs.getString("role_name"));
                    list.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

// Thêm hàm lấy tổng số user để tính tổng trang
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Users login(String username, String password) {
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
                + "WHERE u.username = ? AND u.password = ? AND u.active_flag = 1";

        try {
            Connection connection = Context.getJDBCConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setDob(rs.getDate("dob"));
                user.setPhone(rs.getString("phone"));
                user.setFullname(rs.getString("fullname"));
                user.setActiveFlag(rs.getInt("active_flag"));
                user.setCreateDate(rs.getTimestamp("create_date"));
                user.setRoleName(rs.getString("role_name")); // Lấy role
                return user;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFullName(int userId) {
        String fullName = null;
        String sql = "SELECT fullname FROM users WHERE id = ?";

        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fullName = rs.getString("fullname");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fullName;
    }
    
    public Date getDoB(int userId) {
        Date dob = null;
        String sql = "SELECT DoB FROM users WHERE id = ?";

        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dob = rs.getDate("DoB");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dob;
    }

}