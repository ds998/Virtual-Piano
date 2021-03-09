package muzika;

import java.util.Vector;

public class Recorder{
	private Vector<Pair<Symbol,Long>> vec;
	private Composition comp;
	private long ftime;
	private int status=0;
	public Recorder() {
		this.comp=new Composition();
		vec=new Vector<Pair<Symbol,Long>>();
	}
	public void nadd(Note n) {
		long time=System.currentTimeMillis();
		if(time-ftime>=250) {
			long x=time-ftime;
			while(x>=250) {
				vec.add(new Pair<Symbol,Long>(new Pause(2),time));
				x-=250;
			}
			while(x>=250) {
				vec.add(new Pair<Symbol,Long>(new Pause(1),time));
				x-=125;
			}
		}
		vec.add(new Pair<Symbol,Long>(n,time));
		ftime=time;
		
	}
	public void add(Note n) {
		if(status==1) {
			nadd(n);
		}
	}
	public void start() {status=1; ftime=System.currentTimeMillis();vec.clear();}
	public void end() {
		status=2;
		org();
	}
	public void org() {
		int i=0;
		while(i<vec.size()) {
		    if(vec.get(i).getFirst().note()) {
			    Symbol sym=vec.get(i).getFirst();
			    long t=vec.get(i).getSecond();
			    if(sym.Trajanje()==2) {
				    int n=1;
				    int index=i+1;
				    while(index<vec.size() && vec.get(index).getFirst().note() && vec.get(index).getFirst().Trajanje()==2 && vec.get(index).getSecond()-t<=1000*(n+1)) {
				    	n++;
				    	index++;
				    	
				    }
				    if(n==1) {
				    	comp.add(sym);
				    	i++;
				    }
				    else {
				    	Symbol mult[]=new Symbol[n];
				    	mult[0]=sym;
				    	for(int j=1;j<n;j++) {
				    		mult[j]=vec.get(i+j).getFirst();
				    	}
				    	Chord chord=new Chord(2);
				    	for(int j=0;j<n;j++) {
				    		Note note=(Note)mult[j].clone();
				    		chord.add(note);
				    	}
				    	comp.add(chord);
				    	i+=n;
				    }
			    }
			    else {
				    comp.add(sym);
				    i++;
			    }
		    }
		    else {
		    	comp.add(vec.get(i).getFirst());
		    	i++;
		    }
		}
		
	}
	public Composition getComp() {
		if(status==2) {
			return comp;
		}
		return null;
	}
	public int status() {
		return status;
	}
	public int size() {
		return vec.size();
	}
	

}
