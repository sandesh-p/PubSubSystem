/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchResults extends Activity {

	String university_name;
	String category;
	String searchKey;
	protected List<String> topicNames;
	protected Handler topicHandler;
	private Runnable displayResults = new Runnable() {
		public void run() {
			reloadInterface();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);

		Bundle bundle = this.getIntent().getExtras();
		category = bundle.getString("category").replaceAll("\\s+", "")
				.concat("_");

		university_name = bundle.getString("university_name")
				.replaceAll("\\s+", "").concat("_");

		searchKey = bundle.getString("searchKey");
		topicHandler = new Handler();
		getTopicNames();

	}

	/*
	 * Reloads the interface with fetched data Assigns the topics to array
	 * adapter
	 */
	private void reloadInterface() {
		ListView list = (ListView) findViewById(R.id.searchListView);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				topicNames);

		list.setAdapter(adapter);

		list.setVisibility(View.VISIBLE);

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
					filterTopics();
					topicHandler.post(displayResults);
				} catch (Throwable e) {

				}
			}

			/*
			 * Filter topics according to the searachKey
			 */
			private void filterTopics() {
				for (int i = 0; i < topicNames.size(); i++) {
					if (!topicNames.get(i).contains(searchKey)) {
						topicNames.remove(i);
					}
				}
			}
		};
		t.start();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_search_results, menu);
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
	 * exit current activity and go back to parent activity
	 */
	public void backAction(View view) {
		finish();
	}

}
