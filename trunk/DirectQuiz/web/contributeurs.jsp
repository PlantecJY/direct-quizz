<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
                        
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
        
        
        <title>DirectQuiz - Contributeurs</title>
        <c:import url="page/scripts.jsp"/>   
    </head>
    <body>

        <div id="enveloppe">

            <c:import url="page/header.jsp"/>
            <c:import url="page/test_connexion.jsp"/>
            <c:import url="page/help.jsp"/>
          
            <div id ="texte">
            <c:choose>
                    <c:when test="${!empty sessionScope.sessionUtilisateur}">
                        <script type="text/javascript">
                            var page = "contributeurs";
                        </script>
                    </c:when>
                </c:choose>

                <p>Plusieurs personnes ont contribué au projet DirectQuiz :</p>

                <ul>
                    <li><a href="http://fr.linkedin.com/pub/jean-yves-plantec/28/91b/a79/fr">Jean Yves Plantec</a>, créateur et coordinateur du projet</li>
                    <li><a href="https://www.linkedin.com/pub/salah-atmitim/aa/253/5b5">Salah Atmitim</a>, contributeur entre décembre 2014 et juin 2015</li>
                    <li><a href="https://fr.linkedin.com/pub/anthony-gourraud/b2/918/271">Gourraud Anthony</a>, contributeur entre décembre 2014 et juin 2015</li>
                    <li><a href="https://fr.linkedin.com/pub/arthur-grévin/b2/918/b1a">Grévin Arthur</a>, contributeur entre décembre 2014 et juin 2015</li>
                    <li><a href="https://fr.linkedin.com/pub/samih-lachegur/b1/57a/172">Samih Lachegur</a>, contributeur entre décembre 2014 et juin 2015</li>
                </ul>
                
            </div>

            <c:import url="page/footer.jsp"/>

        </div>

    </body>
</html>