package com.utibe.olympics.loadtables.output.tables.countries;

import java.util.Objects;

public class Countries {

    private String noc;
    private String team;



    public Countries( String noc, String team) {
        this.noc = noc;
        this.team = team;
    }

    public Countries(){

    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Countries)) return false;
        Countries that = (Countries) o;
        return getNoc().equals(that.getNoc() ) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNoc());
    }
}
