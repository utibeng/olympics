package com.utibe.olympics.output.tables.olympicevents;

import java.util.Objects;

public class OlympicEvents {

    private String sport;
    private String event;
    private String year;
    private String games;
    private String season;

    public OlympicEvents(String sport, String event, String year, String games, String season) {
        this.sport = sport;
        this.event = event;
        this.year = year;
        this.games = games;
        this.season = season;
    }

    public OlympicEvents() {

    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    //generated by intellij
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OlympicEvents)) return false;
        OlympicEvents that = (OlympicEvents) o;
        return Objects.equals(getEvent(), that.getEvent()) && Objects.equals(getGames(), that.getGames());
    }

    //generated by intellij
    @Override
    public int hashCode() {
        return Objects.hash(getEvent(), getGames());
    }

}