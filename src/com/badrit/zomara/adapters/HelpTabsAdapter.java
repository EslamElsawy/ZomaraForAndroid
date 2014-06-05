package com.badrit.zomara.adapters;

import com.badrit.zomara.fragments.HelpAwakeFragment;
import com.badrit.zomara.fragments.HelpSleepingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class HelpTabsAdapter extends FragmentPagerAdapter {

	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public HelpTabsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new HelpSleepingFragment();
		case 1:
			return new HelpAwakeFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}
}
