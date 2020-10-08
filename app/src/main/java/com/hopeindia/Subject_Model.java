package com.hopeindia;

public class Subject_Model {
    String subject,subject_id;

    public Subject_Model(String subject, String subject_id) {
        this.subject = subject;
        this.subject_id = subject_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
