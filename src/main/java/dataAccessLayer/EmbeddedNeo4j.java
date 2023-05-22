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

public class EmbeddedNeo4j implements AutoCloseable {

    private final Driver driver;

    public EmbeddedNeo4j(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void printGreeting(String message) {
        try (Session session = driver.session()) {
            CompletableFuture<Void> greeting = CompletableFuture.supplyAsync(() -> {
                try (Transaction tx = session.beginTransaction()) {
                    Result result = tx.run(
                            "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters("message", message)
                    );
                    ResultSummary summary = result.consume();
                    tx.commit();
                }
                return null;
            });
            greeting.get(); // Espera a que se complete la transacción
            System.out.println("Greeting created successfully.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<String> getActors() {
        try (Session session = driver.session()) {
            CompletableFuture<LinkedList<String>> actors = CompletableFuture.supplyAsync(() -> {
                LinkedList<String> myactors = new LinkedList<>();
                try (Transaction tx = session.beginTransaction()) {
                    Result result = tx.run("MATCH (people:Person) RETURN people.name");
                    List<Record> registros = result.list();
                    for (int i = 0; i < registros.size(); i++) {
                        myactors.add(registros.get(i).get("people.name").asString());
                    }
                    tx.commit();
                }
                return myactors;
            });
            return actors.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LinkedList<String> getMoviesByActor(String actor) {
        try (Session session = driver.session()) {
            CompletableFuture<LinkedList<String>> movies = CompletableFuture.supplyAsync(() -> {
                LinkedList<String> mymovies = new LinkedList<>();
                try (Transaction tx = session.beginTransaction()) {
                    Result result = tx.run("MATCH (tom:Person {name: \"" + actor + "\"})-[:ACTED_IN]->(actorMovies) RETURN actorMovies.title");
                    List<Record> registros = result.list();
                    for (int i = 0; i < registros.size(); i++) {
                        mymovies.add(registros.get(i).get("actorMovies.title").asString());
                    }
                    tx.commit();
                }
                return mymovies;
            });
            return movies.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String insertProduct(String nombreProducto, int precioProducto, String descripcionProducto) {
        try (Session session = driver.session()) {
            CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run("CREATE (Test:Producto {nombre:'" + nombreProducto + "', precio:" + precioProducto + ", descripcion:'" + descripcionProducto + "'})");
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

    public String insertMovie(String movieTitle, int parseInt, String tagline) {
        return movieTitle;
    }
}




