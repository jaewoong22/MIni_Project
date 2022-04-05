package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class PurchaseDAO {

	public PurchaseDAO() {
	}

	public void insertPurchase(Purchase purchase) throws Exception {
		System.out.println("insertPurchase시작");
		System.out.println(purchase);

		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setInt(1, purchase.getPurchaseProd().getProdNo());	//reference ProductVO
		pStmt.setString(2, purchase.getBuyer().getUserId());			//reference UserVO
		pStmt.setString(3, purchase.getPaymentOption());
		pStmt.setString(4, purchase.getReceiverName());
		pStmt.setString(5, purchase.getReceiverPhone());
		pStmt.setString(6, purchase.getDivyAddr());
		pStmt.setString(7, purchase.getDivyRequest());
		pStmt.setString(8, purchase.getTranCode());
		pStmt.setDate(9, purchase.getOrderDate());
		pStmt.setString(10, purchase.getDivyDate());
		pStmt.executeUpdate();

		con.close();

	}

	public Purchase findPurchase(int tranNo) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "select * from transaction where tran_no=?";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, tranNo);

		ResultSet rs = pStmt.executeQuery();

		Purchase purchase = null;
		Product product = null;
		User user = null;

		while (rs.next()) {
			purchase = new Purchase();
			product = new Product();
			user = new User();
			purchase.setTranNo(tranNo);
			product.setProdNo(Integer.parseInt(rs.getString("prod_no")));
			purchase.setPurchaseProd(product);
			user.setUserId(rs.getString("buyer_id"));
			purchase.setBuyer(user);
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setTranCode(rs.getString("tran_status_code"));
		}

		con.close();

		return purchase;
	}

	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {

		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		System.out.println("======="+buyerId);
		
		String sql="";
		
		if(buyerId.equals("admin")) {
			sql = "SELECT p.prod_name, t.* FROM product p, transaction t  WHERE p.prod_no=t.prod_no(+) AND t.tran_no IS NOT NULL ORDER BY tran_no";
		}else {
			sql = "SELECT p.prod_name, t.* FROM product p, transaction t  WHERE p.prod_no=t.prod_no(+) AND buyer_id='"+buyerId +"' ORDER BY tran_no";
			
		}
		
		System.out.println("PurchaseDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Purchase> list = new ArrayList<Purchase>();

		while(rs.next()){
				Purchase purchase = new Purchase();
				Product product = new Product();
				User user = new User();
				purchase.setTranNo(rs.getInt("tran_no"));
				product.setProdNo(Integer.parseInt(rs.getString("prod_no")));
				product.setProdName(rs.getString("prod_name"));
				purchase.setPurchaseProd(product);
				user.setUserId(rs.getString("buyer_id"));
				purchase.setBuyer(user);
				purchase.setPaymentOption(rs.getString("payment_option"));
				purchase.setReceiverName(rs.getString("receiver_name"));
				purchase.setReceiverPhone(rs.getString("receiver_phone"));
				purchase.setDivyAddr(rs.getString("demailaddr"));
				purchase.setDivyRequest(rs.getString("dlvy_request"));
				purchase.setDivyDate(rs.getString("dlvy_date"));
				purchase.setOrderDate(rs.getDate("order_data"));
				purchase.setTranCode(rs.getString("tran_status_code"));

				list.add(purchase);
		
		}
		
		// ==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		// ==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}

	public void updatePurchase(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();
		String sql = "update transaction set buyer_id=?, payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date=? where tran_no=?";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, purchase.getBuyer().getUserId());
		pStmt.setString(2, purchase.getPaymentOption());
		pStmt.setString(3, purchase.getReceiverName());
		pStmt.setString(4, purchase.getReceiverPhone());
		pStmt.setString(5, purchase.getDivyAddr());
		pStmt.setString(6, purchase.getDivyRequest());
		pStmt.setString(7, purchase.getDivyDate());
		pStmt.setInt(8, purchase.getTranNo());
		pStmt.executeUpdate();

		con.close();
	}
	
	public void updateTranCode(Purchase purchase) throws Exception{
		
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE transaction SET tran_status_code=? WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, purchase.getTranCode());
		pStmt.setInt(2, purchase.getTranNo());
		pStmt.executeUpdate();

		pStmt.close();
		con.close();
		
	}
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

}
