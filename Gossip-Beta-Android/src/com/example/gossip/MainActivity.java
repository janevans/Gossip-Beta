package com.example.gossip;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gossip.gossipmessageendpoint.Gossipmessageendpoint;
import com.example.gossip.gossipmessageendpoint.Gossipmessageendpoint.ListGossipMessage;
import com.example.gossip.gossipmessageendpoint.model.GossipMessage;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

public class MainActivity extends Activity {      

	/**
	 * EndPoint to send GossipMessage;
	 */
  Gossipmessageendpoint endpoint;
  
	@Override
  protected void onCreate(Bundle savedInstanceState) {
  	Gossipmessageendpoint.Builder builder = new Gossipmessageendpoint.Builder(
	      AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
	      null);
		builder = CloudEndpointUtils.updateBuilder(builder);
		endpoint = builder.build();
		
		final Button pullButton = (Button) findViewById(R.id.pullButon);
		pullButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	EditText showPlace = (EditText) findViewById(R.id.showPlace);
        	String showMsg = "";
        	try {
						ListGossipMessage listGossipMessage = endpoint.listGossipMessage();
						for(GossipMessage mesg : listGossipMessage.execute().getItems()) {
							showMsg = String.format("%s\n(%s,%s):%s\n",
												  showMsg,
													mesg.getLongitude(),
													mesg.getLatitude(),
													mesg.getMessage()
							);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	showPlace.setText(showMsg);
        }
    });
    
    final Button addButton = (Button) findViewById(R.id.addButton);
    addButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	EditText messageContent = (EditText) findViewById(R.id.message);
        	EditText longitude = (EditText) findViewById(R.id.longitude);
        	EditText latitude = (EditText) findViewById(R.id.latitude);
        	GossipMessage message = new GossipMessage();
        	message.setMessage(messageContent.getText().toString());
        	message.setLatitude(Integer.valueOf(latitude.getText().toString()));
        	message.setLongitude(Integer.valueOf(longitude.getText().toString()));
        }
    });
		
		
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  

  private String addGossipMessage(String message) {
  	GossipMessage gossip = new GossipMessage();
      
  	// Set the ID of the store where the user is. 
  	// This would be replaced by the actual ID in the final version of the code. 
  	gossip.setMessage("Message");
  	try {
  		endpoint.insertGossipMessage(gossip);
  	} catch (IOException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}

  	return null;
  }

}
