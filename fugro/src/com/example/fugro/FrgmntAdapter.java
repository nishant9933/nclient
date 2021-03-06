package com.example.fugro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FrgmntAdapter extends ArrayAdapter<HappeningItems> {

	private Context context;
	private ArrayList<HappeningItems> subcontactList = new ArrayList<HappeningItems>();
	// int layoutResourceId;
	LayoutInflater inflater;

	public FrgmntAdapter(Context context, int layoutResourceId,
			ArrayList<HappeningItems> subcontactList) {
		super(context, layoutResourceId, subcontactList);

		// this.layoutResourceId = layoutResourceId;
		this.subcontactList = subcontactList;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View v = convertView;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.fraglist, null);

			viewHolder.viewbtn = (Button) v.findViewById(R.id.btnview);
			viewHolder.textTitle = (TextView) v.findViewById(R.id.listTitle);
			viewHolder.textDuration = (TextView) v
					.findViewById(R.id.listDuration);
			viewHolder.textDescription = (TextView) v
					.findViewById(R.id.listDescription);
			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		HappeningItems dataalerts = subcontactList.get(position);
		if (viewHolder != null) {
			viewHolder.textTitle.setText(dataalerts.getEventTitle());
			viewHolder.textDuration.setText("From "
					+ dataalerts.getEventdurationFrom() + " to "
					+ dataalerts.getEventto());
			viewHolder.textDescription
					.setText(dataalerts.getEventdescription());

			viewHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent in = new Intent(getContext(),
							AlertDetailsActivity.class);
					in.putExtra("Lat", HomeActivity.lat);
					in.putExtra("Lng", HomeActivity.lng);
					context.startActivity(in);
				}
			});
		}
		return v;
	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {
		return subcontactList.size();
	}

	public static class ViewHolder {
		public TextView textTitle, textDuration, textDescription;
		public Button viewbtn;
	}
}