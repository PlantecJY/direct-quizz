<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Inscription</title>
        <link type="text/css" rel="stylesheet" href="style/forms.css" />
        <link type="text/css" rel="stylesheet" href="style/global.css" />
    </head>
    <body>
    <div id="enveloppe">
    	<c:import url="page/header.jsp"/>
	<c:import url="page/menu.jsp"/>
        
        <div id ="texte">

            <c:if test="${empty form.erreurs == false || form.erreurs == null}">   
            
                <form method="post" action="connexion">
                        <input type="hidden" name="action" value="enregistrement">
                        <input type="hidden" name="data" value="true">

                    <p>Merci de remplir le formulaire ci-dessous. Vous pourrez utiliser la plateforme dès que votre inscription aura été validée.</p>

                    <label for="login">Login choisi <span class="requis">*</span></label>
                    <input type="text" id="login" name="login" value="<c:out value="${utilisateur.login}"/>" size="20" maxlength="20" />
                    <span class="erreur">${form.erreurs['login']}</span>
                    <br />

                    <label for="email">Adresse email <span class="requis">*</span></label>
                    <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>" size="20" maxlength="60" />
                    <span class="erreur">${form.erreurs['email']}</span>
                    <br />

                    <label for="mot_de_passe">Mot de passe <span class="requis">*</span></label>
                    <input type="password" id="motdepasse" name="mot_de_passe" value="" size="20" maxlength="20" />
                    <span class="erreur">${form.erreurs['mot_de_passe']}</span>
                    <br />

                    <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>
                    <input type="password" id="confirmation" name="confirmation" value="" size="20" maxlength="20" />
                    <span class="erreur">${form.erreurs['confirmation']}</span>
                    <br />

                    <input type="submit" value="Inscription" class="sansLabel" />
                    <br />
                    
                </form>
                
            </c:if>
            <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
        </div>
        
        <c:import url="page/footer.jsp"/>
    </div>    
    </body>
</html>
