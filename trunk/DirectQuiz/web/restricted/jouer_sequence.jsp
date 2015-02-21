<%-- 
    Document   : liste_sequences
    Created on : 19 déc. 2012, 15:23:35
    Author     : JYP
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Jeu de séquence</title>
    <link type="text/css" rel="stylesheet" href="style/tables.css" />
    <link type="text/css" rel="stylesheet" href="style/global.css" />
    <link type="text/css" rel="stylesheet" href="style/sequence.css" />
    <link href="" rel="stylesheet" type="text/css" id="partcss" />
    <link href="" rel="stylesheet" type="text/css" id="scocss" />
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
        <script src="js/ui/jquery.ui.tooltip.js"></script>
	<link rel="stylesheet" href="js/themes/base/jquery.ui.all.css">
    <script type="text/javascript">
        var requete;

        function recuperer(id) {
           //var donnees = document.getElementById("donnees");
           // id de la séquence
            var url = "Resultats?action=maj&id=" +  id;
           if (window.XMLHttpRequest) {
               requete = new XMLHttpRequest();
           } else if (window.ActiveXObject) {
               requete = new ActiveXObject("Microsoft.XMLHTTP");
           }
           requete.open("GET", url, true);
           requete.onreadystatechange = mettre_a_jour;
           requete.send(null);
        }
        
        function mettre_a_jour() {
          var message = "";

          if (requete.readyState == 4) {
            if (requete.status == 200) {
              //alert("mettre_a_jour");
              // exploitation des données de la réponse
              var messageTag = requete.responseXML.getElementsByTagName("message")[0];
              // texte du message
              message = messageTag.childNodes[0].nodeValue;
              mdiv = document.getElementById("resultats");
              mdiv.innerHTML = message;
            }
          }
        }
        
        function Horloge() {
            //var dt=new Date();
            //document.getElementById("date_sequence").innerHTML = dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
            var id = '${sequence.id}';
            recuperer(id);
        }
        function afficheQuestion(id) {
            var texteQuestion = document.getElementById("uneQuestion"+id).innerHTML;
            return texteQuestion;
        }
        
        var timer=setInterval("Horloge()", 1000);
        
        // initialisation du nb de réponses
        function afficheReponses(id,justes){
            //alert('affiche '+id);
            if(justes=="false"){
                document.getElementById("uneQuestion"+id).style.display = "block";
            } else {
                document.getElementById("uneQuestionJuste"+id).style.display = "block";
            }
        }
        function cacheReponses(id){
            //alert('cache '+id);
            document.getElementById("uneQuestion"+id).style.display = "none";
            document.getElementById("uneQuestionJuste"+id).style.display = "none";
        }
        function changeTexteReponse(texteInitial, reponseId){
            // <span id="_reponse<c:out value="${item.id}"/>">
            var chaineAtrouver = '(<span id="_reponse'+reponseId+'">)';
            var reg = new RegExp(chaineAtrouver, "g");
            return texteInitial.replace(reg,'<span class="faux">X'); 
        }
        var plusminP = 1;
        function colonneParticipants(){
            plusminP *= -1;
            var stlP = plusminP>0 ? '' : 'style/invisibleParticipants.css';
            var chaine = plusminP>0 ? 'Masquer participants' : 'Afficher participants';
            $('#partcss[rel=stylesheet]').attr('href', stlP);
            $("#colPart").html(chaine);
            //$('td.part').addClass('unvisible');
        }
        var plusminS = 1;
        function colonneScores(){
            plusminS *= -1;
            var stlS = plusminS>0 ? '' : 'style/invisibleScores.css';
            var chaine = plusminS>0 ? 'Masquer scores' : 'Afficher scores';
            $('#scocss[rel=stylesheet]').attr('href', stlS);
            $("#colSco").html(chaine);
            //$('td.part').addClass('unvisible');
        }
    </script>
    <script>
	$(function() {
		$( document ).tooltip({
			items: "[title], [reponses]",
			content: function() {
				var element = $( this );
				if ( element.is( "[title]" ) ) {
					return "le retour : " + element.attr( "title" );
				}
				if ( element.is( "[reponses]" ) ) {
                                        // id de la question qxx -> xx
                                        var idQ = element.attr( "id" ).substring(1);
                                        // texte de la question
                                        texte = afficheQuestion(idQ);
                                        // changement des styles
                                        var listeReponses = element.attr( "reponses" ).split('-');
                                        //alert(listeReponses);
                                        for (var i=0; i<listeReponses.length; i++){
                                            if(listeReponses[i]!="" && listeReponses[i]!=null){
                                                texte = changeTexteReponse(texte,listeReponses[i]);
                                            }
                                        }
                                        // affichage du texte de la question
					return texte;
				}
			}
		});
	});
    </script>
    <style>
	.ui-tooltip {
		max-width: 750px;
	}
    </style>

</head>
<body>
<div id="enveloppe">

<c:import url="../page/header.jsp"/>

<c:import url="../page/menu_membre.jsp"/>

<c:import url="../page/help.jsp"/>

<div id ="texte">
    
    <h2 class="titre">Jeu de la séquence : ${sequence.code}</h2>
    <p id="date_sequence"></p>

    <script type="text/javascript">
        var nbItems = 0;
        var page = "jouer_mode_libre";
    </script>

    <c:if test="${!empty sequence}">
        <div id="uneSequence">
            <p>Mot de passe : ${sequence.motDePasse}</p>
            <ul>
            <c:forEach var="item" items="${sequence.questions}" >
                <li>Question <c:out value="${item.id}" /> : <c:out value="${item.titre}" /> - <a href="#" onmouseover="javascript:afficheReponses('${item.id}','false')" onmouseout="javascript:cacheReponses('${item.id}')">Réponses</a> | <a href="#" onmouseover="javascript:afficheReponses('${item.id}','true')" onmouseout="javascript:cacheReponses('${item.id}')">Réponses justes</a>
                    <ul id="uneQuestionJuste${item.id}" style="background-color:#eee; display:none;"><c:out value="${item.enonce}" />
                        <c:forEach var="itemR" items="${item.getReponses()}" varStatus="status">
                            <li>
                            <c:choose>
                                <c:when test="${itemR.valeur == '1'}"><span class="juste">${itemR.texte}</span></c:when>
                                <c:when test="${itemR.valeur == '0'}"><span>${itemR.texte}</span></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                            </li>
                        </c:forEach> 
                    </ul>
                    <ul id="uneQuestion${item.id}" style="background-color:#eee; display:none;"><c:out value="${item.enonce}" />
                        <c:forEach var="itemR" items="${item.getReponses()}" varStatus="status">
                            <li>${itemR.texte}
                                <span id="_reponse${itemR.id}"></span>
                            </li>
                        </c:forEach> 
                    </ul>
                </li>
            </c:forEach>
            </ul>
        </div> 
        
        <!-- différents modes de jeu -->
        <c:choose>
            <c:when test="${mode == 'dirige'}">
                <c:if test="${numeroQuestion == '1'}">
                    <p>Avant de cliquer sur le lien ci-dessous, merci de demander à l'assistance de se connecter à la séquence comme participant et d'afficher la première question.
                        Un participant ne pourra voir que les questions qui seront lancées APRES sa connexion.</p>
                </c:if>
                <p>${retourTopic}</p>
                <br>
                <c:choose>
                    <c:when test="${fin == 'oui'}"><a href="UserServletTopic?id=${sequence.id}&numeroQuestion=${numeroQuestion}">Clôturer le sondage</a></c:when>
                    <c:when test="${fin == 'display'}">Résultats de la séquence :</c:when>
                    <c:otherwise><p><a href="UserServletTopic?id=${sequence.id}&numeroQuestion=${numeroQuestion}">Lancer la question n°${numeroQuestion}</a><br>(affiche la question courante pour tous les participants même s'ils n'ont pas répondu à la précédente)</p></c:otherwise>
                </c:choose>
            </c:when>
                    
            <c:when test="${mode == 'libre'}">
                <br>Merci de demander à l'assistance de se connecter à la séquence comme participant et de jouer le quiz librement. Les résultats s'afficheront ci-dessous.
                <br><br><a href="sequence?action=fermer&id=${sequence.id}">Clôturer le sondage</a>&nbsp;|&nbsp;<a id="colPart" href="#" onClick="javascript:colonneParticipants()">Masquer participants</a>&nbsp;|&nbsp;<a id="colSco" href="#" onClick="javascript:colonneScores()">Masquer scores</a>
            </c:when>
                
            <c:otherwise></c:otherwise>       
        </c:choose>

        <!-- résultats (pour les deux modes) -->
        <p id="resultats"></p>
        
    </c:if>

</div>

<c:import url="../page/footer.jsp"/>
</div>
</body>
</html>
