package trail_module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * 
 * @author vinny
 *
 * This class implements Serializable to allow for easy file read / write.
 * It contains a MAIN static treemap that holds all the trails the user will be able to access and add to there collection.
 */
@SuppressWarnings("serial")
public class Trail implements Serializable {
	
	/** 
	 * Main trail bag, key is trail name. must be unique.
	 */
	public static TreeMap<String,Trail> MainTreeMap = new TreeMap<String,Trail>();
	
	private String trailName;
	private String trailHeadAddress;
	private float length;
	private float elevationGain;
	private TrailDifficulty difficulty;
	private TrailType type;
	
	public String getTrailName() {
		return trailName;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

	public String getTrailHeadAddress() {
		return trailHeadAddress;
	}

	public void setTrailHeadAddress(String trailHeadAddress) {
		this.trailHeadAddress = trailHeadAddress;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getElevationGain() {
		return elevationGain;
	}

	public void setElevationGain(float elevationGain) {
		this.elevationGain = elevationGain;
	}

	public TrailDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(TrailDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public TrailType getType() {
		return type;
	}

	public void setType(TrailType type) {
		this.type = type;
	}

	public Trail(String name, String address, float length, float elevationGain, TrailDifficulty diff, TrailType type )
	{
		this.trailName = name;
		this.trailHeadAddress = address;
		this.length = length;
		this.elevationGain = elevationGain;
		
		// misc
		this.difficulty = diff;
		this.type = type;
	}
	
	public static void writeTrailsToFile()
	{
		try
		{
			//Saving of object in a file 
			FileOutputStream file = new FileOutputStream("Trails_Serialized.txt"); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			
			// Method for serialization of object 
			out.writeObject(MainTreeMap); 
			
			out.close(); 
			file.close();			
		}
		catch( Exception ex ) {
			//ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void readTrailsFromFile()
	{
		// Deserialization 
        try
        {    
            // Reading the object from a file 
            FileInputStream file = new FileInputStream("Trails_Serialized.txt"); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            MainTreeMap = (TreeMap<String,Trail>)in.readObject(); 
              
            // null?
            if( MainTreeMap == null )
            	MainTreeMap = new TreeMap<String,Trail>();
            
            in.close(); 
            file.close(); 
        } 
        catch( Exception ex ) {
        	//ex.printStackTrace();
        }
	}
}