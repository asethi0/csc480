import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Board implements Runnable{

	ArrayList<UI> observers = new ArrayList<UI>();
	ReentrantLock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	ArrayList<String> item = new ArrayList<String>();
	String userinput = "";
	String wins = "0";
	String losses = "0";
	ArrayList<Character> dealer = new ArrayList<Character>();
	ArrayList<Character> player = new ArrayList<Character>();
	
	public void updateItem(String s){
		lock.lock();
		try{
			this.item.add(0,s);
			this.notifyObservers();
		} finally{
			lock.unlock();
		}
	}
	
	public void updateMyHand(ArrayList<Character> hand){
		lock.lock();
		try{
			player.clear();
			for(Character g:hand){
				player.add(g);
			}
		} finally{
			lock.unlock();
		}
	}
	
	public void updateDealHand(ArrayList<Character> hand){
		lock.lock();
		try{
			dealer.clear();
			for(Character g:hand){
				dealer.add(g);
			}
		} finally{
			lock.unlock();
		}
	}
	
	public void updateUser(String s){
		lock.lock();
		try{
			userinput = s;
			this.notifyObservers();
		} finally{
			lock.unlock();
		}
	}
	
	public void updateWL(String w, String l){
		lock.lock();
		try{
			wins = w;
			losses = l;
			this.notifyObservers();
		} finally{
			lock.unlock();
		}
	}
	
	public void addObserver(UI ui){
		observers.add(ui);
	}
	
	public void removeObserver(UI ui){
		lock.lock();
		try{
			observers.remove(ui);
		}
		finally{
			lock.unlock();
		}
	}
	
	public void notifyObservers(){
		lock.lock();
		ArrayList<UI> local = observers;
		lock.unlock();
		for(UI ui: local){
			ui.notified();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
        	lock.lock();
        	try{
	        	if(observers.isEmpty()){
	        		return;
	        	}
        	}
        	finally{
        		lock.unlock();
        	}
        }
	}

}
