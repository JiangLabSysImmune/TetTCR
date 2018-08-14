//this is for reading in the distance matrix (mix? org?) and do the RCM ordering

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


public class runRCM {
        public static void main(String[] args) throws IOException{
                String listFile = args[0];
		int clusThresh = Integer.parseInt(args[1]);
                 try {
                        BufferedReader listReader = new BufferedReader(new FileReader(new File(listFile)));
                        String temp;
                        while ((temp = listReader.readLine()) != null) {
                                //String[] cut = temp.split("\t");
                                //System.out.println(cut.length);
                                //String matFile = temp;
                                temp = temp.split("\t")[0];
				int len = temp.length();
                                //String ind = temp.substring(0,6);
                                //String tp = temp.substring(7,8);
				String bc = temp.substring(0,len-6);
				String matFile = "./disRe/"+bc+"_org.dist";
				File rcmFile = new File("./levRCM/"+bc+"_rcm.res");
				System.out.println(bc);

 				//System.out.println("reading matrix start");
				//printCurrentTime();	
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
						if (cutInt <= clusThresh) {
							cutInt = 1;
						}
						else {
							cutInt = 0;
						}
						disMat[lineNum][i] = cutInt;;
					}
				}
				matReader2.close();
				
				//System.out.println("RCM start");
				//printCurrentTime();

				int[] rcmRe = new int[]{1};
				//System.out.println("==================");
				//System.out.println(disMat.length);
				if (disMat.length > 1) { 
					RcmOrder rcm = new RcmOrder();
					rcmRe = rcm.ordering(disMat);
					//rcmRe = new int[]{1};
				
				}
				else if (disMat.length == 1){
					rcmRe = new int[]{1};
				}
				//else if (disMat.length == 2) {
				//	rcmRe = new int[]{1,2};
				//}
				//output
				//System.out.println(matFile);
				//for (int i = 0;i < rcmRe.length;i++) {
				//	System.out.print(rcmRe[i]+"\t");
				//}
				//System.out.print("\n");
				//System.out.println("RCM finished");
				//printCurrentTime();
				
				FileWriter writerRCM = new FileWriter(rcmFile);
				BufferedWriter bwRCM = new BufferedWriter(writerRCM);
				for (int i = 0;i < rcmRe.length;i++) {
					bwRCM.write(rcmRe[i]+"\t");
				}
				//bwRCM.write(rcmRe[rcmRe.length-1]);
				bwRCM.flush();
				bwRCM.close();
				writerRCM.close();




                        }
                        listReader.close();
                }catch(Exception e){
                        e.printStackTrace();
                }


        }

        private static void printCurrentTime() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(df.format(new Date()));
        }







}


