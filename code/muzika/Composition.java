package muzika;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
public class Composition implements Iterable<Symbol> {
	private Vector<Symbol> comp;
	static Map<Character, Pair<String, Integer>> notes;
	static Map<String, Integer> midinote;
	static Map<String,Character> notekey;
	private boolean tref = true;
    private boolean fchord = false;
    private boolean  chord = false;
    String filename;
    private Chord ch;
    static {
    	notes=new HashMap<Character,Pair<String,Integer>>();
		midinote=new HashMap<String,Integer>();
		notekey=new HashMap<String,Character>();
    	MakeAMap();
    }
    public void Read() {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
			
			stream.forEach((String s)->{this.ReadIn(s);});

		} catch (IOException e) {
			e.printStackTrace();
		}
        comp.remove(comp.size()-1);
    }
    public Iterator<Symbol> iterator(){
		return comp.iterator();
	}
    public void ReadIn(String s) {
    	String pattern="([^ ]*)";
    	Pattern r=Pattern.compile(pattern);
		Matcher result=r.matcher(s);
    	while(result.find()) {
		    String lineName = result.group(1);
		    if (lineName.length() == 0) { tref = false; }
		    else if (lineName.length() == 1) {
			    if (lineName.charAt(0) == '|') {
				    comp.add(new Pause(2));
				    tref = false;
			    }
			    else {
				    if (fchord) {
					    comp.add(new Note(1, notes.get(lineName.charAt(0)).getFirst()));
					    tref = false;
				    }
				    else {
					    comp.add(new Note(2, notes.get(lineName.charAt(0)).getFirst()));
					    tref = true;
				    }
				
			    }
		    }
		    else {
			    for (int i = 0; i < lineName.length(); i++) {
				    if (lineName.charAt(i) == '[') {
					    if (i==lineName.length()-2) {
						    fchord = true;
						    comp.add(new Note(1, notes.get(lineName.charAt(i+1)).getFirst()));
						    tref = false;
						    break;
						
					    }
					    else {
						    ch = new Chord(2);
						    chord = true;
						    tref = false;
					    }
				    }
				    else if (lineName.charAt(i) == ']') {
					    if (i==1) {
						    fchord = false;
						    if (lineName.length() == 2) {
							     tref = true;
						    }
					    }
					    else {
					    	Chord nh = new Chord(ch.Trajanje());
					    	int nom=0;
					    	for (Note n : ch) {
								nh.add(new Note(n.Trajanje(),n.toString()));
								nom++;
							}
					    	if(nom==1) {
					    		for(Note n:ch) {
					    			comp.add(new Note(1,n.toString()));
					    		}
					    	}
					    	else comp.add(nh);
						    chord = false;
						    ch = null;
						    if (i == lineName.length() - 1) {
							    tref = true;
						    }
					    }
				    }
				    else {
					    if (chord) {
						    ch.add(new Note(2, notes.get(lineName.charAt(i)).getFirst()));
					    }
					    else if (fchord) {
						    comp.add(new Note(1, notes.get(lineName.charAt(i)).getFirst()));
					    }
					    else {
						    comp.add(new Note(2, notes.get(lineName.charAt(i)).getFirst()));
					    }
					    if (i == lineName.length() - 1) {
						    tref = true;
					    }
				   }
			   }
		    }
		    if (tref) {
			    comp.add(new Pause(1));
			    tref = false;
		    }
    	}
	    
    }
	public static void putmap(String s) {
		String pattern="([^,]*),([^,]*),([^,]*).*";
		Pattern r=Pattern.compile(pattern);
		Matcher result=r.matcher(s);
		if (result.find()) {
			char t=result.group(1).charAt(0);
			String note = result.group(2);
			int tef = Integer.parseInt(result.group(3));
			notes.put(t,new Pair<String,Integer>(note, tef));
			midinote.put(note,tef);
			notekey.put(note, t);
		}
	}
	public static void MakeAMap() {
		String fileName = "map.csv";

		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			
			stream.filter(s->s.length()>0).forEach((String s)->{putmap(s);});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Composition(String filename) {
		this.filename=filename;
		comp=new Vector<Symbol>();
		Read();
	}
	public Composition() {
		comp=new Vector<Symbol>();
		this.filename="Snimak"+(long)(5000*Math.random());
	}
	public void add(Symbol s) {
		comp.add(s);
	}
	public int getsize() {
		return comp.size();
	}
	public Symbol getSym(int i) {
		return comp.get(i);
	}
	public void setfilename(String s) {
		filename=s;
	}
	public String noteback() {
		int i=0;
		StringBuilder s=new StringBuilder();
		while(i<comp.size()) {
			if(comp.get(i).note() && comp.get(i).Trajanje()==1) {
				int index=i+1;
				int n=1;
				while(index<comp.size() &&comp.get(index).note() && comp.get(i).Trajanje()==1) {
					index++;
					n++;
				}
				s.append("[");
				int j;
				for(j=i;j<i+n;j++) {
					if(j!=i)s.append(" ");
					s.append(comp.get(i).noteback());
				}
				s.append("]");
				i=j;
			}
			else {
				s.append(comp.get(i).noteback());
				i++;
			}
		}
		return s.toString();
	}
	/*public static void main(String[] args) {
		Composition compos=new Composition("////vmware-host//Shared Folders//Desktop//jingle_bells.txt",3,2);
		System.out.print(compos);
		
	}
	*/
}
