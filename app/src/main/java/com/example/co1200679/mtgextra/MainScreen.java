package com.example.co1200679.mtgextra;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Vibrator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

public class MainScreen extends AppCompatActivity {

    private List<String> PlanechaseList = new ArrayList<String>();
    private List<String> ArchenemyList = new ArrayList<String>();
    private List<String> TokenList = new ArrayList<String>();
    private List<String> CardbackList = new ArrayList<String>();
    private List<String> Players = new ArrayList<String>();
    private List<Integer> roleImages = new ArrayList<Integer>();
    private List<RelativeLayout> Slots = new ArrayList<RelativeLayout>();
    private List<EditText> Fields = new ArrayList<EditText>();
    private List<Integer> Colors = new ArrayList<Integer>();
    private int planechaseCrawler = -1;
    private int archenemyCrawler = -1;
    private LinearLayout usurper;
    private LinearLayout roles;
    private LinearLayout dealer;
    private TextView rules;
    private ScrollView dad;
    private Vibrator viba;
    private LinearLayout tb;
    private GridView grid;
    private int mode = 0; // 0:starting, 1:planechase, 2:archenemy, 3:tokens
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Fill Planechase list
        for (int k = 0; k < 77; k++)
            PlanechaseList.add("planechase" + k);
        Collections.shuffle(PlanechaseList);

        //Fill Archenemy list and Suffle it
        for (int k = 0; k < 50; k++)
            ArchenemyList.add("archenemy" + k);
        Collections.shuffle(ArchenemyList);

        //Fill Cardback List
        CardbackList.add("standard");
        CardbackList.add("planechase");
        CardbackList.add("archenemy");

        //Fill Tokens List
        Field[] ID_Fields = R.drawable.class.getFields();
        String temp = "";
        for (Field f : ID_Fields) {
            try {
                if (f.getName().contains("token_")) {
                    temp = f.getName().toString();
                    TokenList.add(temp);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        //Setting up Usurper
        initRolesImages();
        usurper = (LinearLayout) findViewById(R.id.theUsurper);
        dealer = (LinearLayout) findViewById(R.id.dealer);
        roles = (LinearLayout) findViewById(R.id.roles);
        Slots.add((RelativeLayout) findViewById(R.id.roleLayout0));
        Slots.add((RelativeLayout) findViewById(R.id.roleLayout1));
        Slots.add((RelativeLayout) findViewById(R.id.roleLayout2));
        Slots.add((RelativeLayout) findViewById(R.id.roleLayout3));
        Slots.add((RelativeLayout) findViewById(R.id.roleLayout4));
        Fields.add((EditText) findViewById(R.id.editText0));
        Fields.add((EditText) findViewById(R.id.editText1));
        Fields.add((EditText) findViewById(R.id.editText2));
        Fields.add((EditText) findViewById(R.id.editText3));
        Fields.add((EditText) findViewById(R.id.editText4));
        Colors.add(getResources().getColor(R.color.manaBlack));
        Colors.add(getResources().getColor(R.color.manaBlue));
        Colors.add(getResources().getColor(R.color.manaGreen));
        Colors.add(getResources().getColor(R.color.manaRed));
        Colors.add(getResources().getColor(R.color.manaWhite));
        ImageView uspButton = (ImageView) findViewById(R.id.usurper);
        usurper.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeTop() {
                tb.getLayoutParams().height = 0;
                tb.setVisibility(View.GONE);
            }

            @Override
            public void onSwipeBottom() {
                tb.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tb.setVisibility(View.VISIBLE);
            }
        });
        uspButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Players.clear();
                viba.vibrate(60);
                loadUsurper(v);
                return true;
            }
        });

        //Init you private vibrator ( ͡° ͜ʖ ͡°)
        viba = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Finding these guys
        tb = (LinearLayout) findViewById(R.id.toolbar);
        rules = (TextView) findViewById(R.id.rulesText);
        dad = (ScrollView) findViewById(R.id.daddy);

        //Find ImageView and load the starting image
        image = (ImageView) this.findViewById(R.id.imageView);
        changeImage(CardbackList.get(mode));
        setListeners(image);

        //Finding and filling the Grid
        grid = (GridView) findViewById(R.id.theGrid);
        grid.setAdapter(new GridAdapter(this, mode));
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                tb.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                changeImage(TokenList.get(position));
            }
        });
        grid.setVisibility(View.GONE);
    }

    @Override
    // Couple changes to backbutton so you can back into mainmenu from tokenselection
    public void onBackPressed() {
        if (grid.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            grid.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            tb.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viba.vibrate(60);
        }
    }

    public void initRolesImages(){
        roleImages.clear();
        roleImages.add(R.drawable.role_usurper);
        roleImages.add(R.drawable.role_assassin);
        roleImages.add(R.drawable.role_assassin);
        roleImages.add(R.drawable.role_guard);
        roleImages.add(R.drawable.role_traitor);
    }

    //Loads app manual (how to use getsures)
    public void loadGuide(View view) {
        switch (view.getId()) {
            case R.id.guide: {
                mode = 0;
                changeImage("guide2");
                dad.setVisibility(View.VISIBLE);
                rules.setText(R.string.guide_suomi);
                break;
            }
            default:
                break;
        }
        Log.d("Swoosh!", "Guide loaded");
    }

    public void loadUsurper(final View view) {
        cleanUsurper();
        image.setImageDrawable(null);
        dad.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        dealer.setVisibility(View.GONE);
        roles.setVisibility(View.GONE);
        usurper.setVisibility(View.VISIBLE);
        TextView Ttemp;
        ImageView Itemp;

        if (Players.isEmpty()) {
            dealer.setVisibility(View.VISIBLE);
        } else {
            for (int k = 0; k < Players.size(); k++) {
                Ttemp = (TextView) Slots.get(k).getChildAt(1);
                Itemp = (ImageView) Slots.get(k).getChildAt(0);
                Ttemp.setText(Players.get(k));
                Itemp.setAlpha(0f);
                Picasso.with(this).load(roleImages.get(k)).into(Itemp);
                Slots.get(k).setBackgroundColor(Colors.get(k));
                Itemp.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (v.getAlpha() < 0.4f)
                            v.setAlpha(0.4f);
                        viba.vibrate(60);
                        return true;
                    }
                });
                Itemp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setAlpha(0);
                    }
                });
            }
            RelativeLayout Kunkku = (RelativeLayout) findViewById(R.id.roleLayoutKing);
            Kunkku.setBackgroundColor(getResources().getColor(R.color.gold));
            Itemp = (ImageView) findViewById(R.id.roleImageKing);
            Picasso.with(this).load(R.drawable.role_king);
            roles.setVisibility(View.VISIBLE);
        }
    }

    //Undbinding usurper (Frees memory)
    public void cleanUsurper() {
        ImageView Itemp;
        if (Players.size() > 0) {
            for (int k = 0; k < Players.size(); k++) {
                Itemp = (ImageView) Slots.get(k).getChildAt(0);
                Itemp.setImageDrawable(null);
            }
        }
    }

    //Loads image from Planechase list
    public void loadPlanechase(View view) {
        mode = 1;
        cleanUsurper();
        changeImage(CardbackList.get(mode));
        Log.d("Swoosh!", "Planechase images loaded");
    }

    //Load image from Archenemt list
    public void loadArchenemy(View view) {
        mode = 2;
        cleanUsurper();
        changeImage(CardbackList.get(mode));
        Log.d("Swoosh!", "Archenemy images loaded");
    }

    public void loadTokens(View view) {
        mode = 3;
        cleanUsurper();
        tb.getLayoutParams().height = 0;
        if (grid.getVisibility() == View.GONE) {
            grid.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            usurper.setVisibility(View.GONE);
            dad.setVisibility(View.GONE);
        }
        Log.d("Swoosh!", "Token gridview visible");
    }

    //Fills names from the fields, if enough names are presented new game is possible, and will start
    public void dealRoles(View view) {
        cleanUsurper();
        initRolesImages();
        Players.clear();
        TextView nominee = (TextView) findViewById(R.id.editTextKing);
        for (int k = 0; k < Fields.size(); k++)
            if (Fields.get(k).getText().toString().length() > 0)
                Players.add(Fields.get(k).getText().toString());
        if (Players.size() < 4 || nominee.getText().toString().length() <= 0) {
            Toast.makeText(this, "Not enough players for Usurper game", Toast.LENGTH_SHORT).show();
        } else {
            RelativeLayout King = (RelativeLayout) findViewById(R.id.roleLayoutKing);
            if (Players.size() == 4) {
                Slots.get(4).setVisibility(View.GONE);
                roleImages.remove(0);
                King.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            else {
                Slots.get(4).setVisibility(View.VISIBLE);
                King.getLayoutParams().width  = 0;
            }
            TextView kingT = (TextView) findViewById(R.id.roleTextKing);
            kingT.setText(nominee.getText());
            Collections.shuffle(Colors);
            Collections.shuffle(Players);
            Collections.shuffle(roleImages);
            loadUsurper(view);
        }
    }

    public void nextCard(View view) {
        switch (mode) {
            case 1: {
                planechaseCrawler++;
                if (planechaseCrawler >= PlanechaseList.size()) {
                    Collections.shuffle(PlanechaseList);
                    changeImage(CardbackList.get(mode));
                    planechaseCrawler = -1;
                    viba.vibrate(60);
                } else {
                    changeImage(PlanechaseList.get(planechaseCrawler));
                }
                changeImage(PlanechaseList.get(planechaseCrawler));
                break;
            }
            case 2: {
                archenemyCrawler++;
                Log.d(ArchenemyList.size() + "", "" + archenemyCrawler);
                if (archenemyCrawler >= ArchenemyList.size()) {
                    Collections.shuffle(ArchenemyList);
                    changeImage(CardbackList.get(mode));
                    archenemyCrawler = -1;
                    viba.vibrate(60);
                } else {
                    changeImage(ArchenemyList.get(archenemyCrawler));
                }
                break;
            }
            case 3:
                break;
            default:
                break;
        }
    }

    public void previousCard(View view) {
        switch (mode) {
            case 1: {
                if (planechaseCrawler > 0) {
                    planechaseCrawler--;
                    changeImage(PlanechaseList.get(planechaseCrawler));
                }
                break;
            }
            case 2: {
                if (archenemyCrawler > 0) {
                    archenemyCrawler--;
                    changeImage(ArchenemyList.get(archenemyCrawler));
                }
                break;
            }
            case 3:
                break;
            default:
                break;
        }
    }

    public void changeImage(String imageName) {
        int id = getResources().getIdentifier("@drawable/" + imageName, "drawable", getPackageName());
        cleanUsurper();
        dad.setVisibility(View.GONE);
        usurper.setVisibility(View.GONE);
        image.setTag(mode);
        image.setVisibility(View.VISIBLE);
        Picasso.with(this).load(id).into(image);
    }

    public void setListeners(final ImageView image) {
        image.setClickable(true);
        image.setEnabled(true);
        image.setFocusable(true);
        image.setFocusableInTouchMode(true);
        image.setOnTouchListener(new OnSwipeTouchListener(MainScreen.this) {
            @Override
            public void onSwipeTop() {
                tb.getLayoutParams().height = 0;
                tb.setVisibility(View.GONE);
            }

            @Override
            public void onSwipeBottom() {
                tb.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSwipeLeft() {
                nextCard(image);
            }

            @Override
            public void onSwipeRight() {
                previousCard(image);
            }

            @Override
            public void onLongClick() {
                switch (mode) {
                    case 1: {
                        Collections.shuffle(PlanechaseList);
                        planechaseCrawler = -1;
                        changeImage(CardbackList.get(mode));
                        viba.vibrate(60);
                        break;
                    }
                    case 2: {
                        Collections.shuffle(ArchenemyList);
                        archenemyCrawler = -1;
                        changeImage(CardbackList.get(mode));
                        viba.vibrate(60);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }
}
