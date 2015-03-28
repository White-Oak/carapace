package me.whiteoak.carapace.android;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import java.util.LinkedList;
import java.util.List;
import me.whiteoak.carapace.Carapace;
import me.whiteoak.carapace.metadata.Topic;
import me.whiteoak.carapace.metadata.User;

public class AndroidLauncher extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Create a progress bar to display while the list loads
	ProgressBar progressBar = new ProgressBar(this);
	progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	progressBar.setIndeterminate(true);
	getListView().setEmptyView(progressBar);

	// Must add the progress bar to the root of the layout
	ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
	root.addView(progressBar);

	AsyncTask<Carapace, Void, List<String>> asyncTask = new AsyncTask<Carapace, Void, List<String>>() {

	    @Override
	    public List<String> doInBackground(Carapace... s) {
		s[0].authorize();
		List<Topic> unreadTopics = s[0].getUnreadTopics();
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
			AndroidLauncher.this,
			android.R.layout.simple_list_item_1,
			result);
		getListView().setAdapter(arrayAdapter);
	    }

	};
	asyncTask.execute(new Carapace[]{carapace});
    }

    Carapace carapace = new Carapace(new User(5354, "default"));

}
