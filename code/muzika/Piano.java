package muzika;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Piano extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6899874040441186890L;
	private Player player;
	private Saving save=new Saving(this);
	private Loading load=new Loading(this);
	private boolean ended=false;
	private boolean toggle=true;
	private class Loading extends JDialog{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1537946460687155584L;
		private JLabel jl;
		private JTextField jtxf;
		private JButton jb;
		private JPanel jp;
		public Loading(JFrame parent) {
			super(parent,"Loading",true);
			setSize(500,100);
			jp=new JPanel();
			jp.setLayout(new FlowLayout());
			jl=new JLabel("Load composition(filename,without extensions):");
			jtxf=new JTextField("",20);
			jb=new JButton("Load");
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tef=jtxf.getText()+".txt";
					player.load(tef);
					setVisible(false);
				}
			});
			jp.add(jl);
			jp.add(jtxf);
			jp.add(jb);
			add(jp);
		}
	}
	private class Saving extends JDialog{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4619553801213880335L;
		private JTextField txf;
		private JLabel label;
		private JPanel panel1,panel2;
		private JButton btn1,btn2;
		public Saving(JFrame parent) {
			super(parent, "Saving",true);
			setSize(500,100);
			setLayout(new GridLayout(2,1));
			panel1=new JPanel();
			panel1.setLayout(new FlowLayout());
			label=new JLabel("Enter the filename(without extensions):");
			txf=new JTextField("",20);
			panel1.add(label);
			panel1.add(txf);
			add(panel1);
		    panel2=new JPanel();
			btn1=new JButton("Save as midi");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Composition comper=player.getComp();
					String xef=txf.getText();
					if(xef!="") {
						comper.setfilename(xef+".midi");
					}
					MIDIFormatter mx=new MIDIFormatter(comper);
					mx.format();
					setVisible(false);
					player.setSaved(true);
					if(ended==true) {dispose();parent.dispose();}
				}
			});
			panel2.add(btn1);
			btn2=new JButton("Save as txt");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Composition comper=player.getComp();
					String xef=txf.getText();
					if(xef!="") {
						comper.setfilename(xef+".txt");
					}
					TXTFormatter tx=new TXTFormatter(comper);
					tx.format();
					setVisible(false);
					player.setSaved(true);
					if(ended==true) {dispose();parent.dispose();}
				}
			});
			panel2.add(btn2);
			add(panel2);
			
		}
	}
	private void addMenu() {
		JMenuBar mb=new JMenuBar();
		JMenu menu=new JMenu("Commands");
		this.setJMenuBar(mb);
		mb.add(menu);
		
		JMenuItem item=new JMenuItem("Start recording");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.recordstart();
			}
		});
		menu.add(item);
		item=new JMenuItem("End recording");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.record();
				
			}
		});
		menu.add(item);
		JSeparator jsep=new JSeparator();
		menu.add(jsep);
		item=new JMenuItem("Load composition");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load.setVisible(true);
				
			}
		});
		menu.add(item);
		item=new JMenuItem("Play composition");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.playthr();
			
				
			}
		});
		menu.add(item);
		jsep=new JSeparator();
		menu.add(jsep);
		item=new JMenuItem("Toggle notes");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.toggle(!toggle);
				toggle=!toggle;
			
				
			}
		});
		menu.add(item);
		
		
		
		
	}
	public Dimension getPreferredSize() {
		return new Dimension(Player.WIDTH+15,Player.HEIGHT+61);
	}
	public void save() {
			save.setVisible(true);
	}
	public Piano() {
		super("Piano");
		addMenu();
		player=new Player(this);
		player.setFocusable(true);
		add(player,"Center");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ended=true;
				if(!player.saved())player.record();
			}
		});
		setLocationByPlatform(true);
		pack();
		setVisible(true);
	}
	public static void createAndShowGui() {
		new Piano();
	}
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
	

	

}
