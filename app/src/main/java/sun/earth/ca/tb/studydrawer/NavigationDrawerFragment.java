package sun.earth.ca.tb.studydrawer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udinic.accounts_authenticator_example.authentication.AccountGeneral;
import com.udinic.accounts_authenticator_example.authentication.ParseComServerAuthenticate;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import static com.udinic.accounts_authenticator_example.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     * 第一次抽屉打开
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    //原来登入的名字
    public static final String PREF_USER_LOGIN_NAME = "user_login_name";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     * 连接抽屉和actionbar的纽带
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    //抽屉的view
    private View mFragmentContainerView;

    //the position did not save in sp
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    RelativeLayout userlogin;
    RelativeLayout userinfo;
    TextView loginusername;
    TextView mUserNameView;
    TextView mUserEmailView;

    String mUsername;

    private AccountManager mAccountManager;
    private AlertDialog.Builder mAlertDialog;
    private boolean mInvalidate;

    String mAuthtoken;
    int choice=0;

    SharedPreferences sp;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mAccountManager = AccountManager.get(this.getActivity());

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.

        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        mUsername = sp.getString(PREF_USER_LOGIN_NAME,"");

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        //记住主fragment
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //抽屉界面实现
        View root;
        root =  inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView) root.findViewById(R.id.list_item);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //抽屉动作
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        userlogin = (RelativeLayout) root.findViewById(R.id.signuplongin);
        userinfo = (RelativeLayout) root.findViewById(R.id.userDrawer);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userinfoclick();
            }
        });

        loginusername = (TextView) root.findViewById(R.id.titsign);

        mUserNameView = (TextView) root.findViewById(R.id.tituloDrawer);
        mUserEmailView = (TextView) root.findViewById(R.id.subTituloDrawer);
        flashuserinfo();


        return root;
    }

    public void flashuserinfo(){

        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUsername = sp.getString(PREF_USER_LOGIN_NAME,"");

        if(mUsername.length()==0) {
            userinfo.setVisibility(View.GONE);
            userlogin.setVisibility(View.VISIBLE);
        }else{
            userinfo.setVisibility(View.VISIBLE);
            userlogin.setVisibility(View.GONE);
            int nowusernumber=-1;

            final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

            String name[] = new String[availableAccounts.length];
            if(availableAccounts.length>0){
                for (int i = 0; i < availableAccounts.length; i++) {
                    name[i] = availableAccounts[i].name;
                    if(mUsername!=null){
                        if(mUsername.equals(name[i])){
                            nowusernumber=i;
                        }
                    }
                }
                if(nowusernumber!=-1){
                    getExistingAccountAuthToken(availableAccounts[nowusernumber], AUTHTOKEN_TYPE_FULL_ACCESS);
                }
            }
        }
    }

    private void userinfoclick() {
//        Toast.makeText(this.getActivity(),"user info click",Toast.LENGTH_SHORT).show();
        userLogin();
    }

    public void userLogin() {
        //Toast.makeText(this.getActivity(),"user login or signup",Toast.LENGTH_SHORT).show();
        //用户注册
        //getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
        //addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

        showAccountPicker(AUTHTOKEN_TYPE_FULL_ACCESS, false);
        //getnowuserinfo();
    }

    private void getnowuserinfo() {
        if(mAuthtoken!=null){
            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("X-Parse-Application-Id", "zpDBLWZb3QXcxHh9LnGeRGtZVRUva3RFpO0NOYEp");
                    request.addHeader("X-Parse-REST-API-Key","anK8StLPPdJLOzzy3dKPwLAqx6xq4NIQFPvxXUbB");
                    //request.addHeader("Content-Type", "application/json");
                }
            };

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ParseComServerAuthenticate.URL_BASE)
                    .setRequestInterceptor(requestInterceptor)
                    .build();

            ParseComServerAuthenticate.userInterface user = restAdapter.create(ParseComServerAuthenticate.userInterface.class);
            final ParseComServerAuthenticate.User nownuser = user.getuserinfofromsession(mAuthtoken);

            if(nownuser!=null){
                this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //loginusername.setText(nownuser.getUsername());
                        userinfo.setVisibility(View.VISIBLE);
                        userlogin.setVisibility(View.GONE);

                        mUserNameView.setText(nownuser.getUsername());
                        mUserEmailView.setText(nownuser.getEmail());
                    }
                });

//                loginusername.postInvalidate();

            }
        }
    }

    /**
     * Add new account to the account manager
     * @param accountType
     * @param authTokenType
     */
    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this.getActivity(), new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    showMessage("Account was created");
                    Log.d("udinic", "AddNewAccount name is: " + bnd.getString("authAccount"));
                    sp.edit().putString(PREF_USER_LOGIN_NAME,bnd.getString("authAccount")).apply();
                    flashuserinfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }, null);
    }

    /**
     * Show all the accounts registered on the account manager. Request an auth token upon user select.
     * @param authTokenType
     */
    private void showAccountPicker(final String authTokenType, final boolean invalidate) {
        mInvalidate = invalidate;
        int nowusernumber=0;
        mUsername = sp.getString(PREF_USER_LOGIN_NAME,"");
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        String name[] = new String[availableAccounts.length+1];
        if(availableAccounts.length>0){
            for (int i = 0; i < availableAccounts.length; i++) {
                name[i] = availableAccounts[i].name;
                if(mUsername!=null){
                    if(mUsername.equals(name[i])){
                        nowusernumber=i;
                    }
                }
            }
        }
        name[availableAccounts.length]="Add a new account";
//        if (availableAccounts.length == 0) {
//            Toast.makeText(this.getActivity(), "No accounts", Toast.LENGTH_SHORT).show();
//        } else {
//            String name[] = new String[availableAccounts.length];
//            for (int i = 0; i < availableAccounts.length; i++) {
//                name[i] = availableAccounts[i].name;
//            }
//
//            // Account picker
//            mAlertDialog = new AlertDialog.Builder(this.getActivity()).setTitle("Pick Account").setAdapter(new ArrayAdapter<String>(this.getActivity().getBaseContext(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if(invalidate)
//                        invalidateAuthToken(availableAccounts[which], authTokenType);
//                    else
//                        getExistingAccountAuthToken(availableAccounts[which], authTokenType);
//                }
//            }).create();
//            mAlertDialog.show();
//        }

        mAlertDialog = new AlertDialog.Builder(this.getActivity());
        mAlertDialog.setTitle("Pick Account");



        mAlertDialog.setSingleChoiceItems(name, nowusernumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                }
        );
        mAlertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(choice<availableAccounts.length)
                        if(invalidate)
                            invalidateAuthToken(availableAccounts[choice], authTokenType);
                        else {
                            getExistingAccountAuthToken(availableAccounts[choice], authTokenType);
                            sp.edit().putString(PREF_USER_LOGIN_NAME,availableAccounts[choice].name).apply();
                            //getnowuserinfo();
                        }
                    else
                        addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

            }
        });

        mAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAlertDialog.create();
        mAlertDialog.show();
    }

    /**
     * Invalidates the auth token for the account
     * @param account
     * @param authTokenType
     */
    private void invalidateAuthToken(final Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this.getActivity(), null,null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();

                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    mAccountManager.invalidateAuthToken(account.type, authtoken);
                    showMessage(account.name + " invalidated");
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Get the auth token for an existing account on the AccountManager
     * @param account
     * @param authTokenType
     */
    private void getExistingAccountAuthToken(Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, this.getActivity(), null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bnd = future.getResult();

                    mAuthtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    showMessage((mAuthtoken != null) ? "SUCCESS!\ntoken: " + mAuthtoken : "FAIL");
                    Log.d("udinic", "GetToken Bundle is " + bnd);
                    getnowuserinfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     * @param accountType
     * @param authTokenType
     */
    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this.getActivity(), null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            showMessage(((authtoken != null) ? "SUCCESS!\ntoken: " + authtoken : "FAIL"));
                            Log.d("udinic", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }
                , null);
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NavigationDrawerFragment.this.getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
                | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_HOME_AS_UP);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        //激活回掉
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        // 抽屉在的时候，菜单和标题由抽屉定
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //这个有点怪，这个菜单项的处理为什么放在抽屉的fragment中
//        if (item.getItemId() == R.id.action_example) {
//            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public boolean isUsernameHere(){
        return mUsername.length()!=0;
    }
    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
