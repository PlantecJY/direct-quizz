<%-- 
    Document   : formulaire_envoi
    Created on : 24 mars 2013
    Author     : JYP
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <c:import url="../page/scripts.jsp"/>
        <script type="text/javascript" src="js/jquery.js"></script>
        <!--<script type="text/javascript" src="js/question.js"></script>-->
        <title>Envoi de mail à tous les membres</title>        
    </head>
    <body>
        <div id="enveloppe">

    	<c:import url="../page/header.jsp"/>
	<c:import url="../page/menu_membre.jsp"/>
        
        <div id ="texte">
        <h2 class="titre">Envoi de mail à tous les membres</h2>

            <form method="post" action="membre?action=envoiTousMembres&data=true">

                <input type="hidden" name="data" value="true"/>


                <!-- texte -->
                <textarea cols="60" rows="4" class="message" name="message"></textarea>
                    
                <br /><br />
                <input type="button" value="Annuler" onclick='location.href="membre?action=lister"'/>
                <input type="submit" value="Valider" />
                <br />

            </form>
        </div>
        <c:import url="../page/footer.jsp"/>
        </div>
    </body>
</html>
