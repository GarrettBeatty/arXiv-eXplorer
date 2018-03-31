package com.gbeatty.arxivexplorer.paper.base;

public interface PapersViewBase {
    void setTitle(String title);

    void setAuthors(String authors);

    void setPublishedDate(String publishedDate);

    void setUpdatedDate(String updatedDate);

    void setSummary(String summary);

    void setFavoritedIcon();

    void setNotFavoritedIcon();
}
