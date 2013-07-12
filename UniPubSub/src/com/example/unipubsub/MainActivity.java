/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*This class is the starting point for the app*/
public class MainActivity extends Activity {
	String university_name;
	String category;
	public final static String EXTRA_MESSAGE = "com.example.unipubsub.MESSAGE";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle bundle = this.getIntent().getExtras();
		category = bundle.getString("category").replaceAll("\\s+", "")
				.concat("_");

		university_name = bundle.getString("university_name")
				.replaceAll("\\s+", "").concat("_");
		
		TextView textView = (TextView) findViewById(R.id.welcomeText);
		textView.setText("Welcome to " + bundle.getString("university_name")
				+ "'s" + " UniPubSub system");

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*
	 * Starts the activity to create topic
	 * 
	 * @param View view
	 */
	public void createTopic(View view) {

		Intent createTopicIntent = new Intent(this, CreateTopic.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String topic = editText.getText().toString();

		String modifiedTopic = university_name + category + topic;

		System.out.println(modifiedTopic);

		createTopicIntent.putExtra(EXTRA_MESSAGE, modifiedTopic);
		startActivity(createTopicIntent);
	}

	/*
	 * Starts the activity to publish message in a topic
	 * 
	 * @param View view
	 */
	public void publishToTopic(View view) {
		Intent publishIntent = new Intent(this, PublishToTopic.class);

		publishIntent.putExtra("university_name", university_name);
		publishIntent.putExtra("category", category);
		startActivity(publishIntent);
	}

	/*
	 * Starts the activity to subscribe to a created topic
	 * 
	 * @param View view
	 */
	public void subscribeToTopic(View view) {
		Intent subscribeIntent = new Intent(this, SubscribeToTopic.class);

		subscribeIntent.putExtra("university_name", university_name);
		subscribeIntent.putExtra("category", category);
		startActivity(subscribeIntent);
	}
}
