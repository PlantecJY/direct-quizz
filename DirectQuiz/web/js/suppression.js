function confirmation(item, link1, id1, link2, id2){
    link2 = link2 || "";
    id2 = id2 || "";
    var answer = confirm ("Voulez vous supprimer "+item+" ?")
    if (answer)
    window.location=link1+id1+link2+id2
}
