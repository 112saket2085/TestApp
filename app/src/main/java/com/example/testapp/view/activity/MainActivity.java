package com.example.testapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.testapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        initActionBar();
    }

    public NavController getNavController() {
        return navController;
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    public void setActionBarTitle(String msg) {
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setTitle(msg);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
