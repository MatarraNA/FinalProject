package hikingHistory;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @author vinny
 *
 * This class implements serializable to allow us to write our history linkedlist to a file for easy read / write.
 * It just contains private data for our history object as well as getters / setters.
 */
@SuppressWarnings("serial")
public class HikingHistory implements Serializable {
	
	private String trailName;
	private LocalDate dateTraversed;
	private float distance;
	private float duration;
	private int picturesTaken;
	private float averagePace;
	
	public HikingHistory(String name, LocalDate dateTraversed, float trailDistance, float trailDuration, int picturesTaken )
	{
		this.trailName = name;
		this.dateTraversed = dateTraversed;
		this.distance = trailDistance;
		this.duration = trailDuration;
		this.picturesTaken = picturesTaken;
		
		// calculate average pace
		// distance is in miles, duration is in days
		this.averagePace = this.distance / (this.duration * 24);
	}

	public String getTrailName() {
		return trailName;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

	public LocalDate getDateTraversed() {
		return dateTraversed;
	}

	public void setDateTraversed(LocalDate dateTraversed) {
		this.dateTraversed = dateTraversed;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public int getPicturesTaken() {
		return picturesTaken;
	}

	public void setPicturesTaken(int picturesTaken) {
		this.picturesTaken = picturesTaken;
	}

	public float getAveragePace() {
		return averagePace;
	}

	public void setAveragePace(float averagePace) {
		this.averagePace = averagePace;
	}
}
