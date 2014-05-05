package ca.mcnallydawes.dartsscoring.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ca.mcnallydawes.dartsscoring.ExtrasNames;
import ca.mcnallydawes.dartsscoring.MySQLiteHelper;
import ca.mcnallydawes.dartsscoring.R;
import ca.mcnallydawes.dartsscoring.datasources.CricketDataSource;
import ca.mcnallydawes.dartsscoring.models.CricketGame;

public class TabbedHistoryActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_history);

        String playerName = new String();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString(ExtrasNames.PLAYER_NAME);
        }

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mSectionsPagerAdapter.playerName = playerName;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.tabbed_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public String playerName;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return PlaceHolderFragment.newInstance(0, playerName);
                case 1:
                    return PlaceHolderFragment.newInstance(1, playerName);
                case 2:
                    return PlaceHolderFragment.newInstance(2, playerName);
                case 3:
                    return PlaceHolderFragment.newInstance(3, playerName);
                default:
                    return PlaceHolderFragment.newInstance(0, playerName);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.history_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.history_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.history_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.history_section4).toUpperCase(l);
            }
            return null;
        }
    }

    public static class PlaceHolderFragment extends Fragment {
        private static final String ARG_SECTION_NUM = "section_num";
        private static final String ARG_PLAYER_NAME = "player_name";

        public static PlaceHolderFragment newInstance(int sectionNum, String playerName) {
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUM, sectionNum);
            args.putString(ARG_PLAYER_NAME, playerName);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceHolderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_history, container, false);

            int sectionNum = getArguments().getInt(ARG_SECTION_NUM);
            String playerName = getArguments().getString(ARG_PLAYER_NAME);

            List<String> values = new ArrayList<String>();

            switch (sectionNum) {
                case 0:
                    CricketDataSource cricketSource = new CricketDataSource(getActivity());
                    cricketSource.open();

                    String[] args = new String[2];
                    args[0] = playerName;
                    args[1] = playerName;

                    String question = MySQLiteHelper.COLUMN_PLAYER_1 + " = ? OR "
                            + MySQLiteHelper.COLUMN_PLAYER_2 + " = ?";

                    List<CricketGame> cricketGames = cricketSource
                            .getAllCricketGamesWhere(question, args);

                    for (CricketGame game : cricketGames) {
                        values.add(game.getDate() + " " + game.getWinner() + " beat "
                                + game.getLoser() + " at " + game.getFinish());
                    }

                    cricketSource.close();
                    break;
                case 1:
                    values.add("not implemented");
                    break;
                case 2:
                    values.add("not implemented");
                    break;
                case 3:
                    values.add("not implemented");
                    break;
                default:
                    values.add("default");
                    break;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.darts_simple_list_item_1, values);

            ListView listView = (ListView) rootView.findViewById(R.id.tabbed_history_listView);
            listView.setAdapter(adapter);

            return rootView;
        }
    }

}
