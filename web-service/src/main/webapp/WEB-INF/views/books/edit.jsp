<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Book</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
    <div class="container form-container">
        <h1>Edit Book</h1>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <form:form method="post" modelAttribute="bookForm" action="${pageContext.request.contextPath}/books/${bookForm.isbn}">
            <div class="form-group">
                <form:label path="isbn">ISBN</form:label>
                <form:input path="isbn" readonly="true" />
                <form:errors path="isbn" cssClass="field-error" />
            </div>
            
            <div class="form-group">
                <form:label path="name">Name *</form:label>
                <form:input path="name" />
                <form:errors path="name" cssClass="field-error" />
            </div>
            
            <div class="form-group">
                <form:label path="publishDate">Publish Date (dd/MM/yyyy) *</form:label>
                <form:input path="publishDate" placeholder="dd/MM/yyyy" />
                <form:errors path="publishDate" cssClass="field-error" />
            </div>
            
            <div class="form-group">
                <form:label path="price">Price *</form:label>
                <form:input type="number" path="price" step="0.01" min="0" />
                <form:errors path="price" cssClass="field-error" />
            </div>
            
            <div class="form-group">
                <form:label path="bookType">Book Type *</form:label>
                <form:select path="bookType">
                    <form:option value="">-- Select Book Type --</form:option>
                    <c:forEach var="type" items="${bookTypes}">
                        <form:option value="${type}">${type}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="bookType" cssClass="field-error" />
            </div>
            
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Update Book</button>
                <a href="<c:url value='/books'/>" class="btn btn-secondary">Cancel</a>
            </div>
        </form:form>
    </div>
</body>
</html>

