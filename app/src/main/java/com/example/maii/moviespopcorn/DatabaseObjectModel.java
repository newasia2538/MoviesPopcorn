package com.example.maii.moviespopcorn;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by MAII on 14/7/2559.
 */
public class DatabaseObjectModel implements Iterator {

    private String titleName, Posterpath, ReleaseDate, Overview;
    private Boolean isFavourite = false;

    public DatabaseObjectModel() {

    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getPosterpath() {
        return Posterpath;
    }

    public void setPosterpath(String posterpath) {
        Posterpath = posterpath;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer action) {

    }
}
