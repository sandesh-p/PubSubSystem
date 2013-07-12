/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;

/*This class gives us the AWS SNS instance*/
public class SNSClient {

	private AmazonSNSClient awsSNSClient = null;
	/*
	 * Default constructor for Amazon SNS Client
	 */
	public SNSClient() {
	}

	/*
	* Returns Amazon SNS client instance
	* 
	* @return	AmazonSNSClient	Instance of Amazon SNS Client
	*/
	public AmazonSNSClient getSNSClient() {
		if (awsSNSClient == null) {
			AWSCredentials awsCredentials = new BasicAWSCredentials(
					CredentialsCheck.getInstance().getAccessKey(),
					CredentialsCheck.getInstance().getSecretKey());
			awsSNSClient = new AmazonSNSClient(awsCredentials);
		}
		return awsSNSClient;
	}
}