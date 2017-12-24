package api;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for the API, containing the endpoints for the codenames app
 * to hit
 * 
 * @author grant hughes
 * 
 */
@RestController
public class MainController {

    private Registration registration;

    /**
     * Endpoint to create a game instance. Generates a gameID and saves pair with
     * host IP address in database
     * 
     * @param requestContext
     * @return gameID with Status OK or error message with status Internal Server
     *         Error
     * @throws JSONException
     */
    @RequestMapping(value = "/gamecreate", method = RequestMethod.POST)
    public ResponseEntity<Object> gamecreate(HttpServletRequest requestContext) throws JSONException {

        // Connecting to database
        try {
            registration = new Registration();
        } catch (Exception e) {
            return new ResponseEntity<>("Could not locate internal database", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Generates gameID
        String gameId = Service.generateGameId(registration);

        // Gets IP Address sent from requester
        String hostAddress = requestContext.getRemoteAddr();

        // Inserts gameID and host address into database
        if (!registration.insertHostAddressAndGameId(gameId, hostAddress)) {
            return new ResponseEntity<>("Could not create game", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(gameId, HttpStatus.CREATED);
    }

    /**
     * Endpoint to join a game instance. Gets host address from database given the
     * gameID
     * 
     * @param gameId
     * @return
     * @throws JSONException
     */
    @RequestMapping(value = "/gamejoin", method = RequestMethod.POST)
    public ResponseEntity<Object> gamejoin(@RequestParam("gameId") String gameId) throws JSONException {

        // Connecting to database
        try {
            registration = new Registration();
        } catch (Exception e) {
            return new ResponseEntity<>("Could not locate internal database", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Gets host IP Address from database
        String hostAddress = registration.getHostAddressFromGameId(gameId);

        if (hostAddress == null) {
            return new ResponseEntity<>("No host address found with GameID: " + gameId, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(hostAddress, HttpStatus.OK);
    }

    /**
     * Deletes game instance from database
     * 
     * @param gameId
     * @return
     * @throws JSONException
     */
    @RequestMapping(value = "/gametakedown", method = RequestMethod.DELETE)
    public ResponseEntity<Object> gametakedown(@RequestParam("gameId") String gameId) throws JSONException {

        // Connecing to database
        try {
            registration = new Registration();
        } catch (Exception e) {
            return new ResponseEntity<>("Could not locate internal database", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Deletes game instance from database
        if (!registration.deleteByGameId(gameId)) {
            return new ResponseEntity<>("Could not delete game with GameID: " + gameId,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Entry deleted", HttpStatus.OK);
    }

}