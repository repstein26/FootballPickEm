import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Image;
import javax.swing.*;
import java.awt.GridBagLayout;

public class ImagePanel extends JPanel{

	public Image image;

	public ImagePanel(String filename, GridBagLayout gbl) {
		super(gbl);
		if(image == null){
			try {

				//image= ImageIO.read(new File(filename)); 
				image= ImageIO.read(getClass().getResource(filename)); 
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);         
	}

}