package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Entry point to the API
 *
 * @author ghughes3
 * @version 13 November 2017
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {

    // Runs the Eureka Discover Service
    public static void  main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}