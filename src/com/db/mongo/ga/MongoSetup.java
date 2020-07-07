package com.db.mongo.ga;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoSetup {
	Mongo mongo = null;
	DB database = null;
	String databaseName;

	public MongoSetup() {
		this.setup();
	}

	public MongoSetup(String database) {
		this.setup();
		this.databaseName = database;
	}

	private void setup() {
		try {
			this.mongo = new Mongo();
		} catch (Exception var2) {
			System.out.println();
		}

	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public DB getDatabase() {
		return this.database;
	}

	public DBCollection getCollection(String collection) {
		return this.getCollection(this.databaseName, collection);
	}

	public DBCollection getCollection(String database, String collection) {
		this.database = this.mongo.getDB(database);
		return this.database.getCollection(collection);
	}
}