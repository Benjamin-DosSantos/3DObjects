package objects.core;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class SizeSlider extends Applet{
	public void paint(Graphics g){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		
		String main_frame_title = "Slider Tests";
		JFrame main_frame = new JFrame(main_frame_title);
		JPanel main_panel = new JPanel();
		
		main_frame.setVisible(true);
		main_panel.setVisible(true);
		main_frame.add(main_panel);
		
		main_frame.setSize(new Dimension(width, height));
		
		int slider_min = 1, slider_max = 30, slider_init = 1;
		JSlider slider = new JSlider(JSlider.HORIZONTAL, slider_min, slider_max, slider_init);
		
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		int oval_size = 10;
		
		g.fillOval(300, 300, oval_size * slider.getValue(), oval_size * slider.getValue());
		
	}
}
