package space.games.quiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import space.games.quiz.model.Image;
import space.games.quiz.model.Level;

public class LevelActivity extends AppCompatActivity {


    public static final String LEVEL_KEY = "LEVEL_KEY";
    public static final String LEVEL_MAX = "LEVEL_MAX";

    private ProgressDialog progressDialog;
    private DatabaseReference reference;

    private Level level;

    private Dialog dialog;
    private Dialog dialogEnd;

    private int count = 0;
    private Pair<Image, Image> pair;
    private int maxLevel;

    private final int[] progress = {
            R.id.point1,
            R.id.point2,
            R.id.point3,
            R.id.point4,
            R.id.point5,
            R.id.point6,
            R.id.point7,
            R.id.point8,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        progressDialog = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference();

        maxLevel = getIntent().getIntExtra(LEVEL_MAX, 0);

        level = new Level();
        level.setKey(getIntent().getStringExtra(LEVEL_KEY));
        loadLevel(level.getKey());

        Button btnback = findViewById(R.id.button_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LevelActivity.this, LevelsActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });
    }

    private void dialogStart() {
        dialog = new Dialog(this); //Диалоговое окно
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dilog_start);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Прозрачный фон
        dialog.setCancelable(false);
        TextView dialogStartText = dialog.findViewById(R.id.dialog_start_text);
        dialogStartText.setText(level.getDialogStartText());
        TextView btnclose = dialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //К выбору уровня
                    Intent intent = new Intent(LevelActivity.this, LevelsActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
                dialog.dismiss();
            }
        });


        Button btncontinue = dialog.findViewById(R.id.btncontinue);
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {

                }
            }
        });

        dialog.show();
    }

    private void dialogEnd() {
        //Диалоговое окно в конце
        dialogEnd = new Dialog(this); //Диалоговое окно
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.dilog_end);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Прозрачный фон
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);

        TextView dialogEndText = dialogEnd.findViewById(R.id.dialog_end_text);
        dialogEndText.setText(level.getDialogEndText());

        TextView btnclose2 = dialogEnd.findViewById(R.id.btnclose);
        btnclose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LevelActivity.this, LevelsActivity.class);
                    startActivity(intent);
                    dialogEnd.dismiss();
                    finish();
                } catch (Exception e) {

                }
            }
        });

        dialogEnd.show();
    }

    private void loadLevel(String key) {
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.show();
        reference.child("levels").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Image image;
                    ArrayList<Image> images = new ArrayList<>();
                    level.setName(dataSnapshot.child("name").getValue().toString());
                    level.setDialogStartText(dataSnapshot.child("dialog_start").getValue().toString());
                    level.setDialogEndText(dataSnapshot.child("dialog_end").getValue().toString());
                    level.setNumber(Integer.parseInt(dataSnapshot.child("number").getValue().toString()));
                    for (DataSnapshot imageData : dataSnapshot.child("images").getChildren()) {
                        image = new Image();
                        image.setName(imageData.child("name").getValue().toString());
                        image.setSrc(imageData.child("src").getValue().toString());
                        image.setValue(Integer.parseInt(imageData.child("value").getValue().toString())); //Разве "src" а не "value"
                        images.add(image);
                    }
                    level.setImages(images);
                }
                progressDialog.cancel();

                game();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
            }
        });
    }

    private void game() {

        dialogStart();
        TextView text_levels = findViewById(R.id.text_levels);
        text_levels.setText(level.getName());
        //Animation
        final Animation a = AnimationUtils.loadAnimation(LevelActivity.this, R.anim.alpha);


        TextView textLeft = findViewById(R.id.text_left);
        TextView textRight = findViewById(R.id.text_right);


        pair = level.getPair();

        textLeft.setText(pair.first.getName());
        textRight.setText(pair.second.getName());

        Picasso.get().load(pair.first.getSrc()).into((ImageView) findViewById(R.id.img_left));
        Picasso.get().load(pair.second.getSrc()).into((ImageView) findViewById(R.id.img_right));

        final ImageView img_left = findViewById(R.id.img_left);
        final ImageView img_right = findViewById(R.id.img_right);

        final FrameLayout item_left = findViewById(R.id.item_left);
        final FrameLayout item_right = findViewById(R.id.item_right);

        item_left.setOnTouchListener(new View.OnTouchListener() { // Button left img
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    img_right.setEnabled(false);
                    if (pair.first.getValue() > pair.second.getValue()) {
                        img_left.setImageResource(R.drawable.img_true);
                    } else {
                        img_left.setImageResource(R.drawable.img_false);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (pair.first.getValue() > pair.second.getValue()) {
                        if (count < 8) count++;

                        for (int i = 0; i < 8; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    } else {
                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count--;
                                count--;
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 8) {
                        if (maxLevel == level.getNumber()) {
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            SharedPreferences.Editor editor = save.edit();
                            editor.putInt(LEVEL_MAX, ++maxLevel);
                            editor.commit();
                        }
                        dialogEnd();
                    } else {
                        TextView textLeft = findViewById(R.id.text_left);
                        TextView textRight = findViewById(R.id.text_right);

                        pair = level.getPair();

                        textLeft.setText(pair.first.getName());
                        textRight.setText(pair.second.getName());

                        Picasso.get().load(pair.first.getSrc()).into((ImageView) findViewById(R.id.img_left));
                        img_left.startAnimation(a);
                        Picasso.get().load(pair.second.getSrc()).into((ImageView) findViewById(R.id.img_right));
                        img_right.startAnimation(a);

                        img_right.setEnabled(true);
                    }
                }
                return true;
            }
        });

        item_right.setOnTouchListener(new View.OnTouchListener() { // Button right img
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    img_left.setEnabled(false);
                    if (pair.second.getValue() > pair.first.getValue()) {
                        img_right.setImageResource(R.drawable.img_true);
                    } else {
                        img_right.setImageResource(R.drawable.img_false);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (pair.second.getValue() > pair.first.getValue()) {
                        if (count < 8) count++;

                        for (int i = 0; i < 8; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }

                    } else {
                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count--;
                                count--;
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }

                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 8) {
                        if (maxLevel == level.getNumber()) {
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            SharedPreferences.Editor editor = save.edit();
                            editor.putInt(LEVEL_MAX, ++maxLevel);
                            editor.commit();
                        }
                        dialogEnd();
                    } else {
                        TextView textLeft = findViewById(R.id.text_left);
                        TextView textRight = findViewById(R.id.text_right);

                        pair = level.getPair();

                        textLeft.setText(pair.first.getName());
                        textRight.setText(pair.second.getName());

                        Picasso.get().load(pair.first.getSrc()).into((ImageView) findViewById(R.id.img_left));
                        img_left.startAnimation(a);
                        Picasso.get().load(pair.second.getSrc()).into((ImageView) findViewById(R.id.img_right));
                        img_right.startAnimation(a);

                        img_left.setEnabled(true);
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(LevelActivity.this, LevelsActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}
