package Crawler;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Date;
import java.util.regex.*;

public class SpammerChecker {
	private int twitCnt = 0;
	private double ratioAv = 0;
	private double rsRatio = 0;
	private double hashAv = 0.05;
	private double urlAv = 0.08;
	
	// If return 1, not spammer. If return 0, can be spammer.
	// If return -1, ignore(bot or useless text).
	public int testTwitterText(Status status) {
		//getFRR(status);
		double contentScore = 0;
		double behaviorScore = 0;
		if (status.getUser().getName().contains("��") == true) {
			return -1;
		}
		if (status.getUser().getName().contains("bot") == true) {
			return -1;
		}
		if (status.getText().length() <= 20) {
			return -1;
		}
		if (isCreatedInADay(status) == true) {
			return -1;
		}
		if (status.getUser().getStatusesCount() <= 50) {
			return -1;
		}
		AccessDB ADB = new AccessDB();
		if (ADB.getBlackListCount(status) >= 5) {
			// spammer
			return -1;
		} 
		if (getRS(status) <= 0.01) {
			contentScore += 0.1;
		}
		if (getSpecialCharacterRatio(status.getUser().getDescription()) >= 0.3) {
			contentScore += 0.2;
		}
		if (getSpecialCharacterRatio(status.getText()) >= 0.3) {
			contentScore += 0.3;
		}
		int spamCount = 0;
		if (ADB.getSpamWordCount(status) >= 1) {
			contentScore += 0.4 * spamCount;
		}
		
		if (contentScore >= 0.6) {
			return 0;
		} else {
			return 1;
		}
	}
	
	boolean isCreatedInADay(Status status) {
		Date today = new Date();
		
		long todayNum = (today.getTime() / 1000 / 60 / 60 / 24);
		long statusNum = (status.getUser().getCreatedAt().getTime()
				/ 1000 / 60 / 60 / 24);
		
		if (todayNum < (statusNum + 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	private double getSpecialCharacterRatio(String str) {
		int length = str.length();
		int count = 0;
		
		// Special character count
		for (int i = 0; i < length; i++) {
			if (((str.charAt(i) >= 0) && (str.charAt(i) <= 47))
					 || ((str.charAt(i) >= 123) && (str.charAt(i) <= 127))
					|| ((str.charAt(i) > 57) && (str.charAt(i) < 65))) {
				count++;
			}
		}
		
		return (double)count / (double)length;	
	}
	
	
	public double getRS(Status status) {
		// RS calculating function
		if (status.getUser().getFriendsCount() == 0) {
			return 0;
		}
		long followerCount = status.getUser().getFollowersCount();
		long followingCount = status.getUser().getFriendsCount();
		
		return (double)followerCount / (double)followingCount;
	}
	
	public void getFRR(Status status) {
		// FRR calculating function
		System.out.println("����� ID : " + status.getUser().getName());
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
