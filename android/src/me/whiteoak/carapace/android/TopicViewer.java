package me.whiteoak.carapace.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import java.util.LinkedList;
import java.util.List;
import me.whiteoak.carapace.Cache;
import me.whiteoak.carapace.Carapace;
import me.whiteoak.carapace.metadata.Post;
import me.whiteoak.carapace.metadata.Topic;

/**
 *
 * @author White Oak
 */
public class TopicViewer extends ListActivity {

    private List<Post> posts;
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
	setTitle(topic.getName());
	// Create a progress bar to display while the list loads
	ProgressBar progressBar = new ProgressBar(this);
	progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	progressBar.setIndeterminate(true);
	getListView().setEmptyView(progressBar);

	// Must add the progress bar to the root of the layout
	ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
	root.addView(progressBar);

	AsyncTask<Void, Void, List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {

	    @Override
	    public List<String> doInBackground(Void... s) {
		Carapace carapace = Carapace.applyCache(cache);

		List<Post> unreadTopics = carapace.getTopicPosts(topic);
		posts = unreadTopics;
		List<String> topics = new LinkedList<>();
		for (Post post : unreadTopics) {
		    StringBuilder builder = new StringBuilder();
		    builder.append(post.getNickname())
			    .append('\n')
			    .append(post.getDate())
			    .append('\n')
			    .append(post.getText().trim());
		    topics.add(builder.toString());
		}
		return topics;
	    }

	    @Override
	    protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			TopicViewer.this,
			android.R.layout.simple_list_item_1,
			result);
		getListView().setAdapter(arrayAdapter);
	    }

	};
	asyncTask.execute(new Void[]{});
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);

	final Post get = posts.get(position);
	Intent intent = new Intent(this, PostReplier.class);
	intent.putExtra("post", new Gson().toJson(get));
	intent.putExtra("topic", new Gson().toJson(topic));
	intent.putExtra("cache", new Gson().toJson(cache));
	startActivity(intent);
    }

}
