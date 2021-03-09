package muzika;

public abstract class Symbol implements Cloneable{
    Duration d;
	public Symbol(int x) {
		d = new Duration(x);
	}
	public abstract boolean note();
	public abstract boolean chord();
	public int Trajanje(){
		return d.getDur();
	}
	public void setTrajanje(int x) {
		d.setDur(x);
	}
	public abstract String toString();
	public abstract String noteback();
	protected  Symbol clone() {
		try {
			Symbol sym=(Symbol)super.clone();
			sym.d=(Duration) d.clone();
			return sym;
		}
		catch(CloneNotSupportedException c) {}
		return null;
	}

}
