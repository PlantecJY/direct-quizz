
$(document).ready(function(){
    
    $( "#dialog" ).dialog({
        position:{
            my:'right top',
            at:'right top',
            of: "#texte"
        },
        autoOpen: false,
        width: 400,
        show: {         
            effect: "blind",
            duration: 500
        },
        hide: {
            //effect: "explode",
            //duration: 1000
        },
        open: function() { saveHelp("true"); },
        close: function() { saveHelp("false"); }
    });
    //alert("t="+nbItems);

});

function afficheTutoriel(){

    // page
    var texte = "";
    switch (page) { 
        case "accueil": 
            texte = "Ce petit tutoriel vous permet de découvrir DirectQuiz.<br>Cette fenêtre va vous accompagner tout au long du tutoriel. Dans chaque page, vous pouvez la déplacer à l'endroit de votre choix.<br>Pour commencer, cliquez sur le lien \"Thèmes\" du menu principal.";
            break; 
        case "liste_themes": 
            texte = "Cette page liste les thèmes.<br>Un thème est associé à une question et permet de les ranger pour les retrouver plus facilement.";
            if(nbItems == 0){
                texte = texte + "<br>Aucun thème n'est encore créé, nous vous proposons d'un créer un : cliquez sur le lien \"Ajouter thème\".";
            } else if (nbItems > 1) {
                texte = texte + "<br>"+nbItems+" thèmes ont été créés que vous pouvez supprimer ou éditer. Nous vous proposons de créer une question : cliquez sur le lien \"Questions\" du menu principal.";                
            } else if (nbItems == 1) {
                texte = texte + "<br>Un thème a été créé que vous pouvez supprimer ou éditer. Nous vous proposons à présent de créer une question : cliquez sur le lien \"Questions\" du menu principal.";                                
            }
            break; 
        case "formulaire_theme": 
            texte = "Cette page permet de créer un thème.<br>Entrez un nom de thème, puis cliquez sur \"Valider\". "; 
            break;
        case "liste_questions": 
            texte = "Cette page liste les questions.";
            if(nbItems == 0){
                texte = texte + "<br>Aucune question n'est encore créée, nous vous proposons d'un créer une : cliquez sur le lien \"Ajouter question\".";
            } else if (nbItems > 1) {
                texte = texte + "<br>"+nbItems+" questions ont été créées que vous pouvez supprimer, éditer ou dont vous pouvez voir le contenu. Nous vous proposons de créer une séquence : cliquez sur le lien \"Séquences\" du menu principal.";                
            } else if (nbItems == 1) {
                texte = texte + "<br>Une question a été créée que vous pouvez supprimer, éditer ou dont vous pouvez voir le contenu. Nous vous proposons à présent de créer une séquence : cliquez sur le lien \"Séquences\" du menu principal.";                                
            }
            break; 
        case "formulaire_question": 
            texte = "Cette page permet de créer une question.<br>Sélectionnez un thème, entrez le titre de la question et l'énoncé de la question.<br>Entrez un nombre de points pour cette question (cette information est enregistrée, mais n'est pas encore prise en compte).<br>Ajouter autant de réponses possibles que vous le souhaitez sans oublier de cocher celles qui sont justes. Vous pouvez supprimer une réponse si nécessaire en cliquant sur le bouton \"-\".<br>Enfin, cliquez sur \"Valider\".";
            texte = texte + "<br><br>Suggestions : <ul><li>Proposez des questions simples et ciblant un concept d'une façon univoque.</li><li>Si ce sont des questions à choix multiple, précisez-le dans l'énoncé.</li><ul>"; 
            break;
        case "liste_sequences": 
            texte = "Cette page liste les séquences.";
            if(nbItems == 0){
                texte = texte + "<br>Aucune séquence n'est encore créée, nous vous proposons d'un créer une : cliquez sur le lien \"Ajouter séquence\".";
            } else if (nbItems > 1) {
                texte = texte + "<br>"+nbItems+" séquences ont été créées que vous pouvez supprimer, éditer ou dont vous pouvez voir le contenu. Nous vous proposons de jouer une séquence : cliquez sur le lien \"Jouer en mode libre\" dans la ligne correspondant à la séquence choisie.";                
            } else if (nbItems == 1) {
                texte = texte + "<br>Une séquence a été créée que vous pouvez supprimer, éditer ou dont vous pouvez voir le contenu. Nous vous proposons à présent de jouer une séquence : cliquez sur le lien \"Jouer en mode libre\" dans la ligne correspondant à la séquence choisie.";                                
            }
            break; 
        case "formulaire_sequence": 
            texte = "Cette page permet de créer une séquence.<br>Entrer le code et le mot de passe de cette séquence (ce sont les identifiants que vous communiquerez plus tard à votre auditoire).<br>Dans la liste des questions disponibles, sélectionnez celles que vous voulez proposer dans cette séquence.<br>Enfin, cliquez sur \"Valider\".";
            break; 
        case "jouer_mode_libre": 
            texte = "Cette page permet de jouer une séquence.<br>Communiquez le code et le mot de passe de cette séquence à votre auditoire et demandez-leur d'ouvrir DirectQuiz à la rubrique \"Participer\", puis d'entrer ce code, ce mot de passe ainsi qu'un identifiant de leur choix (sans caractères accentués) et de répondre aux différentes questions.<br>Cette page devrait alors afficher leur progression.<br>A tout moment, mais en général seulement une fois que tout l'auditoire aura répondu, vous pouvez clôre la séquence de jeu et étudier les réponses, participant par participant.<br>Attention, ces données ne sont pas enregistrées par l'application (prochainement, vous pourrez les exporter pour post-traitement).";
            break; 
        case "contributeurs": 
            texte = "Cette page liste tous ceux qui ont participé au développement de DirectQuiz. <br> Vous avez la possibilité d'accéder au profil LinkedIn de chacun des contributeurs." ; 
            break;    
        case null: 
            texte = "Pas d'aide pour cette page.";
            break; 
    }   

    $( "#dialog" ).attr("title", "Questions...");
    $( "#dialog" ).html(texte);
    $( "#dialog" ).dialog( "open" );

}


function saveHelp(etat) {
    // servlet SaveHelpStatus qui sauvegarde l'état de l'aide
    var url = "SaveHelpStatus?etat="+etat;
    if (window.XMLHttpRequest) {
        requete = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        requete = new ActiveXObject("Microsoft.XMLHTTP");
    }
    requete.open("GET", url, true);
    requete.onreadystatechange = null;
    requete.send(null);
}


