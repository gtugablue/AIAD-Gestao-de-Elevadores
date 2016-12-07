package lift_management.gui;

import java.awt.Color;
import java.io.IOException;

import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.agents.Lift.DoorState;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.TextureLayer;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VImage2D;
import saf.v3d.scene.VSpatial;


public class LiftStyle extends DefaultStyleOGL2D {
	public static final float scale = 15;
	public static final int liftDoorImageWidth = 200;
	public static final int liftDoorImageHeight = 342;

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Lift) {
			Lift lift = (Lift)agent;
			VComposite composite = new TextureLayer();
			try {
				VImage2D liftImage;
				if (lift.getDoorState().equals(DoorState.OPEN)) {
					liftImage = shapeFactory.createImage("icons/lift_open.jpg");
				} else {
					liftImage = shapeFactory.createImage("icons/lift_closed.jpg");
				}
				NdPoint position = lift.getPosition();
				liftImage.translate(scale * (float)position.getX(), scale * ((float)position.getY() + 0.8f * Building.floorHeight / 2), 0);
				liftImage.scale(scale * 0.8f * Building.floorHeight / liftDoorImageHeight);
				composite.addChild(liftImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			spatial = composite;
		}
		return spatial;
	}

	@Override
	public Color getColor(Object agent) {
		return Color.GRAY;
	}
}
