

import java.io.*;
import java.util.*;

public class readFasta {
	List<faUnit> seqs = new ArrayList<faUnit>();
	//for using small mem to read FASTA- still need modification to be right - not using it here
	public void readFasta_small_mem(File inputFile) throws IOException{
		try{
			Reader reader = new InputStreamReader(new FileInputStream(inputFile));
			
			String faName = "";
			String faSeq = "";
			
			int tempChar;
			int lastChar = 0;
			while((tempChar = reader.read()) != -1){
				if (((char) tempChar) == '>') {
					while((tempChar = reader.read()) != -1) {
						if ((char)tempChar != '\r' && (char)tempChar != '\n') {
							faName = faName+(char)tempChar;
						}
						else {
							lastChar = tempChar;
							break;
						}
					}
				}
				else{
					if ((char)lastChar == '>'){
						lastChar = 0;
						while((tempChar = reader.read()) != -1) {
							if ((char)tempChar != '\r' && (char)tempChar != '\n') {
								faName = faName+(char)tempChar;
							}
							else {
								lastChar = tempChar;
								break;
							}
						}
					}
					else {
						while((tempChar = reader.read()) != -1) {
							if ((char)tempChar == '\r' || (char)tempChar == '\n') {
								continue;
							}
							else if ((char)tempChar == '>') {
								faUnit tempUnit = new faUnit(faName, faSeq);
								seqs.add(tempUnit);
								faName = "";
								faSeq = "";
								lastChar = tempChar;
								break;
							}
							else {
								faSeq = faSeq+(char)tempChar;
							}
						}
					}
				}
		}
		//record the last one faUnit
		faUnit tempUnit = new faUnit(faName, faSeq);
		seqs.add(tempUnit);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void readUnormal_rb(File inputFile) throws IOException { //too time consuming, not using it
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			String allString = "";
			String tempLine;
			while ((tempLine = reader.readLine()) != null) {
				allString = allString+" "+tempLine.trim();
			}
			//System.out.println(allString);
			String[] cut2group = allString.split(">");
			for (int i = 1;i < cut2group.length;i++) {
				String[] cut2read = cut2group[i].split(" ");
				String bcName = cut2read[0];
				int seqCount = 0;
				for (int j = 1;j < cut2read.length;j++) {
					if (cut2read[j].length() != 0) {
						seqCount++;
						String tempName = ">"+bcName+"_"+seqCount;
						faUnit tempFA = new faUnit(tempName,cut2read[j]);
						seqs.add(tempFA);
					}
				}
			}

		} catch(Exception e){
			e.printStackTrace();
		}


	}


	public void readUnormal(File inputFile) throws IOException { 
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			String bcName = "";
			String tempLine;
			int readCount = 0;
			while ((tempLine = reader.readLine()) != null) {
				String tempHead = tempLine.substring(0,1);
				//System.out.println(tempLine);
				if (tempHead.equals(">")) {
					bcName = tempLine.trim();
					readCount = 0;
				}
				else if (Character.isLetter((char)tempHead.getBytes()[0])) {
					readCount++;
					String tempName = bcName+"_"+readCount;
					faUnit tempFA = new faUnit(tempName,tempLine);
					seqs.add(tempFA);
				}

			}
		} catch(Exception e){
			e.printStackTrace();
		}


	}


	
	public void readFastaSlow(File inputFile) throws IOException{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String allString = "";
			String tempLine;
			while ((tempLine = reader.readLine()) != null) {
				String tempHead = tempLine.substring(0,1);
				//System.out.println(tempLine);
				if (tempHead.equals(">")) {
					tempLine = tempLine.trim()+";";
				}
				allString = allString+tempLine;
			}
			//System.out.println(allString);
			//cut - first to faUnit, then to name/seq
			String[] cut1 = allString.split(">");
			for (int i = 0;i < cut1.length;i++) {
				if (cut1[i].length() != 0) {
					String[] cut2 = cut1[i].split(";");
					faUnit tempFA = new faUnit(cut2[0],cut2[1]);
					//System.out.println(cut2[1]);
					seqs.add(tempFA);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void readFastaFast(File inputFile) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String tempLine;
			String curName = "";

			while ((tempLine = reader.readLine()) != null && tempLine.length() > 0) {
				tempLine = tempLine.trim();
				String tempHead = tempLine.substring(0,1);
                                if (tempHead.equals(">")) {
					curName = tempLine.substring(1);
                                }
				else if ((isLetter(tempHead))||(tempHead.equals("-"))) {	
					faUnit tempFA = new faUnit(curName,tempLine);
					seqs.add(tempFA);
					//System.out.println(curName+"\n"+tempLine);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	


	public static boolean isLetter(String str)

	{ 
          java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z]+"); 
          java.util.regex.Matcher m = pattern.matcher(str);
          return m.matches();     
	 }
	
	public List<faUnit> getSeqs() {
		return seqs;
	}

}
