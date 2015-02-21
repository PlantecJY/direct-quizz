<%-- 
    Document   : test_connexion
    Created on : 14 déc. 2012, 19:04:01
    Author     : JYP
--%>

<%-- Vérification de la présence d'un objet utilisateur en session --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
    <c:when test="${!empty sessionScope.sessionUtilisateur}">
        <c:import url="page/menu_membre.jsp"/>
    </c:when>
    <c:otherwise>
        <c:import url="page/menu.jsp"/>
    </c:otherwise>
</c:choose>