/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/*This class publishes a new message to a particular topic using AWS SNS*/
public class PublishToTopic extends Activity {

	protected Handler topicHandler;
	protected Spinner topicSpinner;
	protected ArrayAdapter<String> topicArrayAdapter;
	protected EditText messagePublishInput;
	protected Button publishButton;
	protected List<String> topicList;
	String university_name;
	String category;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_to_topic);
		Bundle bundle = this.getIntent().getExtras();
		university_name = bundle.getString("university_name");
		category = bundle.getString("category");
		topicSpinner = (Spinner) findViewById(R.id.topic_spinner);
		messagePublishInput = (EditText) findViewById(R.id.publish_message_input);
		publishButton = (Button) findViewById(R.id.publish_button);
		topicHandler = new Handler();
		getTopicList();
	}

	/*
	 * Fetches the topics from Amazon SNS
	 */
	public void getTopicList() {
		Thread topicThread = new Thread() {
			public void run() {
				try {
					topicList = PubSubOperations.getTopics(university_name,category);
					topicHandler.post(displayTopics);
				} catch (Throwable e) {

				}
			}
		};

		topicThread.start();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_publish_to_topic, menu);
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
	 * Starts the thread to reload the interface
	 */
	private Runnable displayTopics = new Runnable() {

		public void run() {
			reloadInterface();
		}
	};

	/*
	 * Reloads the interface with fetched data Assigns the topics to array
	 * adapter
	 */
	private void reloadInterface() {
		topicArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, topicList);
		topicSpinner.setAdapter(topicArrayAdapter);
		linkPublishButton();
	}

	/*
	 * Invokes the PublishTask UI thread when Publish button is clicked
	 */
	private void linkPublishButton() {
		publishButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				topicSpinner.setVisibility(View.INVISIBLE);
				messagePublishInput.setVisibility(View.INVISIBLE);
				new PublishTask().execute();
			}
		});
	}

	/*
	 * Private class extending from AsynchTask to handle UI thread and to
	 * perform the publishing of message related to topic in the background.
	 */
	private class PublishTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... voids) {

			try {
				String appendText = "arn:aws:sns:us-east-1:525576497761:"
						+ (university_name+category);

				PubSubOperations.publishTopic(appendText
						+ topicSpinner.getSelectedItem().toString(),
						messagePublishInput.getText().toString());

			} catch (Throwable e) {
				System.out.println(e.toString());
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			finish();
		}
	}

}
