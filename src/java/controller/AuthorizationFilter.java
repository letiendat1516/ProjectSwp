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

        // CATEGORY
        urlPermissionMap.put("/categoriesforward.jsp", "CATEGORYFORWARD");
        urlPermissionMap.put("/category_product/list.jsp", "CATEGORY_LIST");
        urlPermissionMap.put("/category/list", "CATEGORY_LIST");
        urlPermissionMap.put("/category_product/create.jsp", "CATEGORY_ADD");
        urlPermissionMap.put("/category/create", "CATEGORY_ADD");
        urlPermissionMap.put("/category_product/edit.jsp", "CATEGORY_EDIT");
        urlPermissionMap.put("/category/edit", "CATEGORY_EDIT");

        // SUPPLIER
        urlPermissionMap.put("/LishSupplier.jsp", "SUPPLIER_VIEW");
        urlPermissionMap.put("/LishSupplier", "SUPPLIER_VIEW");
        urlPermissionMap.put("/AddNewSupplier.jsp", "SUPPLIER_ADD");
        urlPermissionMap.put("/AddNewSupplier", "SUPPLIER_ADD");
        urlPermissionMap.put("/UpdateSupplier.jsp", "SUPPLIER_EDIT");
        urlPermissionMap.put("/UpdateSupplier", "SUPPLIER_EDIT");
        urlPermissionMap.put("/DeleteSupplier", "SUPPLIER_DELETE");
        
        urlPermissionMap.put("/SupplierEvaluation.jsp", "SUPPLIER_EVALUATE");
        urlPermissionMap.put("/TableSupplierEvaluation", "SUPPLIER_EVALUATE");
        urlPermissionMap.put("/ViewSupplierEvaluation.jsp", "SUPPLIER_EVALUATE_VIEW");
        urlPermissionMap.put("/ViewSupplierEvaluation", "SUPPLIER_EVALUATE_VIEW");
        urlPermissionMap.put("/StatisticSupplierEvaluation.jsp", "SUPPLIER_EVALUATE_STATISTIC");
        urlPermissionMap.put("/StatisticSupplierEvaluation", "SUPPLIER_EVALUATE_STATISTIC");
        
        // DEPARTMENT
        
        
        // MATERIAL_UNIT
        urlPermissionMap.put("/material_unit/materialUnit.jsp", "UNIT_VIEW");
        urlPermissionMap.put("/material_unit/materialUnit", "UNIT_VIEW");
        urlPermissionMap.put("/material_unit/createMaterialUnit.jsp", "UNIT_ADD");
        urlPermissionMap.put("/material_unit/createMaterialUnit", "UNIT_ADD");
        urlPermissionMap.put("/material_unit/editMaterialUnit.jsp", "UNIT_EDIT");
        urlPermissionMap.put("/material_unit/editMaterialUnit", "UNIT_EDIT");
        urlPermissionMap.put("/material_unit/deleteMaterialUnit", "UNIT_DELETE");
        
        
        // PRODUCT
        urlPermissionMap.put("/product-list.jsp", "PRODUCT_VIEW");
        urlPermissionMap.put("/product-list", "PRODUCT_VIEW");
        urlPermissionMap.put("/add-product.jsp", "PRODUCT_ADD");
        urlPermissionMap.put("/add-product", "PRODUCT_ADD");
        urlPermissionMap.put("/update-product.jsp", "PRODUCT_EDIT");
        urlPermissionMap.put("/update-product", "PRODUCT_EDIT");
        urlPermissionMap.put("/deleted-products.jsp", "PRODUCT_DELETE");
        urlPermissionMap.put("/deleted-products", "PRODUCT_DELETE");
        urlPermissionMap.put("/recover-product", "PRODUCT_RECOVER");
        
        
        //REQUEST
        urlPermissionMap.put("/RequestForward.jsp", "REQUEST_PAGE");     
        
        //IMPORT
        urlPermissionMap.put("/ListRequestImport.jsp", "REQUEST_PURCHASE_VIEW");        
        urlPermissionMap.put("/import", "REQUEST_PURCHASE_VIEW");
        urlPermissionMap.put("/ItemsSupplyRequestForm.jsp", "REQUEST_PURCHASE_CREATE");        
        urlPermissionMap.put("/loadingrequest", "REQUEST_PURCHASE_CREATE");

        
//        urlPermissionMap.put("/.jsp", "IMPORT_CONFIRM");
//        urlPermissionMap.put("/", "IMPORT_CONFIRM");
        
        //EXPORT
        urlPermissionMap.put("/ExportRequest.jsp", "EXPORT_CREATE");
        urlPermissionMap.put("/exportRequest", "EXPORT_CREATE");
        
        // QUOTE
        urlPermissionMap.put("/ListPurchaseOrder.jsp", "QUOTE_VIEW");
        urlPermissionMap.put("/listpurchaseorder", "QUOTE_VIEW");
        urlPermissionMap.put("/PurchaseOrderForm.jsp", "QUOTE_CREATE");
        urlPermissionMap.put("/purchaseorderform", "QUOTE_CREATE");


        //APPROVE REQUEST
        urlPermissionMap.put("/ApproveListForward.jsp", "REQUEST_RESPONSE_PAGE"); 
        urlPermissionMap.put("/ApprovePurchaseRequest.jsp", "REQUEST_PURCHASE_APPROVE");        
        urlPermissionMap.put("/approvepurchaserequest", "REQUEST_PURCHASE_APPROVE");
        urlPermissionMap.put("/ApprovePurchaseQuoted.jsp", "QUOTE_APPROVE");
        urlPermissionMap.put("/approvepurchasequoted", "QUOTE_APPROVE");
        
        // ...các chức năng khác (Nếu có)
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        List<String> userPermissions = null;
        if (session != null) {
            Object permObj = session.getAttribute("userPermissions");
            if (permObj instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> checkedList = (List<String>) permObj;
                userPermissions = checkedList;
            }
        }

        String path = request.getServletPath();
        String permissionRequired = urlPermissionMap.get(path);

        if (permissionRequired != null) {
            if (userPermissions == null || !userPermissions.contains(permissionRequired)) {
                request.setAttribute("deniedFeature", path);
                request.getRequestDispatcher("access-denied.jsp").forward(request, response);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
