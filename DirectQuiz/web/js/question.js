//jQuery.noConflict();
//var $j = jQuery;
//$j().ready(function() {


function ajouteReponse(){
    nbReponses++;
    var valueOut = '<c:out value="${question.reponse}"\/>';
    valueOut = '';
    document.getElementById("reponses").innerHTML += '<label for="reponse'+nbReponses+'">Réponse n°'+nbReponses+' <span class="requis">*</span></label>';
    document.getElementById("reponses").innerHTML += '<input type="text" id="reponse'+nbReponses+'" name="reponse'+nbReponses+'" value="'+valueOut+'" size="70" maxlength="150" />';
    document.getElementById("reponses").innerHTML += '<span class="erreur">${form.erreurs["reponse1"]}</span>';
    document.getElementById("reponses").innerHTML += '<br />';
}

//});