package com.darwinsys.database;

import java.io.Serializable;

/**
 * Interface to be implemented by DAOs; using Java 5 Generics.
 * @author Per Mellqvist, http://www.ibm.com/developerworks/java/library/j-genericdao.html
 */
public interface GenericDAO<T, PK extends Serializable> {

    /** Persist the newInstance object into database
	 * @param newInstance The object to be persisted
	 * @return The primary key of the newly inserted object
	 */
    PK create(T newInstance);

    /** Retrieve an object that was previously saved in the database
     * under the given primary key (id).
	 * @param id The id to be read
	 * @return The found object
     */
    T read(PK id);

    /** Save changes made to a persistent object.
	 * @param transientObject The object to be updated.
	 */
    void update(T transientObject);

    /** Remove an object from persistent storage in the database
	 * @param persistentObject the object to be deleted.
	 */
    void delete(T persistentObject);
}
