package com.jing.blogs.domain;

import javax.persistence.*;

@Entity
@Table(name = "t_podcast")
public class Podcast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String fileName;
    private String URL;
    private String Description;
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }
}
