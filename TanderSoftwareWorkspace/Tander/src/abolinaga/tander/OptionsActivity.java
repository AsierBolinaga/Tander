package abolinaga.tander;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import abolinaga.tander.custom.CustomActivity;

public class OptionsActivity extends CustomActivity
{
	private TextView textView;
	
	private Button buttonFindTanderFriend;
    private Button buttonViewTanderFriends;
    
    /** The user. */
    private String strUserName;
	public static ParseUser user;
	
    @Override
    protected void onCreate(Bundle _bdSavedInstanceState)
    {
        super.onCreate(_bdSavedInstanceState);
        setContentView(R.layout.options);

        textView = (TextView) findViewById(R.id.textViewUserName);
        
        strUserName = getIntent().getStringExtra("USER_NAME");
		final ParseUser pu = new ParseUser();
		pu.setUsername(strUserName);
		pu.setPassword("");
		pu.signUpInBackground(new SignUpCallback() 
		{
			@Override
			public void done(ParseException arg0) 
			{
				user = pu;
			}
		});
        
        textView.setText("Welcome " + strUserName);
        
        buttonFindTanderFriend = (Button) findViewById(R.id.buttonFindTanderFriend);
        buttonViewTanderFriends = (Button) findViewById(R.id.buttonViewTanderFriends);
        
        GetFriendsList();
        
        //Setting listeners to button
        buttonFindTanderFriend.setOnClickListener(this);
        buttonViewTanderFriends.setOnClickListener(this);
        
        updateUserStatus(true);
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	updateUserStatus(false);
    }
    
    @Override
	public void onClick(View _v) 
	{
		if(_v == buttonFindTanderFriend)
        {
        	/* TODO: Friends Finder */
        }
		else if(_v == buttonViewTanderFriends)
		{
			Intent intent = new Intent(OptionsActivity.this, TanderFriendsList.class);                  
			startActivity(intent);
		}
		else
		{
			/* Do Nothing */
		}
	}
    
    /**
	 * Update user status.
	 * 
	 * @param online
	 *            true if user is online
	 */
	private void updateUserStatus(boolean _bOnline)
	{

	}
    
    private void GetFriendsList()
    {
    	
    }
}
