import java.io.*;
import java.util.*;

public class runGroup {
	public static void main(String[] args) throws IOException {
		String listFile = args[0];
		int clusThresh = Integer.parseInt(args[1]);

		//System.out.println(listFile);
		BufferedReader listReader = new BufferedReader(new FileReader(new File(listFile)));
		String temp;
                while ((temp = listReader.readLine()) != null) {
			temp = temp.split("\t")[0];
			int len = temp.length();
			//String tp = temp.substring(7,8);
			//String inde = temp.substring(0,6);
			String bc = temp.substring(0,len-6);
			System.out.println(bc);
			String matFile = "./disRe/"+bc+"_org.dist";
			String rcmFile = "./levRCM/"+bc+"_rcm.res";
			String fasFile = "./bc_groups/"+bc+".fasta";

			String grpFile = "./levGroup/"+bc+"_group.res";
			BufferedReader matReader1 = new BufferedReader(new FileReader(matFile));
                        String matTemp1;
                        int lineCount = 0;
                        int matSize = 1;
                        while((matTemp1 = matReader1.readLine()) != null) {
                              lineCount++;
                              if (lineCount == 1) {
                                     String[] cut = matTemp1.split("\t");
                                     matSize = cut.length;
                              }
                        }
                        matReader1.close();

			int[][] disMat = new int[matSize][matSize];			
			BufferedReader matReader2 = new BufferedReader(new FileReader(matFile));
			int lineNum = -1;
			String matTemp2;
  	                 while((matTemp2 = matReader2.readLine()) != null) {
                                        lineNum++;
                                        String[] cut = matTemp2.split("\t");
                                        for (int i = 0;i < cut.length;i++) {
                                                int cutInt = Integer.parseInt(cut[i]);
                                                disMat[lineNum][i] = cutInt;;
                                        }
                          }
                         matReader2.close();
			
			//read in RCM results;
			BufferedReader rcmReader = new BufferedReader(new FileReader(rcmFile));
			String rcmString = "";
			String rcmTemp;
			while ((rcmTemp = rcmReader.readLine()) != null) {
				rcmString = rcmString+rcmTemp;
			}
			rcmReader.close();
			String[] cts = rcmString.split("\t");
			int[] rcmRe = new int[cts.length];
			for (int i = 0;i <cts.length;i++) {
				rcmRe[i] = Integer.parseInt(cts[i]);
			}

			HashMap<Integer,Integer> rcmRc = new HashMap<Integer,Integer>();
			int[][] afRcm = new int[rcmRe.length][rcmRe.length];
			for (int i = 0;i < rcmRe.length;i++) {
				for (int j = 0;j <rcmRe.length;j++) {
					afRcm[i][j] = disMat[rcmRe[i]-1][rcmRe[j]-1];
					//System.out.println(afRcm[i][j]);
				}
				rcmRc.put(i,rcmRe[i]-1);
			}

			//run RCMgroup
			RcmGroup grouper = new RcmGroup();
			grouper.clusThresh = clusThresh;
			ArrayList<int[]> subClust = grouper.clust(afRcm);
			
			//read in sequence,name and index info
			HashMap<Integer,String> faInd = new HashMap<Integer,String>();
			HashMap<String,String> faSeq = new HashMap<String,String>();
			int ind = 0;
			String curName = "";

			BufferedReader seqReader = new BufferedReader(new FileReader(fasFile));

			String seqTemp;
			while ((seqTemp = seqReader.readLine()) != null && seqTemp.length() >0 ) {
				seqTemp = seqTemp.trim();
				String head = seqTemp.substring(0,1);
				if (head.equals(">")) {
					curName = seqTemp.substring(1);
					faInd.put(ind,curName);
					ind++;
				}
				else {
					faSeq.put(curName,seqTemp);
				}
			}
			seqReader.close();


			//transform group results
			FileWriter writer = new FileWriter(grpFile);
			BufferedWriter bw = new BufferedWriter(writer); 
			int gpID = 0;

			for (int[] re:subClust) {
				gpID++;
				for (int i = 0;i < re.length;i++) {
					int p = re[i];
					int orId = rcmRc.get(p);
					String orName = faInd.get(orId);
					String orSeq = faSeq.get(orName);
					bw.write(orName+"\t"+gpID);
					bw.newLine();
				}
			}
		


			bw.flush();
			bw.close();
			writer.close();

		}




	}
}
