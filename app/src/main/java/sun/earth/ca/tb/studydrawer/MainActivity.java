package sun.earth.ca.tb.studydrawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks ,MainFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
        fragmentManager.beginTransaction().replace(R.id.container,MainFragment.newInstance("start","now"))
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
////        fragmentManager.beginTransaction()
////                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
////                .commit();
//        fragmentManager.beginTransaction().replace(R.id.container,MainFragment.newInstance("start","now"))
//        .commit();
    }

    public void onSectionAttached(int number) {
        //when fragment in main contain change,called from new fragment 's on attach
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        //change actionbar's title,from onCreateOptionsMenu
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //drawer不在的时候，这里决定标题和菜单
            getMenuInflater().inflate(R.menu.main, menu);
            String usrname = sp.getString(NavigationDrawerFragment.PREF_USER_LOGIN_NAME,"");
            if(usrname.length()!=0){
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(true);
            }else{
                menu.findItem(R.id.action_login).setVisible(true);
                menu.findItem(R.id.action_logout).setVisible(false);
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, getString(R.string.Setup_action), Toast.LENGTH_SHORT).show();
            //return true;
        }
        //从抽屉移动过来看看，看来activity,fragment都调用
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(this, "Example action.", Toast.LENGTH_SHORT).show();
            //return true;
        }

        if(item.getItemId() == R.id.action_login){
            //sp.edit().putString(NavigationDrawerFragment.PREF_USER_LOGIN_NAME,"username").apply();
            mNavigationDrawerFragment.userLogin();
            supportInvalidateOptionsMenu();
            mNavigationDrawerFragment.flashuserinfo();
        }

        if(item.getItemId() == R.id.action_logout){
            sp.edit().putString(NavigationDrawerFragment.PREF_USER_LOGIN_NAME,"").apply();
            supportInvalidateOptionsMenu();
            mNavigationDrawerFragment.flashuserinfo();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(int tab) {
        mTitle = getString(tab);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
