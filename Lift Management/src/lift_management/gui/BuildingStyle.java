package lift_management.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JLabel;

import com.jogamp.opengl.util.awt.TextRenderer;

import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.calls.CallSystem;
import lift_management.calls.DestinationDispatchCallSystem;
import lift_management.calls.DirectionalCallSystem;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.Label;
import saf.v3d.scene.Position;
import saf.v3d.scene.TextureLayer;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VImage2D;
import saf.v3d.scene.VLabelLayer;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VSpatial;

public class BuildingStyle extends DefaultStyleOGL2D {
	private static final float LIFT_HEIGHT = 0.8f;
	private static final int LIFT_DOOR_IMAGE_WIDTH = 200;
	private static final int LIFT_DOOR_IMAGE_HEIGHT = 342;
	private enum LiftButton { UP, DOWN };

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Building) {
			Building building = (Building)agent;
			VComposite composite = new VLayer();
			VComposite layer = new TextureLayer();
			CallSystem callSystem = building.getCallSystem();
			if (callSystem instanceof DirectionalCallSystem) {
				DirectionalCallSystem directionCallSystem = (DirectionalCallSystem) callSystem;				
				try {
					for (int j = 0; j < building.getNumFloors(); j++) {
						for (int i = 0; i < building.getNumLifts(); i++) {
							VImage2D liftImage = shapeFactory.createImage("icons/lift_open.jpg");
							VImage2D liftUpButtonImage, liftDownButtonImage;
							
							if (directionCallSystem.toClimb(j))
								liftUpButtonImage = shapeFactory.createImage("icons/up_activated.png");
							else
								liftUpButtonImage = shapeFactory.createImage("icons/up_deactivated.png");
							
							if (directionCallSystem.toDescend(j))
								liftDownButtonImage = shapeFactory.createImage("icons/down_activated.png");
							else
								liftDownButtonImage = shapeFactory.createImage("icons/down_deactivated.png");
							
							liftImage.translate(StyleUtils.SCALE * (i + 1f), StyleUtils.SCALE * (j * Building.floorHeight + LIFT_HEIGHT * Building.floorHeight / 2), 0);
							liftImage.scale(StyleUtils.SCALE * 0.8f * Building.floorHeight / LIFT_DOOR_IMAGE_HEIGHT);
							layer.addChild(translateButtonLiftImage(i, j, liftUpButtonImage, LiftButton.UP));
							layer.addChild(translateButtonLiftImage(i, j, liftDownButtonImage, LiftButton.DOWN));
							layer.addChild(liftImage);
						}
						int n = building.getGod().getNumHumansInFloor(j);
						BufferedImage labelImage = StyleUtils.textToImage("" + n, new JLabel().getFont(), 100);
						VImage2D label = shapeFactory.createImage("Number " + n, labelImage);
						label.translate(- 0.1f * labelImage.getWidth() / 2, StyleUtils.SCALE * (j * Building.floorHeight + 0.4f), 0);
						label.scale(0.1f);
						layer.addChild(label);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (callSystem instanceof DestinationDispatchCallSystem){
				DestinationDispatchCallSystem destinationDispatchCallSystem = (DestinationDispatchCallSystem) callSystem;				
				try {
					for (int j = 0; j < building.getNumFloors(); j++) {
						for (int i = 0; i < building.getNumLifts(); i++) {
							VImage2D liftImage = shapeFactory.createImage("icons/lift_open.jpg");
							VImage2D liftUpButtonImage, liftDownButtonImage;
							
							if (destinationDispatchCallSystem.toClimb(j))
								liftUpButtonImage = shapeFactory.createImage("icons/up_activated.png");
							else
								liftUpButtonImage = shapeFactory.createImage("icons/up_deactivated.png");
							
							if (destinationDispatchCallSystem.toDescend(j))
								liftDownButtonImage = shapeFactory.createImage("icons/down_activated.png");
							else
								liftDownButtonImage = shapeFactory.createImage("icons/down_deactivated.png");
							
							liftImage.translate(StyleUtils.SCALE * (i + 1f), StyleUtils.SCALE * (j * Building.floorHeight + LIFT_HEIGHT * Building.floorHeight / 2), 0);
							liftImage.scale(StyleUtils.SCALE * 0.8f * Building.floorHeight / LIFT_DOOR_IMAGE_HEIGHT);
							layer.addChild(translateButtonLiftImage(i, j, liftUpButtonImage, LiftButton.UP));
							layer.addChild(translateButtonLiftImage(i, j, liftDownButtonImage, LiftButton.DOWN));
							layer.addChild(liftImage);
						}
						
						int n = building.getGod().getNumHumansInFloor(j);
						BufferedImage labelImage = StyleUtils.textToImage("" + n, new JLabel().getFont(), 100);
						VImage2D label = shapeFactory.createImage("Number " + n, labelImage);
						label.translate(0, StyleUtils.SCALE * (j * Building.floorHeight + 0.4f), 0);
						label.scale(0.1f);
						layer.addChild(label);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			composite.addChild(layer);
			spatial = composite;
		}
		return spatial;
	}

	private VSpatial translateButtonLiftImage(int i, int j, VImage2D liftButtonImage, LiftButton direction) {
		float buttonScale = StyleUtils.SCALE * 0.3f * Building.floorHeight / LIFT_DOOR_IMAGE_HEIGHT;
		switch (direction) {
		case UP:
			liftButtonImage.translate(StyleUtils.SCALE * (i + 0.65f), StyleUtils.SCALE * (j * Building.floorHeight + 0.5f * Building.floorHeight), 0);
			break;
		case DOWN:
			liftButtonImage.translate(StyleUtils.SCALE * (i + 0.65f), StyleUtils.SCALE * (j * Building.floorHeight + 0.3f * Building.floorHeight), 0);
			break;
		default:
			return liftButtonImage;
		}
		liftButtonImage.scale(buttonScale);
		return liftButtonImage;
	}
}