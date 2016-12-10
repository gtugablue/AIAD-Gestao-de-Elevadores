package lift_management.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import lift_management.agents.Building;
import lift_management.agents.Lift;
import lift_management.agents.Lift.DoorState;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData2D;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.Label;
import saf.v3d.scene.Position;
import saf.v3d.scene.TextureLayer;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VImage2D;
import saf.v3d.scene.VLabelLayer;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VNode;
import saf.v3d.scene.VSpatial;


public class LiftStyle extends DefaultStyleOGL2D {
	public static final float scale = 15;
	public static final int liftDoorImageWidth = 200;
	public static final int liftDoorImageHeight = 342;
	
	public LiftStyle() {
		super();
	}

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Lift) {
			Lift lift = (Lift)agent;
			VComposite composite = new VLayer();
			VComposite imageLayer = new TextureLayer();
			VComposite labelLayer = new VLayer();
			try {
				VImage2D liftImage;
				if (lift.getDoorState().equals(DoorState.OPEN)) {
					liftImage = shapeFactory.createImage("icons/lift_inside.jpg");
				} else {
					liftImage = shapeFactory.createImage("icons/lift_closed.jpg");
				}
				
				liftImage.translate(0, scale * 0.8f * Building.floorHeight / 2, 0);
				liftImage.scale(scale * 0.8f * Building.floorHeight / liftDoorImageHeight);
				imageLayer.addChild(liftImage);
				
				Font font = new JLabel().getFont();
				VLabelLayer layers = new VLabelLayer(font);
				Label label = new Label("" + lift.getNumHumansInside(), liftImage, Position.EAST);
				label.setColor(Color.BLACK);
				layers.addLabel(label);
				labelLayer.addChild(layers);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			composite.addChild(imageLayer);
			composite.addChild(labelLayer);
			spatial = composite;
		}
		return spatial;
	}

	@Override
	public Color getColor(Object agent) {
		return Color.GRAY;
	}
	
	@Override
	public String getLabel(Object agent) {
		return "AAA";
	}
}
