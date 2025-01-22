package com.example.myzametki;

import static java.util.Locale.filter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myzametki.Adapter.ListAdapter;
import com.example.myzametki.Database.RoomDB;
import com.example.myzametki.Models.Zametki;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ListAdapter listAdapter;
    RoomDB dataBase;
    List<Zametki> zametki = new ArrayList<>();
    SearchView searchView_home;
    Zametki selektedZametka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        addButton = findViewById(R.id.leb_add);

        searchView_home = findViewById(R.id.searchView_home);

        dataBase = RoomDB.getInstance(this);
        zametki = dataBase.mainDAO().getAll();
        updateRecycler(zametki);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ZametkiTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                return false;
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void filter(String newText) {
        List<Zametki> filteredList = new ArrayList<>();
        for (Zametki singleZametki : zametki){
            if (singleZametki.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleZametki.getZametki().toLowerCase().contains(newText.toLowerCase())); {
                filteredList.add(singleZametki);
            }
        }
        listAdapter.filterList(filteredList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK){
                Zametki new_zametki = (Zametki) data.getSerializableExtra("zametki");
                dataBase.mainDAO().insert(new_zametki);
                zametki.clear();
                zametki.addAll(dataBase.mainDAO().getAll());
                listAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 102) {
            if(resultCode == Activity.RESULT_OK) {
                Zametki new_zametki = (Zametki) data.getSerializableExtra("zametki");
                dataBase.mainDAO().update(new_zametki.getID(), new_zametki.getTitle(), new_zametki.getZametki());
                zametki.clear();
                zametki.addAll(dataBase.mainDAO().getAll());
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Zametki> zametki) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        listAdapter = new ListAdapter(MainActivity.this, zametkiClickListener, zametki);
        recyclerView.setAdapter(listAdapter);
    }
    private final ZametkiClickListener zametkiClickListener = new ZametkiClickListener() {
        @Override
        public void onClick(Zametki zametki) {
            Intent intent = new Intent(MainActivity.this, ZametkiTakerActivity.class);
            intent.putExtra("old_text", zametki);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Zametki zametki, CardView cardView) {
            selektedZametka = new Zametki();
            selektedZametka = zametki;
            showPopup (cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.pin) {
            if (selektedZametka.isPinned()) {
                dataBase.mainDAO().pin(selektedZametka.getID(), false);
                Toast.makeText(MainActivity.this, "Откреплено", Toast.LENGTH_SHORT).show();
            } else {
                dataBase.mainDAO().pin(selektedZametka.getID(), true);
                Toast.makeText(MainActivity.this, "Прикреплено", Toast.LENGTH_SHORT).show();
            }
            zametki.clear();
            zametki.addAll(dataBase.mainDAO().getAll());
            listAdapter.notifyDataSetChanged();
            return true;
        }
        if(item.getItemId()==R.id.delete) {
            dataBase.mainDAO().delete(selektedZametka);
            zametki.remove(selektedZametka);
            listAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
