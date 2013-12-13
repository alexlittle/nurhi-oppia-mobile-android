/* 
 * This file is part of OppiaMobile - http://oppia-mobile.org/
 * 
 * OppiaMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OppiaMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OppiaMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package org.digitalcampus.oppia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.fragment.DownloadMediaListFragment;
import org.digitalcampus.oppia.listener.DownloadMediaListener;
import org.digitalcampus.oppia.model.DownloadProgress;
import org.digitalcampus.oppia.model.Media;
import org.digitalcampus.oppia.task.DownloadMediaTask;
import org.digitalcampus.oppia.task.Payload;
import org.digitalcampus.oppia.utils.ConnectionUtils;
import org.digitalcampus.oppia.utils.UIUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bugsense.trace.BugSenseHandler;

public class DownloadMediaActivity extends AppFragmentActivity implements DownloadMediaListener {

	public static final String TAG = DownloadMediaActivity.class.getSimpleName();
	private ArrayList<Media> missingMedia = new ArrayList<Media>();
	private AlertDialog alertDialog;
	private DownloadMediaListFragment dmlf;
	private DownloadMediaTask task;
	private boolean inProgress = false;
	private DialogFragment downloadDialog;
	private DownloadMediaListener mDownloadListener;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_media);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			missingMedia = (ArrayList<Media>) bundle.getSerializable(DownloadMediaActivity.TAG);

			FragmentManager fm = getSupportFragmentManager();
			dmlf = (DownloadMediaListFragment) fm.findFragmentByTag("task");

			if (dmlf == null) {
				Log.d(TAG,"dmlf is null");
				dmlf = new DownloadMediaListFragment();
				dmlf.setArguments(bundle);
				fm.beginTransaction().add(R.id.missing_media_list, dmlf, "task").commit();
			} else {
				Log.d(TAG,"dmlf is not null");
			}
		}

		Button downloadViaPCBtn = (Button) this.findViewById(R.id.download_media_via_pc_btn);
		downloadViaPCBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				downloadViaPC();
			}
		});

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = prefs.edit();
		e.putLong(getString(R.string.prefs_last_media_scan), 0);
		e.commit();
	}

	@Override
	public void onPause() {
		super.onPause();
		// kill any open dialogs
		if (alertDialog != null) {
			alertDialog.dismiss();
		}
		if (downloadDialog != null) {
			downloadDialog.dismiss();
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		// kill any open dialogs
		if (downloadDialog != null) {
			downloadDialog.dismiss();
		}
		
	}
	private void downloadViaPC() {
		String filename = "mobile-learning-media.html";
		String strData = "<html>";
		strData += "<head><title>" + this.getString(R.string.download_via_pc_title) + "</title></head>";
		strData += "<body>";
		strData += "<h3>" + this.getString(R.string.download_via_pc_title) + "</h3>";
		strData += "<p>" + this.getString(R.string.download_via_pc_intro) + "</p>";
		strData += "<ul>";
		for (Object o : missingMedia) {
			Media m = (Media) o;
			strData += "<li><a href='" + m.getDownloadUrl() + "'>" + m.getFilename() + "</a></li>";
		}
		strData += "</ul>";
		strData += "</body></html>";
		strData += "<p>" + this.getString(R.string.download_via_pc_final, "/digitalcampus/media/") + "</p>";

		File file = new File(Environment.getExternalStorageDirectory(), filename);
		try {
			FileOutputStream f = new FileOutputStream(file);
			Writer out = new OutputStreamWriter(new FileOutputStream(file));
			out.write(strData);
			out.close();
			f.close();
			UIUtils.showAlert(this, R.string.info, this.getString(R.string.download_via_pc_message, filename));
		} catch (FileNotFoundException e) {
			BugSenseHandler.sendException(e);
			e.printStackTrace();
		} catch (IOException e) {
			BugSenseHandler.sendException(e);
			e.printStackTrace();
		}
	}

	public void setDownloadMediaListener(DownloadMediaListener dml) {
        synchronized (this) {
        	mDownloadListener = dml;
        }
    }
	
	public void downloadProgressUpdate(DownloadProgress msg) {
		if(downloadDialog != null){
			//downloadDialog.setMessage(msg.getMessage());
			//downloadDialog.setProgress(msg.getProgress());
		}
		Log.d(TAG,"message:" + msg.getProgress());
	}

	public void downloadComplete(Payload response) {
		this.closeDialog();
		this.inProgress = false;
		synchronized (this) {
			if (mDownloadListener != null) {
				mDownloadListener.downloadComplete(response);
			}
		}
		
		if (response.isResult()) {
			missingMedia.remove((Media) response.getData().get(0));
			alertDialog = UIUtils.showAlert(this, R.string.info, response.getResultResponse());
		} else {
			alertDialog = UIUtils.showAlert(this, R.string.error, response.getResultResponse());
		}
	}
	
	public void download(Media media) {
		if(!ConnectionUtils.isOnWifi(this)){
			UIUtils.showAlert(this, R.string.warning, R.string.warning_wifi_required);
			return;
		}

		// show progress dialog
		this.showProgressDialog();
		this.inProgress = true;
		
		ArrayList<Media> alMedia = new ArrayList<Media>();
		alMedia.add(media);
		task = new DownloadMediaTask(this);
		Payload p = new Payload(alMedia);
		task.setDownloadListener(this);
		task.execute(p);
	}
	
	public void showProgressDialog(){
		// show progress dialog
		/*downloadDialog = new ProgressDialog(this);
		downloadDialog.setTitle(R.string.downloading);
		downloadDialog.setMessage(this.getString(R.string.download_starting));
		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadDialog.setProgress(0);
		downloadDialog.setMax(100);
		downloadDialog.setCancelable(false);	
		downloadDialog.show();*/
		DialogFragment newFragment = MyAlertDialogFragment.newInstance(R.string.app_name);
	    newFragment.show(getSupportFragmentManager(), "dialog");

	}

	
	public void closeDialog(){
		if (downloadDialog != null){
			downloadDialog.dismiss();
		}
	}
	
	public void openDialog(){
		if (downloadDialog != null && this.inProgress){
			//downloadDialog.show();
		}
	}
	
	private static class MyAlertDialogFragment extends DialogFragment {

		private AlertDialog pDialog;
	    public static MyAlertDialogFragment newInstance(int title) {
	        MyAlertDialogFragment frag = new MyAlertDialogFragment();
	        Bundle args = new Bundle();
	        args.putInt("title", title);
	        frag.setArguments(args);
	        return frag;
	    }

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        int title = getArguments().getInt("title");
	        pDialog = new AlertDialog.Builder(getActivity()).create();
	        pDialog.setTitle(title);
	        return pDialog;
	       /*return new ProgressDialog.Builder(getActivity())
	                .setTitle(title)
	                .setMessage("Hello")
	                .setPositiveButton(R.string.ok,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            Log.d(TAG,"positive");
	                        }
	                    }
	                )
	                .setNegativeButton(R.string.cancel,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                        	Log.d(TAG,"negative");
	                        }
	                    }
	                )
	                .create();*/
	    }
	    
	}
}
