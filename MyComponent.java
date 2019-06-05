import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class MyComponent extends Component{

	private Board b;
	public double x = 0.0, y = 0.0, w = 0.0, h = 0.0;
	public Rectangle2D[][] bounds = new Rectangle2D[17][25];
	public Cards c;
	
	public MyComponent(Board b) throws IOException{
		this.b = b;
		this.c = new Cards();
	}
	
	public void paint(Graphics gIn){
		Graphics2D g = (Graphics2D) gIn;
		Dimension size = getSize();
		double componentH = size.getHeight();
		double componentW = size.getWidth();
		double minDimension = Math.min(componentH, componentW);
		
		g.setColor(new Color (0.23f, 0.80f, .54f));
		g.fillRect(0, 0, size.width, size.height);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Log", 12, (int) (componentH) - 12);
		g.drawString("Wins: " + b.wins, (int) (componentW *(3.0/4.0)), 26);
		g.drawString("Losses: " + b.losses, (int) (componentW *(3.0/4.0)), 52);
		g.drawString("Dealer Hand:", (int) (componentW *(2.0/4.0)), (int) (componentH *(1.0/4.0)) - 34);
		g.drawString("Your Hand:", (int) (componentW *(2.0/4.0)), (int) (componentH *(2.0/4.0)) - 34);
		g.drawString("Input:", 110, (int) (componentH) - 12);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 0, 18));
		g.drawString(b.userinput, 184, (int) (componentH) - 12);
		for(int i = 0; i< b.item.size(); i++){
			g.drawString(b.item.get(i), 12, (int) (componentH) - 40 - (24*i));
		}
		for(int i = 0; i<b.dealer.size();i++){
			char f = b.dealer.get(i);
			g.drawImage(c.getImage(), (int) (componentW * (2.0/4.0)+ (72 * i)), (int) (componentH * (1.0/4.0)), (int) (componentW * (2.0/4.0) + (72 * (i+1))), (int) (componentH * (1.0/4.0) + 96), c.getX1(f), c.getY1(f), c.getX2(f), c.getY2(f), null);
		}
		for(int i = 0; i<b.player.size();i++){
			char f = b.player.get(i);
			g.drawImage(c.getImage(), (int) (componentW * (2.0/4.0)+ (72 * i)), (int) (componentH * (2.0/4.0)), (int) (componentW * (2.0/4.0) + (72 * (i+1))), (int) (componentH * (2.0/4.0) + 96), c.getX1(f), c.getY1(f), c.getX2(f), c.getY2(f), null);
		}
	}
	
}
