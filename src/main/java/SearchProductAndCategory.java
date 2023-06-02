import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dataAccessLayer.EmbeddedNeo4j;

public class SearchProductAndCategory extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SearchProductAndCategory() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String idCliente = request.getParameter("idCliente");
        JSONArray matchesJson = new JSONArray();

        try (EmbeddedNeo4j dataBase = new EmbeddedNeo4j("neo4j+s://40c6bfa9.databases.neo4j.io", "neo4j",
                "tojG1xseDQwqu_DVyu9g1VAzGfM5COUKbLn9hs2vBIs")) {
            List<Map<String, Object>> recomendaciones = dataBase.recomendaciones(idCliente);
            for (Map<String, Object> recomendacion : recomendaciones) {
                JSONObject recomendacionJson = new JSONObject();
                recomendacionJson.put("Categoria", recomendacion.get("Categoria"));
                recomendacionJson.put("Productos", recomendacion.get("Productos"));
                matchesJson.add(recomendacionJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Escribir la respuesta JSON en la salida
        out.println(matchesJson);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
