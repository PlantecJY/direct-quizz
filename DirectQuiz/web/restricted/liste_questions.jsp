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
    <title>Liste des questions</title>
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
    <script type="text/javascript">
        // modification de la liste des thèmes > sélectionné
        function modifieTheme(idT){
            document.getElementById("sel"+idT).selected = true;
        }
        // rechargement avec nouvau thème
        function rechargeAvecTheme(){
            // thème sélectionné
            var selectedElement = document.getElementById("theme");
            var indexChoisi = selectedElement.options[selectedElement.selectedIndex].value;
            // redirection
            window.location = "question?action=lister&idTheme="+indexChoisi;
        }
     </script>   
</head>
<body>
<div id="enveloppe">

<c:import url="../page/header.jsp"/>

<c:import url="../page/menu_membre.jsp"/>

<c:import url="../page/help.jsp"/>

<div id ="texte">
    
    <h2 class="titre">Liste des questions</h2>
        
    <p class="faux">${message}</p>
    
    <a href="question?action=ajouter&data=false&idTheme=${idTheme}">Ajouter question</a><br><br>

    <!-- Thème -->
    <input type='hidden' name='idTheme' value="<c:out value="${idTheme}"/>"/> 
    <label for="theme">Thème</label>
        <select id="theme" name="theme" onchange="javascript:rechargeAvecTheme()">
            <option value="0">tous...</option>
            <c:forEach var="item" items="${listeThemes}" >
                <option value="<c:out value="${item.id}"/>" id="sel<c:out value="${item.id}"/>">${item.getTitre()}</option>
            </c:forEach>
        </select>
        <!--- modification du thème choisi -->
        <script type="text/javascript">
            var idTheme = '${idTheme}';
            modifieTheme(idTheme);
        </script>
    <br />

    <script type="text/javascript">
        var nbItems = 0;
        var page = "liste_questions";
    </script>
    
    <table class="defaut">
        <tr>
            <th>Theme</th>
            <th>Titre</th>
            <th>Enoncé</th>
            <th>Points</th>        
            <th colspan="3" align="left">Actions</th>        
        </tr>
    <c:forEach var="item" items="${listeQuestions}" >
        <tr>
            <td><c:out value="${item.getTheme().titre}" /></td>
            <td><c:out value="${item.titre}" /></td>
            <td><c:out value="${item.enonce}" /></td>
            <td><c:out value="${item.points}" /></td>
            <td><a href="#" onclick="confirmation('cette question','question?action=supprimer&id=',${item.id},'&idTheme=',${idTheme});">Supprimer</a></td>
            <td><a href="question?action=editer&id=${item.id}&idTheme=${idTheme}">éditer</a></td>
            <td><a href="question?action=lister&id=${item.id}&idTheme=${idTheme}">voir</a></td>
        <tr>
        <script type="text/javascript">
            nbItems++;
        </script>
    </c:forEach>
    </table>
    <br>
    <c:if test="${!empty question}">
        <div id="uneQuestion">
            <p>Thème : ${question.getTheme().getTitre()}</p>
            <p>Titre : ${question.titre}</p>
            <!-- test sur image (et non imageRealName) -->
            <c:choose>
                <c:when test="${question.image != null}">
                    <img src="./img/${question.imageRealName}" height="200 px">
                </c:when>
            </c:choose>
            <p>Enoncé : ${question.enonce}</p>
            <p>Nbre de points : ${question.points}</p>
            <p>Réponses :</p>
            <ul>
                <c:forEach var="item" items="${question.getReponses()}" varStatus="status">
                    <c:choose>
                        <c:when test="${item.valeur == '1'}">
                            <li>${item.texte} (x)</li>
                        </c:when>
                        <c:otherwise>
                            <li>${item.texte}</li>
                        </c:otherwise>
                    </c:choose>        
                </c:forEach> 
            </ul>
        </div>
    </c:if>

</div>

<c:import url="../page/footer.jsp"/>
</div>
</body>
</html>
