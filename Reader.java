import java.awt.Component;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class Reader implements Runnable {
	
	Board b;
	UI ui;
	ReentrantLock lock = new ReentrantLock();
	private File f = new File("pipe.txt");
	Scanner input = null;
	
	public Reader(Board b, UI ui){
		this.b = b;
		this.ui = ui;
		try {
			input = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		String s = "NONE";
		while(true){
			lock.lock();
			try{
				if(b.observers.isEmpty()){
					input.close();
					break;
				}
			}
			finally{
				lock.unlock();
			}
			lock.lock();
			try{
				input = new Scanner(f);
				if (input.hasNextLine()){
					s = input.nextLine();
					if(s.equals("q")){
						for(int i = 0;i<b.observers.size();i++){
							b.observers.get(i).notifyWindowClosing();
						}
					}
					else if(s.contains(";")){
						int d = s.indexOf(";");
						String w = s.substring(0, d);
						String l = s.substring(d+1);
						b.updateWL(w, l);
					}
					else if (s.contains("dealer:")){
						String d = s.substring(7);
						b.updateItem(d);
						String lng;
						if(d.length()>20){
							lng = d.substring(23);
						}
						else{
							lng = d;
						}
						ArrayList<Character> h = new ArrayList<Character>();
						for(int i = 0; i<lng.length();i++){
							if(lng.charAt(i)!='['&&lng.charAt(i)!=']'&&lng.charAt(i)!=','&&lng.charAt(i)!=' '&&lng.charAt(i)!='\''&&lng.charAt(i)!='0'){
								h.add(lng.charAt(i));
							}
						}
						b.updateDealHand(h);
					}
					else if (s.contains("you:")){
						String d = s.substring(4);
						b.updateItem(d);
						ArrayList<Character> h = new ArrayList<Character>();
						for(int i = 0; i<d.length();i++){
							if(d.charAt(i) == '['){
								for(int j = i; j<d.length();j++){
									if(d.charAt(j)!='['&&d.charAt(j)!=']'&&d.charAt(j)!=','&&d.charAt(j)!=' '&&d.charAt(j)!='\''&&d.charAt(j)!='0'){
										h.add(d.charAt(j));
									}
									if(d.charAt(j) == ']'){
										break;
									}
								}
							}
						}
						b.updateMyHand(h);
					}
					else{
						b.updateItem(s);
					}
					FileWriter gk = new FileWriter(f);
					gk.flush();
					gk.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				lock.unlock();
			}
		}
		
	}

}
