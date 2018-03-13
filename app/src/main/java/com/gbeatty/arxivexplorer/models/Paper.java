package com.gbeatty.arxivexplorer.models;

import com.orm.SugarRecord;

import java.io.Serializable;


public class Paper extends SugarRecord<Paper> implements Serializable {

    private String title;
    private String summary;
    private String updatedDate;
    private String publishedDate;
    private String paperID;
    private String pdfURL;
    private String author;


    public Paper() {

    }

    public Paper(String title,
                 String author,
                 String summary,
                 String updatedDate,
                 String publishedDate,
                 String paperID,
                 String pdfURL) {
        this.title = title;
        this.author = author;
        this.summary = summary;
        this.updatedDate = updatedDate;
        this.publishedDate = publishedDate;
        this.paperID = paperID;
        this.pdfURL = pdfURL;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }


    public String getPublishedDate() {
        return publishedDate;
    }


    public String getPaperID() {
        return paperID;
    }

    public String getSummary() {
        return summary;
    }

    public String getPDFURL() {
        return pdfURL;
    }


    public String getAuthor() {
        return author;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
