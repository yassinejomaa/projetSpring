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


function getClient() {
	$.ajax({
		url: '/clients/all-json',
		type: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		success: function(response) {
			if (response != null && response != undefined && response.length > 0) {
				var object = '';
				$.each(response, function(index, item) {

					object += '<tr>'
					object += '<td>' + item.cin + '</td>';
					object += '<td>' + item.nom + '</td>';
					object += '<td>' + item.prenom + '</td>';
					object += '<td>';

					object += '<button onclick="deleteClient(' + item.cin + ')" class="button"><img src="/image/delete.png" class="image"></button>';
					object += '</td>';
					object += '<td>';
					object += '<button onclick="edit(' + item.cin + ')" data-bs-toggle="modal" data-bs-target="#ModaleUpdate" class="button"><img src="/image/pencil.png" class="image"></button> ';
					object += '</td>';


					object += '</tr>';
				});

				$('#tbody').html(object);
			}
		},
		error: function() {
			alert('Impossible de lire les données');
		}
	});
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
		url: '/clients/saveModale',
		data: client,
		type: 'post',
		success: function(reponse) {
			if (reponse == null || reponse == undefined || reponse.length == 0) {
				alert('Il y a une erreur');
			} else {
				
				getClient();
				
			}
		},
		error: function() {
			alert('Il y a une erreur');
		}
	});
}

