/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

/*This class created a new topic AWS SNS*/
public class CreateTopic extends Activity {

	public static SNSClient snsClient = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_topic);

		snsClient = new SNSClient();
		new CreateTopicTask().execute();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_topic, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Private class extending from AsynchTask to handle UI thread and
	 * to perform the creation of topic in the background.
	 * 
	 * */
	private class CreateTopicTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... voids) {

			try {
				Intent receivedIntent = getIntent();
				PubSubOperations.createTopicAWS(receivedIntent);

			} catch (Throwable e) {

			}

			return null;
		}

		protected void onPostExecute(Void result) {
			finish();
		}
	}
}
