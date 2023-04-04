package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Courses;
import com.mycompany.myapp.repository.CoursesRepository;
import com.mycompany.myapp.service.CoursesService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Courses}.
 */
@Service
public class CoursesServiceImpl implements CoursesService {

    private final Logger log = LoggerFactory.getLogger(CoursesServiceImpl.class);

    private final CoursesRepository coursesRepository;

    public CoursesServiceImpl(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    @Override
    public Courses save(Courses courses) {
        log.debug("Request to save Courses : {}", courses);
        return coursesRepository.save(courses);
    }

    @Override
    public Courses update(Courses courses) {
        log.debug("Request to update Courses : {}", courses);
        return coursesRepository.save(courses);
    }

    @Override
    public Optional<Courses> partialUpdate(Courses courses) {
        log.debug("Request to partially update Courses : {}", courses);

        return coursesRepository
            .findById(courses.getId())
            .map(existingCourses -> {
                if (courses.getCourseName() != null) {
                    existingCourses.setCourseName(courses.getCourseName());
                }
                if (courses.getCourseCost() != null) {
                    existingCourses.setCourseCost(courses.getCourseCost());
                }

                return existingCourses;
            })
            .map(coursesRepository::save);
    }

    @Override
    public List<Courses> findAll() {
        log.debug("Request to get all Courses");
        return coursesRepository.findAll();
    }

    @Override
    public Optional<Courses> findOne(String id) {
        log.debug("Request to get Courses : {}", id);
        return coursesRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Courses : {}", id);
        coursesRepository.deleteById(id);
    }
}
