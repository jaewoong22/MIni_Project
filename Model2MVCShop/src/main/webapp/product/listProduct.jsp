<%@ page contentType="text/html; charset=euc-kr" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 
<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	// 검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  
	function fncGetList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
	   	document.detailForm.submit();		
	}
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="/listProduct.do?&menu=${param.menu }" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					<c:choose>
						<c:when test="${ param.menu.equals('manage')}">
							상품 목록조회
						</c:when>
						<c:when test="${ param.menu.equals('search')}">
							<c:if test="${ !user.role.equals('admin')}">
							상품목록
							</c:if>
							<c:if test="${ user.role.equals('admin')}">
							상품 관리
							</c:if>
						</c:when>
					</c:choose>
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37">
		</td>
	</tr>
</table>



<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
		
			<c:if test="${ user.role.equals('admin')}">
				<select name="searchCondition" class="ct_input_g" style="width:80px">
				 	<option value="0"  ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>상품번호</option>
					<option value="1"  ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>상품명</option>
					<option value="2"  ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>상품가격</option>
				</select>
			</c:if>
				
			<c:if test="${ !user.role.equals('admin')}">
				<input type="hidden" name="searchCondition" value="1">
			</c:if>	

			
			<input 	type="text" name="searchKeyword" value="${! empty search.searchKeyword ? search.searchKeyword : ""}"  class="ct_input_g" 
							style="width:200px; height:20px" >
		</td>
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23"/>
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetList('1');">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<table width="180px" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="left">
			<c:if test="${ !user.role.equals('admin')}">
				<select name="orderCondition" class="ct_input_g" style="width:100px">
				 	<option value="0"  ${ ! empty search.orderCondition && search.orderCondition==0 ? "selected" : "" }>--정렬하기--</option>
					<option value="1"  ${ ! empty search.orderCondition && search.orderCondition==1 ? "selected" : "" }>낮은가격순</option>
					<option value="2"  ${ ! empty search.orderCondition && search.orderCondition==2 ? "selected" : "" }>높은가격순</option>
					<option value="3"  ${ ! empty search.orderCondition && search.orderCondition==3 ? "selected" : "" }>판매중인 상품</option>
				</select>
			</c:if>
			<c:if test="${ user.role.equals('admin') && param.menu.equals('manage')}">
				<select name="orderCondition" class="ct_input_g" style="width:100px">
					<option value="0"  ${ ! empty search.orderCondition && search.orderCondition==0 ? "selected" : "" }>--정렬하기--</option>
					<option value="3"  ${ ! empty search.orderCondition && search.orderCondition==3 ? "selected" : "" }>판매중</option>
					<option value="4"  ${ ! empty search.orderCondition && search.orderCondition==4 ? "selected" : "" }>구매완료</option>
					<option value="5"  ${ ! empty search.orderCondition && search.orderCondition==5 ? "selected" : "" }>배송중</option>
					<option value="6"  ${ ! empty search.orderCondition && search.orderCondition==6 ? "selected" : "" }>배송완료</option>
				</select>
			</c:if>
			<c:if test="${ user.role.equals('admin') && param.menu.equals('search')}">
				<select name="orderCondition" class="ct_input_g" style="width:100px">
					<option value="0"  ${ ! empty search.orderCondition && search.orderCondition==0 ? "selected" : "" }>--정렬하기--</option>
					<option value="3"  ${ ! empty search.orderCondition && search.orderCondition==3 ? "selected" : "" }>판매중</option>
					<option value="7"  ${ ! empty search.orderCondition && search.orderCondition==7 ? "selected" : "" }>구매완료</option>
				</select>
			</c:if>
			
		</td>
		<td align="left" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23"/>
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetList('1');">조회</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23"/>
					</td>
				</tr>
			</table>
		</td>
		
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >
			전체   ${resultPage.totalCount } 건수,	현재 ${resultPage.currentPage} 페이지
		</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>			
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
 
	 <c:set var="i" value="0" />
	<c:forEach var="product" items="${list}">
		<c:set var="i" value="${ i+1 }" />
		<tr class="ct_list_pop">
			<td align="center">${ i }</td>
			<td></td>
			<td align="left">
			
				<c:if test="${param.menu.equals('manage') }">
					<a href="/updateProductView.do?prodNo=${product.prodNo }&menu=${param.menu}">${product.prodName }</a>
				</c:if>
				<c:if test="${param.menu.equals('search') }">
					<c:choose>
						<c:when test="${!product.proTranCode.equals('000') && !(user.role.equals('admin'))}">
							${product.prodName }
						</c:when>
						<c:otherwise>
							<a href="/getProduct.do?prodNo=${product.prodNo }&menu=${param.menu}">${product.prodName }</a>
						</c:otherwise>
					</c:choose>				
				</c:if>
			
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<td align="left">${product.regDate}
			</td>		
			<td></td>
			<c:choose>
				<c:when test="${product.proTranCode.equals('000') }">
					<td align="left">판매중</td>
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${param.menu.equals('manage') && user.role.equals('admin')}">
						<c:if test="${product.proTranCode.equals('001') }">
							<td align="left">구매완료 <a href="/updateTranCode.do?prodNo=${product.prodNo }&tranCode=${product.proTranCode}">배송하기</a></td>
						</c:if>
						<c:if test="${product.proTranCode.equals('002') }">
							<td align="left">배송중</td>
						</c:if>
						<c:if test="${product.proTranCode.equals('003') }">
							<td align="left">배송완료</td>
						</c:if>
					</c:when>
					<c:when test="${param.menu.equals('search') && user.role.equals('admin')}">
						<td align="left">구매완료</td>
					</c:when>
					<c:otherwise>
						<td align="left">재고없음</td>
					</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			
		</tr>
		<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>
	</c:forEach>
</table>

<!-- PageNavigation Start... -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top:10px;">
	<tr>
		<td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>

			 <jsp:include page="../common/pageNavigator.jsp"/>
    	</td>
	</tr>
</table>
<!-- PageNavigation End... -->

</form>
</div>

</body>
</html>