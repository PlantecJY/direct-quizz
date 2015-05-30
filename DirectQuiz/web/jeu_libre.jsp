<%-- 
    Document   : admin
    Created on : 09 mar. 2013
    Author     : JYP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <c:import url="page/scripts.jsp"/>
        <link type="text/css" rel="stylesheet" href="style/shadow.css" />
        <title>Page de question</title>
    </head>
    <body>
    <div id="enveloppe">
        <c:import url="page/header.jsp"/>
        <div id ="texte">
            <form id="formJeu" method="post" action="SequenceJeuLibre?action=recuperer_question_et_envoi">

                <input type="hidden" name="identifiant" value="${identifiant}"/>
                <input type="hidden" name="idSequence" value="${idSequence}"/>
                <input type="hidden" name="numeroQuestion" value="${numeroQuestion}"/>

                <!-- zone recevant la question -->
                <div id="question">
                 
                    <c:if test="${testquestion}">
                        <p>
                            <input type="hidden" name="idQuestion" value="${question.id}"/>
                            <h2 class="titre"> Question n°${numeroQuestion-1}</h2>
                            <br><b>${question.titre}</b><br><table><tr><td> ${question.enonce}</td></tr></table>
                            <c:if test="${testimage}">
                            <br><img src='./img/${question.imageRealName}' width='200px'><br>
                            </c:if>
                             <ul>
                                <c:forEach items ="${question.reponses}" var="i">
                                <li class="reponse">
                                    <table>
                                        <tr>
                                            <td>
                                                <input type="checkbox" class="checkq" name="valeurs" id="${i.id}" value="${i.id}"/>
                                            </td>
                                            <td>
                                                <span id="${i.id}" onclick="document.getElementById('${i.id}').checked=!document.getElementById('${i.id}').checked;" class="checkboxtext">${i.texte}</span>
                                            </td>
                                        </tr>
                                    
                                    </table>
                                </li>
                                </c:forEach> 
                            </ul>
                            <br><input id="bouton" type="submit" value="Soumettre"/></p>
                
                        </p>
                    </c:if>
                    <c:if test="${not testquestion}" >
                        <p>Merci de vous être prêtés à ce sondage !</p>
                    </c:if>
                    
                    
                </div>  

            </form>
            <div id="soumis">Demande envoyée. Merci de patienter.</div>
            <br>
            <a href="SequenceJeuLibre?action=finir_participer">Quitter le quiz</a>
        </div>
    <c:import url="page/footer.jsp"/>
    </div>
    </body>
</html>
