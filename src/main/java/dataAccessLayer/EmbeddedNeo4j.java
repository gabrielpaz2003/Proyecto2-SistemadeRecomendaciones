package dataAccessLayer;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.summary.ResultSummary;

import static org.neo4j.driver.Values.parameters;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedNeo4j implements AutoCloseable {

    private final Driver driver;

    public EmbeddedNeo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public String insertProduct(String nombreProducto, int precioProducto, String descripcionProducto, String categoriaProducto) {
        try (Session session = driver.session()) {
            CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run("MERGE (p:Producto {nombreProducto: '"+nombreProducto+"', precioProducto: '"+precioProducto+"', descripcionProducto: '"+descripcionProducto+"' , categoriaProducto: '"+categoriaProducto+"'})");
                    tx.commit();
                    return "OK";
                }
            });
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String productoPerteneceA(String nombreProducto, String categoriaProducto) {
    	try (Session session = driver.session()) {
            String result = session.writeTransaction( new TransactionWork<String>(){
                @Override
                public String execute( Transaction tx ){
                    tx.run("MATCH (p:Producto {nombreProducto: '"+nombreProducto+"'}), (c:Category {name: '"+categoriaProducto+"'}) MERGE (p)-[:PERTENECE_A]->(c)");
                    return "OK";
                }
            }
   		 );
            return result;
        } catch (Exception e) {
        	return e.getMessage();
        }
    }

    public String compra(String nombreProducto, String idCliente) {
    	try (Session session = driver.session()) {
            String result = session.writeTransaction( new TransactionWork<String>(){
                @Override
                public String execute( Transaction tx ){
                    tx.run("MATCH (u:User {idCliente: '"+idCliente+"'}), (p:Producto {nombreProducto: '"+nombreProducto+"'}) MERGE (u)-[:COMPRA]->(p)");
                    return "OK";
                }
            }
   		 );
            return result;
        } catch (Exception e) {
        	return e.getMessage();
        }
    }



    public String insertClient(String nombreCliente, String apellidoCliente, int edadCliente ,String idCliente) {
        try (Session session = driver.session()) {
            CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run("MERGE (u:User {nombreCliente: '"+nombreCliente+"', apellidoCliente: '"+apellidoCliente+"', edadCliente: '"+edadCliente+"', idCliente: '"+idCliente+"'})");
                    tx.commit();
                    return "OK";
                }
            });
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Map<String, Object>> recomendaciones(String idCliente) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> results = session.readTransaction(new TransactionWork<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> execute(Transaction tx) {
                    Result result = tx.run(
                        "MATCH (u:User {idCliente: $idCliente})-[:COMPRA]->(:Producto)-[:PERTENECE_A]->(c:Category),"
                        + "(c)-[:PERTENECE_A]->(p:Producto) "
                        + "WHERE NOT EXISTS((u)-[:COMPRA]->(p)) "
                        + "RETURN c.nombre AS Categoria, COLLECT(p.nombre) AS Productos",
                        parameters("idCliente", idCliente)
                    );

                    List<Map<String, Object>> results = new LinkedList<>();
                    while (result.hasNext()) {
                        Record record = result.next();
                        Map<String, Object> resultItem = new HashMap<>();
                        resultItem.put("Categoria", record.get("Categoria").asString());
                        resultItem.put("Productos", record.get("Productos").asList());
                        results.add(resultItem);
                    }
                    return results;
                }
            });
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    



}



 /* "MATCH (u:User {userId: $userId})-[:LIVES_IN]->(l:Location)<-[:LIVES_IN]-(m:User),"
                            + "(u)-[:WANTS_RELATIONSHIP]->(r:RelationshipType)<-[:WANTS_RELATIONSHIP]-(m),"
                            + "(u)-[:HAS_SEX]->(s:Sex), (m)-[:HAS_SEX]->(oppositeSex:Sex),"
                            + "(u)-[:INTERESTED_IN]->(i:Interest)<-[:INTERESTED_IN]-(m)"
                            + "WHERE NOT s = oppositeSex "
                            + "WITH u, m, collect(i) AS sharedInterests "
                            + "RETURN m AS matchedUser, sharedInterests, size(sharedInterests) AS commonInterestCount "
                            + "ORDER BY commonInterestCount DESC"*/
