package muzika;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
public class Player extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4655946818026374851L;
	public static int WIDTH=1400;
	public static int HEIGHT=400;
	private static int WHITE_P_WIDTH=40;
	private static int WHITE_P_HEIGHT=150;
	private static int BLACK_P_WIDTH=20;
	private static int BLACK_P_HEIGHT=100;
	private String notes[]= {"C","D","E","F","G","A","B"};
	private int bdir[]= {1,2,4,5,6};
	private String regnotes[]=new String[35];
	private String heightnotes[]=new String[25];
	private int regpos[]=new int[35];
	private int high_pos[]=new int[25];
	public static int NOTE_HEIGHT=HEIGHT-WHITE_P_HEIGHT-20;
	private MidiPlayer mplayer;
	private Color regcolor;
	private Color regtextcolor;
	private Color highcolor;
	private Color hightextcolor;
	private CompositionText cmptext;
	private long mousestart;
	private long mousefinish;
	private long keystart;
	private long keyfinish;
	private boolean colored=false;
	private boolean heightc=false;
	private Color colorcolor=Color.RED;
	private int colori;
	private Map<Character,Boolean> keysDown;
	private Recorder recorder;
	private boolean first=false;
	private boolean record=false;
	private boolean saved=false;
	private boolean loaded=false;
	private boolean toggle=true;
	private Piano piano;
	public Player(Piano piano) {
		this.piano=piano;
		recorder=new Recorder();
		keysDown=new HashMap<Character,Boolean>();
		keysDown.put('1', false);
		keysDown.put('2', false);
		keysDown.put('3', false);
		keysDown.put('4', false);
		keysDown.put('5', false);
		keysDown.put('6', false);
		keysDown.put('7', false);
		keysDown.put('8', false);
		keysDown.put('9', false);
		keysDown.put('0', false);
		keysDown.put('q', false);
		keysDown.put('w', false);
		keysDown.put('e', false);
		keysDown.put('r', false);
		keysDown.put('t', false);
		keysDown.put('y', false);
		keysDown.put('u', false);
		keysDown.put('i', false);
		keysDown.put('o', false);
		keysDown.put('p', false);
		keysDown.put('a', false);
		keysDown.put('s', false);
		keysDown.put('d', false);
		keysDown.put('f', false);
		keysDown.put('g', false);
		keysDown.put('h', false);
		keysDown.put('j', false);
		keysDown.put('k', false);
		keysDown.put('l', false);
		keysDown.put('z', false);
		keysDown.put('x', false);
		keysDown.put('c', false);
		keysDown.put('v', false);
		keysDown.put('b', false);
		keysDown.put('n', false);
		keysDown.put('!', false);
		keysDown.put('@', false);
		keysDown.put('$', false);
		keysDown.put('%', false);
		keysDown.put('^', false);
		keysDown.put('*', false);
		keysDown.put('(',false);
		keysDown.put('Q', false);
		keysDown.put('W', false);
		keysDown.put('E', false);
		keysDown.put('T', false);
		keysDown.put('Y', false);
		keysDown.put('I', false);
		keysDown.put('O', false);
		keysDown.put('P', false);
		keysDown.put('S', false);
		keysDown.put('D', false);
		keysDown.put('G', false);
		keysDown.put('H', false);
		keysDown.put('J', false);
		keysDown.put('L', false);
		keysDown.put('Z', false);
		keysDown.put('C', false);
		keysDown.put('V', false);
		keysDown.put('B', false);
		regcolor=hightextcolor=Color.white;
		highcolor=regtextcolor=Color.black;
		for(int i=0;i<regpos.length;i++) {
			if(i==0) regpos[i]=0;
			else regpos[i]=regpos[i-1]+WHITE_P_WIDTH;
		}
		for(int i=0;i<high_pos.length;i++) {
			high_pos[i]=((i/5)*7+bdir[i%5])*(WHITE_P_WIDTH)-BLACK_P_WIDTH/2;
		}
		for(int i=2;i<=6;i++) {
			for(int j=0;j<7;j++) {
				regnotes[(i-2)*7+j]=notes[j]+i;
			}
			for(int j=0;j<5;j++) {
				heightnotes[(i-2)*5+j]=notes[bdir[j]-1]+"#"+i;
			}
		}
		try {
			mplayer=new MidiPlayer();
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					int x=e.getX();
					int y=e.getY();
					boolean height=false;
					int note=-1;
					if(y>=HEIGHT-WHITE_P_HEIGHT-20 && y<HEIGHT) {
						for(int i=0;i<regpos.length;i++) {
							if(x>=regpos[i] && x<regpos[i]+WHITE_P_WIDTH) {
								note=i;
								break;
							}
						}
						for(int i=0;i<high_pos.length;i++) {
							if(x>=high_pos[i] && x<high_pos[i]+BLACK_P_WIDTH && y<NOTE_HEIGHT+BLACK_P_HEIGHT+10) {
								height=true;
								note=i;
								break;
							}
						}
					}
					if(note>=0) {
						String note_txt;
						int f;
						if(height) {
							note_txt=heightnotes[note];
							//System.out.println(note_txt);
							f=Composition.midinote.get(note_txt);
							heightc=true;
							mplayer.play(f);
						}
						else {
							note_txt=regnotes[note];
							//System.out.println(note_txt);
							f=Composition.midinote.get(note_txt);
							heightc=false;
							mplayer.play(f);
						}
						mousestart=System.currentTimeMillis();
						colori=note;
						colored=true;
						repaint();
					}
				}
				public void mouseReleased(MouseEvent e) {
					int x=e.getX();
					int y=e.getY();
					boolean height=false;
					int note=-1;
					if(y>=HEIGHT-WHITE_P_HEIGHT-20 && y<HEIGHT) {
						for(int i=0;i<regpos.length;i++) {
							if(x>=regpos[i] && x<regpos[i]+WHITE_P_WIDTH) {
								note=i;
								break;
							}
						}
						for(int i=0;i<high_pos.length;i++) {
							if(x>=high_pos[i] && x<high_pos[i]+BLACK_P_WIDTH && y<NOTE_HEIGHT+BLACK_P_HEIGHT+10) {
								height=true;
								note=i;
								break;
							}
						}
					}
					if(note>=0) {
						int f;
						String note_txt;
						if(record && !first) {
							recorder.start();
							first=false;
						}
						if(height) {
							note_txt=heightnotes[note];
							f=Composition.midinote.get(note_txt);
						}
						else {
							note_txt=regnotes[note];
							f=Composition.midinote.get(note_txt);
							
						}
						mplayer.release(f);
						mousefinish=System.currentTimeMillis();
						long traj=mousefinish-mousestart;
						colored=false;
						
						    if(traj>=125) {
							    if(traj<250) {
								    try {
									    if(loaded)cmptext.update(new Note(1,note_txt));
									    if(record)recorder.add(new Note(1,note_txt));
									    if(loaded && record && cmptext.ended) {
										    record();
									    }
									    repaint();
								    } catch (InterruptedException e1) {
									    // TODO Auto-generated catch block
									    e1.printStackTrace();
								    }
							    }
							    else {
								    try {
									    if(loaded)cmptext.update(new Note(2,note_txt));
									    if(record)recorder.add(new Note(2,note_txt));
									    if(loaded && record && cmptext.ended) {
										    record();
									    }
									    repaint();
								    } catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									    e1.printStackTrace();
								    }
							   }
						   }
						
					}
				}
			});
			this.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					int key=e.getKeyCode();
					char c='#';
					if(e.isShiftDown()) {
						switch(key) {
						case KeyEvent.VK_1:
							c='!';
							break;
						case KeyEvent.VK_2:
							c='@';
							break;
						case KeyEvent.VK_4:
							c='$';
							break;
						case KeyEvent.VK_5:
							c='%';
							break;
						case KeyEvent.VK_6:
							c='^';
							break;
						case KeyEvent.VK_8:
							c='*';
							break;
						case KeyEvent.VK_9:
							c='(';
							break;
						case KeyEvent.VK_Q:
							c='Q';
							break;
						case KeyEvent.VK_W:
							c='W';
							break;
						case KeyEvent.VK_E:
							c='E';
							break;
						case KeyEvent.VK_T:
							c='T';
							break;
						case KeyEvent.VK_Y:
							c='Y';
							break;
						case KeyEvent.VK_I:
							c='I';
							break;
						case KeyEvent.VK_O:
							c='O';
							break;
						case KeyEvent.VK_P:
							c='P';
							break;
						case KeyEvent.VK_S:
							c='S';
							break;
						case KeyEvent.VK_D:
							c='D';
							break;
						case KeyEvent.VK_G:
							c='G';
							break;
						case KeyEvent.VK_H:
							c='H';
							break;
						case KeyEvent.VK_J:
							c='J';
							break;
						case KeyEvent.VK_L:
							c='L';
							break;
						case KeyEvent.VK_Z:
							c='Z';
							break;
						case KeyEvent.VK_C:
							c='C';
							break;
						case KeyEvent.VK_V:
							c='V';
							break;
						case KeyEvent.VK_B:
							c='B';
							break;
						}
					}
					else {
						switch(key) {
						case KeyEvent.VK_1:
							c='1';
							break;
						case KeyEvent.VK_2:
							c='2';
							break;
						case KeyEvent.VK_3:
							c='3';
							break;
						case KeyEvent.VK_4:
							c='4';
							break;
						case KeyEvent.VK_5:
							c='5';
							break;
						case KeyEvent.VK_6:
							c='6';
							break;
						case KeyEvent.VK_7:
							c='7';
							break;
						case KeyEvent.VK_8:
							c='8';
							break;
						case KeyEvent.VK_9:
							c='9';
							break;
						case KeyEvent.VK_0:
							c='0';
							break;
						case KeyEvent.VK_Q:
							c='q';
							break;
						case KeyEvent.VK_W:
							c='w';
							break;
						case KeyEvent.VK_E:
							c='e';
							break;
						case KeyEvent.VK_R:
							c='r';
							break;
						case KeyEvent.VK_T:
							c='t';
							break;
						case KeyEvent.VK_Y:
							c='y';
							break;
						case KeyEvent.VK_U:
							c='u';
							break;
						case KeyEvent.VK_I:
							c='i';
							break;
						case KeyEvent.VK_O:
							c='o';
							break;
						case KeyEvent.VK_P:
							c='p';
							break;
						case KeyEvent.VK_A:
							c='a';
							break;
						case KeyEvent.VK_S:
							c='s';
							break;
						case KeyEvent.VK_D:
							c='d';
							break;
						case KeyEvent.VK_F:
							c='f';
							break;
						case KeyEvent.VK_G:
							c='g';
							break;
						case KeyEvent.VK_H:
							c='h';
							break;
						case KeyEvent.VK_J:
							c='j';
							break;
						case KeyEvent.VK_K:
							c='k';
							break;
						case KeyEvent.VK_L:
							c='l';
							break;
						case KeyEvent.VK_Z:
							c='z';
							break;
						case KeyEvent.VK_X:
							c='x';
							break;
						case KeyEvent.VK_C:
							c='c';
							break;
						case KeyEvent.VK_V:
							c='v';
							break;
						case KeyEvent.VK_B:
							c='b';
							break;
						case KeyEvent.VK_N:
							c='n';
							break;
						}
						
					}
					if(c!='#' && keysDown.get(c)!=true) {
						String notestr=Composition.notes.get(c).getFirst();
						
						if(e.isShiftDown()) {
							heightc=true;
							for(int i=0;i<25;i++) {
								if(heightnotes[i].equals(notestr)){
									colori=i;
									break;
								}
							}
						}
						else {
							heightc=false;
							for(int i=0;i<35;i++) {
								if(regnotes[i].equals(notestr)){
									colori=i;
									break;
								}
							}
						}
						int notenum=Composition.notes.get(c).getSecond();
						mplayer.play(notenum);
						keystart=System.currentTimeMillis();
						keysDown.put(c, true);
						colored=true;
						repaint();
					}
				}
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					int key=e.getKeyCode();
					char c='#';
					if(e.isShiftDown()) {
						switch(key) {
						case KeyEvent.VK_1:
							c='!';
							break;
						case KeyEvent.VK_2:
							c='@';
							break;
						case KeyEvent.VK_4:
							c='$';
							break;
						case KeyEvent.VK_5:
							c='%';
							break;
						case KeyEvent.VK_6:
							c='^';
							break;
						case KeyEvent.VK_8:
							c='*';
							break;
						case KeyEvent.VK_9:
							c='(';
							break;
						case KeyEvent.VK_Q:
							c='Q';
							break;
						case KeyEvent.VK_W:
							c='W';
							break;
						case KeyEvent.VK_E:
							c='E';
							break;
						case KeyEvent.VK_T:
							c='T';
							break;
						case KeyEvent.VK_Y:
							c='Y';
							break;
						case KeyEvent.VK_I:
							c='I';
							break;
						case KeyEvent.VK_O:
							c='O';
							break;
						case KeyEvent.VK_P:
							c='P';
							break;
						case KeyEvent.VK_S:
							c='S';
							break;
						case KeyEvent.VK_D:
							c='D';
							break;
						case KeyEvent.VK_G:
							c='G';
							break;
						case KeyEvent.VK_H:
							c='H';
							break;
						case KeyEvent.VK_J:
							c='J';
							break;
						case KeyEvent.VK_L:
							c='L';
							break;
						case KeyEvent.VK_Z:
							c='Z';
							break;
						case KeyEvent.VK_C:
							c='C';
							break;
						case KeyEvent.VK_V:
							c='V';
							break;
						case KeyEvent.VK_B:
							c='B';
							break;
						}
					}
					else {
						switch(key) {
						case KeyEvent.VK_1:
							c='1';
							break;
						case KeyEvent.VK_2:
							c='2';
							break;
						case KeyEvent.VK_3:
							c='3';
							break;
						case KeyEvent.VK_4:
							c='4';
							break;
						case KeyEvent.VK_5:
							c='5';
							break;
						case KeyEvent.VK_6:
							c='6';
							break;
						case KeyEvent.VK_7:
							c='7';
							break;
						case KeyEvent.VK_8:
							c='8';
							break;
						case KeyEvent.VK_9:
							c='9';
							break;
						case KeyEvent.VK_0:
							c='0';
							break;
						case KeyEvent.VK_Q:
							c='q';
							break;
						case KeyEvent.VK_W:
							c='w';
							break;
						case KeyEvent.VK_E:
							c='e';
							break;
						case KeyEvent.VK_R:
							c='r';
							break;
						case KeyEvent.VK_T:
							c='t';
							break;
						case KeyEvent.VK_Y:
							c='y';
							break;
						case KeyEvent.VK_U:
							c='u';
							break;
						case KeyEvent.VK_I:
							c='i';
							break;
						case KeyEvent.VK_O:
							c='o';
							break;
						case KeyEvent.VK_P:
							c='p';
							break;
						case KeyEvent.VK_A:
							c='a';
							break;
						case KeyEvent.VK_S:
							c='s';
							break;
						case KeyEvent.VK_D:
							c='d';
							break;
						case KeyEvent.VK_F:
							c='f';
							break;
						case KeyEvent.VK_G:
							c='g';
							break;
						case KeyEvent.VK_H:
							c='h';
							break;
						case KeyEvent.VK_J:
							c='j';
							break;
						case KeyEvent.VK_K:
							c='k';
							break;
						case KeyEvent.VK_L:
							c='l';
							break;
						case KeyEvent.VK_Z:
							c='z';
							break;
						case KeyEvent.VK_X:
							c='x';
							break;
						case KeyEvent.VK_C:
							c='c';
							break;
						case KeyEvent.VK_V:
							c='v';
							break;
						case KeyEvent.VK_B:
							c='b';
							break;
						case KeyEvent.VK_N:
							c='n';
							break;
						}
						
					}
					if(c!='#' && keysDown.get(c)) {
						int notenum=Composition.notes.get(c).getSecond();
						mplayer.release(notenum);
						if(record && !first) {
							recorder.start();
							first=true;
						}
						keyfinish=System.currentTimeMillis();
						long traj=keyfinish-keystart;
						colored=false;
						if(traj>=125) {
							if(traj<250) {
								try {
									if(loaded)cmptext.update(new Note(1,Composition.notes.get(c).getFirst()));
									if(record)recorder.add(new Note(1,Composition.notes.get(c).getFirst()));
									if(loaded && record && cmptext.ended) {
										recorder.end();
										record();
									}
									repaint();
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							else {
								try {
									if(loaded)cmptext.update(new Note(2,Composition.notes.get(c).getFirst()));
									if(record)recorder.add(new Note(2,Composition.notes.get(c).getFirst()));
									if(loaded && record && cmptext.ended) {
										recorder.end();
										record();
									}
									repaint();
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						keysDown.put(c, false);
					}
				}

			});
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, HEIGHT-WHITE_P_HEIGHT-40, WIDTH, WHITE_P_HEIGHT+40);
		g.setColor(regcolor);
		for(int i=0;i<regpos.length;i++) {
			if(colored && colori==i && !heightc) {
				g.setColor(colorcolor);
			}
			g.fillRect(regpos[i],NOTE_HEIGHT, WHITE_P_WIDTH, WHITE_P_HEIGHT);
			g.fillOval(regpos[i],HEIGHT-40,40,40);
			if(colored && colori==i && !heightc) {
				g.setColor(regcolor);
			}
		}
		g.setColor(Color.BLACK);
		g.drawLine(0, HEIGHT-WHITE_P_HEIGHT-20, WIDTH, HEIGHT-WHITE_P_HEIGHT-20);
		for(int i=1;i<regpos.length;i++) {
			g.drawLine(regpos[i],NOTE_HEIGHT,regpos[i],HEIGHT);
		}
		g.setColor(highcolor);
		for(int i=0;i<high_pos.length;i++) {
			if(colored && colori==i && heightc) {
				g.setColor(colorcolor);
			}
			g.fillRect(high_pos[i],NOTE_HEIGHT, BLACK_P_WIDTH, BLACK_P_HEIGHT);
			g.fillOval(high_pos[i], NOTE_HEIGHT+BLACK_P_HEIGHT-10, 20, 20);
			if(colored && colori==i && heightc) {
				g.setColor(highcolor);
			}
		}
		g.setColor(regtextcolor);
		for(int i=0;i<regpos.length;i++) {
			int wx=regpos[i]+12;
			g.drawString(regnotes[i], wx, HEIGHT-WHITE_P_HEIGHT+130);
			g.drawString(Composition.notekey.get(regnotes[i]).toString(), wx+5, HEIGHT-WHITE_P_HEIGHT+110);
		}
		g.setFont(new Font(g.getFont().getName(),Font.PLAIN,(int)(0.9*g.getFont().getSize())));
		g.setColor(hightextcolor);
		for(int i=0;i<high_pos.length;i++) {
			int xw=high_pos[i]+1;
			g.drawString(heightnotes[i],xw,HEIGHT-WHITE_P_HEIGHT+80);
			g.drawString(Composition.notekey.get(heightnotes[i]).toString(),xw+4,HEIGHT-WHITE_P_HEIGHT+60);
			
		}
		if(loaded) {
		for(int i=0;i<cmptext.symbols.length;i++) {
			g.setColor(cmptext.colors[i]);
			g.fillRect(cmptext.symbols[i][0], cmptext.symbols[i][1], cmptext.symbols[i][2],cmptext.symbols[i][3]);
			g.setColor(Color.BLACK);
			int x=cmptext.symbols[i][0]+cmptext.symbols[i][2]*3/8;
			int y=cmptext.symbols[i][1]+12;
			for(int j=0;j<cmptext.strings[i].length;j++) {
				if(toggle) g.drawString(cmptext.strings[i][j], x,y);
				else {
					String s=cmptext.strings[i][j];
					if(s==" ") {
						g.drawString(" ", x, y);
					}
					else {
						g.drawString(""+Composition.notekey.get(s),x,y);
					}
					
				}
				y+=20;
			}
			
		}
		}
		g.setColor(Color.black);
		for(int i=WIDTH/2;i<WIDTH;i+=40) {
			g.drawLine(i,NOTE_HEIGHT-40, i,NOTE_HEIGHT-20);
		}
		
	}
	public void record() {
		record=false;
		first=false;
		recorder.end();
		save();
	}
	public void recordstart() {record=true;saved=false;}
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH,HEIGHT);
	}
	public Composition getComp() {
		return recorder.getComp();
	}
	public boolean saved() {
		return (recorder.status()==0 || saved);
	}
	public void setSaved(boolean f) {
		saved=f;
	}
	public void save() {
		piano.save();
	}
	public void load(String filename) {
		try {
			Composition compos=new Composition(filename);
			cmptext=new CompositionText(compos);
			loaded=true;
			repaint();
		}
		catch(Exception e) {}
	}
	public void play() {
		String sef[]=cmptext.strings[cmptext.current];
		if(sef.length>1) {
			for(int j=0;j<sef.length;j++) {
				try {
					colori=-1;
					for(int k=0;k<25;k++) {
						if(heightnotes[k].equals(sef[j])){
							colori=k;
							heightc=true;
							break;
						}
					}
					if(colori==-1) {
						for(int k=0;k<35;k++) {
							if(regnotes[k].equals(sef[j])){
								colori=k;
								heightc=false;
								break;
							}
						}
					}
					colored=true;
					paintImmediately(0, 0,1400,400);
					mplayer.play(Composition.midinote.get(sef[j]),250);
					cmptext.update(new Note(2,sef[j]));
					colored=false;
					paintImmediately(0, 0,1400,400);
					
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}
		else {
			
			try {
				colori=-1;
				for(int k=0;k<25;k++) {
					if(heightnotes[k].equals(sef[0])){
						colori=k;
						heightc=true;
						break;
					}
				}
				if(colori==-1) {
					for(int k=0;k<35;k++) {
						if(regnotes[k].equals(sef[0])){
							colori=k;
							heightc=false;
							break;
						}
					}
				}
				colored=true;
				paintImmediately(0, 0,1400,400);
				mplayer.play(Composition.midinote.get(sef[0]),(cmptext.symbols[cmptext.current][2]/CompositionText.width)*125);
				cmptext.update(new Note(cmptext.symbols[cmptext.current][2]/CompositionText.width,sef[0]));
				colored=false;
				paintImmediately(0, 0,1400,400);
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
		}
		
		
	}
	public void playthr() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(!saved()) record();
				cmptext.movestart();
				Graphics g=getGraphics();
				paintComponent(g);
				while(!cmptext.ended)play();
				cmptext.ended=false;
			}
		});
	}
	public void toggle(boolean t) {
		toggle=t;
		if(loaded)repaint();
	}
	/*public static void createAndShowGui() {
		JFrame frame=new JFrame();
		Player player=new Player();
		player.setFocusable(true);
		frame.add(player);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
	}*/
	
	/*public static void  main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}*/

	


}
