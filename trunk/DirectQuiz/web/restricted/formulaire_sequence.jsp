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
        <c:import url="../page/scripts.jsp"/>
        <link type="text/css" rel="stylesheet" href="style/sequence.css" />
        
        <script type="text/javascript" src="js/jquery.js"></script>
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
        <script src="js/ui/jquery"></script>
        <script type="text/javascript">
            // modification des questions : coche si dans la séquence
            function modifieQuestion(idQ) {
                document.getElementById("check" + idQ).checked = true;
            }

            // rechargement avec nouvau thème
            function rechargeAvecTheme(ref) {
                var form = ref;
                // thème sélectionné
                var selectedIndex = document.getElementById("theme").selectedIndex;
                // le nom du thème choisi
                var themeChoisi = document.getElementById("theme").options[selectedIndex].innerHTML;
                while (form.parentNode && form.nodeName.toLowerCase() != 'form') {
                    form = form.parentNode;
                }
                var elements = form.getElementsByTagName('input');
                for (var i = 0; i < elements.length; i++) {
                    if (elements[i].type == 'checkbox') {
                        if (elements[i].name == 'cochertout') {
                            elements[i].checked = false;
                            } else {
                                if ((elements[i].nextElementSibling.innerHTML == themeChoisi) || (themeChoisi == "tous...")) {
                                    //le carré checkbox 
                                    elements[i].style.display = 'inline';
                                //le thème 
                                elements[i].nextElementSibling.style.display = 'inline';
                                //le reste de la ligne après le thème
                                elements[i].nextElementSibling.nextElementSibling.style.display = 'inline';
                            }
                            else {
                                //le carré checkbox 
                                elements[i].style.display = 'none';
                                //le thème
                                elements[i].nextElementSibling.style.display = 'none';
                                //le reste de la ligne après le thème
                                elements[i].nextElementSibling.nextElementSibling.style.display = 'none';
                            }
                        }
                    }
                }
            }
            //fonction qui coche tous les éléments checkbox
            function CocheTout(ref) {
                var form = ref;
                while (form.parentNode && form.nodeName.toLowerCase() != 'form') {
                    form = form.parentNode;
                }

                var elements = form.getElementsByTagName('input');
                for (var i = 0; i < elements.length; i++) {
                    // name = valeurs pour distinguer des autres checkbox non concérnées par ce traitement
                    if (elements[i].type == 'checkbox' && elements[i].name == 'valeurs' && (elements[i].style.display != 'none')) {
                        elements[i].checked = ref.checked;
                    }
                }
            }
        </script>
        <c:choose>
            <c:when test="${action} == 'ajouter'">
                <title>Création de séquence</title>
            </c:when>
            <c:otherwise>
                <title>Edition de séquence</title>
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
                        <h2 class="titre">Création de séquence</h2>
                    </c:when>
                    <c:otherwise>
                        <h2 class="titre">Edition de séquence</h2>
                    </c:otherwise>
                </c:choose>

                <script type="text/javascript">
                    var nbItems = 0;
                    var page = "formulaire_sequence";</script>

                <form method="post" action="sequence">


                    <input type="hidden" name="action" value="<c:out value="${action}"/>"/>
                    <input type="hidden" name="id" value="<c:out value="${id}"/>"/>
                    <input type="hidden" name="data" value="true"/>

                    <!-- Code -->
                    <label for="code">Code <span class="requis">*</span></label>
                    <input type="text" id="code" name="code" size="20" maxlength="50" value="<c:out value="${sequence.code}"/>"/>
                    <span class="erreur">${form.erreurs['code']}</span>
                    <br />

                    <!-- Mot de passe -->
                    <label for="motDePasse">Mot de passe <span class="requis">*</span></label>
                    <input type="text" id="motDePasse" name="motDePasse" size="20" maxlength="50" value="<c:out value="${sequence.motDePasse}"/>"/>
                    <span class="erreur">${form.erreurs['motDePasse']}</span>
                    <br />

                    <!-- div pour les questions -->
                    <p>Questions</p>
                    <!-- Thème -->
                    <input type='hidden' name='idTheme' value="<c:out value="${idTheme}"/>"/> 
                    <label for="theme">Thème</label>
                    <select id="theme" onchange="javascript:rechargeAvecTheme(this)">
                        <option value="0">tous...</option>
                        <c:forEach var="item" items="${listeThemes}" >
                            <option value="<c:out value="${item.id}"/>" id="sel<c:out value="${item.id}"/>">${item.getTitre()}</option>
                        </c:forEach>
                    </select>
                    <!-- on liste toutes les questions disponibles ; si la question est dans sequence.getQuestions() alors on la coche -->
                    <c:if test="${empty listeQuestions}">Aucune question disponible</c:if>
                    <span class="erreur">${form.erreurs['questions']}</span>
                    <div id="question" name="reponses">
                        <ul class="listeSansPuces">
                            Tout cocher/décocher : <input name="cochertout" onclick="CocheTout(this, 'valeurs');" type="checkbox"><br/><br/>
                            <c:forEach var="item" items="${listeQuestions}" >

                                <li>
                                    <input type="checkbox" id="check<c:out value="${item.id}" />" name="valeurs" value="<c:out value="${item.id}" />"/>
                                    <span class="themeQuestion"><c:out value="${item.getTheme().titre}" /></span><span> - <c:out value="${item.titre}" /> - <c:out value="${item.enonce}" /></span>
                                </li>
                            </c:forEach>
                        </ul>
                        <!-- on coche les questions sélectionnées -->
                        <c:forEach var="item2" items="${sequence.getQuestions()}">
                            <script type="text/javascript">
                                var idQuestionDeSequence = '${item2.id}';
                                modifieQuestion(idQuestionDeSequence);
                            </script>
                        </c:forEach> 

                    </div>            

                    <input type="submit" value="Valider" class="sansLabel" />
                    <br />

                    <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                </form>
            </div>

            <c:import url="../page/footer.jsp"/>
        </div>
    </body>
</html>
