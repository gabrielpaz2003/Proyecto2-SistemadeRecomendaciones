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

	  $("#btn-consultar-recomendaciones").click(function() {
        $.ajax({
            type: "GET",
            url: "/Seccion10Grupo18/SearchProductAndCategory?idCliente=" + $('#txt-id-cliente').val(),
            success: function(data) {
                console.log("Success");
                console.log("Recomendaciones: " + JSON.stringify(data));
                
                // Limpiar el contenido anterior del contenedor
                $("#lista-recomendaciones").empty();
                
                // Recorrer los resultados y generar el HTML
                $.each(data, function(i, recomendacion) {
                    var html = "<p><strong>Categoría:</strong> " + recomendacion.Categoria + "</p>";
                    html += "<p><strong>Productos:</strong> " + recomendacion.Productos.join(", ") + "</p>";
                    $("#lista-recomendaciones").append(html);
                });
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
            }
        });
    });
	
  
  })(jQuery); // End of use strict