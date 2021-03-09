package muzika;

public class Duration implements Cloneable{
	private int x;
	public Duration(int f) {
			x = f;
	}
	int getDur()  { return x; }
	void setDur(int xf) { x = xf; }
	public Duration clone() {
		try {
			Duration d=(Duration) super.clone();
			return d;
		}
		catch(CloneNotSupportedException c) {}
		return null;
	}

}
