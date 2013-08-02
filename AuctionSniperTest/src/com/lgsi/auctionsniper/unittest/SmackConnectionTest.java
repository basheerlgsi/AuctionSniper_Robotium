package com.lgsi.auctionsniper.unittest;


import junit.framework.TestCase;
import com.lgsi.auctionsniper.test.FakeAuctionServer;

public class SmackConnectionTest extends TestCase {

	public void testServerConnection() throws Exception {
		final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
		auction.startSellingItem();
	}
}
