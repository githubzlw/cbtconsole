package com.importExpress.utli;

import com.cbt.util.SysParamUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;

import org.slf4j.LoggerFactory;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author luohao
 * @date 2018/10/16
 */

public enum MongoDBHelp {

    /**
     * single instance
     */
    INSTANCE;

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MongoDBHelp.class);

    private static final String MONGODB_URL = SysParamUtil.getParam("mongodb.url");
    private static final String MONGODB_DB = SysParamUtil.getParam("mongodb.db");
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;

    private void getConnection(){
        this.mongoClient = MongoClients.create(MONGODB_URL);
        this.mongoDatabase = mongoClient.getDatabase(MONGODB_DB);
    }

    private void closeConnection(){

        if(this.mongoClient!=null){
            this.mongoClient.close();
            this.mongoDatabase=null;
        }

    }

    public void insert(String collectionName,String json){
        this.getConnection();

        if(mongoDatabase!=null){
            mongoDatabase.getCollection(collectionName).insertOne(Document.parse(json));
        }else{
            logger.error("mongoDatabase is null");
        }

        this.closeConnection();
    }

    public String findOne(String collectionName,String pkName,String pkValue){
        this.getConnection();
        String result = null;

        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(eq(pkName, pkValue));
        if(documents!=null && documents.first()!=null) {
            result = documents.first().toJson();
        }

        this.closeConnection();
        return result;
    }

    public  List<String> findAny(String collectionName,String pkName,String pkValue){
        this.getConnection();
        List<String> result = new ArrayList<>();

        BasicDBObject q = new BasicDBObject();
        q.put(pkName,  java.util.regex.Pattern.compile(pkValue));
        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(q);
        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()){
            result.add(iterator.next().toJson());
        }
        this.closeConnection();
        return result;
    }
    
    public  long count(String collectionName,BasicDBObject q){
    	this.getConnection();
    	long count = mongoDatabase.getCollection(collectionName).countDocuments(q);
    	this.closeConnection();
    	return count;
    }
    public  List<String> findAny(String collectionName,BasicDBObject find,BasicDBObject sort,int startNum,int limitNum){
    	this.getConnection();
    	List<String> result = new ArrayList<>();
    	FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(find);
    	if(sort != null) {
    		documents = documents.sort(sort);
    	}
    	
    	documents = documents.skip(startNum).limit(limitNum);
    	
    	MongoCursor<Document> iterator = documents.iterator();
    	while (iterator.hasNext()){
    		result.add(iterator.next().toJson());
    	}
    	this.closeConnection();
    	return result;
    }
    public  List<String> findAny(String collectionName,BasicDBObject find,BasicDBObject sort){
    	this.getConnection();
    	List<String> result = new ArrayList<>();
    	FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(find);
    	if(sort != null) {
    		documents = documents.sort(sort);
    	}
//    	
//    	documents = documents.skip(startNum).limit(limitNum);
    	
    	MongoCursor<Document> iterator = documents.iterator();
    	while (iterator.hasNext()){
    		result.add(iterator.next().toJson());
    	}
    	this.closeConnection();
    	return result;
    }

    public static void main(String[] args){
        //System.out.println(MongoDBHelp.INSTANCE.findOne("webhook_test","id","WH-1RD175966K076994H-53R00852K9690533R"));
        System.out.println(MongoDBHelp.INSTANCE.findAny("webhook_test","id","WH*"));
    }
}