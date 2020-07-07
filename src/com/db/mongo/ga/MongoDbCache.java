package com.db.mongo.ga;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbCache {
	private static final String uri = "mongodb://localhost:27017/?ssl=true";
	private static MongoClientURI connectionString = null;
	private static MongoClient mongo = null;
	private static volatile MongoDbCache instance = null;

	public MongoClient getConnection() {
		return mongo;
	}

	public void destroyConnection() {
		if (mongo != null) {
			mongo.close();
			mongo = null;
			instance = null;
		}

	}

	private MongoDatabase getDB() {
		return mongo.getDatabase("test");
	}

	private MongoCollection<Document> getUserCollection() {
		return this.getDB().getCollection("user");
	}

	public static MongoDbCache getInstance() {
		if (instance == null) {
			Class var0 = MongoDbCache.class;
			synchronized (MongoDbCache.class) {
				if (instance == null) {
					instance = new MongoDbCache();

					try {
						System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/firstTrustStore");
						System.setProperty("javax.net.ssl.trustStorePassword", "bizlem123");
						System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/MongoClientKeyCert.jks");
						System.setProperty("javax.net.ssl.keyStorePassword", "bizlem123");
						Builder options = MongoClientOptions.builder().connectionsPerHost(1);
						connectionString = new MongoClientURI("mongodb://localhost:27017/?ssl=true", options);
						mongo = new MongoClient(connectionString);
					} catch (Exception var2) {
						;
					}
				}
			}
		}

		return instance;
	}
}