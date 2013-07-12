/*
 * Sandesh Pardeshi <ssp9237@rit.edu>, 
 * Koustubh Kulkarni <krk9372@rit.edu>
 * Date: November 13th 2012
 * 
 * */

package com.example.unipubsub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;

import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Topic;

/*This class provides access to static methods to create topics, publish and subscribe to topics*/
public class PubSubOperations {

	static SNSClient snsClient = null;

	/*
	 * Creates the topic on Amazaon SNS
	 * 
	 * @param Intent receivedIntent
	 * 
	 * @return CreateTopicResult Result after creating topic
	 */
	public static CreateTopicResult createTopicAWS(Intent receivedIntent) {
		snsClient = new SNSClient();

		CreateTopicResult result = null;

		try {

			CreateTopicRequest request = new CreateTopicRequest(
					receivedIntent.getStringExtra(MainActivity.EXTRA_MESSAGE));

			result = snsClient.getSNSClient().createTopic(request);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return result;
	}

	/*
	 * Returns the list of topics on Amazon SNS
	 * 
	 * @param String UniversityName
	 * 
	 * @param String Category
	 * 
	 * @return List<String> List of topics
	 */
	public static List<String> getTopics(String universityName, String category) {
		snsClient = new SNSClient();

		List<Topic> topics = snsClient.getSNSClient().listTopics().getTopics();
		Iterator<Topic> topicIterator = topics.iterator();
		List<String> topicNames = new ArrayList<String>(topics.size());

		while (topicIterator.hasNext()) {

			String topicNew;

			topicNew = ((Topic) topicIterator.next()).getTopicArn();
			String textToreplace = "arn:aws:sns:us-east-1:525576497761:"
					+ universityName;

			String topicModified = (topicNew.replace(textToreplace, ""))
					.replace(category, "");

			if (topicNew.contains(universityName + category)) {

				topicNames.add(topicModified);

			}

		}
		return topicNames;
	}

	/*
	 * Publishes the topic for selected topic and returns the publish result
	 * 
	 * @param String selectedTopic
	 * 
	 * @param String topicMessage
	 * 
	 * @return PublishResult Result of after publishing message
	 */
	public static PublishResult publishTopic(String selectedTopic,
			String topicMessage) {
		snsClient = new SNSClient();
		PublishRequest req = new PublishRequest(selectedTopic, topicMessage);
		return snsClient.getSNSClient().publish(req);
	}

	public static SubscribeResult subscribeTopic(String topicNames,
			String emailInput) {
		snsClient = new SNSClient();
		SubscribeRequest req = new SubscribeRequest(topicNames, "email",
				emailInput);
		return snsClient.getSNSClient().subscribe(req);
	}
}
