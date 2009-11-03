package bbc.social.shindig.example.service;

/**
 * Relationship service provides interface to users' relationships
 * @author bens
 */
public interface RelationshipService {
	/**
	 * Create a relationship between two users
	 * @param personId
	 * @param friendId
	 */
	public void createRelationship(String personId, String friendId);
}
