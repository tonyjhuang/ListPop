package com.tonyjhuang.listpop;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter implements Filterable {
	    private LayoutInflater mInflater;
	    private Context context;
	    private ArrayList<String> data;

	    public CustomAdapter(Context context, ArrayList<String> _data) {
	      // Cache the LayoutInflate to avoid asking for a new one each time.
	      mInflater = LayoutInflater.from(context);
	      this.context = context;
	      this.data = _data;
	    }

		/**
	     * Make a view to hold each row.
	     * 
	     * @see android.widget.ListAdapter#getView(int, android.view.View,
	     *      android.view.ViewGroup)
	     */
	    public View getView(final int position, View convertView, ViewGroup parent) {
	      // A ViewHolder keeps references to children views to avoid
	      // unnecessary calls
	      // to findViewById() on each row.
	      ViewHolder holder;

	      // When convertView is not null, we can reuse it directly, there is
	      // no need
	      // to reinflate it. We only inflate a new View when the convertView
	      // supplied
	      // by ListView is null.
	      if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.list_item_e, null);

	        // Creates a ViewHolder and store references to the two children
	        // views
	        // we want to bind data to.
	        holder = new ViewHolder();
	        holder.textLine = (TextView) convertView.findViewById(R.id.listname);
	        holder.editLine = (Button) convertView.findViewById(R.id.editline);
	        holder.deleteLine = (Button) convertView.findViewById(R.id.deleteline);
	        
	        convertView.setOnClickListener(new OnClickListener() {
	          private int pos = position;

	          @Override
	          public void onClick(View v) {
	            Toast.makeText(context, "Click-" + String.valueOf(pos), Toast.LENGTH_SHORT).show();    
	          }
	        });

	        
	        
	        holder.editLine.setOnClickListener(new OnClickListener() {
	          private int pos = position;

	          @Override
	          public void onClick(View v) {
	            Toast.makeText(context, "Delete-" + String.valueOf(pos), Toast.LENGTH_SHORT).show();

	          }
	        });
	        
	        
	        holder.deleteLine.setOnClickListener(new OnClickListener() {
	            private int pos = position;

	            @Override
	            public void onClick(View v) {
	              Toast.makeText(context, "Details-" + String.valueOf(pos), Toast.LENGTH_SHORT).show();

	            }
	          });
	        
	        
	        convertView.setTag(holder);
	      } else {
	        // Get the ViewHolder back to get fast access to the TextView
	        // and the ImageView.
	        holder = (ViewHolder) convertView.getTag();
	      }

	      
	      holder.textLine.setText(data.get(position));
	      
	      
	      return convertView;
	    }

	    static class ViewHolder {
	      TextView textLine;
	      Button editLine;
	      Button deleteLine;
	      
	    }

	    @Override
	    public Filter getFilter() {
	      // TODO Auto-generated method stub
	      return null;
	    }

	    @Override
	    public long getItemId(int position) {
	      // TODO Auto-generated method stub
	      return 0;
	    }

	    @Override
	    public int getCount() {
	      // TODO Auto-generated method stub
	      return data.size();
	    }

	    @Override
	    public Object getItem(int position) {
	      // TODO Auto-generated method stub
	      return data.get(position);
	    }
}