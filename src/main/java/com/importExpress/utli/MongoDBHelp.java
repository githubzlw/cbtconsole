package com.importExpress.utli;

import com.cbt.util.SysParamUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;

import org.slf4j.LoggerFactory;
import org.bson.Document;
import org.bson.conversions.Bson;

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

    private static final String MONGODB_HOST = SysParamUtil.getParam("mongodb.host");
    private static final String MONGODB_PORT = SysParamUtil.getParam("mongodb.port");
    private static final String MONGODB_DB = SysParamUtil.getParam("mongodb.db");
    private static final String MONGODB_USERNAME = SysParamUtil.getParam("mongodb.username");
    private static final String MONGODB_POSSWORD = SysParamUtil.getParam("mongodb.password");
    private static final String MONGODB_MAX_WAIT_TIME = SysParamUtil.getParam("mongodb.maxWaitTime");
    private static final String MONGODB_SOCKET_TIMEOUT = SysParamUtil.getParam("mongodb.socketTimeout");
    private static final String MONGODB_MAX_CONNECTION_LIFE_TIME = SysParamUtil.getParam("mongodb.maxConnectionLifeTime");
    private static final String MONGODB_CONNECTION_TIMEOUT = SysParamUtil.getParam("mongodb.connectTimeout");
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;

    private void getConnection(){
    	List<ServerAddress> seeds = new ArrayList<>();
    	ServerAddress addr = new ServerAddress(MONGODB_HOST, Integer.valueOf(MONGODB_PORT));
    	seeds.add(addr);
    	MongoClientOptions options = MongoClientOptions.builder()
    			.maxWaitTime(Integer.valueOf(MONGODB_MAX_WAIT_TIME))
    			.socketTimeout(Integer.valueOf(MONGODB_SOCKET_TIMEOUT))
    			.maxConnectionLifeTime(Integer.valueOf(MONGODB_MAX_CONNECTION_LIFE_TIME))
    			.connectTimeout(Integer.valueOf(MONGODB_CONNECTION_TIMEOUT)).build();
    	MongoCredential credential = MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_DB, MONGODB_POSSWORD.toCharArray());
        this.mongoClient = new MongoClient(seeds,credential,options);
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
    /**更新mongodb
     * @param collectionName
     * @param filter
     * @param update
     * @return
     */
    public long update(String collectionName,Bson filter,Bson update){
        this.getConnection();
        long result = 0;
        if(mongoDatabase!=null){
			UpdateResult updateMany = mongoDatabase.getCollection(collectionName).updateMany(filter, update);
			result = updateMany.getModifiedCount();
        }else{
            logger.error("mongoDatabase is null");
        }

        this.closeConnection();
        return result;
    }
}