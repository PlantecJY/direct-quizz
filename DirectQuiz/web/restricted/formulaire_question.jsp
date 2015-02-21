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
        <script type="text/javascript">
            // initialisation du nb de réponses
            nbReponses = 0;
            textes = new Array();
            textes[0] = "";
            coches = new Array();
            coches[0] = "";
            function ajouteReponse(texte, valeur){
                //alert("début de ajouteReponse() "+texte);
                // sauvegarde des textes des réponses et des valeurs cochées et suppression des réponses
                if(nbReponses>0){              
                    for (var jj=1; jj<=nbReponses;jj++){
                        // uniquement pour les reponses non supprimées
                        if(document.getElementById("rep"+jj).innerHTML != ""){
                            textes[jj] = escapeHTML(document.getElementById("reponse"+jj).value);
                            if(document.getElementById("check"+jj).checked){
                              coches[jj] = '1';  
                            } else {
                              coches[jj] = '0';  
                            }
                            document.getElementById("rep"+jj).innerHTML = '';
                        }
                    }
                }
                // recréation des réponses
                nbReponses++;
                textes[nbReponses] = texte;
                coches[nbReponses] = valeur;
                for (var i=1; i<=nbReponses;i++){
                    //alert(textes[i]);
                    // uniquement pour les reponses non supprimées
                    if(textes[i] != "-"){
                        document.getElementById("reponses").innerHTML += '<div id="rep'+i+'"></div>';
                        document.getElementById("rep"+i).innerHTML += '<span class="requis">*</span></label>';
                        if(coches[i] == '1'){
                            document.getElementById("rep"+i).innerHTML += '<input type="checkbox" id="check'+i+'" name="valeurs" value="'+i+'" checked/>';
                        } else {
                            document.getElementById("rep"+i).innerHTML += '<input type="checkbox" id="check'+i+'" name="valeurs" value="'+i+'"/>';                    
                        }
                        document.getElementById("rep"+i).innerHTML += '<input type="text" id="reponse'+i+'" name="reponse'+i+'" value="'+textes[i]+'" size="100" maxlength="150" />';
                        document.getElementById("rep"+i).innerHTML += '<INPUT type="button" value="-" onClick="javascript:supprimeReponse('+i+')">';
                        document.getElementById("rep"+i).innerHTML += '<span class="erreur">${form.erreurs["reponse1"]}</span>';
                        document.getElementById("rep"+i).innerHTML += '<br />';
                    }
                }
                // mise à jour de la valeur nbReponses
                document.getElementById("nbReponses").value = nbReponses;
            }
            function supprimeReponse(idReponse){
                document.getElementById("rep"+idReponse).innerHTML = '';
                textes[idReponse]="-";
            }
            // modification de la liste des thèmes > sélectionné
            function modifieTheme(idT){
                document.getElementById("sel"+idT).selected = true;
            }
            var escapeHTML = (function () {
                'use strict';
                var chr = { '"': '&quot;', '&': '&amp;', '<': '&lt;', '>': '&gt;' };
                return function (text) {
                    return text.replace(/[\"&<>]/g, function (a) { return chr[a]; });
                };
            }());
       </script>
        <c:choose>
            <c:when test="${action} == 'ajouter'">
                <title>Création de question</title>
            </c:when>
            <c:otherwise>
                <title>Edition de question</title>
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
                <h2 class="titre">Création de question</h2>
            </c:when>
            <c:otherwise>
                <h2 class="titre">Edition de question</h2>
            </c:otherwise>   
        </c:choose>

                
            <script type="text/javascript">
                var nbItems = 0;
                var page = "formulaire_question";
            </script>
 
            <form method="post" action="question" enctype="multipart/form-data">

                <input type="hidden" name="action" value="<c:out value="${action}"/>"/>
                <input type="hidden" name="idTheme" value="<c:out value="${idTheme}"/>"/>
                <input type="hidden" name="id" value="<c:out value="${id}"/>"/>
                <input type="hidden" name="data" value="true"/>
                <!-- nbReponses nul au départ -->
                <input type='hidden' id='nbReponses' name='nbReponses' value='0'/>
                <!-- image éventuellement présente -->
                <input type='hidden' name='image_old' value="<c:out value="${question.image}"/>"/>
                <input type='hidden' name='imageRealName_old' value="<c:out value="${question.imageRealName}"/>"/>

                <!-- Thèmes -->
                <label for="theme">Thème</label>
                <select name="theme">
                    <option value="0">-</option>
                    <c:forEach var="item" items="${listeThemes}" >
                       <option value="<c:out value="${item.id}"/>" id="sel<c:out value="${item.id}"/>">${item.getTitre()}</option>
                    </c:forEach>
                </select>
                <!--- modification du thème courant en cas de mise à jour de Q ou d'ajout de Q-->
                <script type="text/javascript">
                    var idTheme = '${question.getTheme().getId()}';
                    if (idTheme != null && idTheme != ""){
                        // update
                        modifieTheme(idTheme);
                    } else {
                        // création (thème éventuellement passé)
                        idTheme = '${idTheme}';
                        modifieTheme(idTheme);
                    }
                </script>
                <br />

                <!-- Titre -->
                <label for="titre">Titre (max 50 car.)<span class="requis">*</span></label>
                <input type="text" id="titre" name="titre" size="70" maxlength="200" value="<c:out value="${question.titre}"/>"/>
                <br><span class="erreur">${form.erreurs['titre']}</span>
                <br />

                 <!-- Enoncé -->
                <label for="enonce">Enoncé <span class="requis">*</span></label>
                <textarea cols="60" rows="4" class="enonce" name="enonce"><c:out value="${question.enonce}"/></textarea>
                <br><span class="erreur">${form.erreurs['enonce']}</span>
                <br />

                <!-- Nombre de points -->
                <label for="enonce">Points</label>
                <input type="text" id="points" name="points" size="2" maxlength="2" value="<c:out value="${question.points}"/>"/>
                <span class="erreur">${form.erreurs['points']}</span>
                <br />

                <!-- Image -->
                <label for="image">Image (max 300 ko)</label>
                <input type="file" id="image" name="image" size="70" maxlength="200" value="<c:out value="${question.image}"/>"/><c:out value="${question.image}"/>
                <br><span class="erreur">${form.erreurs['image']}</span>
                <br />

                <!-- div pour les réponses -->
                <p>Réponses (max 150 car. ; cocher les réponses justes)</p>
                <span class="erreur">${form.erreurs['reponses']}</span>
                <div id="reponses" name="reponses"></div> 
                
                <!-- boucle sur les réponses -->
                <c:forEach var="item" items="${question.getReponses()}">
                    <script type="text/javascript">
                        var texteCourant = "${item.getTexte()}";
                        var valeurCourante = "${item.getValeur()}";
                        ajouteReponse(texteCourant,valeurCourante);
                    </script>
                </c:forEach> 
                    

                <INPUT type="button" value="Ajouter une réponse" onClick="javascript:ajouteReponse('','0')">

                <br /><br />
                <input type="button" value="Annuler" onclick='location.href="question?action=lister"'/>
                <input type="submit" value="Valider" />
                <br />

                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
            </form>
        </div>
        <c:import url="../page/footer.jsp"/>
    </div>    
    </body>
</html>
