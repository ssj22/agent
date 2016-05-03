package net.cloudamize.agent;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import net.cloudamize.agent.config.DataSourceConfiguration;
import net.cloudamize.agent.config.JOOQConfiguration;
import net.cloudamize.agent.verticle.HttpVerticle;
import net.cloudamize.agent.verticle.SetupVerticle;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

@SpringBootApplication
@Configuration
@Import({DataSourceConfiguration.class, JOOQConfiguration.class})
@ComponentScan("net.cloudamize.agent")
@Slf4j
public class SpringBootApp {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootApp.class);

    public static final String SETUP_DEPLOYMENT_OPTIONS = "setup.deployment.options";
    public static final String VERTX_DEPLOYMENT_OPTIONS = "vertx.deployment.options";
    public static final String HTTP_DEPLOYMENT_OPTIONS = "http.deployment.options";
    public static final String VERTICLE_FACTORY_SPRING_EXT_PREFIX = "java-spring:";

    public static void main(String[] args) {

        try {
            // Start and bootstrap Spring application
            SpringApplication app = new SpringApplication(SpringBootApp.class);
            app.setBannerMode(Banner.Mode.OFF);
            app.run(args);

            // Config objects for Vertx and Verticle instance startup
            VertxOptions vertxOptions;
            JsonObject jsonConf;
            String descriptorFile = "conf.json";

            try (InputStream is = SpringBootApp.class.getClassLoader().getResourceAsStream(descriptorFile)) {
                if (is == null) {
                    throw new IllegalArgumentException("Cannot find service descriptor file " + descriptorFile + " on classpath");
                }
                try (Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A")) {
                    String conf = scanner.next();
                    jsonConf = new JsonObject(conf);
                } catch (NoSuchElementException e) {
                    throw new IllegalArgumentException(descriptorFile + " is empty");
                } catch (DecodeException e) {
                    throw new IllegalArgumentException(descriptorFile + " contains invalid json");
                }
            }
            // Vertx deploy options
            vertxOptions = new VertxOptions(jsonConf.getJsonObject(VERTX_DEPLOYMENT_OPTIONS));
            //vertxOptions.setMaxEventLoopExecuteTime(4l * 1000 * 1000000);

            // Verticle deploy options
            DeploymentOptions setupVerticleOptions = new DeploymentOptions(jsonConf.getJsonObject(SETUP_DEPLOYMENT_OPTIONS));
            DeploymentOptions httpVerticleOptions = new DeploymentOptions(jsonConf.getJsonObject(HTTP_DEPLOYMENT_OPTIONS));

            // Set HTTP Verticle to the same number of instances as there are event loops
            httpVerticleOptions.setInstances(vertxOptions.getEventLoopPoolSize());

            Vertx.clusteredVertx(vertxOptions, vertx -> {
                vertx.result().deployVerticle(VERTICLE_FACTORY_SPRING_EXT_PREFIX + SetupVerticle.class.getName()
                                                , setupVerticleOptions);

                vertx.result().deployVerticle(VERTICLE_FACTORY_SPRING_EXT_PREFIX + HttpVerticle.class.getName()
                                                , httpVerticleOptions);
            });
            log.debug("Just for Fun");
        } catch (Exception e) {
            logger.error("Exception while initializing SpringBootApp: " + e.getMessage(), e);
        }

    }


}
