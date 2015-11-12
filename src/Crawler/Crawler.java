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
		// �׽�Ʈ ��� ���
		System.out.println("ID : " + status.getUser().getName()); // ���̵�
		System.out.println("Text : " + status.getText()); // ����
		System.out.println("Favorite: " +
		status.getUser().getFavouritesCount()); // ���ɱ�
		System.out.println("Followers: " +
		status.getUser().getFollowersCount()); // �ȷο�
		System.out.println("Friend: " +
		status.getUser().getFriendsCount()); // �ȷ���
		System.out.println("Description: " +
		status.getUser().getDescription());
		System.out.println("Created at: "
				+ status.getUser().getCreatedAt());	// ���� ���� ��¥
		System.out.println("Status count: "
				+ status.getUser().getStatusesCount());
		System.out.println("Post comment: "
				+ status.getRetweetCount());
		System.out.println("Favorite Count: "
				+ status.getFavoriteCount());
		System.out.println("==================================");	// Ʈ�� ����
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
						ADB.addBlackListUser(status);
						ADB.insertTwitterText(status);
					} else {}
					//printCrawl(status);
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
