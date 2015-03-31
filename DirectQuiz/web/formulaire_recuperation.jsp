<%-- 
    Document   : formulaire_recuperation
    Created on : 08 avr. 2013
    Author     : JYP
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Récupération</title>
        <c:import url="page/scripts.jsp"/>
    </head>
    <body>
        <div id="enveloppe">
    	<c:import url="page/header.jsp"/>
        

        <%-- Vérification de la présence d'un objet utilisateur en session --%>
        <c:choose>
            <c:when test="${!empty sessionScope.sessionUtilisateur}">
                <c:import url="page/menu_membre.jsp"/>
                <div id ="texte"></div>
            </c:when>
            <c:otherwise>
                <c:import url="page/menu.jsp"/>
                
                <div id ="texte">
                    <p>Processus de récupération. Merci de préciser votre login ou votre adresse mail.</p>

                    <form method="post" action="connexion">

                        <input type="hidden" name="action" value="recuperer">
                        <input type="hidden" name="data" value="true">

                        <label for="login">Login</label>
                        <input class="input" type="text" id="login" name="login" value="<c:out value="${utilisateur.login}"/>" size="20" maxlength="20" />
                        <span class="erreur">${form.erreurs['login']}</span>
                        <br />

                        <label for="email">Adresse mail</label>
                        <input class="input" type="text" id="email" name="email" value="<c:out value="${utilisateur.email}"/>" size="40" maxlength="40" />
                        <span class="erreur">${form.erreurs['email']}</span>
                        <br />

                        <input type="submit" value="Envoyer" class="sansLabel" />
                        <br />

                        <p>${form.resultat}</p>

                    </form>
               </div>
                    
            </c:otherwise>
        </c:choose>

        <c:import url="page/footer.jsp"/>
        </div>
    </body>
</html>
