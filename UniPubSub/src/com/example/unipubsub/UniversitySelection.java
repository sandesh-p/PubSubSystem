/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class UniversitySelection extends Activity {

	private String stateArray[];
	private String universityArray[];
	private String categoryArray[];
	private ArrayAdapter<String> stateArrayAdapter;
	private ArrayAdapter<String> universityArrayAdapter;
	private ArrayAdapter<String> categoryArrayAdapter;
	private Scanner scanStateFile;
	private Scanner scanUniversityFile;
	private Scanner scancategoryFile;
	private InputStream stateListInputStream;
	private InputStream universityListInputStream;
	private InputStream categoryListInputStream;
	private String stateName;
	Spinner universitySpinner;
	Spinner categorySpinner;
	protected EditText searchKeyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_university_selection);
		searchKeyword = (EditText) findViewById(R.id.searchKeyEdittext);
		int counter = 0;

		stateArray = new String[3];

		try {

			stateListInputStream = getAssets().open("StateList.txt");
		} catch (IOException ioe) {
			System.out.println("File not found: " + ioe.toString());
		}

		scanStateFile = new Scanner(stateListInputStream);

		while (scanStateFile.hasNextLine() && counter < stateArray.length) {
			stateArray[counter] = scanStateFile.nextLine();
			counter++;
		}

		final Spinner stateSpinner = (Spinner) findViewById(R.id.StateListSpinner);

		stateArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, stateArray);

		stateSpinner.setAdapter(stateArrayAdapter);

		stateSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						stateName = stateSpinner.getSelectedItem().toString();

						if (!stateName.equals("Select state")) {

							String selectedState = stateName + ".txt";
							populateUniversities(selectedState.trim());
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		populateCategories();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * This method populates the spinner with University lists according to the
	 * selected state
	 * 
	 * @param selectedState
	 */
	public void populateUniversities(String selectedState) {
		int universityCounter = 0;
		universityArray = new String[2];

		try {
			universityListInputStream = getAssets().open(selectedState);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		scanUniversityFile = new Scanner(universityListInputStream);

		while (scanUniversityFile.hasNextLine()
				&& universityCounter < universityArray.length) {
			universityArray[universityCounter] = scanUniversityFile.nextLine();
			universityCounter++;
		}

		universitySpinner = (Spinner) findViewById(R.id.UniversityListSpinner);
		universityArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, universityArray);

		universitySpinner.setAdapter(universityArrayAdapter);

	}

	/**
	 * This method populates the spinner with categories lists according
	 * CategoryListFile
	 * 
	 */
	public void populateCategories() {
		categoryArray = new String[6];
		int categoryCounter = 0;
		try {
			categoryListInputStream = getAssets().open("CategoryList.txt");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		scancategoryFile = new Scanner(categoryListInputStream);

		while (scancategoryFile.hasNextLine()) {
			categoryArray[categoryCounter] = scancategoryFile.nextLine();
			categoryCounter++;
		}
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		categoryArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categoryArray);

		categorySpinner.setAdapter(categoryArrayAdapter);
	}

	public void submitAction(View view) {
		Intent startIntent = new Intent(this, MainActivity.class);

		startIntent.putExtra("university_name", universitySpinner
				.getSelectedItem().toString());

		startIntent.putExtra("category", categorySpinner.getSelectedItem()
				.toString());
		startActivity(startIntent);
	}

	/**
	 * This method starts the search activity by passing the selected university
	 * name, category and search keyword
	 * 
	 */
	public void search(View view) {

		Intent searchIntent = new Intent(this, SearchResults.class);

		searchIntent.putExtra("university_name", universitySpinner
				.getSelectedItem().toString());

		searchIntent.putExtra("category", categorySpinner.getSelectedItem()
				.toString());

		searchIntent.putExtra("searchKey", searchKeyword.getText().toString());

		startActivity(searchIntent);
	}
}
