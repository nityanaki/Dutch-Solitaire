import java.awt.*;  
import javax.swing.*;  
import java.io.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.sound.sampled.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
  
public class Board extends Canvas implements MouseListener{  

        public static Card[][] deck = new Card[4][14];
		public static String[] let = {"c","d","s","h"};
		public static ArrayList<Card>nonace=new ArrayList<Card>();
    	public static ArrayList<Card> aces=new ArrayList<Card>();
		public static String imgname="";
		boolean click1=false;
		Card temp1;
		Card temp2;
		boolean click2=false;
		
    public Board()
    {
    	 Toolkit t=Toolkit.getDefaultToolkit();   
    	 	addMouseListener(this);
		
		//putting all of the cards except for the aces into an arraylist to get shuffled
		for(int suit=1; suit<=4; suit++)
		{
			for(int number=2; number<=15; number++)
			{
				if(number==14)//if the card is not an ace
				continue;
				imgname=number+let[suit-1]+".gif";
				Image pic=t.getImage(imgname);
				nonace.add(new Card(number, let[suit-1], pic));
			}
		}
		//System.out.println(nonace.size());
		//putting the aces into their own arraylist to get shuffled
		for(int i=1; i<=4; i++)
		{
			imgname=14+let[i-1]+".gif";
			Image pic=t.getImage(imgname);
			aces.add(new Card(14, let[i-1], pic));
		}
		
		//shuffling the cards
		Collections.shuffle(nonace);
		Collections.shuffle(aces);
		
		for(int r=0; r<4; r++)
        {
        	for(int c=0; c<13; c++)
        	{
        		deck[r][c]=nonace.get(0);
        		nonace.remove(0);
        		deck[r][c].setX(75*c);
        		deck[r][c].setY(100*r);
        		//System.out.print(deck[r][c]+" ");
        	}
        //	System.out.println();
        }
        
        //moving the aces into the matrix and initializing their positions
        for(int r=0; r<4; r++)
        {
        	for(int c=13; c<=13; c++)
        	{
        		deck[r][c]=aces.remove(0);
        		deck[r][c].setX(75*c);
        		deck[r][c].setY(100*r);
        	}
        }
    }  
    public boolean determineWinner()
    {
    	for (int r=0;r<deck.length;r++)
    	{
    		for (int c=1;c<deck[r].length-1;c++)
    		{
    			if(deck[r][c].num>deck[r][c+1].num||deck[r][c].suit.equals(deck[r][c+1].suit)==false)
    				return false;
    		}
    	}
    	return true;
    }
    public void paint(Graphics g) {  
		
		//moving the non ace cards from the array into a matrix, and initializing the card's positions
        for(int r=0; r<4; r++)
        {
        	for(int c=0; c<13; c++)
        	{
        		g.drawImage(deck[r][c].pic, deck[r][c].xval, deck[r][c].yval, this);
        		//System.out.print(deck[r][c]+" ");
        	}
        //	System.out.println();
        }
        
        //moving the aces into the matrix and initializing their positions
        for(int r=0; r<4; r++)
        {
        	for(int c=13; c<=13; c++)
        	{
        		g.drawImage(deck[r][c].pic, deck[r][c].xval, deck[r][c].yval, this);
        	}
        }
        if (click1&&!click2)
        {
        	g.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20));
        	g.setColor(new Color(150, 190,250));
        	g.drawString("card 1 selected", 800, 440);
        }
        if(determineWinner())
       {
        	g.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 48));
        	g.setColor(Color.BLUE);
			g.drawString("YOU WON",50,440);  
			/*File file;
			AudioInputStream stream;
			Clip music;
			file = new File("applause_y.wav");
			stream = AudioSystem.getAudioInputStream(file);
			music= AudioSystem.getClip();
			music.open(stream);
			music.start();
			music.loop(Clip.LOOP_CONTINUOUSLY);*/
			playSound();

        }
    }
    
    public void playSound() {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("applause_y.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
}
    public void mouseClicked(MouseEvent e) {
    	if(!click1)
    	{
    		click1=true;
    		repaint();
    			
    	}
    	else
    	{
    		click2=true;
    		repaint();
    	}
        int x = e.getX();
        int y = e.getY();
    
    	if(click2)
    		temp2=deck[y/100][x/75];
    	else
    		temp1=deck[y/100][x/75];
    		
       // System.out.println(temp1+" "+temp2);
        
        if(click2&&swappable(temp1, temp2))
        {
        	swap(temp1, temp2);
        	temp1=null;
        	temp2=null;
        	click1=false;
        	click2=false;
        	repaint();
        }
        else if(click1&&click2)
        {
        	temp1=null;
        	temp2=null;
        	click1=false;
        	click2=false;
        }
    }
    
    public boolean swappable(Card a, Card b)
    {
    	if (b.num!=15)
    		return false;
    	if (a.num==14)
    		return false;
    	if (b.getcol()==0)
    	{
    		if (a.num!=2||!(a.suit.equals(deck[b.getrow()][13].suit)))
    			return false;
    		else
    			return true;
    	}
    	Card left = deck[b.getrow()][b.getcol()-1];
    	Card right = deck[b.getrow()][b.getcol()+1];
    	String sl = left.suit;
    	String sr = right.suit;
    	int il = left.num;
    	int ir = right.num;
    	
    	if (a.num==il+1&&a.suit.equals(sl))
    		return true;
    	if (a.num==ir-1&&a.suit.equals(sr))
    		return true;
    	return false;
    	
    	
    	
    }
    
    public void swap(Card a, Card  b)
    {
    	int arow=a.yval/100;
    	int acol=a.xval/75;
    	int brow=b.yval/100;
    	int bcol=b.xval/75;
    	
    	Card temp1=new Card(a.num, a.suit, a.pic);
    	temp1.setX(a.xval);
    	temp1.setY(a.yval);
    	Card temp2=new Card(b.num, b.suit, b.pic);
    	temp2.setX(b.xval);
    	temp2.setY(b.yval);
    	deck[arow][acol]=b;
    	deck[brow][bcol]=a;
    	deck[arow][acol].setX(temp1.xval);
    	deck[arow][acol].setY(temp1.yval);
    	deck[brow][bcol].setX(temp2.xval);
    	deck[brow][bcol].setY(temp2.yval);
    	//System.out.println(deck[arow][acol]+" "+deck[brow][bcol]);
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
    }
 
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
      
    public static void main(String[] args) throws Exception {  
        Board m=new Board();  
        JFrame f=new JFrame();  
        f.add(m);  
        f.setSize(1075,500);  
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }  
  
} 

class Card
{
	int num;
	String suit;
	Image pic;
	int xval,yval;
	
	public Card(int n,String s, Image p)
	{
		num=n;
		suit=s;
		pic=p;
	}
	
	public void setX(int x)
	{
		xval=x;
	}
	
	public void setY(int y)
	{
		yval=y;
	}
	public String toString()
	{
		return num+" "+suit+" "+xval+" "+yval;
	}
	
	public int getcol()
	{
		return xval/75;
	}
	
	public int getrow()
	{
		return yval/100;
	}
} 