package org.digitalcampus.oppia.adapter;

import java.util.ArrayList;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.activity.DownloadMediaActivity;
import org.digitalcampus.oppia.model.Media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DownloadMediaListAdapter extends ArrayAdapter<Media>  {

	public static final String TAG = DownloadMediaListAdapter.class.getSimpleName();

	private final DownloadMediaActivity ctx;
	private ArrayList<Media> mediaList;

	
	public DownloadMediaListAdapter(DownloadMediaActivity context, ArrayList<Media> mediaList) {
		super(context, R.layout.media_download_row,mediaList);
		this.ctx = context;
		this.mediaList = mediaList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.media_download_row, parent, false);
		Media m = mediaList.get(position);
		rowView.setTag(m);
		TextView mediaTitle = (TextView) rowView.findViewById(R.id.media_title);
		mediaTitle.setText(m.getFilename());
		TextView mediaFileSize = (TextView) rowView.findViewById(R.id.media_file_size);
		if(m.getFileSize() != 0){
			mediaFileSize.setText(ctx.getString(R.string.media_file_size,m.getFileSize()/(1024*1024)));
		} else {
			mediaFileSize.setVisibility(View.GONE);
		}
		
		
		Button downloadBtn = (Button) rowView.findViewById(R.id.action_btn);
		downloadBtn.setTag(m);
		downloadBtn.setOnClickListener(new View.OnClickListener() {
         	public void onClick(View v) {
         		Media m = (Media) v.getTag();
         		DownloadMediaListAdapter.this.ctx.download(m);
         	}
         });
		return rowView;
	}

}
