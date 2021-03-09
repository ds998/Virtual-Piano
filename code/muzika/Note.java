package muzika;

public class Note extends Symbol {
	private int octave;
	private boolean height;
	private char letter;
	public Note(int x, String s){
		super(x);
		letter = s.charAt(0);
		if (s.length() < 3) height = false;
		else height = true;
		if (height) {
			octave = s.charAt(2)-'0';
		}
		else {
			octave = s.charAt(1)-'0';
		}
	}
	public int getOct() { return octave; }
	public void setOct(int x) { octave = x; }
	public char getLetter(){ return letter; }
	public void setLetter(char c) { letter = c; }
	public boolean getHeight(){ return height; }
	public void setHeight(boolean h) { height = h; }
	public boolean chord() { return false; }
	public boolean note() { return true; }
	public String toString() {
		String s = "";
		s += letter;
		s += (height ? "#" : "");
		s+=octave;
		return s;
	}
	public String noteback() {
		return ""+Composition.notekey.get(toString());
	}
	public Note clone() {
		Note n=(Note)super.clone();
		return n;
	}
};

