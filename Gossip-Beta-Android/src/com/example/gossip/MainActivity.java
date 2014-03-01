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
  
  
	@Override
  protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button pullButton = (Button) findViewById(R.id.pullButon);
		pullButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	EditText showPlace = (EditText) findViewById(R.id.showPlace);
        	String showMsg = "";
        	try {
        		Gossipmessageendpoint endpoint = getGossipmessageendpoint();
						ListGossipMessage listGossipMessage = endpoint.listGossipMessage();
						for(GossipMessage mesg : listGossipMessage.execute().getItems()) {
							showMsg = String.format("%s\n(%s,%s):%s",
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
        	System.out.println(String.format("(%s,%s):%s\n",
							message.getLongitude(),
							message.getLatitude(),
							message.getMessage()));
        	try {
        		
        		getGossipmessageendpoint().insertGossipMessage(message).execute();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  public Gossipmessageendpoint getGossipmessageendpoint() {
  	Gossipmessageendpoint endpoint;
		Gossipmessageendpoint.Builder builder = new Gossipmessageendpoint.Builder(
	      AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
	      null);
		builder = CloudEndpointUtils.updateBuilder(builder);
		endpoint = builder.build();
		return endpoint;
  }

}
