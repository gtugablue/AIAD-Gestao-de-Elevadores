package lift_management.gui;

import java.io.IOException;

import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.agents.Lift.DoorState;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.TextureLayer;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VImage2D;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VNode;
import saf.v3d.scene.VSpatial;

public class BuildingStyle extends DefaultStyleOGL2D {
	public static final float scale = 15;
	public static final int liftDoorImageWidth = 200;
	public static final int liftDoorImageHeight = 342;

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Building) {
			Building building = (Building)agent;
			VComposite composite = new TextureLayer();
			try {
				for (int i = 0; i < building.getNumLifts(); i++) {
					for (int j = 0; j < building.getNumFloors(); j++) {
						VImage2D liftImage = shapeFactory.createImage("icons/lift_open.jpg");
						liftImage.translate(scale * (i + 0.5f), scale * (j * Building.floorHeight + 0.8f * Building.floorHeight / 2), 0);
						liftImage.scale(scale * 0.8f * Building.floorHeight / liftDoorImageHeight);
						composite.addChild(liftImage);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			spatial = composite;
		}
		return spatial;
	}
}
