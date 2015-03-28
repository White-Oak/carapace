package me.whiteoak.carapace.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.gson.Gson;
import me.whiteoak.carapace.Cache;
import me.whiteoak.carapace.Carapace;
import me.whiteoak.carapace.metadata.Topic;

/**
 *
 * @author White Oak
 */
public class PostReplier extends Activity {

    private Cache cache;
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle extras = getIntent().getExtras();

	String get = (String) extras.get("topic");
	topic = new Gson().fromJson(get, Topic.class);
	String get1 = (String) extras.get("cache");
	cache = new Gson().fromJson(get1, Cache.class);
	setContentView(R.layout.reply_post);
    }

    public void sendMessage(View view) {
	final EditText text = (EditText) findViewById(R.id.reply_post);
	AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

	    @Override
	    public Void doInBackground(Void... s) {
		Carapace carapace = Carapace.applyCache(cache);

		carapace.writeToTopic(topic, text.getText().toString());
		return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		PostReplier.this.finish();
	    }

	};
	asyncTask.execute(new Void[]{});
    }
}
