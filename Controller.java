import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class Controller{
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		Board b = new Board();
		UI ui = new UI(b);
		Reader r = new Reader(b, ui);
		ui.setFrameVisible(true);
		b.addObserver(ui);

		Thread uiThread = new Thread(b);
		Thread readThread = new Thread(r);
		uiThread.start();
		readThread.start();
		
	}

}
