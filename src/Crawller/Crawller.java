package Crawller;

import java.util.List;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.*;
import twitter4j.Query.*;

public class Crawller {
	
	public static void crawl() {
		try {
			
			AccessToken accesstoken = new AccessToken("3901386613-EUADFhtXzgTQnEdPfzb5c4AuV4pfECjLn11G5Go", "r2QCMx5QqFBJu8GNtDCTL7pYlc7wsDALwOjOoS1eZTZmo");			
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey("erNn5C3JqtBX0pmVR12ZxcpXB");
			builder.setOAuthConsumerSecret("UtDG95aeSJhza2fAaZkHJF76yTMwpvbBAestvLqZ5bRJ0RSVEb");
			Configuration configuration = builder.build();
			TwitterStreamFactory factory = new TwitterStreamFactory(configuration);
			TwitterStream TS = factory.getInstance();
			 TS.setOAuthAccessToken(accesstoken);
			// TwitterStream TS = new TwitterStreamFactory().getInstance();
			StatusListener SL = new StatusListener() {
				@Override
				public void onException(Exception arg0) {}

				@Override
				public void onDeletionNotice(StatusDeletionNotice arg0) {}

				@Override
				public void onScrubGeo(long arg0, long arg1) {}

				@Override
				public void onStallWarning(StallWarning arg0) {}

				@Override
				public void onStatus(Status status) {
					// 게시글 받았을때 Action
					System.out.println("ID : " + status.getUser().getName()
							+ "\nText : " + status.getText()
							+ "\nTime : " + "구현 예정"
							);
					System.out.println("=====================================================================");
				}

				@Override
				public void onTrackLimitationNotice(int arg0) {}};
			TS.addListener(SL);
			TS.sample("ko");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
}
