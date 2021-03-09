package muzika;
import java.util.*;
public class Chord extends Symbol implements Iterable<Note> {
	private Vector<Note> vec;
	public Chord(int x) {
		super(x);
		vec=new Vector<Note>();
	}
	public void add(Note n) {
		vec.add(n);
	}
	public Iterator<Note> iterator(){
		return vec.iterator();
    }
	public boolean chord() { return true; }
	public boolean note() { return false; }
	public String toString() {
		StringBuilder s=new StringBuilder();
		for (int i = 0; i < vec.size(); i++) {
			s.append(vec.elementAt(i).toString());
			if (i != vec.size() - 1) s.append("/");

		}
		return s.toString();

	}
	public String noteback() {
		StringBuilder s=new StringBuilder();
		s.append("[");
		for (int i = 0; i < vec.size(); i++) {
			s.append(vec.elementAt(i).noteback());

		}
		return s.append("]").toString();
	}
	@SuppressWarnings("unchecked")
	public Chord clone() {
			Chord ch=(Chord)super.clone();
			ch.vec=(Vector<Note>) vec.clone();
			ch.vec.clear();
			for(int i=0;i<vec.size();i++) {
				ch.vec.add((Note)(vec.get(i).clone()));
			}
			return ch;
	}
};

