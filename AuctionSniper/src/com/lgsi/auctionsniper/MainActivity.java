package com.lgsi.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String ITEM_ID = "item-54321";
	private static final int XMPP_PORT = 5222;
	private static final String XMPP_HOSTNAME = "10.168.145.4";
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String SNIPER_USERNAME = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s";
	private Button joinButton;
	private TextView sniperStatus;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        joinButton = (Button) findViewById(R.id.join);
        sniperStatus = (TextView) findViewById(R.id.sniper_status);
        joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sniperStatus.setText(R.string.status_joining);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							joinAuction(connection(XMPP_HOSTNAME,SNIPER_USERNAME,SNIPER_PASSWORD),ITEM_ID);
						} catch (XMPPException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

		});
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
		
    	String auctionid= auctionId(itemId, connection);
    	System.out.println(auctionid);
		Chat chat = connection.getChatManager().createChat(auctionid,new MessageListener() {
			@Override
			public void processMessage(Chat chat, Message message) {
				sniperStatus.setText(R.string.status_lost);
			}
		});
		chat.sendMessage(new Message());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
 //       getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private static XMPPConnection connection(String hostname, String username,
			String password) throws XMPPException {
    	ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(hostname, XMPP_PORT);
		XMPPConnection connection = new XMPPConnection(connectionConfiguration);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		
		return String.format(AUCTION_ID_FORMAT, itemId, connection
				.getServiceName());
	}
    
}
