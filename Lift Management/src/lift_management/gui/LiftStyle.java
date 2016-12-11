package lift_management.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
import saf.v3d.scene.VRoot;
import saf.v3d.scene.VSpatial;


public class LiftStyle extends DefaultStyleOGL2D {
	public static final int liftDoorImageWidth = 200;
	public static final int liftDoorImageHeight = 342;

	public LiftStyle() {
		super();
	}

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (agent instanceof Lift) {
			Lift lift = (Lift)agent;
			VComposite layer = new TextureLayer();
			VComposite composite = new VLayer();
			try {
				VImage2D liftImage;
				if (lift.getDoorState().equals(DoorState.OPEN)) {
					liftImage = shapeFactory.createImage("icons/lift_inside.jpg");
				} else {
					liftImage = shapeFactory.createImage("icons/lift_closed.jpg");
				}

				liftImage.translate(0, StyleUtils.SCALE * 0.8f * Building.floorHeight / 2, 0);
				liftImage.scale(StyleUtils.SCALE * 0.8f * Building.floorHeight / liftDoorImageHeight);

				BufferedImage labelImage = StyleUtils.textToImage(getLabel(agent), new JLabel().getFont(), 100);
				VImage2D label = shapeFactory.createImage("Number " + lift.getNumHumansInside(), labelImage);
				label.translate(StyleUtils.SCALE * 0.4f, StyleUtils.SCALE * 0.8f * Building.floorHeight / 2, 0);
				label.scale(0.1f);
				
				layer.addChild(liftImage);
				layer.addChild(label);
			} catch (IOException e) {
				e.printStackTrace();
			}
			composite.addChild(layer);
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
		if (agent instanceof Lift)
			return "" + ((Lift)agent).getNumHumansInside();
		else
			return null;
	}
}
