package Crawler;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.regex.*;

public class SpammerChecker {
	
	// If return 1, not spammer. If return 0, can be spammer.
	// If return -1, spammer.
	public int testTwitterText(Status status) {
		if (status.getUser().getName().contains("º¿") == true) {
			return -1;
		}
		if (status.getUser().getName().contains("bot") == true) {
			return -1;
		}
		AccessDB ADB = new AccessDB();
		if (ADB.getBlackListCount(status) >= 5) {
			// spammer
			return -1;
		} 
		if (ADB.getSpamWordCount(status) >= 2) {
			return 0;
		}
		if (getNewLineCount(status) >= 10) {
			return 0;
		}
		
		return 1;
	}
	
	private int getNewLineCount(Status status) {
		char b = '\n';
		String str = status.getText();
		int length = str.length();
		int count = 0;
		
		for (int i = 0; i < length; i++) {
			if (str.charAt(i) == b) {
				count++;
			}
		}
		
		return count;	
	}
	
	
	public double getRS(Status status) {
		// RS calculating function
		long followerCount = status.getUser().getFollowersCount();
		long followingCount = status.getUser().getFriendsCount();
		
		return (double)followerCount / (double)followingCount;
	}
	
	public void getFRR(Status status) {
		// FRR calculating function
		System.out.println("»ç¿ëÀÚ ID : " + status.getUser().getName());
		long id = status.getUser().getId();
		long cursor = -1;
		//-------------------------------------------------
		AccessToken accessToken = new AccessToken(
				"3901386613-EUADFhtXzgTQnEdPfzb5c4AuV4pfECjLn11G5Go",
				"r2QCMx5QqFBJu8GNtDCTL7pYlc7wsDALwOjOoS1eZTZmo");
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey("erNn5C3JqtBX0pmVR12ZxcpXB");
		builder.setOAuthConsumerSecret("UtDG95aeSJhza2fAaZkHJF76yTMwpvbBAestvLqZ5bRJ0RSVEb");
		Configuration configuration = builder.build();

		TwitterFactory tF = new TwitterFactory(configuration);
		
		Twitter twitter = tF.getInstance();
		twitter.setOAuthAccessToken(accessToken);
		//-------------------------------------------------
		try {
			IDs friendsIDs = twitter.getFriendsIDs(id, cursor);
			long arr[] = friendsIDs.getIDs();
			System.out.println(twitter.showUser(id).getName());
			System.out.println("===================");
			do {
				for (long i : friendsIDs.getIDs()) {
					System.out.println("followers ID #" + i);
//					System.out.println(twitter.showUser(i).getName());
				}
			} while (friendsIDs.hasNext());
		} catch (TwitterException e) {
			e.printStackTrace();
		}	
	}
}
