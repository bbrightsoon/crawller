package Crawler;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class SpammerChecker {
	
	// If return 1, not spammer. If return 0, can be spammer.
	// If return -1, spammer.
	public int testTwitterText(Status status) {
		return 0;
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
