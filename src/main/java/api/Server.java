package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dao.*;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Course;
import org.sql2o.Sql2o;
import spark.Spark;
import util.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class Server {
    public static void main(String[] args) throws URISyntaxException {
        port(getHerokuAssignedPort());

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        CourseDao courseDao = getCourseDao();

        exception(ApiError.class, (ex, req, res) -> {
            // Handle the exception here
            Map<String, String> map = Map.of("status", ex.getStatus() + "",
                    "error", ex.getMessage());
            res.body(gson.toJson(map));
            res.status(ex.getStatus());
        });


        get("/api/courses", (req, res) -> {
            try {
                String title = req.queryParams("title");
                List<Course> courses;
                if (title != null) {
                    courses = courseDao.readAll(title);
                } else {
                    courses = courseDao.readAll();
                }
                return gson.toJson(courses);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        get("/api/courses/:offeringName", (req, res) -> {
            try {
                String offeringName = req.params("offeringName");
                Course course = courseDao.read(offeringName);
                if (course == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                return gson.toJson(course);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        post("/api/courses", (req, res) -> {
            try {
                Course course = gson.fromJson(req.body(), Course.class);
                courseDao.create(course.getOfferingName(), course.getTitle());
                res.status(201);
                return gson.toJson(course);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        delete("/api/courses/:offeringName", (req, res) -> {
            try {
                String offeringName = req.params("offeringName");
                Course course = courseDao.delete(offeringName);
                if (course == null) {
                    throw new ApiError("Resource doesn't exist", 404);
                }
                return gson.toJson(course);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        put("/api/courses/:offeringName", (req, res) -> {
            try {
                String offeringName = req.params("offeringName");
                Course course = gson.fromJson(req.body(), Course.class);
                if (!course.getOfferingName().equals(offeringName)) {
                    throw new ApiError("offering name does not match the resource identifier", 400);
                }
                course = courseDao.update(course.getOfferingName(), course.getTitle());
                if (course == null) {
                    throw new ApiError("Resource not found", 404);
                }
                return gson.toJson(course);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });


        after((req, res) -> res.type("application/json"));
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
        return new Sql2oCourseDao(Database.getSql2o());
    }

    /**
     * Stop the server.
     */
    public static void stop() {
        Spark.stop();
    }
}


