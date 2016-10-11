package com.example.gridinlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter
{
	private static final String TAG = "MyAdapter";
	private LayoutInflater inflater;
	private Context context;
	private Vector<String> vector;
	private int numGridShowLimit = 4; //Gridview限定显示数目
	
	/**
	 * 搜索Adapter初始化
	 */
	public MyAdapter(Context context,LayoutInflater inflater,Vector<String> vector) 
	{
		this.context = context;
		this.inflater = inflater;
		this.vector = vector;
	}

	/**
	 * 初始化View
	 */
	private static class ViewHolder
	{
		private TextView  titleTextView;
		private GridView  contentGridView;
		private GridView  contentMoreGridView;
	}
	
	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder viewHolder;
		if( convertView == null )
		{
			convertView = inflater.inflate(R.layout.adapter_listview,null);
			viewHolder = new ViewHolder();
			viewHolder.titleTextView  = (TextView) convertView.findViewById(R.id.list_txt_title);
			viewHolder.contentGridView = (GridView) convertView.findViewById(R.id.list_grid);
			viewHolder.contentMoreGridView = (GridView) convertView.findViewById(R.id.list_grid_more);
			convertView.setTag(viewHolder);
		}
		else viewHolder = (ViewHolder) convertView.getTag();
		
		try 
		{
			if(vector != null && vector.size() > 0)
			{
				final String category = vector.get(position);
				if(category != null)
				{
					viewHolder.titleTextView.setText("list item "+category);
				}
				
				if (vector.size() > 0) {
					final ArrayList<HashMap<String, Object>> categoryList = new ArrayList<HashMap<String, Object>>();
					final ArrayList<HashMap<String, Object>> categorySubList1 = new ArrayList<HashMap<String, Object>>();
					final ArrayList<HashMap<String, Object>> categorySubList2 = new ArrayList<HashMap<String, Object>>();
					//先封装整个数据向量到categoryList
					if (vector != null && vector.size() > 0) {
						for (int i = 0; i < vector.size(); i++) {
							String o = vector.get(i);
							if (o != null) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("ItemText", "grid" + o);
								categoryList.add(map);
							}
						}
					}
					final int mypos = position;
					
					//当数据向量长度大于限定显示的grid数目时，将整个数据向量分成两段
					if (vector.size() > numGridShowLimit) {
						
						//封装前段数据到categorySubList1
						for (int i = 0; i < numGridShowLimit; i++) {
							String o = vector.get(i);
							if (o != null) {
								if (i==numGridShowLimit-1) {
									HashMap<String, Object> map = new HashMap<String, Object>();
									if (viewHolder.contentMoreGridView.getVisibility()==View.GONE) {
										//若处于折叠状态则将限定数目的位置的数据修改为“+”（用于点击展开）
										map.put("ItemText", "" + "+");
									}else {
										//若处于展开状态则限定数目的位置的数据正常显示
										map.put("ItemText", "grid" + o);
									}
									categorySubList1.add(map);
								}else {
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("ItemText", "grid" + o);
									categorySubList1.add(map);
								}
								
							}
						}
						
						//封装后段数据到categorySubList2
						for (int i = numGridShowLimit; i < vector.size(); i++) {
							String o = vector.get(i);
							if (o != null) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("ItemText", "grid" + o);
								categorySubList2.add(map);
							}
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("ItemText", "-"); //在第二段的最后添加一个“-”数据（用于点击折叠）
						categorySubList2.add(map);
						
						//创建第一部分GridView的adapter
						final SimpleAdapter simpleAdapter1 = new SimpleAdapter(context, categorySubList1,
								R.layout.adapter_gridview,
								new String[] { "ItemText" }, // 对应map的Key
								new int[] { R.id.ItemText }); // 对应R的Id
						viewHolder.contentGridView.setAdapter(simpleAdapter1);
						
						//创建第二部分GridView的adapter
						final SimpleAdapter simpleAdapter2 = new SimpleAdapter(context, categorySubList2,
								R.layout.adapter_gridview,
								new String[] { "ItemText" }, // 对应map的Key
								new int[] { R.id.ItemText }); // 对应R的Id
						viewHolder.contentMoreGridView.setAdapter(simpleAdapter2);
						
						//设置第二部分GridView的adapter中的具体item点击事件
						viewHolder.contentMoreGridView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> AdapterView, View view,int pos, long row) {
								if (categoryList.size()>numGridShowLimit && pos==categoryList.size()-numGridShowLimit) {
									//若点击末尾的“-”，则将gridview折叠，并修改第一部分最后位置为“+”（可展开状态）
									View rel =  (View) viewHolder.contentGridView.getChildAt(numGridShowLimit-1);
									TextView tv =  (TextView) rel.findViewById(R.id.ItemText);
									tv.setText("+");
									viewHolder.contentMoreGridView.setVisibility(View.GONE);
								}else {
									view.setTag(mypos*100+numGridShowLimit+pos);
									Toast.makeText(context, " list position:"+ mypos+"\ngrid position:"+pos, Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
					else {
						//若数据向量数目不大于限定的显示数目，则正常创建第一部分GridView的adapter（直接使用整体数据categoryList）
						final SimpleAdapter simpleAdapter = new SimpleAdapter(context, categoryList,
								R.layout.adapter_gridview,
								new String[] { "ItemText" }, // 对应map的Key
								new int[] { R.id.ItemText }); // 对应R的Id
						viewHolder.contentGridView.setAdapter(simpleAdapter);
					}
					
					//设置第一部分GridView的adapter中的具体item点击事件
					viewHolder.contentGridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> AdapterView, View view,int pos, long row) {
							if (categoryList.size()>numGridShowLimit && pos==numGridShowLimit-1) {
								TextView tv =  (TextView) view.findViewById(R.id.ItemText);
								String content = tv.getText().toString();
								if (content.equals("+")) {
									//若点击“+”，则进行展开操作，即显示第二部分gridview，并修改最后位置为“+”
									tv.setText("grid"+vector.get(numGridShowLimit-1));
									viewHolder.contentMoreGridView.setVisibility(View.VISIBLE);
								}else {
									view.setTag(mypos*100+pos);
									Toast.makeText(context, " list position:"+ mypos+"\ngrid position:"+pos, Toast.LENGTH_SHORT).show();
								}
							}else {
								view.setTag(mypos*100+pos);
								Toast.makeText(context, " list position:"+ mypos+"\ngrid position:"+pos, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Log.e(TAG, "Exception");
		}
		
		return convertView;
	}
	
	@Override
	public int getCount() 
	{
	    return vector.size();
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) 
	{
		 if (observer != null) {
	         super.unregisterDataSetObserver(observer);
	     }
	}
}