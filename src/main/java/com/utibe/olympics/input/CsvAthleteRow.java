package com.utibe.olympics.input;

public class CsvAthleteRow {

    /*public enum HeaderEnum {
        ID,Name,Sex,Age,Height,Weight,Team,NOC,Games,Year,Season,City,Sport,Event,Medal
    }*/

    private int id;
    private String name;
    private String sex;
    private String age;
    private String height;
    private String weight;
    private String team;
    private String noc;
    private String games;
    private String year;
    private String season;
    private String city;
    private String sport;
    private String event;
    private String medal;

    public CsvAthleteRow(int id, String name, String sex, String age, String height, String weight,
                         String team, String noc, String games, String year, String season, String city,
                         String sport, String event, String medal) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.team = team;
        this.noc = noc;
        this.games = games;
        this.year = year;
        this.season = season;
        this.city = city;
        this.sport = sport;
        this.event = event;
        this.medal = medal;
    }

    public CsvAthleteRow(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    @Override
    public String toString() {
        return "CsvAthleteRow{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", team='" + team + '\'' +
                ", noc='" + noc + '\'' +
                ", games='" + games + '\'' +
                ", year='" + year + '\'' +
                ", season='" + season + '\'' +
                ", city='" + city + '\'' +
                ", sport='" + sport + '\'' +
                ", event='" + event + '\'' +
                ", medal='" + medal + '\'' +
                '}';
    }
}
