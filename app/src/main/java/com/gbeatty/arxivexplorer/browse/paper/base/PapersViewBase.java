package com.gbeatty.arxivexplorer.browse.paper.base;

public interface PapersViewBase {
    void setTitle(String title);

    void setAuthors(String authors);

    void setPublishedDate(String publishedDate);

    void setUpdatedDate(String updatedDate);

    void setFavoritedIcon();

    void setNotFavoritedIcon();
}
