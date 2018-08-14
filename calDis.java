import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class calDis {
	int clusThresh;
	public void calculate (String bc) throws IOException{
		//output file
		//output should include following matrix:
		//- matrix for hamming distance
		//- matrix for lev distance, with hamming ones to be 'NA'
		//- matrix mix hamming and lev distance together
		//- mixed matrix with all sequences, not just unique sequences
		System.out.println(bc);
		File uniqueFile = new File("./disRe/"+bc+"_unique.fasta");
		File hamFile = new File("./disRe/"+bc+"_ham.dist");
		File levFile = new File("./disRe/"+bc+"_lev.dist");
		File mixFile = new File("./disRe/"+bc+"_mix.dist");
		File orgFile = new File("./disRe/"+bc+"_org.dist");
		File seqIndexFile = new File("./disRe/"+"."+bc+"_seq.index");
		
		String divLine = "\r\n";
		String divItem = "\t";
		
		//read in FASTA file into a 'list'
		File fastaFile = new File("./bc_groups/"+bc+".fasta");
		readFasta fastaReader = new readFasta();
		fastaReader.readFastaFast(fastaFile);
		List<faUnit> faList = fastaReader.getSeqs();
		//System.out.println("reading sequences finished");
		
		//for filtering to unique sequences
                System.out.println("filtering start");
                printCurrentTime();
		dataProcessor dataProcessor = new dataProcessor();
		List<faUnit> uniqueList = dataProcessor.findUniqueWithHash(faList);
		//System.out.println("filtering to unique sequences finished");
		//printCurrentTime();		

		//write out unique sequences
		FileWriter writerU = new FileWriter(uniqueFile);
		BufferedWriter bwU = new BufferedWriter(writerU);
		Iterator<faUnit> itU = uniqueList.iterator();
		while(itU.hasNext()) {
			faUnit Unit = itU.next();
			String unitName = Unit.getName();
			String unitSeq = Unit.getSeq();
                        bwU.write (unitName);
                        bwU.newLine();
                        bwU.write(unitSeq);
                        bwU.newLine();
		}
		bwU.flush();
		bwU.close();
		writerU.close();
		
		//for seq index
                Map<String,Integer> seqIndex = new HashMap<String,Integer>();
                seqIndex = dataProcessor.seqIndex;
                FileWriter writerI = new FileWriter(seqIndexFile);
                BufferedWriter bwI = new BufferedWriter(writerI);
                for (String orgSeq:seqIndex.keySet()) {
                        int orgIndex = seqIndex.get(orgSeq);
                        bwI.write(orgSeq+"\t"+orgIndex);
                        bwI.newLine();
                }
                bwI.flush();
                bwI.close();
                writerI.close();
		
		
		//for calculating Hamming distance
		int uniqueNum = uniqueList.size();
		int[][] dist_matrix = new int[uniqueNum][uniqueNum]; //distance matrix
		for (int i = 0; i < uniqueNum;i++) {
			for (int j = i+1; j < uniqueNum; j++) {
				String seqA = uniqueList.get(i).getSeq();
				String seqB = uniqueList.get(j).getSeq();
				int hamDist = dataProcessor.calHam(seqA, seqB);
				dist_matrix[i][j] = hamDist;
				dist_matrix[j][i] = hamDist;
			}
			dist_matrix[i][i] = 0;
		}
		
		//System.out.println("calculating ham distance finished");
		//printCurrentTime();		

		//write out hamming distance matrix
		FileWriter writer1 = new FileWriter(hamFile);
		BufferedWriter bw1 = new BufferedWriter(writer1);
		for (int i = 0;i<uniqueNum;i++) {
			String hamLine = "";
			for (int j = 0;j < uniqueNum;j++) {
				hamLine = hamLine+dist_matrix[i][j]+divItem;
			}
			bw1.write(hamLine);
			bw1.newLine();
		}
		bw1.flush();
		bw1.close();
		writer1.close();

	
		//for calculating levenstein distance
		int[][] dist_matrix_lev = new int[dist_matrix.length][];
		//int[][] dist_mix = dist_matrix;
		int[][] dist_mix = new int[dist_matrix.length][];
		for (int i=0;i<dist_matrix.length;i++) {
			dist_matrix_lev[i] = dist_matrix[i].clone();
			dist_mix[i] = dist_matrix[i].clone();
		}
		
		for (int i = 0; i <uniqueNum;i++) {
			for (int j = i+1; j< uniqueNum;j++) {
				if (dist_matrix[i][j] >clusThresh) {
					String seqA = uniqueList.get(i).getSeq();
					String seqB = uniqueList.get(j).getSeq();
					int levDist = dataProcessor.calLev(seqA, seqB);
					dist_matrix_lev[i][j] = levDist;
					dist_matrix_lev[j][i] = levDist;
					dist_mix[i][j] = levDist;
					dist_mix[j][i] = levDist;
				}
				else {
					dist_matrix_lev[i][j] = -1;
					dist_matrix_lev[j][i] = -1;
				}
			}
		}

		//write out the levenstein distance matrix	
		FileWriter writer2 = new FileWriter(levFile);
		BufferedWriter bw2 = new BufferedWriter(writer2);
		for (int i = 0;i<uniqueNum;i++) {
			String levLine = "";
			for (int j = 0;j < uniqueNum;j++) {
				levLine = levLine+dist_matrix_lev[i][j]+divItem;
			}
			bw2.write(levLine);
			bw2.newLine();
		}
		bw2.flush();
		bw2.close();
		writer2.close();


		//write out mix distance matrix
	
		FileWriter writer3 = new FileWriter(mixFile);
		BufferedWriter bw3 = new BufferedWriter(writer3);
		for (int i = 0;i<uniqueNum;i++) {
			String mixLine = "";
			for (int j = 0;j < uniqueNum;j++) {
				mixLine = mixLine+dist_mix[i][j]+divItem;
			}
			bw3.write(mixLine);
			bw3.newLine();
		}
		bw3.flush();
		bw3.close();
		writer3.close();

		//write out distance matrix of original sequences - write out from uniqueList & dist_mix
		Iterator<faUnit> it2 = faList.iterator();
		String[] orgSeqList = new String[faList.size()];
		int count = 0;
		while(it2.hasNext()) {
			String seqName = it2.next().getName();
			orgSeqList[count] = seqName;
			count++;
		}
		int[][] dist_org = new int[orgSeqList.length][orgSeqList.length];
		
                for (int i = 0;i < orgSeqList.length;i++) {
                        for (int j = i+1;j < orgSeqList.length;j++) {
                                String iName = orgSeqList[i];
                                String jName = orgSeqList[j];
                                int iIndex = seqIndex.get(iName)-1;
                                int jIndex = seqIndex.get(jName)-1;
                                dist_org[i][j] = dist_mix[iIndex][jIndex];
                        }
                }
		//System.out.println("generating orgin matrix finished");
		//printCurrentTime();		


		//write out origin matrix
                FileWriter writer4 = new FileWriter(orgFile);
                BufferedWriter bw4 = new BufferedWriter(writer4);
                if (orgSeqList.length > 1) {
                        for (int i = 0;i<orgSeqList.length;i++) {
                                for (int j = 0;j<orgSeqList.length-1;j++) {
                                        String tempDis = "";
                                        if (i == j) {
                                                tempDis = "0";
                                        }
                                        else if (i > j) {
                                                tempDis = String.valueOf(dist_org[j][i]);
                                        }
                                        else if (i < j) {
                                                tempDis = String.valueOf(dist_org[i][j]);
                                        }
                                        bw4.write(tempDis);
                                        bw4.write(divItem);
                                }
                                String finalDis = "";
                                if (i < orgSeqList.length-1) {
                                        finalDis = String.valueOf(dist_org[i][dist_org.length-1]);
                                }
                                else if (i == orgSeqList.length-1) {
                                        finalDis = "0";
                                }
                                bw4.write(finalDis);
                                bw4.newLine();
                        }
                }
                else {
                        bw4.write("0");
                }

		bw4.flush();
		bw4.close();
		writer4.close();

		//System.out.println("origin matrix finished");
		//printCurrentTime();
	}

	private static void writeRe (File outputFile, String content) {
		try {
			FileWriter fileWriter = new FileWriter(outputFile);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private static void printCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(new Date()));
	}
}

