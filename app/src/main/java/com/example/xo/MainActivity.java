package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class MainActivity extends AppCompatActivity   {
    CardView XCard, OCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        XCard = findViewById(R.id.XCard);
        OCard = findViewById(R.id.OCard);
        XCard.setOnClickListener(v -> navigateToGameActivity("X"));
        OCard.setOnClickListener(v -> navigateToGameActivity("O"));
    }

    private void navigateToGameActivity(String firstPlayer){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("firstPlayer", firstPlayer);
        startActivity(intent);
    }
    }


