import java.io.File;

import javax.swing.JFileChooser;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MidiPlayerDriver extends Application {

	Button stopBut;
	
	MidiPlayer midPlay;
	
	public static Group root;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		stopBut = new Button("Stop");
		stopBut.setOnAction(this::processStop);
		
		root = new Group(stopBut);

		Scene scene = new Scene(root, 1000, 1000);
		
		primaryStage.setTitle("Midi Player");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
		
		FileChooser wah = new FileChooser();
		File files = wah.showOpenDialog(primaryStage);
		
		midPlay = new MidiPlayer(new File(files.getPath())); //"src/midi/var5.mid"
	}
	
	public void processStop(ActionEvent event) {
		midPlay.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
