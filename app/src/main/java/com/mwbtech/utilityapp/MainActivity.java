package com.mwbtech.utilityapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CustomerDetails.CallToBillFragment,BillingAddresssActivity.CallToFragment, TaxRegistrationActivity.CallToBankFragment,ConnectivityReceiver.ConnectivityReceiverListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    public static MainActivity instance;
    private CustomerDetails customerDetails;
    private BillingAddresssActivity billingAddresssActivity;
    private TaxRegistrationActivity taxRegistrationActivity;
    private BankDetailsActvity bankDetailsActvity;
    CustomerTrade customerTrade;
    private TabLayout allTabs;
    IntentFilter intentFilter;
    public static ConnectivityReceiver connectivityReceiver;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = findViewById(R.id.coordinator);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        setupTabIcons();
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void setupTabIcons() {
        allTabs.getTabAt(0).setIcon(R.drawable.customer1);
        allTabs.getTabAt(1).setIcon(R.drawable.billing);
        allTabs.getTabAt(2).setIcon(R.drawable.tax);
        allTabs.getTabAt(3).setIcon(R.drawable.bank);
        allTabs.getTabAt(4).setIcon(R.drawable.customer1);
    }


    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }
    private void setupTabLayout() {
        customerDetails = new CustomerDetails();
        billingAddresssActivity = new BillingAddresssActivity();
        taxRegistrationActivity = new TaxRegistrationActivity();
        bankDetailsActvity = new BankDetailsActvity();
        customerTrade = new CustomerTrade();
        allTabs.addTab(allTabs.newTab().setText("CUSTOMER DETAILS"),true);
        allTabs.addTab(allTabs.newTab().setText("BILLING ADDRESS"));
        allTabs.addTab(allTabs.newTab().setText("TAX REGISTRATION"));
        allTabs.addTab(allTabs.newTab().setText("BANK DETAILS"));
        allTabs.addTab(allTabs.newTab().setText("CUSTOMER TRADE"));
    }
    private void bindWidgetsWithAnEvent()
    {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }



    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(customerDetails);
                break;
            case 1 :
                replaceFragment(billingAddresssActivity);
                break;
            case 2 :
                replaceFragment(taxRegistrationActivity);
                break;
            case 3 :
                replaceFragment(bankDetailsActvity);
                break;
            case 4:
                replaceFragment(customerTrade);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.customer_details:
                startActivity(new Intent(this,MainActivity.class));
                MainActivity.this.finish();
                break;
            case R.id.search_customer:

                startActivity(new Intent(this,SearchCustomer.class));
                MainActivity.this.finish();
                break;
            default:
                return true;


        }


        return true;
    }

    @Override
    public void communicateFragment(int pos) {
        setCurrentTabFragment(pos);
        allTabs.getTabAt(pos).select();

    }

    @Override
    public void callingBillingFragment(int pos) {
        setCurrentTabFragment(pos);
        allTabs.getTabAt(pos).select();
    }

    @Override
    public void callingBankingFragment(int pos) {
        setCurrentTabFragment(pos);
        allTabs.getTabAt(pos).select();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(ConnectivityReceiver.isConnected());
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Good! Connected to Internet";

            //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, ""+message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);
            snackbar.show();

        } else {
            message = "Sorry! Not connected to internet";
            //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, ""+message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }
}
