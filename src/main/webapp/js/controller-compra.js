(function($) {
	"use strict"; // Start of use strict
  
	  //Evento del botón que creara una nueva pelicula
	  $("#btn-insertar-producto").click(function(){
		  
				  
		  $.ajax( {
			  
			  type: "GET",
			  url: '/Seccion10Grupo18/SaveProductServlet?nombreProducto=' + $('#txt-nombre-producto').val() + '&precioProducto=' + $('#txt-precio-producto').val() + '&descripcionProducto=' + $('#txt-descripcion-producto').val() + '&categoriaProducto=' + $('#sel-categoria-producto').val() ,
			  success: function(data) {
				  alert("Resultado: " + data);
			  }
		  } );
		  
		  
	  });


	  //Evento del botón que creara una nueva pelicula
	  $("#btn-comprar-producto").click(function(){
		  
				  
		$.ajax( {
			
			type: "GET",
			url: '/Seccion10Grupo18/BuyByClientServlet?nombreProducto=' + $('#txt-nombre-producto-compra').val() + '&idCliente=' + $('#txt-id-cliente-compra').val() ,
			success: function(data) {
				alert("Resultado: " + data);
			}
		} );
		
		
	});

	  
  
  })(jQuery); // End of use strict