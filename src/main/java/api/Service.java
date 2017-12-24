package api;

import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONException;

/**
 * Facilitating class to generate random gameID
 * 
 * @author ghughes3
 *
 */
public class Service {

    static Random random = new Random();
    private static final int GAMEID_LENGTH = 6;
    private static final Logger LOGGER = Logger.getLogger(Service.class);

    /**
     * Wrapper method to generate random gameID
     * 
     * @param registration
     * @return
     * @throws JSONException
     */
    public static String generateGameId(Registration registration) throws JSONException {

        String gameId = generateGameIdString();

        if (registration.findByGameId(gameId) != null) {
            return generateGameId(registration);
        }

        return gameId;

    }

    /**
     * Generates gameID
     * 
     * @return
     */
    private static String generateGameIdString() {

        String gameId = "";

        for (int index = 0; index < GAMEID_LENGTH; index++) {
            gameId += (char) (random.nextInt(26) + 'a');
        }

        return gameId;

    }

}
