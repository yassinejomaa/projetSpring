function insert() {
	console.log("hello");
	event.preventDefault();

	var cin = $('#cin').val();
	var nom = $('#nom').val();
	var prenom = $('#prenom').val();


	var client = {
		cin: cin,
		nom: nom,
		prenom: prenom,

	};

	$.ajax({
		url: '/clients/saveModale',
		data: client,
		type: 'post',
		success: function(reponse) {
			if (reponse == null || reponse == undefined || reponse.length == 0) {
				alert('Il y a une erreur');
				console.log("erreur");
			} else {
				console.log(reponse);

				alert(reponse);
				console.log("success");
				getClient();

			}
		},
		error: function() {
			alert('Il y a une erreur');
			console.log("erreur1");
		}
	});
}


function getClient(page = 0, size = 1) {
	$.ajax({
		url: `/clients/all-json?page=${page}&size=${size}`,
		type: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(response) {
			if (response && response.content && response.content.length > 0) {
				var object = '';
				$.each(response.content, function(index, item) {
					object += '<tr>';
					object += '<td>' + item.cin + '</td>';
					object += '<td>' + item.nom + '</td>';
					object += '<td>' + item.prenom + '</td>';
					object += '<td>';
					object += '<button onclick="deleteClient(' + item.cin + ')" class="button"><img src="/image/delete.png" class="image"></button>';
					object += '</td>';
					object += '<td>';
					object += '<button onclick="edit(' + item.cin + ')" data-bs-toggle="modal" data-bs-target="#ModaleUpdate" class="button"><img src="/image/pencil.png" class="image"></button>';
					object += '</td>';
					object += '</tr>';
				});

				$('#tbody').html(object);

				// Met à jour la pagination
				updatePagination(response);
			} else {
				$('#tbody').html('<tr><td colspan="5">Aucun client trouvé.</td></tr>');
			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
}
function updatePagination(response) {
	var paginationHtml = '';
	if (response.totalPages > 1) {
		for (var i = 0; i < response.totalPages; i++) {
			paginationHtml += `<button onclick="getClient(${i})" class="pagination-button">${i + 1}</button>`;
		}
	}
	$('#pagination').html(paginationHtml);
}

function deleteClient(cin) {

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
					url: "/clients/delete-ajax",
					type: "POST",
					data: { cin: cin },
					success: function() {
						swal("Poof! Your imaginary file has been deleted!", {
							icon: "success",
						});
						$("#" + cin).remove();
						getClient();

					}
				});
			} else {
				swal("Your imaginary file is safe!");
			}
		});




}


function edit(cin) {
	$.ajax({
		url: '/clients/editModal?cin=' + cin,
		type: 'get',
		contentType: 'application/json;charset=utf-8',
		dataType: 'json',
		success: function(response) {
			if (response == null || response == undefined) {
				alert('Impossible de lire les données');
			} else if (response.length == 0) {
				alert('Données non disponibles avec l\'ID ' + id);
			} else {
				$('#cinUpdate').val(response.cin);
				$('#nomUpdate').val(response.nom);
				$('#prenomUpdate').val(response.prenom);




			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
}


function updateClient() {
	event.preventDefault();

	var cin = $('#cinUpdate').val();
	var nom = $('#nomUpdate').val();
	var prenom = $('#prenomUpdate').val();

	var client = {
		cin: cin,
		nom: nom,
		prenom: prenom,
	};

	$.ajax({
		url: '/clients/update/' + cin,
		contentType: 'application/json',
		data: JSON.stringify(client),
		type: 'PUT',// or form data
		// make sure to set this properly

		success: function(reponse) {
			if (reponse == null || reponse == undefined || reponse.length == 0) {
				alert('Il y a une erreur');
			} else {
				getClient();  // Actualisation de la liste des clients
			}
		},
		error: function() {
			alert('Il y a une erreur');
		}
	});
}


function autocomplet() {
	$.ajax({
		url: '/clients/all-json',
		type: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(response) {
			if (response && response.length > 0) {
				// Transformer les données pour l'autocomplétion
				const transformedData = response.map(item => ({
					label: item.nom, // Utilise la valeur de "nom" comme label pour l'autocomplétion
					value: item.nom   // Utilise la valeur de "nom" pour la valeur de l'élément sélectionné
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

				console.log(response);  // Affiche la réponse du serveur dans la console
			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
}


autocomplet()

