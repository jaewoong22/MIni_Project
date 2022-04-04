package com.model2.mvc.service.product.dao;

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

public class ProductDAO {

	public ProductDAO() {
	}

	public void insertProduct(Product product) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO product VALUES (seq_product_prod_no.NEXTVAL,?,?,?,?,?,sysdate)";

		PreparedStatement pStmt = con.prepareStatement(sql);

		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate().replace("-", ""));
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.executeUpdate();

		pStmt.close();
		con.close();

	}

	public Product findProduct(int prodNO) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "SELECT p.*, NVL(t.tran_status_code,'000') tranCode FROM product p, transaction t WHERE p.prod_no=t.prod_no(+) AND p.prod_no=? ";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNO);

		ResultSet rs = pStmt.executeQuery();

		Product product = null;

		while (rs.next()) {
			product = new Product();
			product.setProdNo(Integer.parseInt(rs.getString("prod_no")));
			product.setProdName(rs.getString("prod_name"));
			product.setFileName(rs.getString("image_file"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(Integer.parseInt(rs.getString("price")));
			product.setRegDate(rs.getDate("REG_DATE"));
			product.setProTranCode(rs.getString("tranCode"));
		}

		rs.close();
		pStmt.close();
		con.close();

		return product;
	}

	public Map<String, Object> getProductList(Search search) throws Exception {

		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT p.*, NVL(t.tran_status_code,'000') tranCode FROM product p, transaction t WHERE p.prod_no=t.prod_no(+)  ";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0")) {
				sql += " AND p.prod_no LIKE '%" + search.getSearchKeyword() + "%'";
			} else if (search.getSearchCondition().equals("1")) {
				sql += " AND p.prod_name LIKE '%" + search.getSearchKeyword() + "%'";
			}else if (search.getSearchCondition().equals("2")) {
				sql += " AND p.prod_price LIKE '%" + search.getSearchKeyword() + "%'";
			}
		}
		
		if(search.getOrderCondition() == null) {
			sql += " ORDER BY p.prod_no";
		}
		if (search.getOrderCondition() != null) {
			if(search.getOrderCondition().equals("0")) {
				sql += " ORDER BY p.prod_no";
			}else if (search.getOrderCondition().equals("1")) {
				sql += " ORDER BY p.price, p.prod_no";
			}else if(search.getOrderCondition().equals("2")) {
				sql += "ORDER BY p.price desc, p.prod_no";
			}else if(search.getOrderCondition().equals("3")) {
				sql += " AND NVL(t.tran_status_code,'000')='000' ORDER BY p.prod_no";
			}else if(search.getOrderCondition().equals("4")) {
				sql += " AND NVL(t.tran_status_code,'000')='001' ORDER BY p.prod_no";
			}else if(search.getOrderCondition().equals("5")) {
				sql += " AND NVL(t.tran_status_code,'000')='002' ORDER BY p.prod_no";
			}else if(search.getOrderCondition().equals("6")) {
				sql += " AND NVL(t.tran_status_code,'000')='003' ORDER BY p.prod_no";
			}else if(search.getOrderCondition().equals("7")) {
				sql += " AND NVL(t.tran_status_code,'000') NOT IN('000') ORDER BY p.prod_no";
			}
		}

		System.out.println("ProductDAO::Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);

		List<Product> list = new ArrayList<Product>();

		while(rs.next()){
				Product vo = new Product();
				vo.setProdNo(Integer.parseInt(rs.getString("prod_no")));
				vo.setProdName(rs.getString("prod_name"));
				vo.setFileName(rs.getString("image_file"));
				vo.setProdDetail(rs.getString("prod_detail"));
				vo.setManuDate(rs.getString("manufacture_day"));
				vo.setPrice(Integer.parseInt(rs.getString("price")));
				vo.setRegDate(rs.getDate("REG_DATE"));
				vo.setProTranCode(rs.getString("tranCode"));
				list.add(vo);
			
		}
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}

	public void updateProduct(Product product) throws Exception {

		Connection con = DBUtil.getConnection();
		System.out.println("DAO의 prodNo:; "+product.getProdNo());
		System.out.println("DAO의 prodName:; "+product.getProdName());
		String sql = "UPDATE product SET prod_name=?,prod_detail=?,manufacture_day=?,price=?,image_file=? WHERE prod_no=?";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.setInt(6, product.getProdNo());
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
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
		
		return sql;
	}

}
