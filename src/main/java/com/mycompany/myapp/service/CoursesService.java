package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Courses;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Courses}.
 */
public interface CoursesService {
    /**
     * Save a courses.
     *
     * @param courses the entity to save.
     * @return the persisted entity.
     */
    Courses save(Courses courses);

    /**
     * Updates a courses.
     *
     * @param courses the entity to update.
     * @return the persisted entity.
     */
    Courses update(Courses courses);

    /**
     * Partially updates a courses.
     *
     * @param courses the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Courses> partialUpdate(Courses courses);

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    List<Courses> findAll();

    /**
     * Get the "id" courses.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Courses> findOne(String id);

    /**
     * Delete the "id" courses.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
