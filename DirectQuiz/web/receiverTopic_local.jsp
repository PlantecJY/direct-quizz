<%-- 
    Document   : admin
    Created on : 22 déc. 2012, 16:17:40
    Author     : JYP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="style/global.css" />
        <link type="text/css" rel="stylesheet" href="style/shadow.css" />
        <!--<script type="text/javascript" src="js/shadow.js"></script>-->
        <script type="text/javascript">
            function shadow(){
                document.getElementById("shadowing").style.display = "block";
                document.getElementById("soumis").style.display = "block";
                document.getElementById("bouton").disabled = false;            }
        </script>
        <title>Page de question</title>
    </head>
    <body>
        <c:import url="page/header.jsp"/>
        <div id="shadowing"></div>
        
        <form method="post" action="ReceiverServletTopic">
            <input type="hidden" name="identifiant" value="${identifiant}"/>
            <input type="hidden" name="numeroQuestion" value="${numeroQuestion}"/>
            <input type="hidden" name="idQuestion" value="${idQuestion}"/>
            <p>${retourTopic}</p>
        </form>
        <div id="soumis">Demande envoyée. Merci de patienter.</div>
        
        
        <!-- test -->
        <form method="post" action="ReceiverServletTopic">
            <input type="hidden" name="identifiant" value="${identifiant}"/>
            <input type="hidden" name="numeroQuestion" value="4"/>
            <input type="hidden" name="idQuestion" value="40"/>
            
            <h2 class="titre">Question n°1</h2>
            <br>Yalta
            <br>Quelle année ?
            <ul>
                <li><input type="checkbox" name="valeurs" value="133"/>1945</li>
                <li><input type="checkbox" name="valeurs" value="134"/>juste</li>
                <li><input type="checkbox" name="valeurs" value="135"/>juste</li>
            </ul>
            <input id="bouton" type="submit" value="SoumettreTest" onclick="shadow()"/>;
        </form>
            
        <form method="post" action="ReceiverServletTopic">
            <input type="hidden" name="identifiant" value="${identifiant}"/>
            <input type="hidden" name="numeroQuestion" value="5"/>
            <input type="hidden" name="idQuestion" value="38"/>  
            
            <h2 class="titre">Question n°2</h2>
            <br>Est-ce que ?
            <br>...
            <ul>
                <li><input type="checkbox" name="valeurs" value="128"/>1948</li>
                <li><input type="checkbox" name="valeurs" value="129"/>1949</li>
                <li><input type="checkbox" name="valeurs" value="130"/>juste</li>
            </ul>
            <input id="bouton" type="submit" value="SoumettreTest" onclick="shadow()"/>;
        </form>
        
    <c:import url="page/footer.jsp"/>
    </body>
</html>
