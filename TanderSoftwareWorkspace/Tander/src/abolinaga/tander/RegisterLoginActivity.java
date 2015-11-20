package abolinaga.tander;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterLoginActivity extends Activity implements View.OnClickListener
{
	public static final String USER_NAME = "USER_NAME";
	
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister;
    private Button buttonLogin;
    
    @Override
    protected void onCreate(Bundle _bndSavedInstanceState) 
    {
        super.onCreate(_bndSavedInstanceState);
        setContentView(R.layout.registerlogin);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        
        //Setting listeners to button
        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View _v) 
    {
        if(_v == buttonRegister)
        {
        	registerUser();
        }
        else if(_v == buttonLogin)
        {
        	loginUser();
        }
        else
        {
        	/* Do nothing */
        }
    }

    private void registerUser() 
    {
        final String strName = editTextName.getText().toString().trim();
        final String strUsername = editTextUsername.getText().toString().trim();
        final String strPassword = editTextPassword.getText().toString().trim();
        final String strEmail = editTextEmail.getText().toString().trim();

        class RegisterUserClass extends AsyncTask<Void,Void,String>
        {
        	ProgressDialog pdLoading;
        	
        	@Override
        	protected void onPreExecute() 
        	{
        		super.onPreExecute();
        		pdLoading = ProgressDialog.show(RegisterLoginActivity.this,"Adding...","Wait...",false,false);
        	}
        	
        	@Override
        	protected void onPostExecute(String _strResult) 
        	{
        		super.onPostExecute(_strResult);
        		pdLoading.dismiss();                
        		Toast.makeText(RegisterLoginActivity.this,_strResult,Toast.LENGTH_LONG).show();
        	}
        	
			@Override
			protected String doInBackground(Void... vParams) 
			{
				HashMap<String,String> hmParams = new HashMap<String,String>();
				hmParams.put(Config.KEY_USER_NAME,strName);                
				hmParams.put(Config.KEY_USER_USERNAME,strUsername);                
				hmParams.put(Config.KEY_USER_PASSWORD,strPassword);
				hmParams.put(Config.KEY_USER_EMAIL,strEmail);
				
				RequestHandler rh = new RequestHandler();
				String res = rh.sendPostRequest(Config.URL_REGISTER, hmParams);
					
				return res;
			}
        }
        
        RegisterUserClass re = new RegisterUserClass();
        re.execute();
    }
    
    private void loginUser()
    {
    	final String strUsername = editTextUsername.getText().toString().trim();       
    	String strPassword = editTextPassword.getText().toString().trim();
    	
    	 class LoginUserClass extends AsyncTask<String,Void,String>
    	 {
    		ProgressDialog pdLoading;
			
    		@Override            
    		protected void onPreExecute() 
    		{                
    			super.onPreExecute();                
    			pdLoading = ProgressDialog.show(RegisterLoginActivity.this,"Please Wait",null,true,true);            
    		}
    		
    		@Override
    		protected void onPostExecute(String _strResult) 
    		{
    			super.onPostExecute(_strResult);
    			pdLoading.dismiss();
    			if(_strResult.equalsIgnoreCase("success"))
    			{                    
    				Intent intent = new Intent(RegisterLoginActivity.this,OptionsActivity.class);                    
    				intent.putExtra(USER_NAME, strUsername);                    
    				startActivity(intent);               
    			}
    			else
    			{                    
    				Toast.makeText(RegisterLoginActivity.this,_strResult,Toast.LENGTH_LONG).show();                
    			}
    		}
			
			protected String doInBackground(String... _strParams) 
			{
				HashMap<String,String> hmData = new HashMap<String,String>();
				hmData.put("username",_strParams[0]);
				hmData.put("password",_strParams[1]);
				
				RequestHandler rh = new RequestHandler();
				
				String strResult = rh.sendPostRequest(Config.URL_GET_USER, hmData);
				
				return strResult;
			}
    		 
    	 }
    	 
    	 LoginUserClass luc = new LoginUserClass();
    	 luc.execute(strUsername,strPassword);
    }
}
