package bbc.social.shindig.example.handler;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.shindig.common.PropertiesModule;
import org.apache.shindig.protocol.ContentTypes;
import org.apache.shindig.protocol.DataServiceServlet;
import org.apache.shindig.protocol.HandlerRegistry;
import org.apache.shindig.protocol.conversion.BeanJsonConverter;
import org.apache.shindig.protocol.conversion.BeanXStreamConverter;
import org.apache.shindig.protocol.model.Enum;
import org.apache.shindig.protocol.model.EnumImpl;
import org.apache.shindig.social.core.model.AddressImpl;
import org.apache.shindig.social.core.model.BodyTypeImpl;
import org.apache.shindig.social.core.model.ListFieldImpl;
import org.apache.shindig.social.core.model.NameImpl;
import org.apache.shindig.social.core.model.OrganizationImpl;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.core.model.UrlImpl;
import org.apache.shindig.social.core.util.BeanXStreamAtomConverter;
import org.apache.shindig.social.core.util.xstream.XStream081Configuration;
import org.apache.shindig.social.dataservice.integration.AbstractLargeRestfulTests;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.Drinker;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.LookingFor;
import org.apache.shindig.social.opensocial.model.NetworkPresence;
import org.apache.shindig.social.opensocial.model.Organization;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.model.Smoker;
import org.apache.shindig.social.opensocial.model.Url;
import org.easymock.EasyMock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import bbc.social.shindig.example.GuiceModule;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * Duplication of https://svn.apache.org/repos/asf/incubator/shindig/trunk/java/social-api/src/test/java/org/apache/shindig/social/dataservice/integration/RestfulJsonPeopleTest.java
 * Added create friends to show how to extend interface
 * @author bens
 */
public class RestfulJsonPeopleTest extends AbstractLargeRestfulTests {
  private Person canonical;

  @SuppressWarnings({ "unchecked", "boxing" })
  @Override
  protected void setUp() throws Exception {
    Injector injector = Guice.createInjector(new PropertiesModule(), new GuiceModule());
    this.setResponse(EasyMock.createMock(HttpServletResponse.class));
    
    // Set data service servlet
    DataServiceServlet servlet = new DataServiceServlet();
    HandlerRegistry dispatcher = injector.getInstance(HandlerRegistry.class);
    dispatcher.addHandlers(injector.getInstance(Key.get(new TypeLiteral<Set<Object>>(){},
        Names.named("org.apache.shindig.social.handlers"))));
    
    servlet.setHandlerRegistry(dispatcher);
    servlet.setBeanConverters(new BeanJsonConverter(injector),
        new BeanXStreamConverter(new XStream081Configuration(injector)),
        new BeanXStreamAtomConverter(new XStream081Configuration(injector)));
    this.setServlet(servlet);
    assertNotNull(servlet);
    
    NameImpl name = new NameImpl("Sir Shin H. Digg Social Butterfly");
    name.setAdditionalName("H");
    name.setFamilyName("Digg");
    name.setGivenName("Shin");
    name.setHonorificPrefix("Sir");
    name.setHonorificSuffix("Social Butterfly");
    canonical = new PersonImpl("canonical", "Shin Digg", name);

    canonical.setAboutMe("I have an example of every piece of data");
    canonical.setActivities(Lists.newArrayList("Coding Shindig"));

    Address address = new AddressImpl("PoBox 3565, 1 OpenStandards Way, Apache, CA");
    address.setCountry("US");
    address.setLatitude(28.3043F);
    address.setLongitude(143.0859F);
    address.setLocality("who knows");
    address.setPostalCode("12345");
    address.setRegion("Apache, CA");
    address.setStreetAddress("1 OpenStandards Way");
    address.setType("home");
    address.setFormatted("PoBox 3565, 1 OpenStandards Way, Apache, CA");
    canonical.setAddresses(Lists.newArrayList(address));

    canonical.setAge(33);
    BodyTypeImpl bodyType = new BodyTypeImpl();
    bodyType.setBuild("svelte");
    bodyType.setEyeColor("blue");
    bodyType.setHairColor("black");
    bodyType.setHeight(1.84F);
    bodyType.setWeight(74F);
    canonical.setBodyType(bodyType);

    canonical.setBooks(Lists.newArrayList("The Cathedral & the Bazaar", "Catch 22"));
    canonical.setCars(Lists.newArrayList("beetle", "prius"));
    canonical.setChildren("3");
    AddressImpl location = new AddressImpl();
    location.setLatitude(48.858193F);
    location.setLongitude(2.29419F);
    canonical.setCurrentLocation(location);

    canonical.setBirthday(new Date());
    canonical.setDrinker(new EnumImpl<Drinker>(Drinker.SOCIALLY));
    ListField email = new ListFieldImpl("work", "shindig-dev@incubator.apache.org");
    canonical.setEmails(Lists.newArrayList(email));

    canonical.setEthnicity("developer");
    canonical.setFashion("t-shirts");
    canonical.setFood(Lists.newArrayList("sushi", "burgers"));
    canonical.setGender(Person.Gender.male);
    canonical.setHappiestWhen("coding");
    canonical.setHasApp(true);
    canonical.setHeroes(Lists.newArrayList("Doug Crockford", "Charles Babbage"));
    canonical.setHumor("none to speak of");
    canonical.setInterests(Lists.newArrayList("PHP", "Java"));
    canonical.setJobInterests("will work for beer");

    Organization job1 = new OrganizationImpl();
    job1.setAddress(new AddressImpl("1 Shindig Drive"));
    job1.setDescription("lots of coding");
    job1.setEndDate(new Date());
    job1.setField("Software Engineering");
    job1.setName("Apache.com");
    job1.setSalary("$1000000000");
    job1.setStartDate(new Date());
    job1.setSubField("Development");
    job1.setTitle("Grand PooBah");
    job1.setWebpage("http://incubator.apache.org/projects/shindig.html");
    job1.setType("job");

    Organization job2 = new OrganizationImpl();
    job2.setAddress(new AddressImpl("1 Skid Row"));
    job2.setDescription("");
    job2.setEndDate(new Date());
    job2.setField("College");
    job2.setName("School of hard Knocks");
    job2.setSalary("$100");
    job2.setStartDate(new Date());
    job2.setSubField("Lab Tech");
    job2.setTitle("Gopher");
    job2.setWebpage("");
    job2.setType("job");

    canonical.setOrganizations(Lists.newArrayList(job1, job2));

    canonical.setUpdated(new Date());
    canonical.setLanguagesSpoken(Lists.newArrayList("English", "Dutch", "Esperanto"));
    canonical.setLivingArrangement("in a house");
    Enum<LookingFor> lookingForRandom =
        new EnumImpl<LookingFor>(LookingFor.RANDOM, "Random");
    Enum<LookingFor> lookingForNetworking =
        new EnumImpl<LookingFor>(LookingFor.NETWORKING, "Networking");
    canonical.setLookingFor(Lists.newArrayList(lookingForRandom, lookingForNetworking));
    canonical.setMovies(Lists.newArrayList("Iron Man", "Nosferatu"));
    canonical.setMusic(Lists.newArrayList("Chieftains", "Beck"));
    canonical.setNetworkPresence(new EnumImpl<NetworkPresence>(NetworkPresence.ONLINE));
    canonical.setNickname("diggy");
    canonical.setPets("dog,cat");
    canonical.setPhoneNumbers(Lists.<ListField> newArrayList(new ListFieldImpl("work",
        "111-111-111"), new ListFieldImpl("mobile", "999-999-999")));

    canonical.setPoliticalViews("open leaning");
    canonical.setProfileSong(new UrlImpl("http://www.example.org/songs/OnlyTheLonely.mp3",
        "Feelin' blue", "road"));
    canonical.setProfileVideo(new UrlImpl("http://www.example.org/videos/Thriller.flv",
        "Thriller", "video"));

    canonical.setQuotes(Lists.newArrayList("I am therfore I code", "Doh!"));
    canonical.setRelationshipStatus("married to my job");
    canonical.setReligion("druidic");
    canonical.setRomance("twice a year");
    canonical.setScaredOf("COBOL");
    canonical.setSexualOrientation("north");
    canonical.setSmoker(new EnumImpl<Smoker>(Smoker.NO));
    canonical.setSports(Lists.newArrayList("frisbee", "rugby"));
    canonical.setStatus("happy");
    canonical.setTags(Lists.newArrayList("C#", "JSON", "template"));
    canonical.setThumbnailUrl("http://www.example.org/pic/?id=1");
    canonical.setUtcOffset(-8L);
    canonical.setTurnOffs(Lists.newArrayList("lack of unit tests", "cabbage"));
    canonical.setTurnOns(Lists.newArrayList("well document code"));
    canonical.setTvShows(Lists.newArrayList("House", "Battlestar Galactica"));

    canonical.setUrls(Lists.<Url>newArrayList(
        new UrlImpl("http://www.example.org/?id=1", "my profile", "Profile"),
        new UrlImpl("http://www.example.org/pic/?id=1", "my awesome picture", "Thumbnail")));
  }
  
  @Test
  public void testCreateFriends() throws Exception {
    Map<String, String> extraParams = Maps.newHashMap();
    extraParams.put("sortBy", "name");
    extraParams.put("sortOrder", null);
    extraParams.put("filterBy", null);
    extraParams.put("startIndex", null);
    extraParams.put("count", "20");
    extraParams.put("fields", null);

    // Currently, for Shindig @all == @friends
    String resp = getResponse("/people/john.doe/@friends", "GET", extraParams, null,
        ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    JSONObject result = getJson(resp);

    assertEquals(3, result.getInt("totalResults"));
    assertEquals(0, result.getInt("startIndex"));

    JSONArray people = result.getJSONArray("entry");

    // The users should be in alphabetical order
    assertPerson(people.getJSONObject(0), "george.doe", "George Doe");
    assertPerson(people.getJSONObject(1), "jane.doe", "Jane Doe");
    
    String postData = "{id: 'canonical'}";
    resp = getResponse("/people/john.doe/@friends", "POST", postData, null,
        ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    
    resp = getResponse("/people/john.doe/@friends", "GET", extraParams, null,
        ContentTypes.OUTPUT_JSON_CONTENT_TYPE);
    result = getJson(resp);

    assertEquals(4, result.getInt("totalResults"));
  }
  
  private void assertPerson(JSONObject person, String expectedId, String expectedName)
      throws Exception {
    assertEquals(expectedId, person.getString("id"));
    assertEquals(expectedName, person.getJSONObject("name").getString("formatted"));
  }
}

