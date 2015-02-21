<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="style/global.css" >
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
	<link rel="stylesheet" href="js/themes/base/jquery.ui.all.css">
<title>DirectQuiz</title>
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
                var nbQuestions = 0;
                var page = "accueil";
            </script>
        </c:when>
    </c:choose>

    <p>Lors d'une séance d'enseignement, d'une présentation, d'une conférence, il est assez utile pour l'enseignant, le présentateur ou le conférencier de savoir si les concepts présentés ont été correctement assimilés par l'auditoire.
        On conviendra que les réponses orales à la question « Est-ce que tout le monde a compris ? » ne sont en général pas exploitables.</p>

    <p>L'utilisation de boîtiers de votes électroniques constitue une solution à ce problème, mais leur mise en oeuvre est souvent coûteuse.</p>

    <p>DirectQuiz permet à un membre connecté de préparer des questions de type QCM ainsi qu'une séquence contenant une succession de questions. Ainsi,
        face à un public :</p>

    <ul>
        <li>l'animateur (membre connecté) lance (depuis un ordinateur ou un smartphone) une séquence de questions et communique oralement aux participants le code et le mot de passe de la séquence ;</li>
        <li>le participant (depuis un ordinateur ou un smartphone) s'identifie et indique un nickname ;</li>
        <li>dans le <span class="important">mode dirigé</span> (en cours de développement) c'est l'animateur qui déclenche le passage d'une question à l'autre et son affichage sur la page vue par le participant ; en <span class="important">mode libre</span>, le participant répond à son rythme ;</li>
        <li>pour les deux modes, l'animateur visualise l'ensemble des réponses obtenues.</li>
    </ul>
    <p class="align"><img id="imageAccueil" src="images/sequence_libre.png" alt="Image de séquence"></p>
    <p>Vous voulez essayer ? <a href="connexion?action=enregistrement&data=false">Enregistrez-vous</a> à la plateforme et attendez la validation par l'administrateur.</p>

</div>
    
<c:import url="page/footer.jsp"/>

</div>

</body>
</html>