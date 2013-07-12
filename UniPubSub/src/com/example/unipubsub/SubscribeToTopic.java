/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 4th 2012
 * 
 * */

package com.example.unipubsub;

/*This class provides the functionality to subscribe to a specific topic
 * This development is under cosntruction presently*/
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class SubscribeToTopic extends Activity {
	protected ArrayAdapter<String> topicsAdapter;
	protected Spinner topicSpinner;
	protected EditText emailInput;
	protected Button subscribeButton;
	protected Handler topicHandler;
	protected List<String> topicNames;
	String university_name;
	String category;
	private Runnable displayResults = new Runnable() {
		public void run() {
			reloadInterface();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_to_topic);

		Bundle bundle = this.getIntent().getExtras();
		university_name = bundle.getString("university_name");
		category = bundle.getString("category");
		topicSpinner = (Spinner) findViewById(R.id.subTopicSpinner);
		emailInput = (EditText) findViewById(R.id.emailInput);
		subscribeButton = (Button) findViewById(R.id.subscribeButton);
		topicHandler = new Handler();
		getTopicNames();
	}

	/*
	 * Fetches the topics from Amazon SNS
	 */
	private void getTopicNames() {
		Thread t = new Thread() {

			public void run() {
				try {
					topicNames = PubSubOperations.getTopics(university_name,
							category);

					topicHandler.post(displayResults);
				} catch (Throwable e) {

				}
			}
		};
		t.start();
	}

	/*
	 * Reloads the interface with fetched data Assigns the topics to array
	 * adapter
	 */
	private void reloadInterface() {
		topicsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
				topicNames);
		topicSpinner.setAdapter(topicsAdapter);
		topicSpinner.setVisibility(View.VISIBLE);
		linkSubscribeButton();
	}

	/*
	 * Invokes the SubscribeTask UI thread when Publish button is clicked
	 */
	private void linkSubscribeButton() {
		subscribeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				topicSpinner.setVisibility(View.INVISIBLE);
				new SubscribeTask().execute();
			}
		});
	}

	/*
	 * Private class extending from AsynchTask to handle UI thread and to allow
	 * the users to subscribe to a topic
	 */
	private class SubscribeTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... voids) {

			try {
				String appendText = "arn:aws:sns:us-east-1:525576497761:"
						+ university_name;
				String modifiedText = (appendText + category)
						+ (String) topicSpinner.getSelectedItem().toString();				
				PubSubOperations.subscribeTopic(modifiedText, emailInput
						.getText().toString());

			} catch (Throwable e) {

			}

			return null;
		}

		protected void onPostExecute(Void result) {
			finish();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_subscribe_to_topic, menu);
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

}
