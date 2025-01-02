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


function getCompte(page = 0, size = 5) {
    $.ajax({
        url: `/comptes/all-json?page=${page}&size=${size}`,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: function(response) {
            if (response && response.content && response.content.length > 0) {
                var tableContent = '';
                $.each(response.content, function(index, item) {
                    tableContent += `
                        <tr>
                            <td>${item.rib}</td>
                            <td>${item.client.nomComplet}</td>
                            <td>${item.solde}</td>
                            <td>
                                <button onclick="deleteCompte('${item.rib}')" class="button">
                                    <img src="/image/delete.png" class="image">
                                </button>
                            </td>
                            <td>
                                <button onclick="edit('${item.rib}')" data-bs-toggle="modal" data-bs-target="#ModaleUpdate" class="button">
                                    <img src="/image/pencil.png" class="image">
                                </button>
                            </td>
                        </tr>`;
                });

                $('#tbody').html(tableContent); // Mise à jour du tableau
                updatePagination(response); // Mise à jour de la pagination
            } else {
                $('#tbody').html('<tr><td colspan="5">Aucun compte trouvé.</td></tr>');
                $('#pagination').html(''); // Vider la pagination
            }
        },
        error: function() {
            alert('Impossible de lire les données');
        }
    });
}

function updatePagination(response) {
    var pagination = '';
    if (response.totalPages > 1) {
        for (let i = 0; i < response.totalPages; i++) {
            pagination += `
                <li>
                    <a class="${i === response.pageable.pageNumber ? 'btn btn-info ms-1' : 'btn btn-outline ms-1'}"
                        onclick="getCompte(${i})">${i + 1}</a>
                </li>`;
        }
    }
    $('#pagination').html(pagination);
    console.log("Pagination mise à jour");
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

function autocomplet() {
	$.ajax({
		url: '/comptes/all-json-autocomplete',
		type: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(response) {
			if (response && response.length > 0) {
				// Supprimer les doublons en utilisant l'attribut 'cin' comme clé unique
				const uniqueClients = [];
				const seenCINs = new Set();

				response.forEach(item => {
					if (!seenCINs.has(item.client.cin)) {
						seenCINs.add(item.client.cin);
						uniqueClients.push(item.client);
					}
				});

				// Transformer les données pour l'autocomplétion
				const transformedData = uniqueClients.map(client => ({
					label: client.nomComplet, // Utilise le nom comme label pour l'autocomplétion
					value: client.cin         // Utilise le CIN comme valeur de l'élément sélectionné
				}));

				// Initialiser l'autocomplétion avec les données transformées
				$('#txtRequestor').autocomplete({
					source: transformedData,
					select: function(event, ui) {
						if (ui.item) {
							console.log('Elément sélectionné :', ui.item.value);
							// Ajoutez ici toute action nécessaire après la sélection d'un élément
						}
					},
					change: function(event, ui) {
						if (!ui.item) {
							alert('Aucun élément sélectionné');
						}
					}
				});
			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
}

autocomplet();
