package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;

public class game_activity extends AppCompatActivity {
    int counter = 0;
    String firstPlayer;
    String secondPlayer;
    String[] board = {"", "", "", "", "", "", "", "", ""};
    int[] indexImage = {
            R.id.img_0, R.id.img_1, R.id.img_2,
            R.id.img_3, R.id.img_4, R.id.img_5,
            R.id.img_6, R.id.img_7, R.id.img_8
    };

    TextView playerTurnText;
    TextView playerXScoreText;
    TextView playerOScoreText;
    TextView timerText;

    int playerXScore = 0;
    int playerOScore = 0;

    CountDownTimer playerXTimer;
    CountDownTimer playerOTimer;
    int playerTimeLimit = 60000;
    boolean isPlayerXTurn = true;
    boolean gameRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        firstPlayer = intent.getStringExtra("firstPlayer");
        secondPlayer = firstPlayer.equals("X") ? "O" : "X";

        playerTurnText = findViewById(R.id.player_turn_text);
        playerXScoreText = findViewById(R.id.playerX_score);
        playerOScoreText = findViewById(R.id.playerO_score);
        timerText = findViewById(R.id.time_counter);

        if (firstPlayer.equals("X")) {
            playerTurnText.setText("Player (X) Turn");
            isPlayerXTurn = true;
        } else {
            playerTurnText.setText("Player (O) Turn");
            isPlayerXTurn = false;
        }

        startTimer();
    }

    public void onClickImg(View view) {
        if (!gameRunning) {
            return;
        }

        ImageView img = (ImageView) view;
        int index = getIndexFromImageView(img);

        if (index != -1 && checkClicked(img, index)) {
            if (checkWin()) {
                if (isPlayerXTurn) {
                    playerXScore++;
                    showWinMessage("Player X");
                } else {
                    playerOScore++;
                    showWinMessage("Player O");
                }
                updateScores();
                resetGameWithDelay();
            } else if (isBoardFull()) {
                showDrawMessage();
                resetGameWithDelay();
            } else {
                updateTurn();
            }
        }
    }

    private int getIndexFromImageView(ImageView img) {
        for (int i = 0; i < indexImage.length; i++) {
            if (img.getId() == indexImage[i]) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkClicked(ImageView img, int index) {
        if (board[index].equals("")) {
            if (isPlayerXTurn) {
                img.setImageResource(R.drawable.x);
                board[index] = "X";
            } else {
                img.setImageResource(R.drawable.o);
                board[index] = "O";
            }
            counter++;
            return true;
        }
        return false;
    }

    private boolean checkWin() {
        for (int i = 0; i < 9; i += 3) {
            if (board[i].equals(board[i + 1]) && board[i + 1].equals(board[i + 2]) && !board[i].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (board[i].equals(board[i + 3]) && board[i + 3].equals(board[i + 6]) && !board[i].equals("")) {
                return true;
            }
        }
        if (board[0].equals(board[4]) && board[4].equals(board[8]) && !board[0].equals("")) {
            return true;
        }
        if (board[2].equals(board[4]) && board[4].equals(board[6]) && !board[2].equals("")) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (String cell : board) {
            if (cell.equals("")) {
                return false;
            }
        }
        return true;
    }

    private void showWinMessage(String player) {
        Toast.makeText(this, player + " Wins!", Toast.LENGTH_SHORT).show();
    }

    private void showDrawMessage() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
    }

    private void resetGame() {
        Arrays.fill(board, "");
        for (int i = 0; i < indexImage.length; i++) {
            ImageView img = findViewById(indexImage[i]);
            img.setImageResource(0);
        }
        counter = 0;
        gameRunning = false;
    }

    private void resetGameWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetGame();
                startNewRound();
            }
        }, 2000);
    }

    private void startNewRound() {
        if (firstPlayer.equals("X")) {
            playerTurnText.setText("Player (X) Turn");
            isPlayerXTurn = true;
        } else {
            playerTurnText.setText("Player (O) Turn");
            isPlayerXTurn = false;
        }
        startTimer();
    }

    private void updateTurn() {
        isPlayerXTurn = !isPlayerXTurn;
        if (isPlayerXTurn) {
            playerTurnText.setText("Player (X) Turn");
        } else {
            playerTurnText.setText("Player (O) Turn");
        }
        cancelTimer();
        startTimer();
    }

    private void startTimer() {
        gameRunning = true;

        if (isPlayerXTurn) {
            playerXTimer = new CountDownTimer(playerTimeLimit, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerText.setText(String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished % 60000) / 1000));
                }

                @Override
                public void onFinish() {
                    updateTurn();  // تغيير الدور عند انتهاء الوقت
                }
            }.start();
        } else {
            playerOTimer = new CountDownTimer(playerTimeLimit, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerText.setText(String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished % 60000) / 1000));
                }

                @Override
                public void onFinish() {
                    updateTurn();
                }
            }.start();
        }
    }

    private void cancelTimer() {
        if (playerXTimer != null) {
            playerXTimer.cancel();
        }
        if (playerOTimer != null) {
            playerOTimer.cancel();
        }
    }

    private void updateScores() {
        playerXScoreText.setText("Score: " + playerXScore);
        playerOScoreText.setText("Score: " + playerOScore);
    }
}
