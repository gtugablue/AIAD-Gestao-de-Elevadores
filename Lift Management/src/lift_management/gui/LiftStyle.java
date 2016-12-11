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
			VComposite layer = new TextureLayer();
			VComposite composite = new VLayer();
			try {
				VImage2D liftImage;
				if (lift.getDoorState().equals(DoorState.OPEN)) {
					liftImage = shapeFactory.createImage("icons/lift_inside.jpg");
				} else {
					liftImage = shapeFactory.createImage("icons/lift_closed.jpg");
				}

				liftImage.translate(0, scale * 0.8f * Building.floorHeight / 2, 0);
				liftImage.scale(scale * 0.8f * Building.floorHeight / liftDoorImageHeight);

				BufferedImage labelImage = textToImage(getLabel(agent), new JLabel().getFont(), 100);
				VImage2D label = shapeFactory.createImage("Number " + lift.getNumHumansInside(), labelImage);
				label.translate(scale * 0.4f, scale * 0.8f * Building.floorHeight / 2, 0);
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
	
	public static BufferedImage textToImage(String Text, Font f, float Size){
	    //Derives font to new specified size, can be removed if not necessary.
	    f = f.deriveFont(Size);

	    FontRenderContext frc = new FontRenderContext(null, true, true);

	    //Calculate size of buffered image.
	    LineMetrics lm = f.getLineMetrics(Text, frc);

	    Rectangle2D r2d = f.getStringBounds(Text, frc);

	    BufferedImage img = new BufferedImage((int)Math.ceil(r2d.getWidth()), (int)Math.ceil(r2d.getHeight()), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = img.createGraphics();

	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

	    g2d.setBackground(Color.WHITE);
	    g2d.setColor(Color.BLACK);

	    g2d.clearRect(0, 0, img.getWidth(), img.getHeight());

	    g2d.setFont(f);

	    g2d.drawString(Text, 0, lm.getAscent());

	    g2d.dispose();

	    return img;
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
