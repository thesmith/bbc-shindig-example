package bbc.social.shindig.example.handler;

import java.util.Set;
import java.util.concurrent.Future;

import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.config.ContainerConfig;
import org.apache.shindig.protocol.HandlerPreconditions;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.Service;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.service.PersonHandler;
import org.apache.shindig.social.opensocial.service.SocialRequestItem;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;

import bbc.social.shindig.example.service.RelationshipService;

import com.google.inject.Inject;

/**
 * Extends shindig's own PersonHandler to add the ability to create new friends
 * 
 * @author bens
 */
@Service(name = "people", path = "/{userId}+/{groupId}/{personId}+")
public class PersonHandlerImpl extends PersonHandler {
  private final RelationshipService relationshipService;

  @Inject
  public PersonHandlerImpl(PersonService personService,
      RelationshipService relationshipService, ContainerConfig config) {
    super(personService, config);
    this.relationshipService = relationshipService;
  }

  /**
   * Create a new friendship between 2 users POST - {id: 'friendId'}
   * /people/{userId}/@friends
   * 
   * @param request
   * @return
   * @throws ProtocolException
   */
  @Operation(httpMethods = "POST", path = "/{userId}+/@friends")
  public Future<?> createFriends(SocialRequestItem request)
      throws ProtocolException {
    Set<UserId> userIds = request.getUsers();
    Person person = request.getTypedParameter("body", Person.class);
    HandlerPreconditions.requireNotEmpty(userIds, "No userId specified");
    HandlerPreconditions.requireSingular(userIds,
        "Multiple userIds not supported");

    if (person == null || person.getId() == null || person.getId().length() < 1) {
      throw new IllegalArgumentException(
          "Cannot create relationship without a person to befriend");
    }
    UserId userId = userIds.iterator().next();
    this.relationshipService.createRelationship(userId.getUserId(request
        .getToken()), person.getId());

    return ImmediateFuture.newInstance(null);
  }
}
