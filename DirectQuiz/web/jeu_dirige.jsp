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
        <!--<script type="text/javascript" src="js/shadow.js"></script>-->
        <script type="text/javascript">
            function shadow(){
                document.getElementById("shadowing").style.display = "block";
                document.getElementById("soumis").style.display = "block";
                document.getElementById("bouton").disabled = false;
            }
        </script>
        <script type="text/javascript">
            var requete;

            function recuperer_question(id) {
               //var url = "SequenceJeuDirige?id=" +  id;
               var url = "SequenceJeuDirige?action=recuperer_question&id=" +  id;
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
                  // exploitation des données
                  var messageTag = requete.responseXML.getElementsByTagName("message")[0];
                  // texte du message
                  message = messageTag.childNodes[0].nodeValue;
                  if(message != ""){
                    // message non-vide
                    mdiv = document.getElementById("question");
                    mdiv.innerHTML = "<p>"+message+"</p>";
                    document.getElementById("shadowing").style.display = "none";
                  }
                }
              }
            }

            function maj() {
                var dt=new Date();
                document.getElementById("date_sequence").innerHTML = dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds();
                var id = '${identifiant}';
                recuperer_question(id);
            }

            var inter=setInterval("maj()", 2000);  
            
        </script>        
        <title>Page de question</title>
    </head>
    <body>
    <div id="enveloppe">
        <div id ="texte">        
        <c:import url="page/header.jsp"/>
            <div id="shadowing"></div>

            <p id="date_sequence">-</p>
            <form id="formJeu" method="post" action="SequenceJeuDirige?action=envoi_reponse">

                <input type="hidden" name="identifiant" value="${identifiant}"/>
                <input type="hidden" name="numeroQuestion" value="${numeroQuestion}"/>

                <!-- zone recevant la question : au départ, ombrage + message -->
                <div id="question">
                    Merci de patienter...
                    <!--script type="text/javascript">shadow()</script>-->
                </div>  

            </form>
            <div id="soumis">Demande envoyée. Merci de patienter.</div>
            <br>
            <a href="SequenceJeuDirige?action=finir_participer">Quitter le quiz</a>
        </div>
    <c:import url="page/footer.jsp"/>
    </div>
    </body>
</html>
