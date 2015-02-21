<%-- 
    Document   : liste_membres
    Created on : 18 déc. 2012, 15:23:35
    Author     : JYP
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Liste des thèmes</title>
    <link type="text/css" rel="stylesheet" href="style/tables.css" />
    <link type="text/css" rel="stylesheet" href="style/global.css" />
    <link type="text/css" rel="stylesheet" href="style/question.css" />
    <script type="text/javascript" src="js/suppression.js"></script>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/tutoriel.js"></script>
	<script src="js/ui/jquery.ui.core.js"></script>
	<script src="js/ui/jquery.ui.widget.js"></script>
	<script src="js/ui/jquery.ui.tabs.js"></script>
	<script src="js/ui/jquery.ui.accordion.js"></script>
	<script src="js/ui/jquery.ui.mouse.js"></script>
	<script src="js/ui/jquery.ui.position.js"></script>
	<script src="js/ui/jquery.ui.resizable.js"></script>
	<script src="js/ui/jquery.ui.draggable.js"></script>
	<script src="js/ui/jquery.ui.button.js"></script>
	<script src="js/ui/jquery.ui.dialog.js"></script>
	<script src="js/ui/jquery.ui.effect.js"></script>
	<script src="js/ui/jquery.ui.effect-blind.js"></script>
	<script src="js/ui/jquery.ui.effect-explode.js"></script>
	<link rel="stylesheet" href="js/themes/base/jquery.ui.all.css">
</head>
<body>
<div id="enveloppe">

<c:import url="../page/header.jsp"/>

<c:import url="../page/menu_membre.jsp"/>

<c:import url="../page/help.jsp"/>


<div id ="texte">
    
    <h2 class="titre">Liste des thèmes</h2>
    
    <p class="faux">${message}</p>

    <a href="theme?action=ajouter&data=false">Ajouter thème</a><br><br>

    <script type="text/javascript">
        var nbItems = 0;
        var page = "liste_themes";
    </script>

    <table class="defaut">
        <tr>
            <th>Titre</th>
            <th colspan="2" align="left">Actions</th>        
        </tr>
    <c:forEach var="item" items="${listeThemes}" >
        <tr>
            <td><c:out value="${item.titre}" /></td>
            <td><a href="#" onclick="confirmation('ce thème','theme?action=supprimer&id=',${item.id});">Supprimer</a></td>
            <td><a href="theme?action=editer&id=${item.id}">éditer</a></td>
        <tr>
        <script type="text/javascript">
            nbItems++;
        </script>
    </c:forEach>
    </table>
    <br>
    <c:if test="${!empty question}">
        <div id="uneQuestion">
            <p>Thème : ${theme.getTitre()}</p>
        </div>
    </c:if>

</div>

<c:import url="../page/footer.jsp"/>
</div>
</body>
</html>
