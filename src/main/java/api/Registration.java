package api;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * Data layer class to connect and make queries to database
 * 
 * @author ghughes3
 *
 */
public class Registration {

    // database constants
    private static final String DBAddress = "localhost";
    private static final int DBPort = 27017;
    private static final String DBName = "codenames_db";

    private DBCollection games;

    private static final Logger LOGGER = Logger.getLogger(Registration.class);

    /**
     * Constructor for the Registration class. Connects to database
     */
    public Registration() {

        try {
            MongoClient mongoClient = new MongoClient(DBAddress, DBPort);
            DB db = mongoClient.getDB(DBName);
            games = db.getCollection("games");
        } catch (Exception e) {
            LOGGER.fatal("Could not locate internal database");
        }

    }

    /**
     * Finds game instance in database given gameID
     * 
     * @param gameId
     * @return
     * @throws JSONException
     */
    public DBObject findByGameId(String gameId) throws JSONException {

        BasicDBObject query = new BasicDBObject();
        query.put("gameId", gameId);
        DBObject document = games.findOne(query);

        if (document == null) {

            return null;
        }
        
        return document;

    }

    /**
     * Inserts game instance in database
     * 
     * @param gameId
     * @param hostAddress
     * @return
     */
    public boolean insertHostAddressAndGameId(String gameId, String hostAddress) {

        BasicDBObject document = new BasicDBObject();
        document.put("gameId", gameId);
        document.put("hostAddress", hostAddress);
        games.insert(document);

        return true;

    }

    /**
     * Get host address from database given gameID
     * 
     * @param gameId
     * @return
     * @throws JSONException
     */
    public String getHostAddressFromGameId(String gameId) throws JSONException {
        
        DBObject document = findByGameId(gameId);
        
        if (document == null) {
            LOGGER.fatal("No host address found with GameID: " + gameId);
            return null;
        }

        return new JSONObject(JSON.serialize(findByGameId(gameId))).get("hostIp").toString();

    }
    
    /**
     * Delete game instance from database given gameID
     * 
     * @param gameId
     * @return
     * @throws JSONException
     */
    public boolean deleteByGameId(String gameId) throws JSONException {
     
        DBObject document = findByGameId(gameId);
        
        if (document == null) {
            LOGGER.fatal("No host address found with GameID: " + gameId);
            return false;
        }
        
        games.remove(document);
        
        return true;
       
    }
    
    
    
    
    
    
    
    
    
    
    
    

}
