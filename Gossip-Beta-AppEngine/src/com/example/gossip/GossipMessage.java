package com.example.gossip;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class GossipMessage {
  /*
   * Autogenerated primary key
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Key key;

  /*
   * The text of the message sent
   */
  private String message;

  /*
   * Timestamp indicating when this device registered with the application.
   */
  private long timestamp;
  
  /*
   * Precision truncated longitude
   */
  private int longitude;
  
  /*
   * Precision truncated latitude
   */
  private int latitude;
  
  public Key getKey() {
    return key;
  }
  
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
  
  public int getLongitude() {
	  return longitude;
  }
	
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public int getLatitude() {
		return latitude;
	}
	
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
}