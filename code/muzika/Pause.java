package muzika;

public class Pause extends Symbol {
    public Pause(int x){super(x);}
	public boolean chord() { return false; }
	public boolean note() { return false; }
	public String toString() {
		if (Trajanje() == 1) {
			return  "o";
		}
		return "x";
	}
	public String noteback() {
		if(Trajanje()==1) return " ";
		else return " | ";
	}
	public Pause clone() {
		Pause p=(Pause)super.clone();
		return p;
	}

}
