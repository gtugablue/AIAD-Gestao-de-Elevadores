package lift_management.gui;

import java.awt.Color;
import java.io.IOException;

import lift_management.agents.Lift;
import lift_management.agents.Lift.DoorState;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;


public class LiftStyle extends DefaultStyleOGL2D {

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Lift) {
			return shapeFactory.createRectangle(1, 1);
			/*Lift lift = (Lift)agent;
			DoorState doorState = lift.getDoorState();
			try {
				if (doorState == DoorState.CLOSED) {
					spatial = shapeFactory.createImage("icons/lift_closed.jpg");
				} else {
					spatial = shapeFactory.createImage("icons/lift_open.jpg");
				}
			} catch (IOException e) {
				spatial = shapeFactory.createRectangle(200, 342);
				e.printStackTrace();
			}*/
		} else {
			if (spatial == null) {
				try {
					spatial = shapeFactory.createImage("icons/lift_closed.jpg");
				} catch (IOException e) {
					spatial = shapeFactory.createRectangle(200, 342);
					e.printStackTrace();
				}
			}
		}
		return spatial;
	}

	@Override
	public Color getColor(Object agent) {
		return Color.GRAY;
	}
}
