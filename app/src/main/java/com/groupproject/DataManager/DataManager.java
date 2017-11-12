package com.groupproject.DataManager;


import java.util.List;

public abstract class DataManager {

    private DataBaseAPI dataBaseAPI;

    public DataManager() {
        this.dataBaseAPI = DataBaseAPI.getInstance();
    }

    public DataManager persist(DataManager object){
        dataBaseAPI.getDataStore().save(object);
        return object;
    }

    public List<DataManager> querry(String[] arguments){
        return null;
    }


}
