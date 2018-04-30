package database;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DatabaseConnection {

	private MongoClient mongoClient = null;

	@Lock(LockType.READ)
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	@PostConstruct
	public void init() throws UnknownHostException {
		mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("agentidb");
	}
	
	public MongoDatabase getDatabase() {
		return mongoClient.getDatabase("agentidb");
	}
	
	public MongoCollection<Document> getUsers() {
		return mongoClient.getDatabase("agentidb").getCollection("User");
	}
}
