package test;

import dao.PurchaseOrderDAO;
import model.PurchaseOrderInfo;

import java.util.List;

public class TestPurchaseOrderDAO {
    public static void main(String[] args) {
        PurchaseOrderDAO dao = new PurchaseOrderDAO();

        // Ví dụ test: lấy tất cả đơn mua hàng
        List<PurchaseOrderInfo> orders = dao.getAllPurchaseOrders(
                1,             // page
                null,          // startDate
                null,          // endDate
                null,          // status
                null           // searchId
        );

        System.out.println("Số lượng đơn lấy được: " + orders.size());
        for (PurchaseOrderInfo po : orders) {
            System.out.println("ID: " + po.getId() + ", Người mua: " + po.getFullname());
        }
    }
}
