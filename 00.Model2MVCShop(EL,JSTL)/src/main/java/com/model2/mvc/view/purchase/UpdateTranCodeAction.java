package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.domain.Purchase;

public class UpdateTranCodeAction extends Action {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tranCode = request.getParameter("tranCode");
		String prodNo = request.getParameter("prodNo");

		System.out.println("UTCAÀÇ trancode|1| => "+tranCode);
		
		if(tranCode.equals("001")) {
			tranCode = "002";
		}else if(tranCode.equals("002")) {
			tranCode = "003";
		}
		
		System.out.println("UTCAÀÇ trancode|2| => "+tranCode);
		
		Purchase purchase = new Purchase();
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(prodNo));

		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(purchase);
		

		return "redirect:/listProduct.do?menu=manage" ;
	}

}
