<%-- 
    Document   : liste_membres
    Created on : 13 déc. 2012, 15:23:35
    Author     : JYP
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Liste des membres</title>
<link type="text/css" rel="stylesheet" href="style/tables.css" />
<link type="text/css" rel="stylesheet" href="style/global.css" />
<script type="text/javascript" src="js/suppression.js"></script>
</head>
<body>
<div id="enveloppe">

<c:import url="../page/header.jsp"/>

<c:import url="../page/menu_membre.jsp"/>

<div id ="texte">

    <h2 class="titre">Liste des membres</h2>

    <p>${message}</p>
    <table class="defaut">
        <tr>
            <th>Login</th>
            <th>Email</th>
            <c:if test="${sessionScope.sessionUtilisateur.gestionnaire == 1}">
                <th>Mot de passe</th>
                <th colspan="3" align="left">Actions</th>        
            </c:if>
        </tr>
    <c:forEach var="item" items="${listeMembres}" >
        <tr>
            <td><c:out value="${item.login}" /></td>
            <td><a href="mailto:<c:out value='${item.email}'/>"><c:out value='${item.email}'/></a></td>

            <%-- actions et password uniquement si gestionnaire --%>
            <c:if test="${sessionScope.sessionUtilisateur.gestionnaire == 1}">
                <td><c:out value="${item.motDePasse}" /></td>
                <td><a href="#" onclick="confirmation('ce membre','membre?action=supprimer&id=',${item.id});">Supprimer</a></td>
                <!--<td><a href="membre?action=supprimer&id=${item.id}">supprimer</a></td>-->
                <td>
                    <c:choose>
                        <c:when test="${item.valide == 0}"><a href="membre?action=valider&id=${item.id}">valider</a></c:when>
                        <c:otherwise><a href="membre?action=devalider&id=${item.id}">dévalider</a></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${item.gestionnaire == 0}"><a href="membre?action=rendreGestionnaire&id=${item.id}">rendre gestionnaire</a></c:when>
                        <c:otherwise><a href="membre?action=nePlusRendreGestionnaire&id=${item.id}">ne plus rendre gestionnaire</a></c:otherwise>
                    </c:choose>
                </td>
            </c:if>

        <tr>
    </c:forEach>
    </table>
    <br>
    <!-- envoi de mail à tous les membres -->
    <c:if test="${sessionScope.sessionUtilisateur.gestionnaire == 1}">
        <a href="membre?action=envoiTousMembres&data=false">Envoyer un mail à tous les membres</a>
    </c:if>
    
</div>

<c:import url="../page/footer.jsp"/>
</div>
</body>
</html>
