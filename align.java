
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.lang.Math;

public class align {
	public static void main(String[] args) throws IOException{
		String refFile = args[0];
		//String refFile = "/home/ch38988/Projects/T_cell_subgroup/tCell_subgroup/data/TCR_check/GACATGATATTT.fasta";
		String queryFile = args[1];
		//String queryFile = "/home/ch38988/Projects/T_cell_subgroup/tCell_subgroup/data/TCR_check/VC.combine";
		//System.out.println("REF: "+refFile);
		//System.out.println("QR: "+queryFile);
		try {
			readFasta refReader = new readFasta();
			refReader.readFastaFast(new File(refFile));
			List<faUnit> refList = refReader.getSeqs();
			
			readFasta queryReader = new readFasta();
			queryReader.readFastaFast(new File(queryFile));
			List<faUnit> queryList = queryReader.getSeqs();
			
			dataProcessor dataProcessor = new dataProcessor();

			Iterator<faUnit> refIt = refList.iterator();
			while(refIt.hasNext()) {
				int bestAlignSt = -1;
				int bestAlignEd = -1;
				int bestAlignMs = 1000;
				String bestAlignQr = "";
				faUnit refUnit = refIt.next();
				String refName = refUnit.getName();
				String refSeq = refUnit.getSeq();
				refSeq = refSeq.toUpperCase();
				int refLen = refSeq.length();
//				System.out.println(refName+"\t"+refSeq);

				Iterator<faUnit> queryIt = queryList.iterator();
			//	System.out.println(queryList.size()+"\n");
				while(queryIt.hasNext()) {
					faUnit queryUnit = queryIt.next();
					String queryName = queryUnit.getName();
				
					String querySeq = queryUnit.getSeq();
					//querySeq = querySeq+refName.substring(0,12);
				
					querySeq = querySeq.toUpperCase();
					//System.out.println(refName+"\n"+querySeq+"\n");

					int queryLen = querySeq.length();
					for (int i = 0;i <= refLen-queryLen;i++) {
						String subRef = refSeq.substring(i,i+queryLen);
						int levDist = dataProcessor.calLev(subRef, querySeq);				

						if (levDist < bestAlignMs) {
							bestAlignSt = i;
							bestAlignEd = i+queryLen;
							bestAlignQr = queryName;
							bestAlignMs = levDist;
						}
					}
				}
				System.out.println(">"+refName+"_VS_"+bestAlignQr+":"+"\t"+bestAlignSt+"\t"+bestAlignEd+"\t"+bestAlignMs);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
        }
}
















