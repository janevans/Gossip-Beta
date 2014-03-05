package com.horsepowerx.gossip;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;
import com.horsepowerx.gossip.datastore.GAEDataStoreAsynTask;
import com.horsepowerx.gossip.gossipmessageendpoint.Gossipmessageendpoint;
import com.horsepowerx.gossip.gossipmessageendpoint.model.GossipMessage;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button pullButton = (Button) findViewById(R.id.pullButon);
		pullButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
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
          new GAEDataStoreAsynTask.putGossipMessageAsynTask().execute(message);
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
