package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Courses;
import com.mycompany.myapp.repository.CoursesRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CoursesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoursesResourceIT {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COURSE_COST = 1;
    private static final Integer UPDATED_COURSE_COST = 2;

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private MockMvc restCoursesMockMvc;

    private Courses courses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courses createEntity() {
        Courses courses = new Courses().courseName(DEFAULT_COURSE_NAME).courseCost(DEFAULT_COURSE_COST);
        return courses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courses createUpdatedEntity() {
        Courses courses = new Courses().courseName(UPDATED_COURSE_NAME).courseCost(UPDATED_COURSE_COST);
        return courses;
    }

    @BeforeEach
    public void initTest() {
        coursesRepository.deleteAll();
        courses = createEntity();
    }

    @Test
    void createCourses() throws Exception {
        int databaseSizeBeforeCreate = coursesRepository.findAll().size();
        // Create the Courses
        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courses)))
            .andExpect(status().isCreated());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeCreate + 1);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourses.getCourseCost()).isEqualTo(DEFAULT_COURSE_COST);
    }

    @Test
    void createCoursesWithExistingId() throws Exception {
        // Create the Courses with an existing ID
        courses.setId("existing_id");

        int databaseSizeBeforeCreate = coursesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courses)))
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCourses() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        // Get all the coursesList
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courses.getId())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME)))
            .andExpect(jsonPath("$.[*].courseCost").value(hasItem(DEFAULT_COURSE_COST)));
    }

    @Test
    void getCourses() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        // Get the courses
        restCoursesMockMvc
            .perform(get(ENTITY_API_URL_ID, courses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courses.getId()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME))
            .andExpect(jsonPath("$.courseCost").value(DEFAULT_COURSE_COST));
    }

    @Test
    void getNonExistingCourses() throws Exception {
        // Get the courses
        restCoursesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCourses() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses
        Courses updatedCourses = coursesRepository.findById(courses.getId()).get();
        updatedCourses.courseName(UPDATED_COURSE_NAME).courseCost(UPDATED_COURSE_COST);

        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourses))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourses.getCourseCost()).isEqualTo(UPDATED_COURSE_COST);
    }

    @Test
    void putNonExistingCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCoursesWithPatch() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses using partial update
        Courses partialUpdatedCourses = new Courses();
        partialUpdatedCourses.setId(courses.getId());

        partialUpdatedCourses.courseCost(UPDATED_COURSE_COST);

        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourses))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourses.getCourseCost()).isEqualTo(UPDATED_COURSE_COST);
    }

    @Test
    void fullUpdateCoursesWithPatch() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();

        // Update the courses using partial update
        Courses partialUpdatedCourses = new Courses();
        partialUpdatedCourses.setId(courses.getId());

        partialUpdatedCourses.courseName(UPDATED_COURSE_NAME).courseCost(UPDATED_COURSE_COST);

        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourses))
            )
            .andExpect(status().isOk());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
        Courses testCourses = coursesList.get(coursesList.size() - 1);
        assertThat(testCourses.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourses.getCourseCost()).isEqualTo(UPDATED_COURSE_COST);
    }

    @Test
    void patchNonExistingCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCourses() throws Exception {
        int databaseSizeBeforeUpdate = coursesRepository.findAll().size();
        courses.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courses in the database
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCourses() throws Exception {
        // Initialize the database
        coursesRepository.save(courses);

        int databaseSizeBeforeDelete = coursesRepository.findAll().size();

        // Delete the courses
        restCoursesMockMvc
            .perform(delete(ENTITY_API_URL_ID, courses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Courses> coursesList = coursesRepository.findAll();
        assertThat(coursesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
