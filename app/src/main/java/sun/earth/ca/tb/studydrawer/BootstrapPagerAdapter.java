package sun.earth.ca.tb.studydrawer;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bo on 2014-10-28.
 */
public class BootstrapPagerAdapter extends FragmentPagerAdapter {
    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public  BootstrapPagerAdapter(final Resources resources, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new NewsFragment();
                break;
            case 1:
                result = new UsersFragment();
                break;
            case 2:
                result = new CheckinsFragment();
                break;
            default:
                result = null;
                break;
        }
        if (result != null) {
            result.setArguments(new Bundle()); //TODO do we need this?
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_news);
            case 1:
                return resources.getString(R.string.page_users);
            case 2:
                return resources.getString(R.string.page_checkins);
            default:
                return null;
        }
    }
}
