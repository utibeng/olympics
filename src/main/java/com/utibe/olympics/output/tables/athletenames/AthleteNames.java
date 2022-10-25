package com.utibe.olympics.output.tables.athletenames;

public class AthleteNames {

    private int id;
    private String name;

    public AthleteNames() {

    }

    public AthleteNames(int ID, String name) {
        this.id= ID;
        this.name = name;
        System.out.println("ID = " + this.id + " Name = " + this.getName());
    }

    /*public long recordCount(Supplier<Stream<CSVRecord>> csvRecordsSupplier){
        return csvRecordsSupplier.get().count();
    }

    public Stream <AthleteNames> getAthleteTableRow(Stream<CSVRecord> csvRecordStream){
        return csvRecordStream.map(csvRecord ->
                new AthleteNames(Integer.parseInt(csvRecord.get("ID")),
                        csvRecord.get("Name") ));
    }*/

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "id=" + this.id + ", Name=" + this.name;
    }
}
