package com.example.jgz.jgzafvalapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener {

    private Menu menuList;

    Class fragmentClass = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.home_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerView.findViewById(R.id.menu_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Intent loginIntent;
        loginIntent = getIntent();

        Account myAccount = new Account(loginIntent.getStringExtra(LoginActivity.EMAIL), LoginActivity.ACCOUNT_TYPE);

        email = (TextView) header.findViewById(R.id.profile_email);
        name = (TextView) header.findViewById(R.id.profile_name);
        email.setText(loginIntent.getStringExtra(LoginActivity.EMAIL));
        name.setText(AccountManager.get(this).getUserData(myAccount, LoginActivity.NAME));

        // Create Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorDarkOrange));
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        // Set default fragment
        navigationView.setCheckedItem(R.id.nav_home);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_icon, menu);
        this.menuList = menu;
        ChangeMenu();
        return true;
    }

    private void ChangeMenu() {
        MenuItem userItem = menuList.findItem(R.id.action_right);
        if (fragmentClass == LogFragment.class) {
            userItem.setVisible(true);
            userItem.setIcon(R.drawable.ic_right_clear);
        }
        else {
            userItem.setVisible(false);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();
        String title = (String) item.getTitle();
        int toolbarColor = R.color.colorDarkBlue;
        int statusbarColor = R.color.colorBlue;

        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);

        switch (id) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                toolbarColor = R.color.colorDarkBlue;
                statusbarColor = R.color.colorBlue;
                break;
            case R.id.nav_log:
                fragmentClass = LogFragment.class;
                toolbarColor = R.color.colorDarkGreen;
                statusbarColor = R.color.colorGreen;
                break;
            case R.id.nav_challenge:
                fragmentClass = ChallengeFragment.class;
                toolbarColor = R.color.colorDarkPink;
                statusbarColor = R.color.colorPink;
                break;
            case R.id.nav_quiz:
                fragmentClass = QuizFragment.class;
                toolbarColor = R.color.colorDarkBlue;
                statusbarColor = R.color.colorBlue;
                break;
            case R.id.nav_info:
                fragmentClass = BlankFragment.class;
                toolbarColor = R.color.colorDarkOrange;
                statusbarColor = R.color.colorOrange;
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_contact:
                fragmentClass = ContactFragment.class;
                toolbarColor = R.color.colorDarkPurple;
                statusbarColor = R.color.colorPurple;
                break;
            default:
                fragmentClass = HomeFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, toolbarColor)));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, statusbarColor));
            setTitle(title);
            setFragment(fragment);
            ChangeMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_home, fragment).commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment().newInstance("Voeding"), "Voeding");
        adapter.addFragment(new InfoFragment().newInstance("Beweging"), "Beweging");
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
