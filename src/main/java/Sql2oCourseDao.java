import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oCourseDao implements CourseDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oCourseDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that  contains a table called
     *   "courses" with two columns: "offeringName" and "title".
     */
    public Sql2oCourseDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Course create(String offeringName, String title) throws DaoException {
        return null; // stub
    }

    @Override
    public Course read(String offeringName) throws DaoException {
        return null; // stub
    }

    @Override
    public List<Course> readAll() throws DaoException {
        return null; // stub
    }

    @Override
    public List<Course> readAll(String titleQuery) throws DaoException {
        return null; // stub
    }

    @Override
    public Course update(String offeringName, String title) throws DaoException {
        return null; // stub
    }

    @Override
    public Course delete(String offeringName) throws DaoException {
        return null; // stub
    }
}