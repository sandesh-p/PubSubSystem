/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import java.util.Properties;

import android.util.Log;

/*
 * This class is used to validate the credentials for AWS SNS
 * This class accesses the access key and secret key provided by AWS SNS
 * */
public class CredentialsCheck {

	private boolean hasCredentials = false;
	private String accessKey = null;
	private String secretKey = null;

	public CredentialsCheck() {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(
					"AwsCredentials.properties"));

			this.accessKey = properties.getProperty("ACCESS_KEY_ID");
			this.secretKey = properties.getProperty("SECRET_KEY");

			if (this.accessKey == null || this.accessKey.equals("")
					|| this.secretKey == null || this.secretKey.equals("")) {
				this.hasCredentials = false;
			} else {
				this.hasCredentials = true;
			}
		}

		catch (Exception exception) {
			Log.e("CredentialsCheck", "Property file could not be read");
		}
	}

	/*
	 * Returns the instance of CredenetialsCheck class
	 * 
	 * @return CredenetialsCheck
	 */
	public static CredentialsCheck getInstance() {
		return new CredentialsCheck();
	}

	/*
	 * Returns true if the CredentialsCheck instance has credentials, false otherwise
	 * 
	 * @return boolean hasCredentials
	 */
	public boolean hasCredentials() {
		return this.hasCredentials;
	}

	/*
	 * Returns access key provided by AWS SNS
	 * 
	 * @return String accessKey
	 */
	public String getAccessKey() {
		return this.accessKey;
	}

	/*
	 * Returns secret key provided by AWS SNS
	 * 
	 * @return String secretKey
	 */
	public String getSecretKey() {
		return this.secretKey;
	}
}