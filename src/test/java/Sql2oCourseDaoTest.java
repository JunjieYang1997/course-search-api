import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


class Sql2oCourseDaoTest {
    private static Sql2o sql2o;
    private static List<Course> samples;
    private CourseDao courseDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        // construct the sql2o object to connect to a (test) database
        String databaseUrl = System.getenv("TEST_DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        sql2o = new Sql2o(dbUrl, username, password);
    }

    @BeforeAll
    static void setSampleCourses() {
        // instantiate "samples" with sample courses
        samples = new ArrayList<>();
        samples.add(new Course("EN.500.112", "GATEWAY COMPUTING: JAVA"));
        samples.add(new Course("EN.601.220", "INTERMEDIATE PROGRAMMING"));
        samples.add(new Course("EN.601.226", "DATA STRUCTURES"));
        samples.add(new Course("EN.601.229", "COMPUTER SYSTEM FUNDAMENTALS"));
        samples.add(new Course("EN.601.230", "AUTOMATA and COMPUTATION THEORY"));
        samples.add(new Course("EN.601.315", "Databases"));
        samples.add(new Course("EN.601.476", "Machine Learning: Data to Models"));
        samples.add(new Course("EN.601.676", "Machine Learning: Data to Models"));
    }

    @BeforeEach
    void injectDependency() {
        // instantiate the courseDao object as a Sql2oCourseDao
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS courses;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS courses("
                    + "offeringName VARCHAR(15) NOT NULL PRIMARY KEY,"
                    + "title VARCHAR(50) NOT NULL"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "INSERT INTO courses(offeringName, title) VALUES(:offeringName, :title);";
            for (Course course : samples) {
                conn.createQuery(sql).bind(course).executeUpdate();
            }
        }

        courseDao = new Sql2oCourseDao(sql2o);
    }

    @Test
    void doNothing() {

    }
}