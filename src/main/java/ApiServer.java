import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class ApiServer {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("EN.500.112","GATEWAY COMPUTING: JAVA"));
        courses.add(new Course("EN.601.220","INTERMEDIATE PROGRAMMING"));
        courses.add(new Course("EN.601.226","DATA STRUCTURES"));
        courses.add(new Course("EN.601.229","COMPUTER SYSTEM FUNDAMENTALS"));
        courses.add(new Course("EN.601.231","AUTOMATA and COMPUTATION THEORY"));

        Gson gson = new Gson();
        get("/api/courses",  (req, res) -> {
            res.type("application/json");
            return courses;
        }, gson::toJson);
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
}


