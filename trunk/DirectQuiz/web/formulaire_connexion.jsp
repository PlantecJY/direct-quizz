<%-- 
    Document   : formulaire_connexion
    Created on : 13 déc. 2012, 17:53:41
    Author     : JYP
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Connexion</title>
        <link type="text/css" rel="stylesheet" href="style/forms.css" />
        <link type="text/css" rel="stylesheet" href="style/global.css" />
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
                    <p>Merci de vous identifier.</p>

                    <form method="post" action="connexion">
                            <input type="hidden" name="action" value="connexion">
                            <input type="hidden" name="data" value="true">

                        <label for="login">Login</label>
                        <input class="input" type="text" id="login" name="login" value="<c:out value="${utilisateur.login}"/>" size="20" maxlength="20" />
                        <span class="erreur">${form.erreurs['login']}</span>
                        <br />

                        <label for="motDePasse">Mot de passe</label>
                        <input type="password" id="motDePasse" name="motDePasse" value="" size="20" maxlength="20" />
                        <span class="erreur">${form.erreurs['motDePasse']}</span>
                        <br />

                        <label for="boutonLogin">&nbsp;</label>
                        <input type="submit" id="boutonLogin" value="Connexion" />
                        <br />

                        <c:if test="${!empty form.resultat}">
                            <p class="erreur">${form.resultat} - <a href="connexion?action=recuperer&data=false">mot de passe oublié ?</a></p>
                        </c:if>

                    </form>
               </div>
                    
            </c:otherwise>
        </c:choose>

        <c:import url="page/footer.jsp"/>
        </div>
    </body>
</html>
