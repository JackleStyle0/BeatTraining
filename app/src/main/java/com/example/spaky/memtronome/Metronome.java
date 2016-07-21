package com.example.spaky.memtronome;

import android.os.Handler;
import android.os.Message;

import java.util.Random;

/**
 * Created by spaky on 17/7/2559.
 */
public class Metronome {

    Handler mHandler;
    private double bpm;
    private int sound;
    private int soundBeat;
    private int beat;
    private int grap;
    private int tick = 1000;
    private int currentBeat = 1;
    private String line;
    private String note;
    private boolean play = true;

    private double[] soundTickArray;
    private double[] soundTockArray;
    private double[] grapSoundArray;

    private Message msg;

    MainActivity mainActivity;

    Random r = new Random();
    AudioGenerator audioGenerator = new AudioGenerator(8000);

    public Metronome(Handler handler) {
        this.mHandler = handler;
        audioGenerator.createPlayer();
    }

    // gen กำหนด tick tock และ เสียงเว้นระยะห่างหรือเสียงเงียบ
    public void calculateGrap() {
        grap = (int) (((60 / bpm) * 8000) - tick);
        soundTockArray = new double[this.tick];
        soundTickArray = new double[this.tick];
        grapSoundArray = new double[this.grap];

        // สร้าง
        double[] tick = audioGenerator.getGrapWave(this.tick, 8000, soundBeat);
        double[] tock = audioGenerator.getGrapWave(this.tick, 8000, sound);

        //สร้างเสียง tick tock ตามความถี่ที่กำหนดไว้
        for (int i = 0; i < this.tick; i++) {
            soundTickArray[i] = tick[i];
            soundTockArray[i] = tock[i];
        }

        //กำหนดเสียงเงียบ
        for (int i = 0; i < this.tick; i++) {
            grapSoundArray[i] = 0;
        }


    }

    public void play() {
        calculateGrap();
        do {
            msg = new Message();
            msg.obj = "" + currentBeat;
            //เมื่อเป็น beat ตัวแรก จะ เอาค่าจะตัวใน soundTockArray เพื่อสร้างเสีงขึ้นมา
            if (currentBeat == 1) {
                audioGenerator.writeSound(soundTockArray);
            } else {
                audioGenerator.writeSound(soundTickArray);
            }
            //เสียงเงียบ
            audioGenerator.writeSound(grapSoundArray);

            // ส่งค่าไปยัง handler ที่รับค่า
            mHandler.sendMessage(msg);
            currentBeat++;
            if (currentBeat > beat) {
                currentBeat = 1;
            }

        } while (play);
    }

    public void stop() {
        play = false;
        audioGenerator.destoryAudioTrack();
    }


    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }


    public void setSoundBeat(int soundBeat) {
        this.soundBeat = soundBeat;
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }
}
