import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.sql2o.Sql2o;
import spark.Spark;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ApiServer {
    public static void main(String[] args) throws URISyntaxException {
        port(getHerokuAssignedPort());

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        CourseDao courseDao = getCourseDao();

        get("/api/courses", (req, res) -> {
            try {
                List<Course> courses = courseDao.readAll();
                res.type("application/json");
                return gson.toJson(courses);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });
    }

    private static int getHerokuAssignedPort() {
        // Heroku stores port number as an environment variable
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        //return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }

    private static CourseDao getCourseDao() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        Sql2o sql2o = new Sql2o(dbUrl, username, password);
        return new Sql2oCourseDao(sql2o);
    }

    /**
     * Stop the server.
     */
    public static void stop() {
        Spark.stop();
    }
}


