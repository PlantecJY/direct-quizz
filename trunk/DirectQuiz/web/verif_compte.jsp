<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <c:import url="page/scripts.jsp"/>
        <title>Inscription</title>
    </head>
    <body>
    <div id="enveloppe">
    	<c:import url="page/header.jsp"/>
	<c:import url="page/menu.jsp"/>
        
        <div id ="texte">

            <p>${txt} </p>
        </div>
        
        <c:import url="page/footer.jsp"/>
    </div>    
    </body>
</html>
