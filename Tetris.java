package paulap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel{
	
	private final Point[][][] myPoint= {
			
			{
				//I
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(3,1)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(1,3)},
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(3,1)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(1,3)}
				
			},
			{
				//J
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(2,0)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(2,2)},
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(0,2)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(0,0)}
				
			},
			{
				//L
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(2,0)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(2,2)},
				{new Point(0,1), new Point(1,1),new Point(2,1),new Point(0,0)},
				{new Point(1,0), new Point(1,1),new Point(1,2),new Point(2,0)}
				
			},
			{
				//O
				{new Point(0,0), new Point(0,1),new Point(01,0),new Point(1,1)},
				{new Point(0,0), new Point(0,1),new Point(01,0),new Point(1,1)},
				{new Point(0,0), new Point(0,1),new Point(01,0),new Point(1,1)},
				{new Point(0,0), new Point(0,1),new Point(01,0),new Point(1,1)}
				
			}
				
			};
		
			private final Color[] myColor= {Color.CYAN,Color.magenta,Color.YELLOW,Color.black,Color.PINK,Color.red};
			
			private Point pt;
			private int currentPiece;
			private int rotation;
			private ArrayList<Integer> nextPiece=new ArrayList<Integer>();
			private long score;
			private Color[][] well;
			
			
			private void init() {
				well=new Color[12][24];
				for(int i=0;i<12;i++) {
					for(int j=0;j<23;j++) {
						if(i==0||i==22) {
							well[i][j]=Color.PINK;
						}else 
						{
							well[i][j]=Color.black;
						}
						
						
					}
					
					
				}
				newPiece();
			}
	
			public void newPiece() {
				pt=new Point(5,2);
				rotation=0;
				if(nextPiece.isEmpty()) {
					Collections.addAll(nextPiece, 0,1,2,3);
					Collections.shuffle(nextPiece);
				}
			}
			
			private boolean collidesAt(int x,int y, int rotation) {
				for(Point p: myPoint[currentPiece][rotation]) {
					if(well[p.x+x][p.y+y]!=Color.black) {
						return true;
					}
				}
				return false;
				}
				
			
			private void rotate(int i) {
				int newRotation=(rotation+i)%4;
				if(newRotation<0) {
					newRotation=3;
				}
				if(!collidesAt(pt.x, pt.y, newRotation)) {
					rotation=newRotation;
				}
				repaint();
			}
			
			public void move(int i) {
				if(!collidesAt(pt.x, pt.y, rotation)) {
					pt.x+=1;
				}
				
				repaint();
			}
			public void drop() {
					if(!collidesAt(pt.x, pt.y, rotation)) {
						pt.y+=1;
					}else {
						fixToWell();
					}
					repaint();
				}
			
				public void fixToWell() {
					for(Point p: myPoint[currentPiece][rotation]) {
						well[pt.x+p.x][pt.y+p.y]=myColor[currentPiece];
					}
					clearRows();
					newPiece();
				}
				
				public void deleteRow(int row) {
					for(int j=row-1;j>0;j--) {
						for(int i=1;i<11;i++) {
							well[i][j+1]=well[i][j];
						}
						
					}
				}
				
				public void clearRows() {
					boolean gap;
					int numClear=0;
					for(int j=21;j>0;j--) {
						gap=false;
						for(int i=1;1<11;i++) {
							if(well[i][j]==Color.black) {
								gap=true;
								break;
							}
						}
					
					if(!gap) {
						deleteRow(numClear);
						j+= 1;
						numClear+=1;
					}	
				}
				
				switch (numClear) {
				case 1:
					score+=100;
					break;
				case 2:
					score+=300;
					break;
				case 3:
					score+=500;
					break;
					case 4:
					score+=800;
					break;
				}
				}

				private void drawPiece(Graphics g) {
					g.setColor(myColor[currentPiece]);
					for(Point p: myPoint[currentPiece][rotation]) {
						g.fillRect((p.x+=pt.x)*26, (pt.y+p.y)*26,25,25);
					}
				}
				
				public void paintComponent(Graphics g) {
					g.fillRect(0, 0, 26*12, 26*23);
					for(int i=0;i<23;i++) {
						for(int j=0;j<23;j++) {
							g.setColor(well[i][j]);
							g.fillRect(26*i, 26*j, 25, 25);
						}
					}
					g.setColor(Color.WHITE);
					g.drawString("Score is :"+score, 19*12,25);
					drawPiece(g);
				}
						

				
				
				public static void main(String[] args) {
					// TODO Auto-generated method stub	
					
					JFrame f=new JFrame("Tetris");
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setSize(12*26+10, 26*23+25);
					f.setVisible(true);
				
					final Tetris game=new Tetris();
					game.init();
					f.add(game);
					
					f.addKeyListener(new KeyListener() {
						
						@Override
						public void keyTyped(KeyEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void keyReleased(KeyEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void keyPressed(KeyEvent e) {
							switch (e.getKeyCode()) {
							case KeyEvent.VK_UP:
								game.rotate(-1);
								break;
							case KeyEvent.VK_DOWN:
								game.rotate(+1);
								break;
								
							case KeyEvent.VK_LEFT:
								game.move(-1);
								break;
								
							case KeyEvent.VK_SPACE:
								game.drop();
								game.score+=1;
								break;	
								
							}
						}	
					});
					new Thread() {
						public void run() {
							while(true) {
								try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
								game.drop();
						}
						}
						
					}.start();
					
					
				}	
	
					
					
}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
			
			
		




































		
	