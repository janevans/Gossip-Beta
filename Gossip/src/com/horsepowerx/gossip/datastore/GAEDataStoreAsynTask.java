package com.horsepowerx.gossip.datastore;

import java.io.IOException;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.horsepowerx.gossip.CloudEndpointUtils;
import com.horsepowerx.gossip.gossipmessageendpoint.Gossipmessageendpoint;
import com.horsepowerx.gossip.gossipmessageendpoint.model.GossipMessage;

public class GAEDataStoreAsynTask {
	public static class putGossipMessageAsynTask extends AsyncTask<GossipMessage, Void, Void> {
    protected Void doInBackground(GossipMessage ... messages) {
    	Gossipmessageendpoint.Builder builder = new Gossipmessageendpoint.Builder(
          AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
          null);
          
      builder = CloudEndpointUtils.updateBuilder(builder);
      Gossipmessageendpoint endpoint = builder.build();
      try {
      	for(GossipMessage msg : messages) {
      		endpoint.insertGossipMessage(msg).execute();
      	}
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
			return null;
    }
	}
}
