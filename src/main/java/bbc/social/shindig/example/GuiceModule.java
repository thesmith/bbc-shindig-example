package bbc.social.shindig.example;

import java.util.Set;

import org.apache.shindig.social.core.config.SocialApiGuiceModule;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.service.ActivityHandler;
import org.apache.shindig.social.opensocial.service.AppDataHandler;
import org.apache.shindig.social.opensocial.service.MessageHandler;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.MessageService;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.sample.oauth.SampleOAuthDataStore;
import org.apache.shindig.social.sample.oauth.SampleRealm;

import bbc.social.shindig.example.handler.PersonHandlerImpl;
import bbc.social.shindig.example.service.RelationshipService;
import bbc.social.shindig.example.service.impl.JsonDbServiceExample;

import com.google.common.collect.ImmutableSet;
import com.google.inject.name.Names;

/**
 * Example GuiceModule binds everything together
 * 
 * @author bens
 */
public class GuiceModule extends SocialApiGuiceModule {

  @Override
  protected void configure() {
    super.configure();
    bind(String.class).annotatedWith(Names.named("shindig.canonical.json.db"))
        .toInstance("sampledata/canonicaldb.json");

    bind(ActivityService.class).to(JsonDbServiceExample.class);
    bind(AppDataService.class).to(JsonDbServiceExample.class);
    bind(PersonService.class).to(JsonDbServiceExample.class);
    bind(MessageService.class).to(JsonDbServiceExample.class);
    bind(RelationshipService.class).to(JsonDbServiceExample.class);

    bind(OAuthDataStore.class).to(SampleOAuthDataStore.class);

    requestStaticInjection(SampleRealm.class);
  }

  @Override
  protected Set<Object> getHandlers() {
    return ImmutableSet.<Object> of(ActivityHandler.class,
        AppDataHandler.class, PersonHandlerImpl.class, MessageHandler.class);
  }
}
