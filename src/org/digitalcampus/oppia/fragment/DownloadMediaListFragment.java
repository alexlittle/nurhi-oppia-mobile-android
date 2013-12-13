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
package org.digitalcampus.oppia.fragment;

import java.util.ArrayList;

import org.digitalcampus.oppia.activity.DownloadMediaActivity;
import org.digitalcampus.oppia.adapter.DownloadMediaListAdapter;
import org.digitalcampus.oppia.model.Media;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;

public class DownloadMediaListFragment extends SherlockListFragment {

	public final static String TAG = DownloadMediaListFragment.class.getSimpleName();
	private ArrayList<Media> mediaList;
	private DownloadMediaListAdapter dmla;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		Bundle bundle = this.getArguments();
		mediaList = (ArrayList<Media>) bundle.getSerializable(DownloadMediaActivity.TAG);
		dmla = new DownloadMediaListAdapter((DownloadMediaActivity) getActivity(), mediaList);
		setListAdapter(dmla);
	}

}