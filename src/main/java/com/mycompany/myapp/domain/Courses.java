package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Courses.
 */
@Document(collection = "courses")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Courses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("course_name")
    private String courseName;

    @Field("course_cost")
    private Integer courseCost;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Courses id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public Courses courseName(String courseName) {
        this.setCourseName(courseName);
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseCost() {
        return this.courseCost;
    }

    public Courses courseCost(Integer courseCost) {
        this.setCourseCost(courseCost);
        return this;
    }

    public void setCourseCost(Integer courseCost) {
        this.courseCost = courseCost;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Courses)) {
            return false;
        }
        return id != null && id.equals(((Courses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Courses{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", courseCost=" + getCourseCost() +
            "}";
    }
}
