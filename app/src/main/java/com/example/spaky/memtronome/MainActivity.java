package com.example.spaky.memtronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {


    AudioTrack audioTrack;

    Button btnStart;
    TextView txtString, txtSpeed, txtTypeSpeed, txtNote;
    ImageView imgStringOne, imgStringTwo, imgStringThree, imgStringFour, imgStringFive, imgStringSix,imgHelp;
    ImageButton btnIncrement, btnReduce;

    int s = 3800;

    int regImagePic[] = {R.drawable.line, R.drawable.light};
    String[] strNote;
    String[] numString;
    String[] strTempo;

    short bpm = 80;
    int beat = 4;
    int sound = 6440;
    int soundbeat = 2440;

    Handler handler;
    Random random;

    short maxBmp = 200;
    short minsBmp = 50;

    MetroAsynTask metroAsynTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define instance
        handler = new Handler();
        random = new Random();


        numString = getResources().getStringArray(R.array.line);
        strNote = getResources().getStringArray(R.array.key);
        strTempo = getResources().getStringArray(R.array.tempo);

        bindWidget();

        txtSpeed.setText("" + bpm);

        metroAsynTask = new MetroAsynTask();

        //on click button and image button
        btnIncrement.setOnClickListener(this);
        btnReduce.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        btnIncrement.setOnLongClickListener(this);
        btnReduce.setOnLongClickListener(this);

        imgHelp.setOnClickListener(this);

    }


    public void setNote() {
        int num = random.nextInt(strNote.length);
        txtNote.setText(strNote[num]);

    }

    public void resetImgRes() {
        imgStringOne.setImageResource(regImagePic[0]);
        imgStringTwo.setImageResource(regImagePic[0]);
        imgStringThree.setImageResource(regImagePic[0]);
        imgStringFour.setImageResource(regImagePic[0]);
        imgStringFive.setImageResource(regImagePic[0]);
        imgStringSix.setImageResource(regImagePic[0]);
    }

    public void setLineAndImgLine() {
        int num = random.nextInt(numString.length);
        txtString.setText(numString[num]);

        resetImgRes();

        if (num == 0) {
            imgStringOne.setImageResource(regImagePic[1]);
        } else if (num == 1) {
            imgStringTwo.setImageResource(regImagePic[1]);
        } else if (num == 2) {
            imgStringThree.setImageResource(regImagePic[1]);
        } else if (num == 3) {
            imgStringFour.setImageResource(regImagePic[1]);
        } else if (num == 4) {
            imgStringFive.setImageResource(regImagePic[1]);
        } else if (num == 5) {
            imgStringSix.setImageResource(regImagePic[1]);
        }
    }

    public void bindWidget() {
        //button & imageButton
        btnIncrement = (ImageButton) findViewById(R.id.btn_increment);
        btnReduce = (ImageButton) findViewById(R.id.btn_reduce);
        btnStart = (Button) findViewById(R.id.btn_start);

        //textview
        txtString = (TextView) findViewById(R.id.txt_string);
        txtSpeed = (TextView) findViewById(R.id.txt_speed);
        txtNote = (TextView) findViewById(R.id.txt_note);
        txtTypeSpeed = (TextView) findViewById(R.id.txt_type);

        //imageview
        imgStringOne = (ImageView) findViewById(R.id.string_one);
        imgStringTwo = (ImageView) findViewById(R.id.string_two);
        imgStringThree = (ImageView) findViewById(R.id.string_three);
        imgStringFour = (ImageView) findViewById(R.id.string_four);
        imgStringFive = (ImageView) findViewById(R.id.string_five);
        imgStringSix = (ImageView) findViewById(R.id.string_six);
        imgHelp = (ImageView) findViewById(R.id.btn_help);


    }

    public void checkBmpMax() {
        if (bpm >= maxBmp) {
            btnIncrement.setEnabled(false);
        } else if (bpm > minsBmp && !(btnReduce.isEnabled())) {
            btnReduce.setEnabled(true);
        }
    }

    public void checkBmpMin() {
        if (bpm <= minsBmp) {
            btnReduce.setEnabled(false);
        } else if (bpm < maxBmp && !(btnIncrement.isEnabled())) {
            btnIncrement.setEnabled(true);
        }
    }

    public void setTxtSpeedTempo() {
        if (bpm >= 50 && bpm <= 60) {
            txtTypeSpeed.setText(strTempo[0]);
        } else if (bpm > 60 && bpm <= 66) {
            txtTypeSpeed.setText(strTempo[1]);
        } else if (bpm > 60 && bpm <= 66) {
            txtTypeSpeed.setText(strTempo[2]);
        } else if (bpm > 66 && bpm <= 76) {
            txtTypeSpeed.setText(strTempo[3]);
        } else if (bpm > 76 && bpm <= 108) {
            txtTypeSpeed.setText(strTempo[4]);
        } else if (bpm > 108 && bpm <= 120) {
            txtTypeSpeed.setText(strTempo[5]);
        } else if (bpm > 120 && bpm <= 168) {
            txtTypeSpeed.setText(strTempo[6]);
        } else if (bpm > 168 && bpm <= 176) {
            txtTypeSpeed.setText(strTempo[7]);
        } else if (bpm > 200) {
            txtTypeSpeed.setText(strTempo[8]);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reduce:
                bpm--;
                txtSpeed.setText("" + bpm);
                metroAsynTask.setBmp(bpm);
                setTxtSpeedTempo();
                checkBmpMin();
                break;
            case R.id.btn_increment:
                bpm++;
                txtSpeed.setText("" + bpm);
                metroAsynTask.setBmp(bpm);
                setTxtSpeedTempo();
                checkBmpMax();
                break;
            case R.id.btn_start:
                String txtInBtn = btnStart.getText().toString();
                if (txtInBtn.equals("START")) {
                    metroAsynTask.execute();
                    btnStart.setText(R.string.stop);
                } else {
                    metroAsynTask.stop();
                    metroAsynTask = new MetroAsynTask();

                    resetImgRes();
                    txtNote.setText("-");
                    txtString.setText("-");
                    Runtime.getRuntime().gc();

                    btnStart.setText(R.string.start);
                }
                break;
            case R.id.btn_help:
                    ImageView tutorail = new ImageView(getApplicationContext());
                    tutorail.setImageResource(R.drawable.tutorail);
                    tutorail.setPadding(16, 16, 16 ,16);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(tutorail)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reduce:
                bpm -= 10;
                if (bpm < minsBmp)
                    bpm = minsBmp;

                txtSpeed.setText("" + bpm);
                setTxtSpeedTempo();
                metroAsynTask.setBmp(bpm);
                checkBmpMin();
                break;
            case R.id.btn_increment:
                bpm += 10;
                if (bpm > maxBmp)
                    bpm = maxBmp;

                txtSpeed.setText("" + bpm);
                setTxtSpeedTempo();
                metroAsynTask.setBmp(bpm);
                checkBmpMax();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        metroAsynTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        metroAsynTask.stop();
        metroAsynTask = new MetroAsynTask();
    }

    //รับ message จาก handler ที่ส่งเข้ามา
    private Handler getHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                if (message.equals("1")) {
                    setNote();
                    setLineAndImgLine();
                }
            }
        };
    }


    public class MetroAsynTask extends AsyncTask<Void, Void, Void> {
        Metronome metronome;

        public MetroAsynTask() {
            handler = getHandler();
            metronome = new Metronome(handler);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            metronome.setBeat(beat);
            metronome.setBpm(bpm);
            metronome.setSound(sound);
            metronome.setSoundBeat(soundbeat);
            metronome.play();

            return null;
        }


        public void stop() {
            metronome.stop();
            metronome = null;
        }

        public void setBmp(int bmp) {
            metronome.setBpm(bmp);
            metronome.calculateGrap();
        }
    }
}
