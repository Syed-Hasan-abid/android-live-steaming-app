package com.example.livevideostreaming.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telecom.Connection;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.livevideostreaming.Adapters.MainRecyclerViewAdapter;
import com.example.livevideostreaming.Models.AllCategory;
import com.example.livevideostreaming.Models.CategoryItemList;
import com.example.livevideostreaming.R;
import com.example.livevideostreaming.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.livevideostreaming.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainScreen extends AppCompatActivity {

    TextView tvSeries,movies;
    BottomNavigationView bottomNavigationView;
    MenuItem menuItem;
    MainRecyclerViewAdapter mainRecyclerViewAdapter;
    RecyclerView mainRecyclerView;
    List<AllCategory> all_CategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();

       tvSeries = findViewById(R.id.tvSeries);
       movies = findViewById(R.id.movies);
       bottomNavigationView = findViewById(R.id.bottomNavigation);

       mainRecyclerView = findViewById(R.id.mainRecyclerView);
       all_CategoryList = new ArrayList<>();




       tvSeries.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainScreen.this,TvSeries.class);
               startActivity(intent);
           }
       });

       movies.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainScreen.this,Movies.class);
               startActivity(intent);
           }
       });

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected())
        {
            AlertDialog.Builder alertDialogue = new AlertDialog.Builder(MainScreen.this);
            alertDialogue.setTitle("No Internet connection");
            alertDialogue.setMessage("Please turn on your internet");
            alertDialogue.setCancelable(false);

            alertDialogue.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                }
            });
            alertDialogue.create().show();


        } else
        {
            // To fetch data from API
            getAllMoviesData();
        }


        Menu menu = bottomNavigationView.getMenu();
        menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_item: Intent intent = new Intent(MainScreen.this,Search.class);
                                            startActivity(intent);
                                           break;
                    case R.id.setting_item: Intent intent1 = new Intent(MainScreen.this,Settings.class);
                                            startActivity(intent1);
                                             break;
                    case R.id.home_item: if(item.getItemId() != R.id.home_item)
                                        {
                                            Intent intent2 = new Intent(MainScreen.this,MainScreen.class);
                                            startActivity(intent2);
                                            finish();
                                        }
                                            break;


                }

                return true;
            }
        });

    }

    private void setRecyclerView(List<AllCategory> allCategoryList)
    {
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(MainScreen.this,allCategoryList);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainScreen.this));
        mainRecyclerView.setAdapter(mainRecyclerViewAdapter);
    }

    private void getAllMoviesData() {

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getAllCategoriesMovies()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<List<AllCategory>>() {
                                @Override
                                public void onNext(@NonNull List<AllCategory> allCategoryList) {
                                 all_CategoryList = allCategoryList;
                                 setRecyclerView(allCategoryList);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            }));

    }

    @Override
    protected void onResume() {
        super.onResume();

        menuItem.setChecked(true);

    }

}