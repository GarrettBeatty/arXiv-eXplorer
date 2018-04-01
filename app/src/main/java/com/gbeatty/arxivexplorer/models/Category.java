package com.gbeatty.arxivexplorer.models;

import java.io.Serializable;

public class Category implements Serializable {

    private String name;
    private final String shortName;
    private final Category[] subCategories;

    public Category(String name, String shortName, Category... subCategories) {
        this.name = name;
        this.shortName = shortName;
        if(subCategories == null) {
            subCategories = new Category[0];
        }
        this.subCategories = subCategories;
    }

    public String getShortName() {
        return shortName;
    }

    public Category[] getSubCategories() {
        return subCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatKey(){
        if (name.equals("All")) return "all";
        return "cat";
    }

}
