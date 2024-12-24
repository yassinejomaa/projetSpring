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
				}
			});
	  } else {
	    swal("Your imaginary file is safe!");
	  }
	});
	
	
	
	
}