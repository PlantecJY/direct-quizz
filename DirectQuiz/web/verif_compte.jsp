<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Inscription</title>
        <link type="text/css" rel="stylesheet" href="style/forms.css" />
        <link type="text/css" rel="stylesheet" href="style/global.css" />
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
