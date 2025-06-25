package com.alibaba.cloudapp.api.scheduler.worker;

public class JobGroupMetadata {
    /**
     * app name
     */
    private String appName;
    
    /**
     * app title
     */
    private String title;
    
    /**
     * app author
     */
    private String author;
    
    /**
     * app alarm email
     */
    private String alarmEmail;
    
    /**
     * app id
     */
    private Integer id;
    
    public Integer getId() {
        return id;
    }
    
    public JobGroupMetadata setId(Integer id) {
        this.id = id;
        return this;
    }
    
    public String getAppName() {
        return appName;
    }
    
    public JobGroupMetadata setAppName(String appName) {
        this.appName = appName;
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public JobGroupMetadata setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public JobGroupMetadata setAuthor(String author) {
        this.author = author;
        return this;
    }
    
    public String getAlarmEmail() {
        return alarmEmail;
    }
    
    public JobGroupMetadata setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
        return this;
    }
    
}
