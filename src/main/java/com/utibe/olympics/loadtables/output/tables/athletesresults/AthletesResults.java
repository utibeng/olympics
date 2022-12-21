package com.utibe.olympics.loadtables.output.tables.athletesresults;

import java.util.Objects;

public class AthletesResults {
    private int id;
    private String noc;
    private String games;
    private String event;
    private String medal;
    private String sex;
    private String age;
    private String height;
    private String weight;

    public AthletesResults(int id, String noc, String games, String event, String medal, String sex, String age,
                           String height, String weight) {
        this.id = id;
        this.noc = noc;
        this.games = games;
        this.event = event;
        this.medal = medal;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public AthletesResults(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AthletesResults)) return false;
        AthletesResults that = (AthletesResults) o;
        return getId() == that.getId() && getGames().equals(that.getGames()) && getEvent().equals(that.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGames(), getEvent());
    }
}
