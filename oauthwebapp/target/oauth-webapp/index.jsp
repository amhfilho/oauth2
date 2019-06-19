<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OAuth2 Web Application Welcome Screen</title>
</head>
<body>
    <h2>Welcome to the OAuth2 Web Application</h2>
    <c:if test="${not empty sessionScope.name}">
        Hello ${name}</br>
        Your access token is: ${accessToken}</br>
        <a href="/logout">Logout</a>

    </c:if>

    <c:if test="${empty sessionScope.name}">
        <a href="login">
            <img src="google-button.png" alt="Google Sign-in"/>
        </a>
    </c:if>
</body>
</html>