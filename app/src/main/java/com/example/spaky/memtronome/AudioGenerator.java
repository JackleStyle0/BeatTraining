package com.example.spaky.memtronome;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by spaky on 17/7/2559.
 */
public class AudioGenerator {

    private AudioTrack audioTrack;
    private int sampleRate;

    public AudioGenerator(int simpleRate) {
        this.sampleRate = simpleRate;
    }


    //สร้างเสียงเว้นวรรค
    public double[] getGrapWave(int samples, int sampleRate, double frequencyOfTone) {
        double[] sample = new double[samples];

        for (int i = 0; i < samples; i++) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / frequencyOfTone));
        }

        return sample;
    }


    public void createPlayer() {
//        int streamType,
//        int sampleRateInHz,
//        int channelConfig,
//        int audioFormat,
//        int bufferSizeInBytes,
//        int mode,
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleRate, AudioTrack.MODE_STREAM);

        audioTrack.play();
    }

    public void destoryAudioTrack() {
        audioTrack.stop();
        audioTrack.release();
    }

    public byte[] get16PCM(double[] samples) {
        byte[] generatorSound = new byte[2 * samples.length];
        int index = 0;
        for (double sample : samples) {
            short maxSample = (short) (sample * Short.MAX_VALUE);

            generatorSound[index++] = (byte) (maxSample & 0x00ff);
            generatorSound[index++] = (byte) ((maxSample & 0xff00) >>> 8);
        }

        return generatorSound;
    }


    public void writeSound(double[] samples) {
        byte[] generatorSound = get16PCM(samples);
        audioTrack.write(generatorSound, 0, generatorSound.length);
    }

}
