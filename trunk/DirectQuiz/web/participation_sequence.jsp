<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="style/global.css" />
    <link type="text/css" rel="stylesheet" href="style/shadow.css" />
    <title>DirectSondage</title>
    <script type="text/javascript">
        function shadow(){
            document.getElementById("shadowing").style.display = "block";
            document.getElementById("soumis").style.display = "block";
            document.getElementById("bouton").disabled = false;
        }
    </script>
</head>
<body>

<div id="enveloppe">

<c:import url="page/header.jsp"/>
<div id="shadowing"></div>
        
<div id ="texte">
    <p>Identifiant : ${identifiant} - mode : ${mode}</p>
    
    <c:choose>
        <c:when test="${mode == 'dirige'}">
            <a href="SequenceJeuDirige?action=participer&identifiant=${identifiant}">Cliquer pour afficher la première question.</a>
        </c:when>
        <c:when test="${mode == 'libre'}">
            <a href="SequenceJeuLibre?action=recuperer_question_et_envoi&numeroQuestion=1&identifiant=${identifiant}&idSequence=${idSequence}">Cliquer pour afficher la première question.</a>
        </c:when>
        <c:otherwise>Mode non défini</c:otherwise>
    </c:choose>
    <br>
    <br>
    <a href="sequence?action=quitter">Quitter la séquence</a>
</div>

<c:import url="page/footer.jsp"/>

</div>

</body>
</html>