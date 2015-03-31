<%-- 
    Document   : menu_membre
    Created on : 13 déc. 2012, 14:50:49
    Author     : JYP
--%>

<span class="boiteDeMenu"><a class="lienDeMenu" href="index.jsp">Accueil</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="theme?action=lister">Thèmes</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="question?action=lister">Questions</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="sequence?action=lister">Séquences</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="membre?action=lister">Membres</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="connexion?action=deconnexion">Se déconnecter</a></span>
<span class="boiteDeMenu"><a class="lienDeMenu" href="contributeurs">Contributeurs</a></span>
&nbsp;<span class="tuto"><a href="#" onclick="afficheTutoriel()"><img src='./images/directQuiz-i-2.png' width="25px" heigth="27" alt="Tutoriel"></a></span>
<br><p class="infoConnexion">Vous êtes connecté : ${sessionScope.sessionUtilisateur.login}</p>
