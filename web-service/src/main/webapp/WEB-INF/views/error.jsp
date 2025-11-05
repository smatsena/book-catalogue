<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error - Book Catalogue</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
    <div class="container error-page">
        <div class="error-image-container">
            <c:choose>
                <c:when test="${errorType == '500' || errorType == '503'}">
                    <img src="<c:url value='/images/not-you-me.jpg'/>" alt="500 Error" class="error-image">
                </c:when>
                <c:otherwise>
                    <img src="<c:url value='/images/side-eye-dog-suspicious-look.gif'/>" alt="404 Suspicious Dog" class="error-image">
                </c:otherwise>
            </c:choose>
        </div>
        <c:choose>
            <c:when test="${errorType == '404'}">
                <div class="error-message">
                    <h2>What are you doing here?</h2>
                </div>
            </c:when>
            <c:otherwise>
                <div class="error-message">
                    <h2>An error occurred</h2>
                    <c:if test="${not empty errorMessage}">
                        <p class="error-detail">${errorMessage}</p>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="error-actions">
            <a href="<c:url value='/books'/>" class="btn btn-primary">Go to Book List</a>
        </div>
    </div>
</body>
</html>

