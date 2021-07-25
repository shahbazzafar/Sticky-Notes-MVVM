package com.example.notesappmvvm.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notesappmvvm.R;
import com.example.notesappmvvm.adapters.NotesAdapter;
import com.example.notesappmvvm.room.Notes;
import com.example.notesappmvvm.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NotesViewModel notesViewModel;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    FloatingActionButton floatingActionButton;
    TextView noFilter,hightolow,lowtohigh;
    List<Notes> filterNotesAllList;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naavigation_drawer);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        recyclerView = findViewById(R.id.notesRecyclerview);
        floatingActionButton = findViewById(R.id.newNotesBtn);
        noFilter = findViewById(R.id.noFilter);
        hightolow = findViewById(R.id.hightolow);
        lowtohigh = findViewById(R.id.lowtohigh);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        noFilter.setBackgroundResource(R.drawable.filter_selected_shape);

        noFilter.setOnClickListener(v -> {
            loadData(0);
           hightolow.setBackgroundResource(R.drawable.filter_shape);
           lowtohigh.setBackgroundResource(R.drawable.filter_shape);
            noFilter.setBackgroundResource(R.drawable.filter_selected_shape);
        });
        hightolow.setOnClickListener(v -> {
            loadData(1);
            noFilter.setBackgroundResource(R.drawable.filter_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_shape);
            hightolow.setBackgroundResource(R.drawable.filter_selected_shape);
        });
        lowtohigh.setOnClickListener(v -> {
            loadData(2);
            hightolow.setBackgroundResource(R.drawable.filter_shape);
            noFilter.setBackgroundResource(R.drawable.filter_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_selected_shape);
        });

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InsertNotesActivity.class);
            startActivity(intent);
        });

        notesViewModel.hightolow.observe(this, notes -> {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            adapter = new NotesAdapter(MainActivity.this, notes);
            recyclerView.setAdapter(adapter);
            filterNotesAllList = notes;
        });

    }

    private void loadData(int i) {
        if (i==0)
        {
            notesViewModel.getAllNotes.observe(this, notes -> {
                setAdapter(notes);
                filterNotesAllList = notes;
            });
        }
        if (i==1)
        {
            notesViewModel.hightolow.observe(this, notes -> {
                setAdapter(notes);
                filterNotesAllList = notes;
            });
        }
        if (i==2)
        {
            notesViewModel.lowtohigh.observe(this, notes -> {
                setAdapter(notes);
                filterNotesAllList = notes;
            });
        }
    }

    private void setAdapter(List<Notes> notes) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new NotesAdapter(MainActivity.this, notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NotesFilter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void NotesFilter(String newText) {
        ArrayList<Notes> arrayList = new ArrayList<>();
        //Toast.makeText(this, "Data: "+filterNotesAllList, Toast.LENGTH_SHORT).show();
          for (Notes notes:this.filterNotesAllList)
          {
              if (notes.notesTitle.contains(newText) || notes.notesSubtitle.contains(newText))
              {
                     arrayList.add(notes);
              }
          }
          this.adapter.searchNotes(arrayList);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_app: {
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            }
            case R.id.share_app: {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out this app at: https://play.google.com/store/apps/details?id=" + this.getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }
//            case R.id.privacy_policy: {
////                startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
////                break;
//            }
            case R.id.more_app: {
                Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Skyhighapps1");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/developer?id=Skyhighapps1")));
                }
                break;
            }
            case R.id.exit_app: {
                finish();
                finishAffinity();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                break;
            }
            case R.id.uninstal_app: {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivity(intent);
                break;
            }
        }


       drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}