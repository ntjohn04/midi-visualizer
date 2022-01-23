import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BoxList {

	public Rectangle[] boxList;
	
	public static int count = -1;
	
	Timeline time;
	
	Line actionLine;
	
	public BoxList(int trackSize) {
		actionLine = new Line(300, 0, 300, 1000);
		actionLine.setStroke(Color.BLACK);
		actionLine.setStrokeWidth(8);
		
		MidiPlayerDriver.root.getChildren().addAll(actionLine);
		
		boxList = new Rectangle[trackSize];
		
		time = new Timeline(new KeyFrame(Duration.millis(25), new 
				EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent t) {
					moveBoxes();
					detectBounds();
					//if (count > 0) System.out.println(pointList[1].accx + pointList[1].accy + pointList[1].accz);
				}
			}
			));
			
			time.setCycleCount(Timeline.INDEFINITE);
			time.play();
			
	}
	
	public void detectBounds() {
		for (int i = 0; i <= count; i++) {
			if (boxList[i] == null) continue;
			else if (boxList[i].getTranslateX() < -690 && boxList[i].getTranslateX() + 20 > -690) {
				boxList[i].setFill(Color.RED);
			}
			else if (boxList[i].getTranslateX() > -690) {
				boxList[i].setFill(Color.DARKSLATEBLUE);
			}
			else if (boxList[i].getTranslateX() < -1500) {
				boxList[i] = null;
			}
			else {
				boxList[i].setFill(Color.DIMGREY);
			}
		}
	}
	
	public void moveBoxes() {
		for (int i = 0; i <= count; i++) {
			if (boxList[i] == null) continue;
			boxList[i].setTranslateX(boxList[i].getTranslateX()-2.75); //-145
		}
	}
	
	public void createShape(int yVal) {
		count++;
		
		boxList[count] = new Rectangle(1000, yVal, 35, 9); //15
		
		//MidiPlayerDriver.root.getChildren().addAll(boxList[count]);
	}
	
}
