<%-- 
    Document   : test_connexion
    Created on : 14 d�c. 2012, 19:04:01
    Author     : JYP
--%>

<%-- V�rification de la pr�sence d'un objet utilisateur en session --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
    <c:when test="${!empty sessionScope.sessionUtilisateur}">
        <c:import url="page/menu_membre.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="page/menu.jsp"/>
    </c:otherwise>
</c:choose>