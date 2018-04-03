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
    private boolean downloaded;
    private boolean favorited;


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
        this.downloaded = false;
        this.favorited = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
