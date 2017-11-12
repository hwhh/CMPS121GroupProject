package com.groupproject.DataManager;


import com.groupproject.Model.Activity;
import com.groupproject.Model.Event;
import com.groupproject.Model.User;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

class DataBaseAPI {

    //TODO hide credentials
    private static final String uri = "mongodb://admin:admin@cluster0-shard-00-00-x1fap.mongodb.net:27017," +
            "cluster0-shard-00-01-x1fap.mongodb.net:27017," +
            "cluster0-shard-00-02-x1fap.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";

    private static DataBaseAPI dataBaseAPI = null;

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static Datastore datastore;
    private static Morphia morphia;


    private DataBaseAPI() {
        init();
    }

    static DataBaseAPI getInstance() {
        if (dataBaseAPI == null)
            dataBaseAPI = new DataBaseAPI();
        return dataBaseAPI;
    }

    private static void init() {
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("Pinned");
//      collection = database.getCollection("NewCollection");
        morphia = new Morphia();
        morphia.map(User.class, Activity.class, Event.class);//TODO Update with a method call
        datastore = morphia.createDatastore(mongoClient, database.getName());
    }

    public Datastore getDataStore() {
        return datastore;
    }
}
