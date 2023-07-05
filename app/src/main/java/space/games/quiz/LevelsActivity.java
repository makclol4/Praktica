package space.games.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import space.games.quiz.adapter.LevelsAdapter;
import space.games.quiz.model.Level;

public class LevelsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView listLevels;
    private LevelsAdapter listLevelsAdapter;

    private DatabaseReference reference;

    private ArrayList<Level> levels = new ArrayList<>();

    private int maxLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelevels);

        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
        maxLevel = save.getInt(LevelActivity.LEVEL_MAX, 0);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);

        listLevels = findViewById(R.id.list_levels);
        listLevels.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listLevelsAdapter = new LevelsAdapter();
        listLevels.setAdapter(listLevelsAdapter);
        listLevelsAdapter.setOnItemClickListener(new LevelsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Level level) {
                Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                intent.putExtra(LevelActivity.LEVEL_KEY, level.getKey());
                intent.putExtra(LevelActivity.LEVEL_MAX, maxLevel);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference();
        loadLevels();
    }

    private void loadLevels() {
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.show();
        reference.child("levels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Level level;
                    for (DataSnapshot levelData : dataSnapshot.getChildren()) {
                        level = new Level();
                        level.setKey(levelData.getKey());
                        level.setName(levelData.child("name").getValue().toString());
                        levels.add(level);
                    }
                    for (int i = maxLevel + 1; i < levels.size(); i++) {
                        levels.get(i).setOpen(false);
                        levels.get(i).setName("XXXXXXXXXXX");
                    }
                    listLevelsAdapter.setData(levels);
                    listLevelsAdapter.notifyDataSetChanged();
                }
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(listLevels, "Произошла ошибка...", Snackbar.LENGTH_LONG).setAction("Повтороить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadLevels();
                    }
                }).show();
                progressDialog.cancel();
            }
        });
    }

    @Override //Системная конпка назад
    public void onBackPressed() {
        try {
            Intent intent = new Intent(LevelsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}
