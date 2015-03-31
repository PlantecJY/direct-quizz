<%-- 
    Document   : formulaire_connexion
    Created on : 19 déc. 2012, 17:53:41
    Author     : JYP
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <c:import url="page/scripts.jsp"/>
        <title>Connexion</title>        
    </head>
    
    <body>
    <div id="enveloppe">
    	<c:import url="page/header.jsp"/>
        <c:import url="page/menu.jsp"/>
        
        <div id ="texte">
        <p>Merci d'entrer le code et le mot de passe de la séquence ainsi qu'un identifiant de votre choix.</p>

                <form method="post" action="sequence">
                    
                    <input type="hidden" name="action" value="participer">
                    <input type="hidden" name="data" value="true">

                    <label for="code" class="participation">Code de la séquence</label>
                    <input type="text" id="code" name="code" value="<c:out value="${code}"/>" size="20" maxlength="20" />
                    <span class="erreur">${form.erreurs['code']}</span>
                    <br />

                    <label for="motDePasse" class="participation">Mot de passe de la séquence</label>
                    <input type="password" id="motDePasse" name="motDePasse" value="" size="20" maxlength="20" />
                    <span class="erreur">${form.erreurs['motDePasse']}</span>
                    <br />

                    <label for="identifiant" class="participation">Votre identifiant</label>
                    <input type="text" id="identifiant" name="identifiant" value="<c:out value="${identifiant}"/>" size="20" maxlength="20" /><br><span class="important">(Pas de caractères accentués SVP)</span>
                    <span class="erreur">${form.erreurs['identifiant']}</span>
                    <br />
                    
                    <label for="boutonParticiper" class="participation">&nbsp;</label>
                    <input type="submit" value="Entrer" id="boutonParticiper"/>
                    <br />

                    <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>

                </form>
                    
        </div>

        <c:import url="page/footer.jsp"/>
    </div>    
    </body>
</html>
