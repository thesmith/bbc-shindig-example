package bbc.social.shindig.example.service;

import static org.junit.Assert.*;

import java.util.Collections;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.testing.FakeGadgetToken;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.protocol.model.FilterOperation;
import org.apache.shindig.protocol.model.SortOrder;
import org.apache.shindig.social.SocialApiTestsGuiceModule;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.junit.Before;
import org.junit.Test;

import bbc.social.shindig.example.service.impl.JsonDbServiceExample;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests the example implementation of extended shindig service
 * @author bens
 */
public class JsonDbServiceExampleTest {
	private static final UserId JOHN_DOE = new UserId(UserId.Type.userId, "john.doe");
	private SecurityToken token = new FakeGadgetToken();
	private JsonDbServiceExample service;
	
	@Before
  public void setUp() throws Exception {
    Injector injector = Guice.createInjector(new SocialApiTestsGuiceModule());
    service = injector.getInstance(JsonDbServiceExample.class);
  }
	
	@Test
	public void shouldCreateFriendship() throws Exception {
		CollectionOptions options = new CollectionOptions();
    options.setSortBy(PersonService.TOP_FRIENDS_SORT);
    options.setSortOrder(SortOrder.ascending);
    options.setFilter(null);
    options.setFilterOperation(FilterOperation.contains);
    options.setFilterValue("");
    options.setFirst(0);
    options.setMax(20);

    RestfulCollection<Person> responseItem = service.getPeople(
        ImmutableSet.of(JOHN_DOE), new GroupId(GroupId.Type.friends, null),
        options, Collections.<String>emptySet(), token).get();
    assertNotNull(responseItem);
    assertEquals(3, responseItem.getTotalResults());
    // Test a couple of users
    assertEquals("jane.doe", responseItem.getEntry().get(0).getId());
    
    service.createRelationship("john.doe", "canonical");
    
    responseItem = service.getPeople(
        ImmutableSet.of(JOHN_DOE), new GroupId(GroupId.Type.friends, null),
        options, Collections.<String>emptySet(), token).get();
    assertNotNull(responseItem);
    assertEquals(4, responseItem.getTotalResults());
	}
}
