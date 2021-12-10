import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class Sound extends JFrame{
    public static int LOOP = 1;
    public static int NOT_LOOP = 0;
    private String path = "res/audio/success.wav";
    private double vol = 0.25;

    public Sound(String s) {
        path = s;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    private void playMusic() {// 背景音乐播放
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            float dB = (float) (Math.log(vol == 0.0 ? 0.0001 : vol) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                //System.out.println(nByte);
                if (nByte != -1)
                sdl.write(buffer, 0, nByte);
            }
            System.out.println("Playing");
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void play(int mode){
        if (mode == LOOP) while(true) playMusic();
        else if (mode == NOT_LOOP) playMusic();
    }
}


