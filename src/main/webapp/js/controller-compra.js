(function($) {
  "use strict"; // Start of use strict

  //Evento del botón que agregara un nuevo producto
	$("#btn-insertar-producto").click(function(){
				
		$.ajax( {
			
			type: "GET",
			url: '/Seccion10Grupo18/SaveProductServlet?nombreProducto=' + $('#txt-nombre-producto').val() + '&precioProducto=' + $('#txt-precio-producto').val() + '&descripcionProducto=' + $('#txt-descripcion-producto').val() ,
			success: function(data) {
			    alert("Resultado: " + data);

				var nombreProducto = document.getElementById("txt-nombre-producto").value;
				console.log("Nombre del producto:", nombreProducto);
			}
		} );
		
		
	});

})(jQuery); // End of use strict

/*
// Función para obtener los valores de los campos de entrada y mostrarlos en la consola
function obtenerValoresProducto() {
	var nombreProducto = document.getElementById("txt-nombre-producto").value;
	var precioProducto = document.getElementById("txt-precio-producto").value;
	var descripcionProducto = document.getElementById("txt-descripcion-producto").value;
	
	console.log("Nombre del producto:", nombreProducto);
	console.log("Precio del producto:", precioProducto);
	console.log("Descripción del producto:", descripcionProducto);
  }
  
  // Agregar el evento de clic al botón
  document.getElementById("btn-insertar-producto").addEventListener("click", obtenerValoresProducto);
  */

