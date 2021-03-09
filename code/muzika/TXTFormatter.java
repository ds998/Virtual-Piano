package muzika;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TXTFormatter {
	private Composition comp;
	public TXTFormatter(Composition comp) {
		this.comp=comp;
	}
	public void format() {
		String filename=comp.filename;
		String s="";
		int i=0;
		while(filename.charAt(i)!='.') {
			s+=filename.charAt(i);
			i++;
		}
		File file=null;
		FileOutputStream fop=null;
		s+="(formatiran).txt";
		try {

			file = new File(s);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = comp.noteback().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();


		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
