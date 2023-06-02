(function($) {
	"use strict"; // Start of use strict
  
	  //Evento del botón que creara una nueva pelicula
	  $("#btn-insertar-cliente").click(function(){
		  
				  
		  $.ajax( {
			  
			  type: "GET",
			  url: '/Seccion10Grupo18/SaveClientServlet?nombreCliente=' + $('#txt-nombre-cliente').val() + '&apellidoCliente=' + $('#txt-apellido-cliente').val() + '&edadCliente=' + $('#txt-edad-cliente').val() ,
			  success: function(data) {
				  alert("Resultado: " + data);
			  }
		  } );
		  
		  
	  });
  
  })(jQuery); // End of use strict