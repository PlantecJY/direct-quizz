function csvExport(idTable, titre) {
    
    // gestion date
    var now = new Date();
    var annee = now.getFullYear();
    var mois = now.getMonth() + 1;
    var jour = now.getDate();
    var heure = now.getHours();
    var minute = now.getMinutes();
    var seconde = now.getSeconds();
    
    var titrefichier = titre + " (" + jour + "-" + mois + "-" + annee + ")" + ".csv";
    
    // obtenir la table en fonction de l'id
    var table = $(idTable)[0];

    // obtenir le nb de colomnes et de lignes
    var rowLength = table.rows.length;
    var colLength = table.rows[0].cells.length;

    // la table en chaine de caractères
    var tableString = "";

    // En-tête 
    for (var i = 0; i < colLength; i++) {
        tableString += table.rows[0].cells[i].innerHTML.split(",").join("") + ",";
    }

    tableString = tableString.substring(0, tableString.length - 1);
    tableString += "\r\n";

    // Données du tableau
    for (var j = 1; j < rowLength - 1; j++) {
        for (var k = 0; k < colLength; k++) {
            tableString += table.rows[j].cells[k].innerHTML.split(",").join("") + ",";
        }
        tableString += "\r\n";
    }
    tableString = 'sep=,\r\n' + tableString;
    
    // détecter IE
    var iev=0;
     var ieold = (/MSIE (\d+\.\d+);/.test(navigator.userAgent));
    var trident = !!navigator.userAgent.match(/Trident\/7.0/);    
    var rv=navigator.userAgent.indexOf("rv:11.0");  
    if (ieold) iev=new Number(RegExp.$1);
    if (navigator.appVersion.indexOf("MSIE 10") != -1) iev=10;
    if (trident&&rv!=-1) iev=11;
    
    //Sauvegarder en CSV    
    if (iev != 0) { 
    // internet explorer détecté        
        myFrame.document.open("text/html", "replace");
        myFrame.document.write(tableString);
        myFrame.document.close();
        myFrame.focus();
        myFrame.document.execCommand('SaveAs', true, titrefichier);
    } 
    else {         
        csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(tableString);
        $(event.target).attr({
            'href': csvData,
            'target': '_blank',
            'download': titrefichier
        });
    }
}
