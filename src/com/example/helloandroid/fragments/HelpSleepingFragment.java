package com.example.helloandroid.fragments;

import java.util.List;

import com.example.helloandroid.R;
import com.example.helloandroid.adapters.ImageAdapter;
import com.example.helloandroid.models.BaseListElement;

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
