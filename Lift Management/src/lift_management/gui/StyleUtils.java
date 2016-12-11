package lift_management.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class StyleUtils {
	public static final float SCALE = 15f; // Value found by trial and error
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
}
