package me.whiteoak.carapace.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.google.gson.Gson;
import java.util.LinkedList;
import java.util.List;
import me.whiteoak.carapace.Cache;
import me.whiteoak.carapace.Carapace;
import me.whiteoak.carapace.metadata.*;

public class UnreadViewer extends ListActivity {

    private List<Topic> topics;

    private Cache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle extras = getIntent().getExtras();

	String get1 = (String) extras.get("cache");
	cache = new Gson().fromJson(get1, Cache.class);
	// Create a progress bar to display while the list loads
	ProgressBar progressBar = new ProgressBar(this);
	progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	progressBar.setIndeterminate(true);
	getListView().setEmptyView(progressBar);

	// Must add the progress bar to the root of the layout
	ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
	root.addView(progressBar);

	AsyncTask<Cache, Void, List<String>> asyncTask = new AsyncTask<Cache, Void, List<String>>() {

	    @Override
	    public List<String> doInBackground(Cache... s) {
		Carapace carapace = Carapace.applyCache(s[0]);
		List<Topic> unreadTopics = carapace.getLastTopics();
		UnreadViewer.this.topics = unreadTopics;
		List<String> topics = new LinkedList<>();
		for (Topic unreadTopic : unreadTopics) {
		    topics.add(unreadTopic.getName());
		}
		return topics;
	    }

	    @Override
	    protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			UnreadViewer.this,
			android.R.layout.simple_list_item_1,
			result);
		getListView().setAdapter(arrayAdapter);
	    }

	};
	asyncTask.execute(new Cache[]{cache});
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	final Topic get = topics.get(position);
	Intent intent = new Intent(this, TopicViewer.class);
	intent.putExtra("topic", new Gson().toJson(get));
	intent.putExtra("cache", new Gson().toJson(cache));
	startActivity(intent);
    }

}
