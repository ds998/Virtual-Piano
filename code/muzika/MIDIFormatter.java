package muzika;

import java.io.*;
import java.util.*;
import javax.sound.midi.*; // package for all midi classes
public class MIDIFormatter{
	private Vector<Integer> comp;
	private Vector<Integer> rhythm;
	private Vector<Boolean> chord;
	private String filename;
	private String name;
	public MIDIFormatter(Composition compos) {
		this.name="";
		for(int i=0;i<compos.filename.length();i++) {
			if(compos.filename.charAt(i)=='.') break;
			this.name+=compos.filename.charAt(i);
		}
		this.filename=this.name+"(formatiran).midi";
		comp=new Vector<Integer>();
		rhythm=new Vector<Integer>();
		chord=new Vector<Boolean>();
		for(Symbol s:compos) {
			if(s.note()) {
				Note nx=(Note)s.clone();
				comp.add(Composition.midinote.get(nx.toString()));
				rhythm.add(nx.Trajanje());
				chord.add(false);
			}
			else if(s.chord()) {
				Chord ch=(Chord)s.clone();
				for(Note n:ch) {
					comp.add(Composition.midinote.get(n.toString()));
					rhythm.add(ch.Trajanje());
					chord.add(true);
				}
			}
			else {
				comp.add(-1);
				rhythm.add(s.Trajanje());
				chord.add(false);
			}
		}
		for (int i = 0; i < chord.size()-1; i++) {
			if (chord.get(i) && !chord.get(i+1)) chord.set(i, false);
		}
		
	}
	public void format() {
		try {
			Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,48);
			Track t=s.createTrack();
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t.add(me);
			MetaMessage mt = new MetaMessage();
	        byte[] bt = {0x02, (byte)0x00, 0x00};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t.add(me);
			mt = new MetaMessage();
			String TrackName = new String(name);
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t.add(me);
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
			mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7F,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
			mm = new ShortMessage();
			mm.setMessage(0xC0, 0x00, 0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
			long actiontime=0;
			int tpq=48;
			for(int i=0;i<comp.size();i++) {
				if(comp.get(i)<0) {actiontime+=tpq/2*rhythm.get(i);}
				else {
					mm=new ShortMessage();
					mm.setMessage(0x90,comp.get(i),0x60);
					me=new MidiEvent(mm,actiontime);
					t.add(me);
					actiontime+=tpq/2*rhythm.get(i);
					mm= new ShortMessage();
					mm.setMessage(0x80,comp.get(i),0x40);
					me = new MidiEvent(mm,actiontime);
					t.add(me);
					if (chord.get(i))actiontime -= tpq / 2 * rhythm.get(i);
				}
			}
			mt = new MetaMessage();
	        byte[] bet = {}; // empty array
			mt.setMessage(0x2F,bet,0);
			me = new MidiEvent(mt, actiontime);
			t.add(me);
			File f = new File(filename);
			MidiSystem.write(s,1,f);
			
		}
		catch(Exception e)
		{
			System.out.println("Exception caught " + e.toString());
		} 
	}
	
  
}
