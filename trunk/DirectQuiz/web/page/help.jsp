<%-- 
    Document   : help
    Created on : 4 juin 2013, 22:49:34
    Author     : iode
--%>

<div id="dialog" title="Aide"></div>

<%-- Vérification de la présence d'un objet en session --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:choose>
    <c:when test="${sessionScope.helpStatus == 'true'}">
        <script type="text/javascript">
            $(document).ready(function(){
                afficheTutoriel()
            });
        </script>
    </c:when>
</c:choose>
