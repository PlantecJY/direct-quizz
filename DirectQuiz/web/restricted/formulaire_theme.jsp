<%-- 
    Document   : formulaire_question
    Created on : 15 déc. 2012, 19:14:51
    Author     : JYP
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="style/forms.css" />
        <link type="text/css" rel="stylesheet" href="style/global.css" />
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
       <!--<script type="text/javascript" src="js/question.js"></script>-->
        <c:choose>
            <c:when test="${action} == 'ajouter'">
                <title>Création de thème</title>
            </c:when>
            <c:otherwise>
                <title>Edition de thème</title>
            </c:otherwise>
        </c:choose>
        
    </head>
    <body>
    <div id="enveloppe">
    	<c:import url="../page/header.jsp"/>
	<c:import url="../page/menu_membre.jsp"/>
        <c:import url="../page/help.jsp"/>
        
        <div id ="texte">
         <c:choose>
            <c:when test="${action == 'ajouter'}">
                <h2 class="titre">Création de thème</h2>
            </c:when>
            <c:otherwise>
                <h2 class="titre">Edition de thème</h2>
            </c:otherwise>
        </c:choose>
                
            <script type="text/javascript">
                var nbItems = 0;
                var page = "formulaire_theme";
            </script>

            <form method="post" action="theme">


                <input type="hidden" name="action" value="<c:out value="${action}"/>"/>
                <input type="hidden" name="id" value="<c:out value="${id}"/>"/>
                <input type="hidden" name="data" value="true"/>


                <!-- Titre -->
                <label for="titre">Titre <span class="requis">*</span></label>
                <input type="text" id="titre" name="titre" size="40" maxlength="100" value="<c:out value="${theme.titre}"/>"/>
                <br><span class="erreur">${form.erreurs['titre']}</span>
                    
                <br /><br />
                <input type="button" value="Annuler" onclick='location.href="theme?action=lister"'/>
                <input type="submit" value="Valider" />
                <br />

                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
            </form>
        </div>
        <c:import url="../page/footer.jsp"/>
    </div>    
    </body>
</html>
