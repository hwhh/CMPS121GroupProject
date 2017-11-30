package com.groupproject.Model;



public class DataBaseItem {

    private String name;
    private String type;

    DataBaseItem(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName(){
        return name;
    }

}
