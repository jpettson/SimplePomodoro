package nu.pettson.simplepomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView amountText;
    TextView totalPomodoros;
    TextView statusText;

    boolean running = false;
    boolean existingTimer = false;
    boolean onDownTime = false;
    CountDownTimer countDownTimer;
    ImageView start;
    ImageView circle;

    private long timeLeft = 0;
    private int timesDone = 0;

    Vibrator v;

    SharedPreferences sharedPref;
    int totalAmountOfTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        totalAmountOfTimes = sharedPref.getInt("totalAmountOfTimes", 0);

        timeText = (TextView) findViewById(R.id.timeText);
        amountText = (TextView) findViewById(R.id.amountText);
        //totalPomodoros = (TextView) findViewById(R.id.totalPomodoros);
        statusText = (TextView) findViewById(R.id.statusText);
        //totalPomodoros.setText("Total Pomodoros: " + totalAmountOfTimes);

        circle = (ImageView) findViewById(R.id.circleTime);
        start = (ImageView) findViewById(R.id.playIcon);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView start = (ImageView) v;
                if (!existingTimer && !running && !onDownTime) {
                    if (timesDone >= 4) {
                        timesDone = 0;
                    }
                    amountText.setText(timesDone + " of 4");
                    createCountDown(10000);
                    start.setImageResource(R.drawable.ic_pause_circle_outline_white_18dp);
                    circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    existingTimer = true;
                    running = true;
                } else if (running && !onDownTime) {
                    start.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                    countDownTimer.cancel();
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    running = false;
                } else if (!running && !onDownTime) {
                    start.setImageResource(R.drawable.ic_pause_circle_outline_white_18dp);
                    createCountDown(timeLeft);
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    running = true;
                } else if (running && onDownTime) {
                    start.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                    countDownTimer.cancel();
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    running = false;
                } else if (!running && onDownTime) {
                    start.setImageResource(R.drawable.ic_pause_circle_outline_white_18dp);
                    createDownTimeCountDown(timeLeft);
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    running = true;
                }
            }
        });


    }

    private void createCountDown(long millis) {
        final TextView timeText = findViewById(R.id.timeText);
        statusText.setText("WORK");
        countDownTimer = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                //timeText.setText(Long.toString(millisUntilFinished / 1000));
                timeText.setText(String.format("%d:%02d", minutes, seconds));
                timeLeft = millisUntilFinished;
            }

            public void onFinish() {
                System.out.println("CreateCountDown is finished");
                //timeText.setText("done!");
                //start.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                //start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                timesDone++;
                amountText.setText(timesDone + " of 4");
                if (timesDone < 4) {
                    createDownTimeCountDown(5000);
                    final long[] pattern = {0, 500, 500, 500, 500};
                    v.vibrate(pattern, -1);
                } else if (timesDone == 4) {
                    createDownTimeCountDown(20000);
                    totalAmountOfTimes = totalAmountOfTimes + 1;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("totalAmountOfTimes", totalAmountOfTimes);
                    editor.commit();
                    //totalPomodoros.setText("Total Pomodoros: " + totalAmountOfTimes);
                    final long[] pattern = {0, 1000, 200, 1000, 200, 1000, 200};
                    v.vibrate(pattern, -1);
                }

            }
        }.start();
    }

    private void createDownTimeCountDown(long millis) {
        final TextView timeText = findViewById(R.id.timeText);
        onDownTime = true;
        statusText.setText("REST");
        countDownTimer = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timeText.setText(String.format("%d:%02d", minutes, seconds));
                timeLeft = millisUntilFinished;
            }

            public void onFinish() {
                if (timesDone == 4) {
                    timeText.setText("Done!");
                    statusText.setText("");
                    start.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                    circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    existingTimer = false;
                    running = false;
                    onDownTime = false;
                    v.vibrate(1500);
                } else {
                    circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    createCountDown(10000);
                    onDownTime = false;
                    v.vibrate(500);
                }
            }
        }.start();
    }
}
