package com.egneese.sellers.activities;

/**
 * Created by nazianoorani on 23/01/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.QRInfoAsyncTask;
import com.egneese.sellers.dashboardfragments.DashboardFragment;
import com.egneese.sellers.dashboardfragments.ProfileFragment;
import com.egneese.sellers.dashboardfragments.ShopsFragment;
import com.egneese.sellers.dashboardfragments.TransactionFragment;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.QRDataDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.security.Encryption;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.util.NetworkCheck;
import com.egneese.sellers.util.RequiredDTOFactory;
import com.google.gson.Gson;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;



public class DashboardActivity extends ActionBarActivity implements MaterialTabListener {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    MaterialTabHost tabHost;
    private Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        res = this.getResources();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("isSent") && bundle.getBoolean("isSent")) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(getResources().getString(R.string.request_placed));
                messageCustomDialogDTO.setContext(this);
                SnackBar.show(this, messageCustomDialogDTO);
            }

            if (bundle.containsKey("qrDataDTO") && bundle.getString("qrDataDTO") != null) {
                getData(bundle.getString("qrDataDTO"));
            }

            if (bundle.containsKey("paid") && bundle.getString("paid") != null && bundle.getString("paid").equals("paid")) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(getResources().getString(R.string.paid));
                messageCustomDialogDTO.setContext(this);
                SnackBar.success(this, messageCustomDialogDTO);
            }

        }
        addShortcut(this);


        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setIcon(getIcon(i)).setTabListener(this)
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);


    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    public void addShortcut(Context context) {
        final Intent shortcutIntent = new Intent(DashboardActivity.this, ScanQRCodeActivity.class);
        final Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("duplicate", false);
        sendBroadcast(intent);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case 0:
                    fragment = new DashboardFragment();
                    break;
                case 1:
                    fragment = new ShopsFragment();
                    break;
                case 2:
                    fragment = new TransactionFragment();
                    break;
                case 3:
                    fragment = new ProfileFragment();
            }
            fragment.setRetainInstance(true);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "tab 1";
                case 1:
                    return "tab 2";
                case 2:
                    return "tab 3";
                case 3:
                    return "tab 4";
                default:
                    return null;
            }
        }
    }

    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private Drawable getIcon(int position) {
        switch (position) {
            case 0:
                return res.getDrawable(R.drawable.ic_tabs_dashobard);
            case 1:
                return res.getDrawable(R.drawable.ic_tabs_shops);
            case 2:
                return res.getDrawable(R.drawable.ic_tabs_transaction);
            case 3:
                return res.getDrawable(R.drawable.ic_tabs_profile);
        }
        return null;
    }

    private void getData(String dataFromQR) {
        try {
            String data = Encryption.decrypt(dataFromQR);

            Gson gson = new Gson();
            QRDataDTO qrDataDTO = gson.fromJson(data, QRDataDTO.class);
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setRequestId(qrDataDTO.getRequestId());

            QRInfoAsyncTask qrInfoAsyncTask = new QRInfoAsyncTask(this, RequiredDTOFactory.getObject(this), requestDTO, null);
            qrInfoAsyncTask.execute();

        } catch (Exception e) {
            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
            messageCustomDialogDTO.setTitle(getResources().getString(R.string.oops));
            messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
            messageCustomDialogDTO.setMessage(getResources().getString(R.string.invalid_qr));
            messageCustomDialogDTO.setContext(this);
            SnackBar.show(this, messageCustomDialogDTO);
        }
    }
}
/*
public class DashboardActivity extends AppCompatActivity {
    @InjectView(R.id.tabs)
    TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.inject(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        int[] tabIcons = {R.drawable.ic_tabs_dashobard, R.drawable.ic_tabs_shops, R.drawable.ic_tabs_transaction, R.drawable.ic_tabs_profile};

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "ONE");
        adapter.addFrag(new HomeFragment(), "TWO");
        adapter.addFrag(new HomeFragment(), "THREE");
        adapter.addFrag(new ProfileFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
*/
