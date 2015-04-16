<%-- 
    Document   : liste_sequences
    Created on : 18 déc. 2012, 15:23:35
    Author     : JYP
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <c:import url="../page/scripts.jsp"/>
        <title>Liste des séquences</title>
        <link type="text/css" rel="stylesheet" href="style/tables.css" />
        <link type="text/css" rel="stylesheet" href="style/sequence.css" />
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
        <script src="js/exportCSV.js"></script>
        <script type="text/javascript">
            // initialisation du nb de réponses
            function afficheReponses(id) {
                //alert('affiche '+id);
                document.getElementById("uneQuestion" + id).style.display = "block";
            }
            function cacheReponses(id) {
                //alert('cache '+id);
                document.getElementById("uneQuestion" + id).style.display = "none";
            }
        </script>
    </head>
    <body>
        <div id="enveloppe">

            <c:import url="../page/header.jsp"/>

            <c:import url="../page/menu_membre.jsp"/>

            <c:import url="../page/help.jsp"/>

            <div id ="texte">

                <p class="faux">${message}</p>

                <h2 class="titre">Liste des séquences</h2>

                <a href="sequence?action=ajouter&data=false">Ajouter séquence</a><br><br>

                <script type="text/javascript">
                    var nbItems = 0;
                    var page = "liste_sequences";
                </script>

                <table class="defaut">
                    <tr>
                        <th>Code</th>
                        <th>Mot de passe</th>        
                        <th colspan="5" align="left">Actions</th>        
                    </tr>
                    <c:forEach var="item" items="${listeSequences}" >
                        <tr>
                            <td><c:out value="${item.code}" /></td>
                            <td><c:out value="${item.motDePasse}" /></td>
                            <td><a href="#" onclick="confirmation('cette séquence', 'sequence?action=supprimer&id=',${item.id});">Supprimer</a></td>
                            <td><a href="sequence?action=editer&id=${item.id}">éditer</a></td>
                            <td><a href="sequence?action=voir&id=${item.id}">voir</a></td>
                            <td>jouer en mode dirigé</td>
                            <td><a href="sequence?action=jouer&id=${item.id}&mode=libre">jouer en mode libre</a></td>
                        <tr>
                        <script type="text/javascript">
                            nbItems++;
                        </script>
                    </c:forEach>
                </table>
                <br>
                <c:if test="${!empty sequence}">
                    <div id="uneSequence">
                        <p>Code : ${sequence.code}</p>
                        <p>Mot de passe : ${sequence.motDePasse}</p>

                        <ul>
                            <c:forEach var="item" items="${sequence.getQuestions()}" >
                                <li><c:out value="${item.titre}" /> - <c:out value="${item.enonce}" /> <a href="#" onmouseover="javascript:afficheReponses('${item.id}')" onmouseout="javascript:cacheReponses('${item.id}')">Réponses</a>
                                    <ul id="uneQuestion${item.id}" style="background-color:#eee; display:none;">
                                        <c:forEach var="itemR" items="${item.getReponses()}" varStatus="status">
                                            <c:choose>
                                                <c:when test="${itemR.valeur == '1'}">
                                                    <li>${itemR.texte} (x)</li>
                                                    </c:when>
                                                    <c:otherwise>
                                                    <li>${itemR.texte}</li>
                                                    </c:otherwise>
                                                </c:choose>        
                                            </c:forEach> 
                                    </ul>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <iframe id="myFrame" style="display:none"></iframe>

                    <!-- résultats de la séquence -->
                    <br> 
                    <p> 
                        <i>  <u>Résultats de la séquence :</u>  </i>
                                ${texteReponses3}      
                        <c:if test="${!empty listeParticipants}">   
                            <a href="#" onclick="csvExport('#tabResultats', 'Résultats Séquence ${sequence.code}');" id="btnExport" >Export des résultats en CSV</a>  
                        </c:if>
                    </p>           

                </c:if>


            </div>

            <c:import url="../page/footer.jsp"/>
        </div>
    </body>
</html>
