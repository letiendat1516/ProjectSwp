package test;

import dao.PurchaseOrderDAO;

public class UpdateStockMain {
    public static void main(String[] args) {
        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        System.out.println("🚀 Bắt đầu cập nhật stock từ các đơn hàng completed...");

        boolean result = dao.updateStockFromCompletedOrders();

        if (result) {
            System.out.println("🎉 Đã cập nhật stock thành công cho các đơn completed!");
        } else {
            System.out.println("❌ Có lỗi xảy ra khi cập nhật stock!");
        }
    }
}
