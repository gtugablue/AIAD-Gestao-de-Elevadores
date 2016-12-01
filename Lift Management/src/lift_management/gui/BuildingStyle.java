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
	private enum LiftButton { UP, DOWN };

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Building) {
			Building building = (Building)agent;
			VComposite composite = new TextureLayer();
			try {
				for (int i = 0; i < building.getNumLifts(); i++) {
					for (int j = 0; j < building.getNumFloors(); j++) {
						VImage2D liftImage = shapeFactory.createImage("icons/lift_closed.jpg");
						VImage2D liftUpButtonImage = shapeFactory.createImage("icons/up_deactivated.png");
						VImage2D liftDownButtonImage = shapeFactory.createImage("icons/up_deactivated.png");
						liftImage.translate(scale * (i + 1f), scale * (j * Building.floorHeight + 0.8f * Building.floorHeight / 2), 0);
						liftImage.scale(scale * 0.8f * Building.floorHeight / liftDoorImageHeight);
						composite.addChild(translateButtonLiftImage(i, j, liftUpButtonImage, LiftButton.UP));
						composite.addChild(translateButtonLiftImage(i, j, liftDownButtonImage, LiftButton.DOWN));
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

	private VSpatial translateButtonLiftImage(int i, int j, VImage2D liftButtonImage, LiftButton direction) {
		float buttonScale = scale * 0.3f * Building.floorHeight / liftDoorImageHeight;
		switch (direction) {
		case UP:
			liftButtonImage.translate(scale * (i + 0.65f), scale * (j * Building.floorHeight + 0.5f * Building.floorHeight), 0);
			liftButtonImage.scale(buttonScale);
			break;
		case DOWN:
			liftButtonImage.translate(scale * (i + 0.65f), scale * (j * Building.floorHeight + 0.3f * Building.floorHeight), 0);
			liftButtonImage.scale(-buttonScale);
			break;
		default:
			break;
		}
		return liftButtonImage;
	}
}