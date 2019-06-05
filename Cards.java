import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cards {

	File folderInput;
    BufferedImage folderImage;
    
    public Cards() throws IOException{
    	this.folderInput = new File("playingcards.png");
        this.folderImage = ImageIO.read(folderInput);
    }
    
    public BufferedImage getImage(){
    	return folderImage;
    }
    
    public int getX1(char g){
    	if(g=='A'){
    		return 1;
    	}
    	else if(g == '2'){
    		return 74;
    	}
    	else if(g == '3'){
    		return 147;
    	}
    	else if(g == '4'){
    		return 1+(73 * 3);
    	}
    	else if(g == '5'){
    		return 1+(73 * 4);
    	}
    	else if(g == '6'){
    		return 1+(73 * 5);
    	}
    	else if(g == '7'){
    		return 1+(73 * 6);
    	}
    	else if(g == '8'){
    		return 1+(73 * 7);
    	}
    	else if(g == '9'){
    		return 1+(73 * 8);
    	}
    	else if(g == '1'){
    		return 1+(73 * 9);
    	}
    	else if(g == 'J'){
    		return 1+(73 * 10);
    	}
    	else if(g == 'Q'){
    		return 1+(73 * 11);
    	}
    	else if(g == 'K'){
    		return 1+(73 * 12);
    	}
    	return 0;
    }
    
    public int getX2(char g){
    	if(g=='A'){
    		return 73*1;
    	}
    	else if(g == '2'){
    		return 73*2;
    	}
    	else if(g == '3'){
    		return 73*3;
    	}
    	else if(g == '4'){
    		return 73*4;
    	}
    	else if(g == '5'){
    		return 73*5;
    	}
    	else if(g == '6'){
    		return 73*6;
    	}
    	else if(g == '7'){
    		return 73*7;
    	}
    	else if(g == '8'){
    		return 73*8;
    	}
    	else if(g == '9'){
    		return 73*9;
    	}
    	else if(g == '1'){
    		return 73*10;
    	}
    	else if(g == 'J'){
    		return 73*11;
    	}
    	else if(g == 'Q'){
    		return 73*12;
    	}
    	else if(g == 'K'){
    		return 73*13;
    	}
    	return 0;
    }
    
    public int getY1(char g){
    	return 1;
    }
    
    public int getY2(char g){
    	return 97;
    }
}
