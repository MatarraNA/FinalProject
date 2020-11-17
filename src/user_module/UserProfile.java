package user_module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.UUID;

import hikingHistory.HikingHistory;

/**
 * 
 * @author vinny
 *
 * Implements serializable to allow for easy file read / write.
 * Contains a MAIN UserProfileMap (treemap) that contains all user profiles written to the file. This map gets serialized every time
 * a change occurs to allow for an efficient autosave.
 * 
 * Each user contains a linkedlist for there hiking history which gets serialized along with the TreeMap.
 */
@SuppressWarnings("serial")
public class UserProfile implements Serializable {

	// initialize main treemap
	public static TreeMap<String,UserProfile> UserProfileMap = new TreeMap<String,UserProfile>();
	public static UserProfile loggedInUser = null;
		
	// misc vars
	private String username;
	private String password;
	private String userId;
	private boolean isAdmin;
	
	// history trails
	public LinkedList<HikingHistory> hikingHistoryList = new LinkedList<HikingHistory>();
	
	// constructor
	public UserProfile(String username, String password )
	{
		this.username = username;
		this.password = password;
		this.userId = UUID.randomUUID().toString(); // generate random id for this user
	}

	// getters setters
	public int GetCompletedTrails()
	{
		int count = 0;
		for( @SuppressWarnings("unused") HikingHistory history : hikingHistoryList )
		{
			count++;
		}
		return count;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean input) {
		this.isAdmin = input;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String GetUserId()
	{
		return this.userId;
	}
	
	// Static Methods
	public static UserProfile searchUser(String username)
	{
		return UserProfileMap.get(username);
	}
	
	public static void removeUser(String username)
	{
		UserProfileMap.remove(username);
	}
	
	public static void addUser(UserProfile user)
	{
		if( user == null ) return;
		UserProfileMap.put(user.getUsername(), user);
	}
	
	public static void writeUsersToFile()
	{
		try
		{
			//Saving of object in a file 
			FileOutputStream file = new FileOutputStream("userstore.txt"); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			
			// Method for serialization of object 
			out.writeObject(UserProfileMap); 
			
			out.close(); 
			file.close();			
		}
		catch( Exception ex ) {
			//ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void readUsersFromFile()
	{
		// Deserialization 
        try
        {    
            // Reading the object from a file 
            FileInputStream file = new FileInputStream("userstore.txt"); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            UserProfileMap = (TreeMap<String,UserProfile>)in.readObject(); 
              
            // null?
            if( UserProfileMap == null )
            	UserProfileMap = new TreeMap<String,UserProfile>();
            
            in.close(); 
            file.close(); 
        } 
        catch( Exception ex ) {
        	//ex.printStackTrace();
        }
	}
}
