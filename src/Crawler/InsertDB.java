package Crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.bson.Document;

import twitter4j.Status;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;

import java.io.FileOutputStream;

public class InsertDB {
	private String text;
	private String id;
	private String dbName;
	private String collectionName;
	private String ip;
	private int port;

	// private SpammerChecker spammerChecker;

	public InsertDB() {
		this.dbName = "default";
		this.collectionName = "default";
		this.ip = "127.0.0.1";
		this.port = 27017;
	}

	public void setDB(String inputDB) {
		this.dbName = inputDB;
	}

	public void setCol(String inputCol) {
		this.collectionName = inputCol;
	}

	public void setIp(String inputip) {
		this.ip = inputip;
	}

	public void setPort(int inputport) {
		this.port = inputport;
	}

	public String getDB() {
		return this.dbName;
	}

	public String getCollectionName() {
		return this.collectionName;
	}

	public void printTwitterText() {
		String output;
		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);

		DBCollection col = db.getCollection(collectionName);
		DBCursor cursor = col.find();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"C:\\Users\\Administrator\\Desktop\\text.txt"));
			while (cursor.hasNext()) {
				output = cursor.next().toString();
				bw.write(output);
				bw.newLine();
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertTwitterText(Status status) {
		// get insert time
		String insertTime;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		insertTime = dateFormat.format(calendar.getTime());

		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);

		// If there is not collection, make it
		if (db.collectionExists(collectionName) == false) {
			db.createCollection(collectionName, new BasicDBObject());
		}

		// insert {Id, Name, Text, Time, ScreenName, StatusId}
		DBCollection addedCollection = db.getCollection(collectionName);
		BasicDBObject inputTwit = new BasicDBObject();
		inputTwit.put("Id", status.getUser().getId()); // User id
		inputTwit.put("Name", status.getUser().getName()); // User nickname
		inputTwit.put("Text", status.getText());
		inputTwit.put("Time", insertTime);
		inputTwit.put("ScreenName", status.getUser().getScreenName()); // User
																		// Screen
																		// name
																		// id
		inputTwit.put("StatusId", status.getId()); // Post number
		addedCollection.insert(inputTwit);

		mongoClient.close();
	}
}
