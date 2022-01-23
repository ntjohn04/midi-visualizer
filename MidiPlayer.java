import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.spi.MidiFileReader;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;


public class MidiPlayer {

	public MidiFileReader reader;
	
	public MidiSystem player;
	
	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
	public static final String[] NOTE_NAMES = {"c", "cs", "d", "ds", "e", "f", "fs", "g", "gs", "a", "as", "b"};
	
	public int trackSize = 0;
	
	public Timer timer = new Timer();
	
	public void stop() {
		timer.cancel();
	}
	
	public MidiPlayer(File file) {
		Sequence seq = null;
		
		MidiFileFormat form = null;
		
		playSound("c4.wav");
		
		try {
			seq = MidiSystem.getSequence(file);
		} catch (InvalidMidiDataException | IOException | NullPointerException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			form = MidiSystem.getMidiFileFormat(file);
		} catch (InvalidMidiDataException | IOException | NullPointerException e) {
			e.printStackTrace();
		}
		
		
		System.out.println(seq);
		System.out.println(form);
		
		int trackNumber = 0;
        for (Track track :  seq.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            trackSize += track.size();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
               
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                   // System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12);
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        if (velocity != 0)System.out.println(event.getTick() + "	" + note + "	" + octave);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                   // System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
            System.out.println();
            System.out.println();
            
           // System.out.println(form.getResolution());
            //System.out.println(form.getDivisionType());
        }
        
        playFile(file);
	}
	
	public void playFile(File file) {
		Sequence seq = null;
		
		MidiFileFormat form = null;
		
		try {
			seq = MidiSystem.getSequence(file);
		} catch (InvalidMidiDataException | IOException | NullPointerException e1) {
			e1.printStackTrace();
		}
		
		try {
			form = MidiSystem.getMidiFileFormat(file);
		} catch (InvalidMidiDataException | IOException | NullPointerException e) {
			e.printStackTrace();
		}
		
		long trackTicks[] = new long[trackSize];
		
		BoxList boxes = new BoxList(trackSize);
		
		for (Track track :  seq.getTracks()) {
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12);
                        int note = key % 12;
                        int velocity = sm.getData2();
                        String noteName = NOTE_NAMES[note];
                        
                        trackTicks[i] = event.getTick();
                        
                        String noteUrl = noteName.concat(String.valueOf(octave)).concat(".wav");
                        
                        int yVal = 250 - (key-78)*18;
                        //System.out.println(key);
                        
                        //int yVal = Math.abs((key-78)*18) + 150;
                      
                        if (velocity != 0) timer.schedule(new TimerTask() {

							@Override
							public void run() {
								Platform.runLater(new Runnable(){

									@Override
									public void run() {
										try {
											boxes.createShape(yVal);
											MidiPlayerDriver.root.getChildren().addAll(boxes.boxList[boxes.count]);
										}
										catch (IllegalArgumentException e) {
											System.out.println("BoxList Woopsie!");
										}
										
									}
								});
							}
                        	
                        }, trackTicks[i]*2);
                        
                        
                        if (velocity != 0) timer.schedule(new TimerTask() {
							@Override
							public void run() {
								playSound(noteUrl);
							}
                        }, trackTicks[i]*2+6150); //+12010
                    }
                }
            }
		}
	}
	
	public synchronized void playSound(String url) {
		try
		{
		    Clip clip = AudioSystem.getClip();
		    clip.open(AudioSystem.getAudioInputStream(new File("src/notes/piano/".concat(url))));
		    clip.start();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
