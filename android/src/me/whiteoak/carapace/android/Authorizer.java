package me.whiteoak.carapace.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.gson.Gson;
import me.whiteoak.carapace.Carapace;
import me.whiteoak.carapace.metadata.User;

/**
 *
 * @author White Oak
 */
public class Authorizer extends Activity {

    static {
	Carapace.additonalUserAgent = "aNNDroid/0.1 (Annimon Client)";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.authorize);
    }

    public void authorize(View view) {
	final EditText user = (EditText) findViewById(R.id.id);
	final EditText password = (EditText) findViewById(R.id.password);
	AsyncTask<Void, Void, Carapace> asyncTask = new AsyncTask<Void, Void, Carapace>() {

	    @Override
	    public Carapace doInBackground(Void... s) {
		User user1 = new User(Integer.parseInt(user.getText().toString()), password.getText().toString());
		Carapace carapace = new Carapace(user1);
		carapace.authorize();
		return carapace;
	    }

	    @Override
	    protected void onPostExecute(Carapace result) {
		super.onPostExecute(result);
		Intent intent = new Intent(Authorizer.this, AndroidLauncher.class);
		intent.putExtra("cache", new Gson().toJson(result.getCache()));
		startActivity(intent);
	    }

	};
	asyncTask.execute(new Void[]{});
    }
}
