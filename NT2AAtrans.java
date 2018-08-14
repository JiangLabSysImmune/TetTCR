//for translate nucleotide sequence into Amino Acid sequence
//
import java.util.HashMap;
import java.math.*;

public class NT2AAtrans {
	HashMap<String,String> ref = new HashMap<String,String>();
	
	public NT2AAtrans() {
		ref = iniRef(ref);
	}

	private static HashMap<String,String> iniRef(HashMap<String,String> ref) {
		ref.put("ATT","I");
	        ref.put("ATC","I");
	        ref.put("ATA","I");
	
	        ref.put("CTT","L");
	        ref.put("CTC","L");
	        ref.put("CTA","L");
	        ref.put("CTG","L");
	        ref.put("TTA","L");
	        ref.put("TTG","L");

        	ref.put("GTT","V");
        	ref.put("GTC","V");
        	ref.put("GTA","V");
        	ref.put("GTG","V");

     		ref.put("TTT","F");
       		ref.put("TTC","F");

       		ref.put("ATG","M");

        	ref.put("TGT","C");
        	ref.put("TGC","C");

        	ref.put("GCT","A");
        	ref.put("GCC","A");
        	ref.put("GCA","A");
        	ref.put("GCG","A");
	
        	ref.put("GGA","G");
        	ref.put("GGC","G");
        	ref.put("GGG","G");
        	ref.put("GGT","G");
	
	        ref.put("CCA","P");
	        ref.put("CCC","P");
	        ref.put("CCG","P");
	        ref.put("CCT","P");
	
	        ref.put("ACA","T");
	        ref.put("ACC","T");
	        ref.put("ACG","T");
	        ref.put("ACT","T");
	
	        ref.put("TCA","S");
	        ref.put("TCC","S");
	        ref.put("TCG","S");
	        ref.put("TCT","S");
	        //ref.put("AGA","S");
	        ref.put("AGC","S");
	        //ref.put("AGG","S");
	        ref.put("AGT","S");

	        ref.put("TAT","Y");
	        ref.put("TAC","Y");
	
	        ref.put("TGG","W");
	
	        ref.put("CAA","Q");
	        ref.put("CAG","Q");
	
	        ref.put("AAT","N");
	        ref.put("AAC","N");
	
	        ref.put("CAT","H");
	        ref.put("CAC","H");
	
	        ref.put("GAA","E");
	        ref.put("GAG","E");
	
	        ref.put("GAT","D");
	        ref.put("GAC","D");
	
        	ref.put("AAA","K");
        	ref.put("AAG","K");
	
	        ref.put("CGA","R");
	        ref.put("CGC","R");
	        ref.put("CGG","R");
	        ref.put("CGT","R");
	        ref.put("AGA","R");
	        ref.put("AGG","R");
	
	        ref.put("TAA","O");
	        ref.put("TAG","O");
	        ref.put("TGA","O");

		return ref;
	}


	public String translate(String nt) {
		String aaSeq = "";
		nt = nt.toUpperCase();
		try {
			double len = nt.length();
			//System.out.println(len);
			double ck = len/3;
			//System.out.println(ck);
			if ((int)Math.floor(ck) == ck) {
				while (nt.length() > 0) {
					String tempAA = "-";
					String subNT = nt.substring(0,3);
					nt = nt.substring(3);
					if (ref.get(subNT) != null) {
						tempAA = ref.get(subNT);
					}
					aaSeq = aaSeq+tempAA;
			
				}
			}
			else {
				System.out.println("the Nucleotide sequence length is incorrect");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
			return aaSeq;
	}
}
