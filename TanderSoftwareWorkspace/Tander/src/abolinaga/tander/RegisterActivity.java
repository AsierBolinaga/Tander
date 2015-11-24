package abolinaga.tander;

import java.util.HashMap;

import abolinaga.tander.custom.CustomActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends CustomActivity
{
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    
    private Button buttonRegister;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        
      //Setting listener to button
        buttonRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View _v) 
	{
		if(_v == buttonRegister)
        {
        	registerUser();
        }
		else
		{
			/* Do Nothing */
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
        		pdLoading = ProgressDialog.show(RegisterActivity.this,"Adding...","Wait...",false,false);
        	}
        	
        	@Override
        	protected void onPostExecute(String _strResult) 
        	{
        		super.onPostExecute(_strResult);
        		pdLoading.dismiss();                
        		Toast.makeText(RegisterActivity.this,_strResult,Toast.LENGTH_LONG).show();
        		
        		if("successfully registered" == _strResult)
        		{
        			Intent intent = new Intent(RegisterActivity.this, MainLoginActivity.class);                    
    				startActivity(intent);
        		}
        		else
        		{
        			/* Do Nothing */
        		}
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
}
