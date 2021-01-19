package com.ld.quicktest.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @OneToMany(mappedBy = "test", cascade = {CascadeType.ALL})
    private List<Result> resultList;

    @OneToMany(mappedBy = "test", cascade = {CascadeType.ALL})
    private List<Question> questionList;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "is_active")
    private boolean isActive;

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}