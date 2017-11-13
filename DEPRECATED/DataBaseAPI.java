package com.groupproject.DataManager;


import com.groupproject.Model.EventActivity;
import com.groupproject.Model.Event;
import com.groupproject.Model.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

class DataBaseAPI {

    //TODO hide credentials
    private static final String MOGOO_DB_URI = "mongodb://admin:admin@cluster0-shard-00-00-x1fap.mongodb.net:27017," +
            "cluster0-shard-00-01-x1fap.mongodb.net:27017," +
            "cluster0-shard-00-02-x1fap.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";

    private static DataBaseAPI dataBaseAPI = null;

//    private static MongoCollection<Document> collection;
    private static Datastore datastore;


    private DataBaseAPI() {
        init();
    }

    static DataBaseAPI getInstance() {
        if (dataBaseAPI == null)
            dataBaseAPI = new DataBaseAPI();
        return dataBaseAPI;
    }

    private static void init() {
        MongoClientURI uri = new MongoClientURI(MOGOO_DB_URI);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Pinned");
//      collection = database.getCollection("NewCollection");
        Morphia morphia = new Morphia();
        morphia.map(User.class, EventActivity.class, Event.class);//TODO Update with a method call
        datastore = morphia.createDatastore(mongoClient, database.getName());
    }

    public Datastore getDataStore() {
        return datastore;
    }
}
