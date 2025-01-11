package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private String firstPlayer;
    private String secondPlayer;
    private String[] board = {"", "", "", "", "", "", "", "", ""};
    private int[] indexImage = {
            R.id.img_0, R.id.img_1, R.id.img_2,
            R.id.img_3, R.id.img_4, R.id.img_5,
            R.id.img_6, R.id.img_7, R.id.img_8
    };

    private TextView playerTurnText;
    private TextView playerXScoreText;
    private TextView playerOScoreText;

    private int playerXScore = 0;
    private int playerOScore = 0;
    private boolean isPlayerXTurn = true;
    private boolean gameEnded = false; // Flag to check if the game has ended

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        firstPlayer = intent.getStringExtra("firstPlayer");
        secondPlayer = firstPlayer.equals("X") ? "O" : "X"; // Determining second player based on first player

        playerTurnText = findViewById(R.id.player_turn_text);
        playerXScoreText = findViewById(R.id.playerX_score);
        playerOScoreText = findViewById(R.id.playerO_score);

        initializeGame();

        findViewById(R.id.rest).setOnClickListener(v -> resetScores());
    }

    private void initializeGame() {
        if (firstPlayer.equals("X")) {
            playerTurnText.setText("Player (X) Turn");
            isPlayerXTurn = true;
        } else {
            playerTurnText.setText("Player (O) Turn");
            isPlayerXTurn = false;
        }
    }

    public void onClickImg(View view) {
        if (gameEnded) return; // Prevent moves after the game has ended

        ImageView img = (ImageView) view;
        int index = getIndexFromImageView(img);

        if (index != -1 && makeMove(img, index)) {
            if (checkWin()) {
                handleWin();
            } else if (isBoardFull()) {
                handleDraw();
            } else {
                switchTurn();
            }
        }
    }

    private int getIndexFromImageView(ImageView img) {
        for (int i = 0; i < indexImage.length; i++) {
            if (img.getId() == indexImage[i]) return i;
        }
        return -1;
    }

    private boolean makeMove(ImageView img, int index) {
        if (board[index].isEmpty()) {
            board[index] = isPlayerXTurn ? "X" : "O";
            img.setImageResource(isPlayerXTurn ? R.drawable.x : R.drawable.o);
            return true;
        }
        return false;
    }

    private boolean checkWin() {
        int[][] winningPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}            // Diagonals
        };

        for (int[] pos : winningPositions) {
            if (board[pos[0]].equals(board[pos[1]]) &&
                    board[pos[1]].equals(board[pos[2]]) &&
                    !board[pos[0]].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (String cell : board) {
            if (cell.isEmpty()) return false;
        }
        return true;
    }

    private void handleWin() {
        String winningPlayer = isPlayerXTurn ? "Player X" : "Player O";
        Toast.makeText(this, winningPlayer + " Wins!", Toast.LENGTH_SHORT).show();

        // Add one point only for the winning player
        if (isPlayerXTurn) {
            playerXScore++;
        } else {
            playerOScore++;
        }
        updateScores(); // Update the scores after a win
        gameEnded = true; // Set game to ended state
        resetGameWithDelay(); // Reset the game after a win
    }

    private void handleDraw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        gameEnded = true; // Set game to ended state
        resetGameWithDelay(); // Reset the game after a draw
    }

    private void resetGameWithDelay() {
        new Handler().postDelayed(() -> {
            resetGame();
            startNewRound();
        }, 2000); // Delay before resetting the game and starting a new round
    }

    private void resetGame() {
        Arrays.fill(board, "");
        for (int id : indexImage) {
            ImageView img = findViewById(id);
            img.setImageResource(0); // Clear the board images
        }
    }

    private void startNewRound() {
        // Reset turn for new round based on firstPlayer
        isPlayerXTurn = firstPlayer.equals("X");
        playerTurnText.setText(isPlayerXTurn ? "Player (X) Turn" : "Player (O) Turn");
        gameEnded = false; // Reset game state for a new round
    }

    private void switchTurn() {
        isPlayerXTurn = !isPlayerXTurn;
        playerTurnText.setText(isPlayerXTurn ? "Player (X) Turn" : "Player (O) Turn");
    }

    private void updateScores() {
        playerXScoreText.setText("Score: " + playerXScore);
        playerOScoreText.setText("Score: " + playerOScore);
    }

    private void resetScores() {
        playerXScore = 0;
        playerOScore = 0;
        updateScores();
        Toast.makeText(this, "Scores reset to 0!", Toast.LENGTH_SHORT).show();
    }
}
