function deleteCompte(rib) {
	
	swal({
	  title: "Are you sure?",
	  text: "Once deleted, you will not be able to recover this imaginary file!",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
		$.ajax({
				url: "/comptes/delete-ajax",
				type: "POST",
				data: { rib: rib },
				success: function() {
					swal("Poof! Your imaginary file has been deleted!", {
					  icon: "success",
					});
					$("#" + rib).remove();
					alert("success");
					getCompte();
				}
			});
	  } else {
	    swal("Your imaginary file is safe!");
	  }
	});
	
	
	
	
}
function insert() {
    console.log("Insertion d'un compte...");
    event.preventDefault();

    // Récupérer les valeurs du formulaire
    var clientCin = $('#client1').val(); // Récupère le CIN sélectionné dans le formulaire
    var solde = $('#solde').val(); // Récupère le solde

    // Construire l'objet JSON à envoyer
    var compte = {
        clientCin: clientCin, // Cin du client sélectionné
        solde: solde          // Solde du compte
    };

    // Envoi de la requête AJAX
    $.ajax({
        url: '/comptes/saveModale', // URL du contrôleur backend
        contentType: 'application/json', // Type de contenu JSON
        data: JSON.stringify(compte), // Convertir l'objet en JSON
        type: 'post',
        success: function(reponse) {
            if (!reponse || reponse.length === 0) {
                alert('Erreur lors de l\'ajout du compte.');
                console.log("Erreur côté backend");
            } else {
                console.log("Réponse :", reponse);
                console.log("Compte ajouté avec succès !");
                getCompte(); // Recharge la liste des comptes
                $('#Modaleadd').modal('hide'); // Ferme le modal
				
            }
        },
        error: function() {
            alert('Erreur lors de l\'ajout du compte.');
            console.log("Erreur côté JavaScript");
        }
    });
}


function getCompte() {
    $.ajax({
        url: '/comptes/all-json',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
            if (response != null && response != undefined && response.length > 0) {
                var object = '';
                $.each(response, function(index, item) {
                    object += '<tr>';
					object += '<td>' + item.rib + '</td>'; 
                    object += '<td>' + item.client.nomComplet + '</td>'; // Correction ici
                    object += '<td>' + item.solde + '</td>';
                    object += '<td>';
                    object += '<button onclick="deleteCompte(' + item.rib + ')" class="button"><img src="/image/delete.png" class="image"></button>';
                    object += '</td>';
                    object += '<td>';
                    object += '<button onclick="edit(' + item.rib + ')" data-bs-toggle="modal" data-bs-target="#ModaleUpdate" class="button"><img src="/image/pencil.png" class="image"></button>';
                    object += '</td>';
                    object += '</tr>';
					console.log(item);
                });

                $('#tbody').html(object);
				 
            }
        },
        error: function() {
            alert('Impossible de lire les données');
        }
    });
}



function edit(rib) {
	$.ajax({
		url: '/comptes/editModal?rib=' + rib,
		type: 'get',
		contentType: 'application/json;charset=utf-8',
		dataType: 'json',
		success: function(response) {
			if (response == null || response == undefined) {
				alert('Impossible de lire les données');
			} else if (response.length == 0) {
				alert('Données non disponibles avec l\'ID ' + id);
			} else {
				$('#ribUpdate').val(response.rib);
				$('#soldeUpdate').val(response.solde);
				$('#client').val(response.client.cin);
				



			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
}


function updateCompte() {
    event.preventDefault();

    // Récupérer les valeurs du formulaire
    var rib = $('#ribUpdate').val();
    var solde = $('#soldeUpdate').val();
	var  clientCin=$('#client').val();

    // Construire l'objet JSON à envoyer
    var compte = {
		rib:rib,
        solde: solde,
		clientCin:clientCin
		
    };

    // Envoi de la requête AJAX (PUT)
    $.ajax({
        url: '/comptes/updateModale/' + rib ,// URL avec le RIB
        contentType: 'application/json',
        data: JSON.stringify(compte),
        type: 'PUT',
        success: function(reponse) {
			getCompte();
            console.log("Compte mis à jour avec succès !");
			console.log(compte)
            // ...
        },
        error: function() {
            // Gestion des erreurs
            console.error("Erreur lors de la mise à jour du compte.");
        }
    });
}
