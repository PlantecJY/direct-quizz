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
                    ${texteQuestion}
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
