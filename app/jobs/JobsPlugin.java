package jobs;

import akka.actor.ActorRef;
import akka.actor.Props;
import org.apache.commons.lang3.StringUtils;
import play.Application;
import play.Plugin;
import play.libs.Akka;
import scala.collection.mutable.ArraySeq;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by PENTAGON on 15.08.14.
 */
public class JobsPlugin extends Plugin {

    private final Application application;
    private ActorRef createJobInstance;

    public JobsPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        createJobInstance = Akka.system().actorOf(Props.create(CreateAdJob.class, new ArraySeq<>(0)));
        Akka.system().scheduler().schedule(
                Duration.Zero(),
                Duration.create(1, TimeUnit.SECONDS),
                createJobInstance, StringUtils.EMPTY, Akka.system().dispatcher(),
                null
        );
    }
}