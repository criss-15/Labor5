package repository;

import java.sql.SQLException;
import java.util.List;

/**
 * The interface Crud repository.
 * @param <T> giving a generic T value for any type of value we may need
 */
public interface ICrudRepository<T> {

    /**
     * function creating an object of type T
     *
     * @param obj the instance of the pbject needed
     * @return returns the object created
     */
    void create(T obj) throws SQLException;


    /**
     * function that can acces the whole list of objects T
     * @return the list of objects
     */
    List<T> getAll() throws SQLException;



    /**
     * the function changes the previous object with a new one
     * @param obj the new obj we want to update
     * @return the object updated
     */
    void update(T obj) throws SQLException;



    /**
     * function that deletes the object of type T from our repoList
     * @param obj the obj we want to delete
     */
    void delete(T obj) throws SQLException;
}
