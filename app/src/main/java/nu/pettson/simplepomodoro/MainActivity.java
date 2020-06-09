package nu.pettson.simplepomodoro;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView amountText;

    boolean running = false;
    boolean existingTimer = false;
    boolean onDownTime = false;
    CountDownTimer countDownTimer;
    ImageView start;
    ImageView circle;

    private long timeLeft = 0;
    private int timesDone = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeText = (TextView) findViewById(R.id.timeText);
        amountText = (TextView) findViewById(R.id.amountText);

        circle = (ImageView) findViewById(R.id.circleTime);
        start = (ImageView) findViewById(R.id.playIcon);

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
                } else if (timesDone == 4) {
                    createDownTimeCountDown(20000);
                }

            }
        }.start();
    }

    private void createDownTimeCountDown(long millis) {
        final TextView timeText = findViewById(R.id.timeText);
        onDownTime = true;
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
                    timeText.setText("done!");
                    start.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                    circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    existingTimer = false;
                    running = false;
                    onDownTime = false;
                } else {
                    circle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    start.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteText));
                    createCountDown(10000);
                    onDownTime = false;
                }
            }
        }.start();
    }
}
