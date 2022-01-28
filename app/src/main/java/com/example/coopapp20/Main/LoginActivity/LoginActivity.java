package com.example.coopapp20.Main.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coopapp20.Main.MainRepository;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ActivitySuperBinding;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private MainRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        repository = new MainRepository(getApplication());
        ActivitySuperBinding binding = ActivitySuperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        NavHostFragment navfrag = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.SuperNavFrag);
        NavController navController = Objects.requireNonNull(navfrag).getNavController();
        viewModel.setSuperNavController(navController);

        SharedPreferences SharedPref = getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);

        //application description
        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(SharedPref.getBoolean(getString(R.string.FirstVisit), false)){
                //open fragment to guide user
                navController.navigate(R.id.SplashToDemo);

            }else{
                if(SharedPref.getBoolean(getString(R.string.LoggedIn), false)){

                    viewModel.setCurrentUser(repository.getUser(SharedPref.getInt(getString(R.string.CurrentUserId), 0)));
                    viewModel.getSuperNavController().navigate(R.id.SplashToMain);

                }else {
                    runOnUiThread(() -> navController.navigate(R.id.SplashToSelection));
                }
            }

        }).start();
    }
}

