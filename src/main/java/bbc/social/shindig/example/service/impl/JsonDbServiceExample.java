package bbc.social.shindig.example.service.impl;

import org.apache.shindig.protocol.conversion.BeanConverter;
import org.apache.shindig.social.sample.spi.JsonDbOpensocialService;
import org.json.JSONArray;
import org.json.JSONException;

import bbc.social.shindig.example.service.RelationshipService;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Simple implementation stores relationships in JsonDb This nieve
 * implementation makes a rather large assumption that friendships are
 * uni-directional
 * 
 * @author bens
 */
@Singleton
public class JsonDbServiceExample extends JsonDbOpensocialService implements
    RelationshipService {

  @Inject
  public JsonDbServiceExample(
      @Named("shindig.canonical.json.db") String jsonLocation,
      @Named("shindig.bean.converter.json") BeanConverter converter)
      throws Exception {
    super(jsonLocation, converter);
  }

  /** {@inheritDoc} */
  public void createRelationship(String personId, String friendId) {
    try {
      if (this.getDb().getJSONObject("friendLinks").has(personId)) {
        JSONArray friends = this.getDb().getJSONObject("friendLinks")
            .getJSONArray(personId);
        boolean found = false;
        for (int i = 0; i < friends.length(); i++) {
          if (friends.getString(i).equals(friendId)) {
            found = true;
          }
        }
        if (!found) {
          friends.put(friendId);
          this.getDb().getJSONObject("friendLinks").put(personId, friends);
        }
      } else {
        JSONArray friends = new JSONArray();
        friends.put(friendId);
        this.getDb().getJSONObject("friendLinks").put(personId, friends);
      }
    } catch (JSONException e) {
      throw new RuntimeException("unable to work with json db", e);
    }
  }
}
