package test;

import dao.PurchaseOrderDAO;

public class TestUpdateDoneStatusV2 {
    
    public static void main(String[] args) {
        System.out.println("🚀 TEST UPDATE DONE STATUS V2 (TRÁNH TRÙNG LẶP)");
        System.out.println("=".repeat(60));
        
        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        
        // 1. Kiểm tra tình trạng hiện tại
        System.out.println("BƯỚC 1: KIỂM TRA TÌNH TRẠNG HIỆN TẠI");
        dao.checkOrdersStatus();
        
        System.out.println("\n" + "=".repeat(60));
        
        // 2. Xem các đơn completed (chưa được cộng)
        System.out.println("BƯỚC 2: XEM CÁC ĐƠN COMPLETED (CHƯA ĐƯỢC CỘNG)");
        dao.showOrdersByStatus("completed");
        
        System.out.println("\n" + "=".repeat(60));
        
        // 3. Xem các đơn done (đã được cộng rồi)
        System.out.println("BƯỚC 3: XEM CÁC ĐƠN DONE (ĐÃ ĐƯỢC CỘNG RỒI)");
        dao.showOrdersByStatus("done");
        
        System.out.println("\n" + "=".repeat(60));
        
        // 4. Test với một đơn cụ thể
        String testOrderId = "PO001"; // Thay bằng ID thực tế
        System.out.println("BƯỚC 4: TEST VỚI ĐƠN " + testOrderId);
        
        // Xem trước
        dao.previewStockUpdateForOrder(testOrderId);
        
        // Thực hiện
        boolean result = dao.updateDoneStatus(testOrderId);
        
        if (result) {
            System.out.println("\n✅ THÀNH CÔNG!");
        } else {
            System.out.println("\n❌ THẤT BẠI!");
        }
        
        System.out.println("\n" + "=".repeat(60));
        
        // 5. Kiểm tra lại sau khi test
        System.out.println("BƯỚC 5: KIỂM TRA LẠI SAU KHI TEST");
        dao.checkOrdersStatus();
        
        System.out.println("\n" + "=".repeat(60));
        
        // 6. Test cộng hàng loạt
        System.out.println("BƯỚC 6: TEST CỘNG HÀNG LOẠT (TẤT CẢ ĐƠN COMPLETED)");
        boolean batchResult = dao.updateStockFromCompletedOrders();
        
        if (batchResult) {
            System.out.println("✅ Cộng hàng loạt thành công!");
        } else {
            System.out.println("❌ Cộng hàng loạt thất bại!");
        }
        
        // 7. Kiểm tra cuối cùng
        System.out.println("\nBƯỚC 7: KIỂM TRA CUỐI CÙNG");
        dao.checkOrdersStatus();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ KẾT THÚC TEST");
    }
}
