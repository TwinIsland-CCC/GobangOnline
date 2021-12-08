import java.io.*;
import javax.sound.sampled.*;
import javax.swing.JFrame;

public class Music extends JFrame{
    static void playMusic(String path, double vol) {// 背景音乐播放
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
                sdl.write(buffer, 0, nByte);
            }
            System.out.println("Playing");
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Music();
    }
}


