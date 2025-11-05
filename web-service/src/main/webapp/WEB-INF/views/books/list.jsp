<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Catalogue</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
    <div class="container">
        <h1>Book Catalogue</h1>
        
        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <a href="<c:url value='/books/new'/>" class="btn btn-primary">Add New Book</a>
        
        <c:choose>
            <c:when test="${not empty books}">
                <table>
                    <thead>
                        <tr>
                            <th>ISBN</th>
                            <th>Name</th>
                            <th>Publish Date</th>
                            <th>Price</th>
                            <th>Book Type</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="book" items="${books}">
                            <tr>
                                <td>${book.isbn}</td>
                                <td>${book.name}</td>
                                <td>
                                    <c:if test="${not empty formattedDates[book.isbn]}">
                                        ${formattedDates[book.isbn]}
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${not empty book.price}">
                                        <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="R" />
                                    </c:if>
                                </td>
                                <td>${book.bookType}</td>
                                <td class="actions">
                                    <a href="<c:url value='/books/${book.isbn}/edit'/>" class="btn btn-edit">Edit</a>
                                    <form method="post" action="<c:url value='/books/${book.isbn}/delete'/>">
                                        <button type="submit" class="btn btn-delete" onclick="return confirm('Are you sure you want to delete this book?')">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No books found.</p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>

