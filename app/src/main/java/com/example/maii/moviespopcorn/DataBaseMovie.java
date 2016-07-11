package com.example.maii.moviespopcorn;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MAII on 11/7/2559.
 */
public class DataBaseMovie extends RealmObject {
    @PrimaryKey
    private long id;
    private String titleName, Posterpath, ReleaseDate, Overview;
    private Boolean isFavourite = false;
    public DataBaseMovie() {
    }
    public DataBaseMovie(String titleName, String Posterpath, String ReleaseDate, String Overview, Boolean isFavourite){
        this.titleName = titleName;
        this.Posterpath = Posterpath;
        this.ReleaseDate = ReleaseDate;
        this.isFavourite = isFavourite;
        this.Overview = Overview;

    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
