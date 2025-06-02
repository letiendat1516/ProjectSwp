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
                + "LEFT JOIN role r ON ur.role_id = r.id";

        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
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

    // Thêm user mới và gán role (giả sử roleId lấy từ tham số)
    public void addUser(Users user, int roleId) throws SQLException {
        Connection connection = Context.getJDBCConnection();
        connection.setAutoCommit(false);
        try {
            // Thêm vào bảng users
            String sqlUser = "INSERT INTO users(username, password, email, fullname, active_flag) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, user.getUsername());
                stmtUser.setString(2, user.getPassword());
                stmtUser.setString(3, user.getEmail());
                stmtUser.setString(4, user.getFullname());
                stmtUser.setInt(5, user.getActiveFlag());
                stmtUser.executeUpdate();

                // Lấy user_id vừa thêm
                try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);

                        // Thêm vào bảng user_role
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

    // Lấy user theo id, bao gồm roleName
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

// Cập nhật user và role
    public void updateUser(Users user, int roleId) throws SQLException {
        Connection connection = Context.getJDBCConnection();
        connection.setAutoCommit(false);
        try {
            // Cập nhật bảng users
            String sqlUser = "UPDATE users SET username=?, fullname=?, email=?, active_flag=? WHERE id=?";
            try (PreparedStatement stmtUser = connection.prepareStatement(sqlUser)) {
                stmtUser.setString(1, user.getUsername());
                stmtUser.setString(2, user.getFullname());
                stmtUser.setString(3, user.getEmail());
                stmtUser.setInt(4, user.getActiveFlag());
                stmtUser.setInt(5, user.getId());
                stmtUser.executeUpdate();
            }

            // Cập nhật bảng user_role (xoá cũ, thêm mới)
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

    public List<Users> filterUsers(Integer roleId, String status, String keyword) {
        List<Users> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT u.*, r.role_name FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (roleId != null) {
            sql.append(" AND r.id = ? ");
            params.add(roleId);
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
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        try (Connection connection = Context.getJDBCConnection(); PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullname(rs.getString("fullname"));
                    user.setEmail(rs.getString("email"));
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

    public List<Users> getUsersByPage(int pageIndex, int pageSize) {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM users u "
                + "LEFT JOIN user_role ur ON u.id = ur.user_id "
                + "LEFT JOIN role r ON ur.role_id = r.id "
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
                user.setFullname(rs.getString("fullname"));
                user.setActiveFlag(rs.getInt("active_flag"));
                user.setCreateDate(rs.getTimestamp("create_date"));
                user.setRoleName(rs.getString("role_name"));
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
