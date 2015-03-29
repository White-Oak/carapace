package me.whiteoak.carapace.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.gson.Gson;
import me.whiteoak.carapace.Cache;
import me.whiteoak.carapace.Carapace;

/**
 *
 * @author White Oak
 */
public class Launcher extends Activity {

    static {
	Carapace.additonalUserAgent = "aNNDroid/0.2 (Carapace Droid Client)";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	SharedPreferences sharedPreferences = getSharedPreferences("carapace_cache", MODE_PRIVATE);
	if (sharedPreferences.contains("cache")) {
	    String string = sharedPreferences.getString("cache", null);
	    Cache cache = new Gson().fromJson(string, Cache.class);
	    AsyncTask<Cache, Void, Carapace> asyncTask = new AsyncTask<Cache, Void, Carapace>() {

		@Override
		protected Carapace doInBackground(Cache... params) {
		    return Carapace.applyCache(params[0]);
		}

		@Override
		protected void onPostExecute(Carapace result) {
		    super.onPostExecute(result);

		    Intent intent = new Intent(Launcher.this, UnreadViewer.class);
		    intent.putExtra("cache", new Gson().toJson(result.getCache()));
		    startActivity(intent);
		}

	    };
	    asyncTask.execute(new Cache[]{cache});
	} else {
	    Intent intent = new Intent(this, Authorizer.class);
	    startActivity(intent);
	}
	finish();
    }

}
