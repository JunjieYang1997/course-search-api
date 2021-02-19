package util;

import model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class with methods to create sample data.
 */
public final class DataStore {

    private DataStore() {
        // This class should not be instantiated.
    }

    /**
     * Create a list of sample CS courses.
     *
     * @return a list of sample CS courses.
     */
    public static List<Course> sampleCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("EN.500.112","GATEWAY COMPUTING: JAVA"));
        courses.add(new Course("EN.601.220","INTERMEDIATE PROGRAMMING"));
        courses.add(new Course("EN.601.226","DATA STRUCTURES"));
        courses.add(new Course("EN.601.229","COMPUTER SYSTEM FUNDAMENTALS"));
        courses.add(new Course("EN.601.231","AUTOMATA and COMPUTATION THEORY"));
        return courses;
    }
}
