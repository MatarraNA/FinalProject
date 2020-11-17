package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import user_module.UserProfile;

/**
 * @author vinny
 *
 * {@summary}
 * A class for JUnit testing setup for the UserProfile class for asserting that searching, adding, and removing all function
 * as expected.
 */
public class TestUserProfile {

	private UserProfile testProfile;
	
	@Test
	public void testSearchUser()
	{
		// clear it
		UserProfile.UserProfileMap.clear();
		
		// create and add profile
		testProfile = new UserProfile("username", "password");
		UserProfile.UserProfileMap.put("testUser", testProfile );
		
		// test user return functions
		assertEquals(testProfile, UserProfile.searchUser("testUser"));
	}
	
	@Test
	public void testRemoveUser()
	{	
		// clear it
		UserProfile.UserProfileMap.clear();
		
		// create and add profile
		testProfile = new UserProfile("username", "password");
		UserProfile.UserProfileMap.put("testUser", testProfile );
		
		// profile current exists in the bag, now test removing it
		UserProfile.removeUser("testUser");
		
		// expected value should now be null
		assertEquals(null, UserProfile.searchUser("testUser") );
	}

	@Test
	public void testAddUser()
	{
		// clear it
		UserProfile.UserProfileMap.clear();
		
		// attempt to add and find our new user
		testProfile = new UserProfile("username", "password");
		UserProfile.UserProfileMap.put("username", testProfile );
		
		// assert it now exists
		assertEquals(testProfile, UserProfile.searchUser("username"));
	}
	
}
