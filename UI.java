import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

public class UI implements MouseListener, KeyListener{
	
	private JFrame frame;
	private Component contents;
	private Board board;
	private ReentrantLock lock = new ReentrantLock();
	private File g = new File("pypipe.txt");
	FileWriter gk = null;
	String f = "1";
	int turn = 1;
	
	public UI(Board board) throws IOException {
		this.frame = new JFrame("Blackjack");
		this.frame.setMinimumSize(new Dimension(1200,800));
		this.frame.setLayout(new BorderLayout());
		this.board = board;
		contents = new MyComponent(board);
		contents.addMouseListener(this);
		contents.addKeyListener(this);
		contents.setFocusable(true);
		this.frame.add(contents);
		frame.addWindowListener(new WindowAdapter(){
			@Override public void windowClosing(WindowEvent e){
				notifyWindowClosing();
			}
		});
		
	}
	public void notifyWindowClosing() {
		// TODO Auto-generated method stub
		try {
			gk = new FileWriter(g);
			gk.write("q");
			gk.flush();
			gk.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.board.removeObserver(this);
		frame.dispose();
	}
	
	public void setFrameVisible(boolean visible){
		frame.pack();
		frame.setVisible(visible);
	}
	
	public void notified(){
		lock.lock();
		try{
			contents.repaint();
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		lock.lock();
		try{
			if(Character.isAlphabetic(e.getKeyChar())){
				f = f+e.getKeyChar();
				board.updateUser(f.substring(1));
			}
			else if(Character.isDigit(e.getKeyChar())){
				f = f+e.getKeyChar();
				board.updateUser(f.substring(1));
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				this.gk = new FileWriter(g);
				CharSequence s = f;
				gk.append(s);
				gk.close();
				turn++;
				if(turn == 10){
					turn = 1;
				}
				f = Integer.toString(turn);
				board.updateUser("");
			}
			else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				if(f.length()>1){
					f = f.substring(0, f.length()-1);
					board.updateUser(f.substring(1));
				}
			}
		} catch (IOException e1){
			e1.printStackTrace();
		} finally {
			lock.unlock();
		}
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
