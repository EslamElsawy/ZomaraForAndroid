package com.badrit.zomara.fragments;

import java.util.List;

import com.badrit.zomara.adapters.ImageAdapter;
import com.badrit.zomara.models.BaseListElement;
import com.badit.zomara.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class HelpSleepingFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_help_sleeping, container, false);

		return rootView;
	}

	public void drawFriendsOnGridView(List<BaseListElement> listElements) {
		GridView gridview = (GridView) getView().findViewById(R.id.gridviewhelpsleeping);
		gridview.setAdapter(new ImageAdapter(getActivity(), listElements));

	}
}
