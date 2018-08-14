
import java.util.*;
import java.lang.Math;

public class dataProcessor {
	//HashMap<String,int[]> seqIndex= new HashMap<String,int[]>();
	Map<String,Integer> seqIndex= new HashMap<String,Integer>();

	public List<faUnit> findUnique(List<faUnit> seqs) {

                List<faUnit> uniques = new ArrayList<faUnit>();
                Iterator<faUnit> it = seqs.iterator();

                while(it.hasNext()) {
                        faUnit nextUnit = it.next();
                        String faName = nextUnit.getName();
                        String faSeq = nextUnit.getSeq();
                        boolean find = false;
                        int unitIndex = 0;
                        if (!uniques.isEmpty()){
                                for (int i = 0;i < uniques.size();i++) {
                                        if (faSeq.equals(uniques.get(i).getSeq())) {
                                                find = true;
                                                String updatedName = uniques.get(i).getName()+";"+faName;
                                                faUnit updatedFA = new faUnit(updatedName,faSeq);
                                                uniques.set(i, updatedFA);
                                                unitIndex = i;
                                                break;
                                        }
                                }
                        }
                        if (find == false) {
                                faUnit newUnit = new faUnit(faName,faSeq);
                                uniques.add(newUnit);
                                unitIndex = uniques.indexOf(newUnit);
                        }
                        seqIndex.put(faName, unitIndex);
                }

                return (uniques);
        }


	public List<faUnit> findUniqueWithHash(List<faUnit> seqs) {
                List<faUnit> uniques = new ArrayList<faUnit>();
                Iterator<faUnit> it = seqs.iterator();
                HashMap<String,Integer> uniquesRec = new HashMap<String,Integer>();
                int unitIndex = 0;
                int seqToUnit;
                while(it.hasNext()) {
                        faUnit nextUnit = it.next();
                        String faName = nextUnit.getName();
                        String faSeq = nextUnit.getSeq();
                        if (uniquesRec.get(faSeq) == null) {
                                unitIndex = unitIndex+1;
                                String uniqueName = ">Uni_"+unitIndex;
                                uniquesRec.put(faSeq,unitIndex);
                                faUnit newUnit = new faUnit(uniqueName,faSeq);
                                uniques.add(newUnit);
                                seqToUnit = unitIndex;
                        }
                        else {
                                seqToUnit = uniquesRec.get(faSeq);
                        }
                        seqIndex.put(faName,seqToUnit);


                }

                return (uniques);
        }


	
	//for calculating Hamming distance
	public int calHam (String seqA, String seqB) {
		int hamDist = 0;
		int lenA = seqA.length();
		int lenB = seqB.length();
		
		if (lenA == lenB) {
			for (int i = 0;i < lenA; i++) {
				String nucleoA = seqA.substring(i,i+1);
				String nucleoB = seqB.substring(i,i+1);
				if (!nucleoA.equals(nucleoB)) {
					hamDist++;
					//System.out.println(nucleoA+"\t"+nucleoB+"\t"+hamDist);
				}
			}
		}
		else {
			hamDist = 10000;
		}
		
		return hamDist;
		
	}
	
	//for calculating Leven distance
	public int calLev(String seqA, String seqB) {
		int levDist = 0;
		int lenA = seqA.length();
		int lenB = seqB.length();
		
		int[][] dist = new int[lenA+1][lenB+1];
		//initial first row
		for (int i = 0; i <lenB+1; i++) {
			dist[0][i] = i;
		}
		//initial first column
		for (int i = 0; i <lenA+1; i++) {
			dist[i][0] = i;
		}
		
		for (int i = 1; i <lenA+1; i++) {
			for (int j = 1; j <lenB+1; j++) {
				int leftOr = dist[i][j-1]+1; //add one
				int upOr = dist[i-1][j]+1;  //delete one
				int mutOr = dist[i-1][j-1];
				if (!seqA.substring(i-1,i).equals(seqB.substring(j-1,j))) {
					mutOr++;
				}
				dist[i][j] = Math.min(mutOr, Math.min(leftOr, upOr));
			}
		}
		
		levDist = dist[lenA][lenB];
		return levDist;
	}
	
}
