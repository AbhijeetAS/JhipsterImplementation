package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Courses;
import com.mycompany.myapp.repository.CoursesRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.CoursesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Courses}.
 */
@RestController
@RequestMapping("/api")
@EnableMethodSecurity
public class CoursesResource {

    private final Logger log = LoggerFactory.getLogger(CoursesResource.class);

    private static final String ENTITY_NAME = "courses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursesService coursesService;

    private final CoursesRepository coursesRepository;

    public CoursesResource(CoursesService coursesService, CoursesRepository coursesRepository) {
        this.coursesService = coursesService;
        this.coursesRepository = coursesRepository;
    }

    /**
     * {@code POST  /courses} : Create a new courses.
     *
     * @param courses the courses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courses, or with status {@code 400 (Bad Request)} if the courses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Courses> createCourses(@RequestBody Courses courses) throws URISyntaxException {
        log.debug("REST request to save Courses : {}", courses);
        if (courses.getId() != null) {
            throw new BadRequestAlertException("A new courses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Courses result = coursesService.save(courses);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing courses.
     *
     * @param id the id of the courses to save.
     * @param courses the courses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courses,
     * or with status {@code 400 (Bad Request)} if the courses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses/{id}")
    public ResponseEntity<Courses> updateCourses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Courses courses
    ) throws URISyntaxException {
        log.debug("REST request to update Courses : {}, {}", id, courses);
        if (courses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Courses result = coursesService.update(courses);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courses.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing courses, field will ignore if it is null
     *
     * @param id the id of the courses to save.
     * @param courses the courses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courses,
     * or with status {@code 400 (Bad Request)} if the courses is not valid,
     * or with status {@code 404 (Not Found)} if the courses is not found,
     * or with status {@code 500 (Internal Server Error)} if the courses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Courses> partialUpdateCourses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Courses courses
    ) throws URISyntaxException {
        log.debug("REST request to partial update Courses partially : {}, {}", id, courses);
        if (courses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Courses> result = coursesService.partialUpdate(courses);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courses.getId()));
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public List<Courses> getAllCourses() {
        log.debug("REST request to get all Courses");
        return coursesService.findAll();
    }

    /**
     * {@code GET  /courses/:id} : get the "id" courses.
     *
     * @param id the id of the courses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Courses> getCourses(@PathVariable String id) {
        log.debug("REST request to get Courses : {}", id);
        Optional<Courses> courses = coursesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courses);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" courses.
     *
     * @param id the id of the courses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourses(@PathVariable String id) {
        log.debug("REST request to delete Courses : {}", id);
        coursesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
