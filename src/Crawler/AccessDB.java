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

public class AccessDB {
	private String text;
	private String id;
	private String dbName;
	private String collectionName;
	private String ip;
	private int port;

	// private SpammerChecker spammerChecker;

	public AccessDB() {
		this.dbName = "SNS_Sensitive_Analysis";
		//this.collectionName = "default";
		this.ip = "127.0.0.1";
		this.port = 27017;
	}

	private String getTime() {
		// get insert time
		String insertTime;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		insertTime = dateFormat.format(calendar.getTime());
	
		return insertTime;
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
		dbName = "SNS_Sensitive_Analysis";
		collectionName = "SNS_Data";

		String output;
		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);

		DBCollection col = db.getCollection(collectionName);
		DBCursor cursor = col.find();

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"C:\\Users\\Administrator\\Desktop\\text.txt"));
			while (cursor.hasNext()) {
				// output = cursor.next().toString();
				DBObject saveCursor = cursor.next();

				output = saveCursor.get("Name").toString();
				bw.write("닉네임 : " + output);
				bw.newLine();
				output = saveCursor.get("Time").toString();
				bw.write("시간 : " + output);
				bw.newLine();
				output = saveCursor.get("Text").toString();
				bw.write("내용 : " + output);
				bw.newLine();
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mongoClient.close();
	}

	public void insertTwitterText(Status status) {
		dbName = "SNS_Sensitive_Analysis";
		collectionName = "SNS_Data";
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
		inputTwit.put("Time", getTime());
		inputTwit.put("ScreenName", status.getUser().getScreenName());
		inputTwit.put("StatusId", status.getId()); // Post number
		addedCollection.insert(inputTwit);

		mongoClient.close();
	}

	public void addBlackListUser(Status status) {
		dbName = "SNS_Sensitive_Analysis";
		collectionName = "Black_List";

		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);

		if (db.collectionExists(collectionName) == false) {
			db.createCollection(collectionName, new BasicDBObject());
		}

		DBCollection addedCollection = db.getCollection(collectionName);

		if (getBlackListCount(status) == 0) {
			BasicDBObject inputUser = new BasicDBObject();
			inputUser.put("Screen_Name", status.getUser().getScreenName());
			inputUser.put("Name", status.getUser().getName());
			inputUser.put("Time", getTime());
			inputUser.put("Text", status.getText());
			inputUser.put("Count", 1);
			addedCollection.insert(inputUser);
		} else {
			BasicDBObject findQuery = new BasicDBObject("Screen_Name", status
					.getUser().getScreenName());
			BasicDBObject newInput = new BasicDBObject("Screen_Name", status.getUser()
					.getScreenName());
			newInput.put("Name", status.getUser().getName());
			newInput.put("Time", getTime());
			newInput.put("Text", status.getText());
			newInput.put("Count", getBlackListCount(status) + 1);
			BasicDBObject updateObject = new BasicDBObject("$set", newInput);
			addedCollection.update(findQuery, updateObject);
		}
		mongoClient.close();
		// BasicDBObject
	}

	public int getBlackListCount(Status status) {
		dbName = "SNS_Sensitive_Analysis";
		collectionName = "Black_List";

		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);
		DBCollection col = db.getCollection(collectionName);

		BasicDBObject findQuery = new BasicDBObject("Screen_Name", status.getUser()
				.getScreenName());
		DBObject result = col.findOne(findQuery);
		
		if (result == null) {
			mongoClient.close();
			return 0;
		} else {
			mongoClient.close();
			return (int)result.get("Count");
		}
		// result.get() 쓰면 원하는거 얻을수 있음
		// 검색결과없으면 result - null
	}
	
	public int getSpamWordCount(Status status) {
		dbName = "SNS_Sensitive_Analysis";
		collectionName = "Spam_Word_List";
		
		MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port));
		DB db = mongoClient.getDB(dbName);
		DBCollection col = db.getCollection(collectionName);
		
		DBCursor cursor = col.find();
		int count = 0;
		
		String text = status.getText();
		String name = status.getUser().getName();
		String desc = status.getUser().getDescription();
		
		while (cursor.hasNext()) {
			DBObject searchWord = cursor.next();
			if (text.contains(searchWord.get("Word").toString()) == true) {
				count++;
			}
			if (name.contains(searchWord.get("Word").toString()) == true) {
				count++;
			}
			if (desc.contains(searchWord.get("Word").toString()) == true) {
				count++;
			}
		}
		mongoClient.close();
		return count;
	}
}
