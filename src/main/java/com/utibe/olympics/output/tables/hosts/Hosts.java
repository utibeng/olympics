package com.utibe.olympics.output.tables.hosts;

import java.util.Objects;

public class Hosts {

    private String games;
    private String year;
    private String season;
    private String city;


    public Hosts(String games, String year, String season, String city) {
        this.games = games;
        this.year = year;
        this.season = season;
        this.city = city;
    }

    public Hosts() {

    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hosts)) return false;
        Hosts hosts = (Hosts) o;
        return getGames().equals(hosts.getGames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGames());
    }
}
