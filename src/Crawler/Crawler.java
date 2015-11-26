package Crawler;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.*;
import WordAnalyzer.WordAnalyzer;

public class Crawler {
	private AccessDB aDB = new AccessDB();
	private SpammerChecker sC = new SpammerChecker();
	
	public void printDB() {
		aDB.printTwitterText();
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
		System.out.println("DesCription: " +
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
					if (sC.testTwitterText(status) == 1) {
						aDB.insertTwitterText(status);	
					} else if (sC.testTwitterText(status) == 0) {
						aDB.addBlackListUser(status);
						aDB.insertTwitterText(status);
					} else {}	
					WordAnalyzer wA = new WordAnalyzer();
					wA.analyzer(status.getText());
					
					// printCrawl(status);
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
