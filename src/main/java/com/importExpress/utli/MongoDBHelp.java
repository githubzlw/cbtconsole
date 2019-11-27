package com.importExpress.utli;

import com.cbt.util.SysParamUtil;
import com.importExpress.pojo.MongoGoodsBean;
import com.mongodb.*;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String MONGODB_HOST2 = SysParamUtil.getParam("mongodb.host2");
    private static final String MONGODB_PORT = SysParamUtil.getParam("mongodb.port");
    private static final String MONGODB_DB = SysParamUtil.getParam("mongodb.db");
    private static final String MONGODB_DB2 = SysParamUtil.getParam("mongodb.db2");
    private static final String MONGODB_DB3 = SysParamUtil.getParam("mongodb.db3");
    private static final String MONGODB_USERNAME = SysParamUtil.getParam("mongodb.username");
    private static final String MONGODB_POSSWORD = SysParamUtil.getParam("mongodb.password");
    private static final String MONGODB_MAX_WAIT_TIME = SysParamUtil.getParam("mongodb.maxWaitTime");
    private static final String MONGODB_SOCKET_TIMEOUT = SysParamUtil.getParam("mongodb.socketTimeout");
    private static final String MONGODB_MAX_CONNECTION_LIFE_TIME = SysParamUtil.getParam("mongodb.maxConnectionLifeTime");
    private static final String MONGODB_CONNECTION_TIMEOUT = SysParamUtil.getParam("mongodb.connectTimeout");
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;
    private MongoClient mongoClient2 = null;
    private MongoDatabase mongoDatabase2 = null;
    private MongoClient mongoClient3 = null;
    private MongoDatabase mongoDatabase3 = null;

    private void getConnection() {
        List<ServerAddress> seeds = new ArrayList<>();
        ServerAddress addr = new ServerAddress(MONGODB_HOST, Integer.valueOf(MONGODB_PORT));
        seeds.add(addr);
        MongoClientOptions options = MongoClientOptions.builder()
                .maxWaitTime(Integer.valueOf(MONGODB_MAX_WAIT_TIME))
                .socketTimeout(Integer.valueOf(MONGODB_SOCKET_TIMEOUT))
                .maxConnectionLifeTime(Integer.valueOf(MONGODB_MAX_CONNECTION_LIFE_TIME))
                .connectTimeout(Integer.valueOf(MONGODB_CONNECTION_TIMEOUT)).build();
        MongoCredential credential = MongoCredential.createCredential(MONGODB_USERNAME, MONGODB_DB, MONGODB_POSSWORD.toCharArray());
        this.mongoClient = new MongoClient(seeds, credential, options);
        this.mongoDatabase = mongoClient.getDatabase(MONGODB_DB);

        List<ServerAddress> seeds2 = new ArrayList<>();
        ServerAddress addr2 = new ServerAddress(MONGODB_HOST2, Integer.valueOf(MONGODB_PORT));
        seeds2.add(addr2);
        this.mongoClient2 = new MongoClient(seeds2, options);
        this.mongoDatabase2 = mongoClient2.getDatabase(MONGODB_DB2);

        List<ServerAddress> seeds3 = new ArrayList<>();
        ServerAddress addr3 = new ServerAddress(MONGODB_HOST2, Integer.valueOf(MONGODB_PORT));
        seeds3.add(addr3);
        this.mongoClient3 = new MongoClient(seeds3, options);
        this.mongoDatabase3 = mongoClient3.getDatabase(MONGODB_DB3);
    }

    private void closeConnection() {

        if (this.mongoClient != null) {
            this.mongoClient.close();
            this.mongoDatabase = null;
        }
        if (this.mongoClient2 != null) {
            this.mongoClient2.close();
            this.mongoDatabase2 = null;
        }
        if (this.mongoClient3 != null) {
            this.mongoClient3.close();
            this.mongoDatabase3 = null;
        }

    }

    public void insert(String collectionName, String json) {
        this.getConnection();

        if (mongoDatabase != null) {
            mongoDatabase.getCollection(collectionName).insertOne(Document.parse(json));
        } else {
            logger.error("mongoDatabase is null");
        }

        this.closeConnection();
    }

    public String findOne(String collectionName, String pkName, String pkValue) {
        this.getConnection();
        String result = null;

        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(eq(pkName, pkValue));
        if (documents != null && documents.first() != null) {
            result = documents.first().toJson();
        }

        this.closeConnection();
        return result;
    }

    public List<String> findAny(String collectionName, String pkName, String pkValue) {
        this.getConnection();
        List<String> result = new ArrayList<>();

        BasicDBObject q = new BasicDBObject();
        q.put(pkName, java.util.regex.Pattern.compile(pkValue));
        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(q);
        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next().toJson());
        }
        this.closeConnection();
        return result;
    }

    public long count(String collectionName, BasicDBObject q) {
        this.getConnection();
        long count = mongoDatabase.getCollection(collectionName).countDocuments(q);
        this.closeConnection();
        return count;
    }

    public List<String> findAny(String collectionName, BasicDBObject find, BasicDBObject sort, int startNum, int limitNum) {
        this.getConnection();
        List<String> result = new ArrayList<>();
        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(find);
        if (sort != null) {
            documents = documents.sort(sort);
        }

        documents = documents.skip(startNum).limit(limitNum);

        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next().toJson());
        }
        this.closeConnection();
        return result;
    }

    public List<String> findAny(String collectionName, BasicDBObject find, BasicDBObject sort) {
        this.getConnection();
        List<String> result = new ArrayList<>();
        FindIterable<Document> documents = mongoDatabase.getCollection(collectionName).find(find);
        if (sort != null) {
            documents = documents.sort(sort);
        }
//    	
//    	documents = documents.skip(startNum).limit(limitNum);

        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next().toJson());
        }
        this.closeConnection();
        return result;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(MongoDBHelp.INSTANCE.findOne("webhook_test","id","WH-1RD175966K076994H-53R00852K9690533R"));
        //System.out.println(MongoDBHelp.INSTANCE.findAny("webhook_test","id","WH*"));
        BasicDBObject find = new BasicDBObject();
        find.put("catid1", "1037268");

        BasicDBObject sort = new BasicDBObject();
        sort.put("pid", -1);

        List<MongoGoodsBean> list;

        // list = MongoDBHelp.INSTANCE.findAnyFromMongo3("product", find, sort, 1, 5);
        // System.err.println(list);

        //find.put("goodsstate", "5");
        BasicDBObject fields = new BasicDBObject();
        fields.put("goodsstate", 1);
        List<String> strList = MongoDBHelp.INSTANCE.findCatidFromMongo3("product", find);
        Map<String, Integer> rsMap = new HashMap<>();
        for (String catid : strList) {
            if (rsMap.containsKey(catid)) {
                rsMap.put(catid, rsMap.get(catid) + 1);
            } else {
                rsMap.put(catid, 1);
            }
        }
        System.err.println(rsMap);

    }

    /**
     * 更新mongodb
     *
     * @param collectionName
     * @param filter
     * @param update
     * @return
     */
    public long update(String collectionName, Bson filter, Bson update) {
        this.getConnection();
        long result = 0;
        if (mongoDatabase != null) {
            UpdateResult updateMany = mongoDatabase.getCollection(collectionName).updateMany(filter, update);
            result = updateMany.getModifiedCount();
        } else {
            logger.error("mongoDatabase is null");
        }

        this.closeConnection();
        return result;
    }

    public long countFromMongo2(String collectionName, BasicDBObject q) {
        this.getConnection();
        long count = mongoDatabase2.getCollection(collectionName).countDocuments(q);
        this.closeConnection();
        return count;
    }

    public List<String> findAnyFromMongo2(String collectionName, BasicDBObject find, BasicDBObject sort, int startNum, int limitNum) {
        this.getConnection();
        List<String> result = new ArrayList<>();
        FindIterable<Document> documents = mongoDatabase2.getCollection(collectionName).find(find);
        if (sort != null) {
            documents = documents.sort(sort);
        }
        if (limitNum > 0) {
            documents = documents.skip(startNum).limit(limitNum);
        }

        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next().toJson());
        }
        this.closeConnection();
        return result;
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * mongo3的操作
     */


    public List<MongoGoodsBean> findAnyFromMongo3(String collectionName, BasicDBObject find, BasicDBObject sort, int startNum, int limitNum) throws Exception {
        this.getConnection();
        List<MongoGoodsBean> result = new ArrayList<>();
        FindIterable<Document> documents = mongoDatabase3.getCollection(collectionName).find(find);
        if (sort != null) {
            documents = documents.sort(sort);
        }
        if (limitNum > 0) {
            documents = documents.skip(startNum).limit(limitNum);
        }

        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            result.add(MapAndBeanUtil.map2bean(iterator.next(), MongoGoodsBean.class));
        }
        this.closeConnection();
        return result;
    }


    public List<String> findCatidFromMongo3(String collectionName, BasicDBObject find) throws Exception {
        this.getConnection();
        List<String> result = new ArrayList<>();
        BasicDBObject fields = new BasicDBObject();
        fields.put("catid1", 1);
        FindIterable<Document> documents = mongoDatabase3.getCollection(collectionName).find(find).projection(fields);
        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document object = iterator.next();
            result.add(object.get("catid1").toString());
            // System.err.println(object);
        }
        this.closeConnection();
        return result;
    }

    public long count3(String collectionName, BasicDBObject q) {
        this.getConnection();
        long count = mongoDatabase3.getCollection(collectionName).countDocuments(q);
        this.closeConnection();
        return count;
    }


    public void insert3(String collectionName, Document json) {
        this.getConnection();
        if (mongoDatabase3 != null) {
            mongoDatabase3.getCollection(collectionName).insertOne(json);
        } else {
            logger.error("mongoDatabase3 is null");
        }
        this.closeConnection();
    }


    public int insertBatch3(String collectionName, List<WriteModel<Document>> list) {
        this.getConnection();

        BulkWriteResult result = null;
        if (mongoDatabase3 != null) {
            result = mongoDatabase3.getCollection(collectionName).bulkWrite(list);
        } else {
            logger.error("mongoDatabase3 is null");
        }

        this.closeConnection();
        return result == null ? 0 : result.getModifiedCount();
    }

    public long update3(String collectionName, Bson filter, Bson update) {
        this.getConnection();
        long result = 0;
        if (mongoDatabase3 != null) {
            UpdateResult updateMany = mongoDatabase3.getCollection(collectionName).updateMany(filter, update);
            result = updateMany.getModifiedCount();
        } else {
            logger.error("mongoDatabase3 is null");
        }
        this.closeConnection();
        return result;
    }


    public long updateBatch3(String collectionName, List<UpdateManyModel<Document>> list) {
        this.getConnection();
        BulkWriteResult result = null;
        if (mongoDatabase3 != null) {
            result = mongoDatabase3.getCollection(collectionName).bulkWrite(list);
        } else {
            logger.error("mongoDatabase3 is null");
        }
        this.closeConnection();
        return result == null ? 0 : result.getModifiedCount();
    }

    /**
     * 根据PID的list查询MongoDB中是否存在
     *
     * @param collectionName
     * @param pidList
     * @return
     */
    public List<String> checkIsMongoByList(String collectionName, List<String> pidList) {
        this.getConnection();
        List<String> result = new ArrayList<>();
        BasicDBObject find = new BasicDBObject();
        find.put("pid", new BasicDBObject(QueryOperators.AND, pidList));
        BasicDBObject fields = new BasicDBObject();
        fields.put("pid", 1);
        FindIterable<Document> documents = mongoDatabase3.getCollection(collectionName).find(find).projection(fields);
        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document object = iterator.next();
            result.add(object.get("pid").toString());
        }
        this.closeConnection();
        return result;
    }


    /**
     * 查询PID是否存在
     * @param collectionName
     * @param pid
     * @return
     */
    public boolean checkIsMongoByPid(String collectionName, String pid) {
        this.getConnection();
        boolean rs = false;
        BasicDBObject find = new BasicDBObject();
        find.put("pid", pid);
        BasicDBObject fields = new BasicDBObject();
        fields.put("pid", 1);
        FindIterable<Document> documents = mongoDatabase3.getCollection(collectionName).find(find).projection(fields);
        MongoCursor<Document> iterator = documents.iterator();
        if (iterator.hasNext()) {
            rs = true;
        }
        this.closeConnection();
        return rs;
    }

}