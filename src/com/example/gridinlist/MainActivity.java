package com.example.gridinlist;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView listView;
	private MyAdapter adapter;
	private Vector<String> stringVector = new Vector<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.list);
		adapter = new MyAdapter(this, this.getLayoutInflater(), stringVector);
		
		stringVector.add("0");
		stringVector.add("1");
		stringVector.add("2");
		stringVector.add("3");
		stringVector.add("4");
		stringVector.add("5");
		
		listView.setAdapter(adapter);
	}
}
