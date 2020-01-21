package com.mwbtech.utilityapp.main_page;

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
import com.mwbtech.utilityapp.Preferences.PrefManager;
import com.mwbtech.utilityapp.R;
import com.mwbtech.utilityapp.objects.CustomerCreation;
import com.mwbtech.utilityapp.search_page.ListCustomersActivity;
import com.mwbtech.utilityapp.search_page.SearchCustomer;
import com.mwbtech.utilityapp.bank_details.BankDetailsActvity;
import com.mwbtech.utilityapp.billing_address.BillingAddresssActivity;
import com.mwbtech.utilityapp.customer_details.CustomerDetails;
import com.mwbtech.utilityapp.customer_details.CustomerTrade;
import com.mwbtech.utilityapp.internet_connection.ConnectivityReceiver;
import com.mwbtech.utilityapp.internet_connection.MyInternetCheck;
import com.mwbtech.utilityapp.tax_register.TaxRegistrationActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CustomerDetails.CallToBillFragment, BillingAddresssActivity.CallToFragment, TaxRegistrationActivity.CallToBankFragment, ConnectivityReceiver.ConnectivityReceiverListener, BankDetailsActvity.customerTradeFragment {

    //private ActionBarDrawerToggle actionBarDrawerToggle;
    //DrawerLayout drawer;
    //NavigationView navigationView;
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
    public static PrefManager prefManager;
    public static CustomerCreation customerCreation;
    public static CustomerCreation creation;
    public static int salesmanID = 1599;
    public static int orgID = 1;
    public static int createdByID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Customer Creation");
        customerCreation = new CustomerCreation();
        prefManager = new PrefManager(this);
        coordinatorLayout = findViewById(R.id.coordinator);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        setupTabIcons();
        //drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
                if(prefManager.getSavedObjectFromPreference(this,"mwb-welcome", "customer", CustomerCreation.class) == null) {
                    allTabs.getTabAt(0).select();
                }else {
                    replaceFragment(billingAddresssActivity);
                }
                break;
            case 2 :
                if(prefManager.getSavedObjectFromPreference(this,"mwb-welcome", "customer", CustomerCreation.class) == null) {
                    allTabs.getTabAt(1).select();
                }else {
                    replaceFragment(taxRegistrationActivity);
                }
                break;
            case 3 :
                if(prefManager.getSavedObjectFromPreference(this,"mwb-welcome", "customer", CustomerCreation.class) == null) {
                    allTabs.getTabAt(2).select();
                }else {
                    replaceFragment(bankDetailsActvity);
                }
                break;
            case 4:
                if(prefManager.getSavedObjectFromPreference(this,"mwb-welcome", "customer", CustomerCreation.class) == null) {
                    allTabs.getTabAt(3).select();
                }else {
                    replaceFragment(customerTrade);
                }
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
    /*@Override
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
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ListCustomersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        MainActivity.this.finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.customer_details:
                startActivity(new Intent(this,MainActivity.class));
                MainActivity.this.finish();
                break;
            case R.id.search_customer:

                startActivity(new Intent(this, SearchCustomer.class));
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

    @Override
    public void callingCustomerTradeFragment(int pos) {
        setCurrentTabFragment(pos);
        allTabs.getTabAt(pos).select();
    }
}
