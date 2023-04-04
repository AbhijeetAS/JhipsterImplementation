package com.mycompany.myapp.config.dbmigrations;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.security.AuthoritiesConstants;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.time.Instant;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    public InitialSetupMigration(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        Authority userAuthority = createUserAuthority();
        userAuthority = template.save(userAuthority);
        Authority adminAuthority = createAdminAuthority();
        adminAuthority = template.save(adminAuthority);
        Authority studentAuthority = createStudentAuthority();
        studentAuthority = template.save(studentAuthority);
        Authority teacherAuthority = createTeacherAuthority();
        teacherAuthority = template.save(teacherAuthority);

        addUsers(userAuthority, adminAuthority, studentAuthority, teacherAuthority);
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createStudentAuthority() {
        Authority studentAuthority = createAuthority(AuthoritiesConstants.STUDENT);
        return studentAuthority;
    }

    private Authority createTeacherAuthority() {
        Authority studentAuthority = createAuthority(AuthoritiesConstants.TEACHER);
        return studentAuthority;
    }

    private Authority createUserAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.USER);
        return userAuthority;
    }

    private void addUsers(Authority userAuthority, Authority adminAuthority, Authority studentAuthority, Authority teacherAuthority) {
        User admin = createAdmin(adminAuthority, userAuthority, studentAuthority, teacherAuthority);
        User teacher = createTeacher(teacherAuthority);
        User student = createstudent(studentAuthority);
        template.save(teacher);
        template.save(student);
        template.save(admin);
    }

    private User createstudent(Authority studentAuthority) {
        User studentUser = new User();
        studentUser.setId("user-3");
        studentUser.setLogin("sanket");
        studentUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        studentUser.setFirstName("sanket");
        studentUser.setLastName("pachghare");
        studentUser.setEmail("sanket@gmail.com");
        studentUser.setActivated(true);
        studentUser.setLangKey("en");
        studentUser.setCreatedBy("admin");
        studentUser.setCreatedDate(Instant.now());
        studentUser.getAuthorities().add(studentAuthority);
        return studentUser;
    }

    private User createTeacher(Authority teacherAuthority) {
        User teacherUser = new User();
        teacherUser.setId("user-2");
        teacherUser.setLogin("ajay");
        teacherUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        teacherUser.setFirstName("ajay");
        teacherUser.setLastName("kakde");
        teacherUser.setEmail("ajay@gmail.com");
        teacherUser.setActivated(true);
        teacherUser.setLangKey("en");
        teacherUser.setCreatedBy("admin");
        teacherUser.setCreatedDate(Instant.now());
        teacherUser.getAuthorities().add(teacherAuthority);
        return teacherUser;
    }

    private User createAdmin(Authority adminAuthority, Authority userAuthority, Authority studentAuthority, Authority teacherAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        adminUser.getAuthorities().add(studentAuthority);
        adminUser.getAuthorities().add(teacherAuthority);
        return adminUser;
    }
}
