package com.hopeindia;

public class Topic_Model {
    String topic_id,sr_no,topic_name,course_level,topic_video,subject,subject_id;

    public Topic_Model(String subject,String subject_id,String topic_id, String sr_no, String topic_name, String course_level, String topic_video) {
        this.topic_id = topic_id;
        this.sr_no = sr_no;
        this.topic_name = topic_name;
        this.course_level = course_level;
        this.topic_video = topic_video;
        this.subject=subject;
        this.subject_id=subject_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getCourse_level() {
        return course_level;
    }

    public void setCourse_level(String course_level) {
        this.course_level = course_level;
    }

    public String getTopic_video() {
        return topic_video;
    }

    public void setTopic_video(String topic_video) {
        this.topic_video = topic_video;
    }
}
