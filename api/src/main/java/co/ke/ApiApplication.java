package co.ke;

import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class name: ApiApplication
 * Creater: wgicheru
 * Date:4/18/2019
 */
public class ApiApplication extends AbstractVerticle {
    private static final Logger LOGGER = LogManager.getLogger(ApiApplication.class);

    private EventBus eventBus;

    /**
     * override the start method to instantiate verticle
     * fails to start in case of throw exception
     *
     * @throws Exception
     */
    public void start() throws Exception{
        eventBus = vertx.eventBus();

        Router router = Router.router(vertx);

        //sort out CORS, allow all but for security concerns, limit this to known addresses
        router.route().handler(CorsHandler.create("*").allowedHeaders(getAllowedHeaders())
                .allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.GET));

        router.post("/").handler(this::authenticationHandler).handler(this::postHandler);
        router.get("/").handler(this::getHandler);

        vertx.createHttpServer().requestHandler(router).rxListen(8080).subscribe(
                (success)->{
                    LOGGER.info("Server listening on port 8080");
                },
                (failure)->{
                    LOGGER.error(failure.getMessage(),failure.getCause());
                });
    }
}
