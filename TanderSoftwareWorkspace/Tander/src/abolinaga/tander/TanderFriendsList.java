package abolinaga.tander;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import abolinaga.tander.custom.CustomActivity;

import com.parse.ParseUser;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class TanderFriendsList extends CustomActivity implements ListView.OnItemClickListener 
{
	private ListView listView;
	
	/** The TanderFriends list. */
	ArrayList<HashMap<String,String>> hmTanderFriendsList = new ArrayList<HashMap<String, String>>();

	
	private String ALLFRIENDS_STRING;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tanderfriendlist);
		
		listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        
		//getActionBar().setDisplayHomeAsUpEnabled(false);

		//updateUserStatus(true);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//updateUserStatus(false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		loadUserList();

	}

	/**
	 * Update user status.
	 * 
	 * @param online
	 *            true if user is online
	 */
	private void updateUserStatus(boolean online)
	{
		OptionsActivity.user.put("online", online);
		OptionsActivity.user.saveEventually();
	}
	
	/**
	 * Show list of Friends.
	 */
	private void ShowTanderFriends()
	{
		JSONObject jsonObject = null;
		
		try
		{
			jsonObject = new JSONObject(ALLFRIENDS_STRING);
			JSONArray jsonResult = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
			
			for(int iLoop = 0; iLoop<jsonResult.length(); iLoop++)
			{
                JSONObject jo = jsonResult.getJSONObject(iLoop);
                String name = jo.getString(Config.TAG_NAME);

                HashMap<String,String> hmTanderFriends = new HashMap<String,String>();
                hmTanderFriends.put(Config.TAG_NAME,name);
                hmTanderFriendsList.add(hmTanderFriends);
            }
		
			ListAdapter laAdapter = new SimpleAdapter(
					TanderFriendsList.this, hmTanderFriendsList, R.layout.list_item,
	                new String[]{Config.TAG_NAME},
	                new int[]{R.id.name});
	
	        listView.setAdapter(laAdapter);
		}
        catch(JSONException e)
		{
			 e.printStackTrace();
		}
	}

	/**
	 * Load list of Friends.
	 */
	private void loadUserList()
	{
		class GetAllTanderFriends extends AsyncTask<Void, Void, String>
		{
			ProgressDialog pdLoading;
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				pdLoading = ProgressDialog.show(TanderFriendsList.this,"Fetching Data","Wait...",false,false);
			}
			
			@Override
			protected void onPostExecute(String _strResult) 
			{
				super.onPostExecute(_strResult);
				pdLoading.dismiss();               
				ALLFRIENDS_STRING = _strResult;                
				ShowTanderFriends();
			}

			@Override
			protected String doInBackground(Void... _vParams) 
			{
				RequestHandler rh = new RequestHandler();
				String strResult = rh.sendGetRequest(Config.URL_GET_ALL);
				return strResult;
			}
		
		}
		
		GetAllTanderFriends gatf = new GetAllTanderFriends();
		gatf.execute();
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent(TanderFriendsList.this, ChatActivity.class);
		HashMap<String,String> hmMap =(HashMap)parent.getItemAtPosition(position);
		String strFriendName = hmMap.get(Config.TAG_NAME).toString();
		intent.putExtra("CHAT_TANDER_FRIEND_NAME",strFriendName);   
		startActivity(intent);
	}
}
