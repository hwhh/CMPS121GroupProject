package com.groupproject.DataManager;


public abstract class DataManager {

    private DataBaseAPI dataBaseAPI;

    public DataManager(DataBaseAPI dataBaseAPI) {
        this.dataBaseAPI = DataBaseAPI.getInstance();
    }


}
