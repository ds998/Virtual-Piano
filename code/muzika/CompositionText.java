package muzika;
import java.awt.Color;
public class CompositionText {
	private Composition comp;
	public int current;
	private Symbol curSym;
	public int symbols[][];
	public String strings[][];
	public static int width=20;
	public Color colors[];
	private int size;
	private int num;
	private long start;
	private long finish;
	private boolean chord[];
	public boolean ended=false;
	public CompositionText(Composition comp){
		this.comp=comp;
		size=comp.getsize();
		current=0;
		curSym=comp.getSym(0);
		symbols=new int[size][4];
		strings=new String[size][];
		colors=new Color[size];
		int i=0;
		num=1;
		if(curSym.chord()) {
			Chord ch=(Chord)curSym.clone();
			int chnum=0;
			for(@SuppressWarnings("unused") Note n:ch) {
				chnum++;
			}
			chord=new boolean[chnum];
		}
		int regpos=Player.WIDTH/2;
		for(Symbol sym:this.comp) {
			if(sym.chord()) {
				Chord ch=(Chord)sym.clone();
				int f=0;
				for(@SuppressWarnings("unused") Note n:ch) {
					f++;
				}
				if(f>num) {
					num=f;
				}
			}
		}
		for(i=0;i<size;i++) {
			Symbol sym=this.comp.getSym(i);
			int sz=0;
			if(sym.chord()) {
				Chord ch=(Chord)sym.clone();
				for(@SuppressWarnings("unused") Note n:ch) {
					sz++;
				}
				
			}
			else sz=1;
			strings[i]=new String[sz];
			int j=0;
			if(sym.note()) {
				strings[i][j]=sym.toString();
			}
			else if(sym.chord()) {
				Chord ch=(Chord)sym.clone();
				for(Note n:ch) {
					strings[i][j]=n.toString();
					j++;
				}
			}
			else strings[i][j]=" ";
		    if(!sym.chord()) {
		    	symbols[i][0]=regpos;
		    	symbols[i][1]=Player.NOTE_HEIGHT-(num/2+1)*20-40;
		    	symbols[i][2]=sym.Trajanje()*width;
		    	symbols[i][3]=20;
		    	if(strings[i][j]!=" ") {
		    	    if(sym.Trajanje()==2) colors[i]=Color.RED;
		    	    else if(sym.Trajanje()==1) colors[i]=Color.GREEN;
		    	}
		    	else {
		    		if(sym.Trajanje()==2) colors[i]=Color.PINK;
		    	    else if(sym.Trajanje()==1) colors[i]=Color.CYAN	;
		    	}
		    }
		    else {
		    	symbols[i][0]=regpos;
		    	symbols[i][1]=Player.NOTE_HEIGHT-(num/2+1)*20-(sz/2-1)*20-40;
		    	symbols[i][2]=sym.Trajanje()*width;
		    	symbols[i][3]=sz*20;
		    	colors[i]=Color.RED;
		    	
		    }
		    regpos+=sym.Trajanje()*width;
		    
			
			
			
		}
	}
	public Symbol curSym() {
		return curSym;
	}
	public void move() {
		int move=curSym.Trajanje()*width;
		for(int i=0;i<symbols.length;i++) {
			symbols[i][0]-=move;
		}
		current=(current+1)%size;
		if(current==0) {
			ended=true;
			int regpos=Player.WIDTH/2;
			for(int i=0;i<symbols.length;i++) {
				symbols[i][0]=regpos;
				regpos+=symbols[i][2];
			}
			
		}
		curSym=comp.getSym(current);
	}
	public void update(Note note) throws InterruptedException {
		boolean bool=false;
		if(curSym.note()){
			if(note.toString().equals(curSym.toString()) && note.Trajanje()==curSym.Trajanje()) {
				bool=true;
			}
		}
		else if(curSym.chord()) {
			Chord ch=(Chord)curSym.clone();
			int chx=0;
			boolean found=false;
			for(Note n:ch) {
				if(note.toString().equals(n.toString()) && note.Trajanje()==n.Trajanje()){
					if(chord[chx]==false) {
						chord[chx]=true;
						found=true;
						break;
						
					}
				}
				chx++;
			}
			if(found) {
				boolean x=true;
				int s=0;
				for(int i=0;i<chord.length;i++) {
					if(!chord[i]) {
						x=false;
						s++;
					}
				}
				if(x) {
					finish=System.currentTimeMillis();
					if(finish-start<=500*chord.length) {
						bool=true;
					}
					else {
						chord=new boolean[chord.length];
					}
				}
				else {
					if(s==chord.length-1) {
						start=System.currentTimeMillis();
					}
				}
			}
			
		}
		if(bool) {
			move();
			while(!curSym.note() && !curSym.chord()) {
				Thread.sleep(125*curSym.Trajanje());
				move();
				
			}
			if(curSym.chord()) {
				Chord ch=(Chord)curSym.clone();
				int chnum=0;
				for(@SuppressWarnings("unused") Note n:ch) {
					chnum++;
				}
				chord=new boolean[chnum];
			}
		}
	}
	public void movestart() {
		int regpos=Player.WIDTH/2;
		while(symbols[0][0]!=regpos) {
			move();
		}
		ended=false;
	}

}
