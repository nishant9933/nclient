package com.example.fugro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT =4;
	private String titles[] ;

	public TabsPagerAdapter(FragmentManager fm, String[] titles2) {

		super(fm);
		titles=titles2;
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
			case 0:
				return AllAlertFragment.newInstance(position);
			case 1:
				return UrgentAlertFragment.newInstance(position);
			case 2:
				return HappeningsAlertFragment.newInstance(position);
			case 3:
				return AlertAfterFragment.newInstance(position);
	 }

		   return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		//pager.setCurrentItem(pager.getCurrentItem()+1, true);
		
		return PAGE_COUNT;
	}

}
