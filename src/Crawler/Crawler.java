package Crawler;

import java.util.List;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.*;
import twitter4j.Query.*;

public class Crawler {
	private AccessDB ADB = new AccessDB();
	private SpammerChecker SC = new SpammerChecker();
	
	public void printDB() {
		ADB.printTwitterText();
	}
	
	private void printCrawl(Status status) {
		// 테스트 출력 기능
		System.out.println("ID : " + status.getUser().getName()); // 아이디
		System.out.println("Text : " + status.getText()); // 내용
		System.out.println("Favorite: " +
		status.getUser().getFavouritesCount()); // 관심글
		System.out.println("Followers: " +
		status.getUser().getFollowersCount()); // 팔로워
		System.out.println("Friend: " +
		status.getUser().getFriendsCount()); // 팔로잉
		System.out.println("Description: " +
		status.getUser().getDescription());
		System.out.println("==================================");
	}
	
	public void crawl() {
		try {
			// Authorization
			AccessToken accessToken = new AccessToken(
					"3901386613-EUADFhtXzgTQnEdPfzb5c4AuV4pfECjLn11G5Go",
					"r2QCMx5QqFBJu8GNtDCTL7pYlc7wsDALwOjOoS1eZTZmo");
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey("erNn5C3JqtBX0pmVR12ZxcpXB");
			builder.setOAuthConsumerSecret("UtDG95aeSJhza2fAaZkHJF76yTMwpvbBAestvLqZ5bRJ0RSVEb");
			Configuration configuration = builder.build();
			TwitterStreamFactory tSFactory = new TwitterStreamFactory(configuration);
			TwitterStream tS = tSFactory.getInstance();
			tS.setOAuthAccessToken(accessToken);

			StatusListener SL = new StatusListener() {
				@Override
				public void onException(Exception arg0) {
				}

				@Override
				public void onDeletionNotice(StatusDeletionNotice arg0) {
				}

				@Override
				public void onScrubGeo(long arg0, long arg1) {
				}

				@Override
				public void onStallWarning(StallWarning arg0) {
				}

				@Override
				public void onStatus(Status status) {
					if (SC.testTwitterText(status) == 1) {
						ADB.insertTwitterText(status);	
					} else if (SC.testTwitterText(status) == 0) {
						// XXX
						ADB.addBlackListUser(status);
						ADB.insertTwitterText(status);
					} else {}
					printCrawl(status);
				}

				@Override
				public void onTrackLimitationNotice(int arg0) {
				}
			};
			tS.addListener(SL);
			tS.sample("ko");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
