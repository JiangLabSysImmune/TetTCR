import java.io.*;
import java.util.*;

public class trans {
	public static void main(String[] args) throws IOException{
		//String ntSeq = "ATTATTATcaggatta";
		String ntFile = args[0];
		//String ntSeq = args[0];

		int ntStart = Integer.parseInt(args[1]);
		int ntEnd = Integer.parseInt(args[2]);

                readFasta ntReader = new readFasta();
                ntReader.readFastaFast(new File(ntFile));
                List<faUnit> ntList = ntReader.getSeqs();		

		Iterator<faUnit> ntIt = ntList.iterator();
		while (ntIt.hasNext()) {
			faUnit ntUnit = ntIt.next();
			String ntName = ntUnit.getName();
			String ntSeq = ntUnit.getSeq();
			String subSeq = ntSeq.substring(ntStart,ntEnd);
			//System.out.println(subSeq);
			NT2AAtrans trans = new NT2AAtrans();
			String aaSeq = trans.translate(subSeq);
			System.out.println(">"+ntName);
			System.out.println(aaSeq);
		}


		//NT2AAtrans trans = new NT2AAtrans();
		//String aa = trans.translate(ntSeq);
		//System.out.println (aa);
	}





}
