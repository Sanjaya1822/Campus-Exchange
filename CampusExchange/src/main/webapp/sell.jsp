<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    if (session == null || session.getAttribute("userEmail") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Sell Item - CampusExchange</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="navbar">
    <a href="dashboard.jsp">Dashboard</a>
    <a href="buy.jsp">Buy</a>
    <a href="sell.jsp">Sell</a>
    <a href="rent.jsp">Rent</a>
    <a href="donate.jsp">Donate</a>
    <a href="logout">Logout</a>
</div>

<div class="auth-container">
    <h2>Sell an Item</h2>

    <form action="sellItem" method="post" enctype="multipart/form-data">

        <label>Item Title</label>
        <input type="text" name="title" required>

        <label>Description</label>
        <textarea name="description" rows="4" required></textarea>

        <label>Price</label>
        <input type="number" name="price" required>

        <label>Category</label>
        <select name="category">
            <option value="buy">Buy</option>
            <option value="rent">Rent</option>
            <option value="donate">Donate</option>
        </select>

        <label>Item Image</label>
        <input type="file" name="image" accept="image/*" required>

        <input type="submit" value="Post Item">
    </form>
</div>

</body>
</html>
