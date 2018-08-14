
import java.util.*;

public class RcmOrder {
	
	public int[] ordering(int[][] orgMat){
		ArrayList<Integer> serList = new ArrayList<Integer>();
	
		//record and initialize the degree info
		HashMap<Integer,Integer> dgRec = new HashMap<Integer,Integer>();
		int[] sumRec = matSum(orgMat);
		for (int i = 0;i <sumRec.length;i++) {
			dgRec.put(i+1, sumRec[i]);
		}
		//serRec record whether the vertex has been added to serial, 
		//0 for not, 1 for yes
		HashMap<Integer,Integer> serRec = new HashMap<Integer,Integer>();
		for (int i = 0;i < orgMat.length;i++) {
			serRec.put(i+1, 0);
		}
		
		
		while (serList.size() < orgMat.length) {
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			int indMin = findMinDegree(dgRec,serRec);
			serList.add(indMin);
			serRec.put(indMin, 1);
			//System.out.println("Ind Min: "+indMin);
			
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			//System.out.println("kkk"+indMin);
			for (int i = 0;i < orgMat[indMin-1].length;i++) {
				if (orgMat[indMin-1][i] == 1 && serRec.get(i+1) == 0) {
					toAdd.add(i+1);
				}
			}
			ArrayList<Integer> dgSortList = sortByDg(toAdd,dgRec);
			for (Integer tmp:dgSortList) {
				tempList.add(tmp);
			}
			
			HashMap<Integer,Integer> tempRec = new HashMap<Integer,Integer>();
			for (int i = 0;i < orgMat.length;i++) {
				tempRec.put(i+1,0);
			}


			while (tempList.size() > 0) {
				//System.out.println(tempList.size());
				int firstTemp = tempList.get(0);
				//System.out.println(firstTemp+"\t"+serRec.get(firstTemp));
				if (serRec.get(firstTemp) == 0) {
					serList.add(firstTemp);
					serRec.put(firstTemp,1);
					ArrayList<Integer> toAdd2 = new ArrayList<Integer>();
					for (int i = 0; i < orgMat[firstTemp-1].length;i++) {
						if (orgMat[firstTemp-1][i] == 1 && serRec.get(i+1) == 0  && tempRec.get(i+1) == 0) {
							toAdd2.add(i+1);
						}
					}
					
					ArrayList<Integer> dgSortList2 = sortByDg(toAdd2,dgRec);
					for (Integer tmp2:dgSortList2) {
						if (tempRec.get(tmp2) == 0) {
							tempList.add(tmp2);
							tempRec.put(tmp2,1);
						}
					}
				}
				tempList.remove(0);
			}		
		}

		//List to array and reverse
		int serListSize = serList.size();
		int[] serArray = new int[serListSize];
		for (int i = 0;i < serListSize;i++) {
			serArray[i] = serList.get(serListSize-1-i);
		}
		return serArray;
		
	}
	
	
	private ArrayList<Integer> sortByDg (ArrayList<Integer> temp, HashMap<Integer,Integer> dgRec) {
		ArrayList<Integer> dgSortList = new ArrayList<Integer>();
		while (temp.size() > 0) {
			int indDgMin = 0;
			int dgMin = dgRec.size()+1;
			for (int i = 0;i < temp.size();i++) {
				if (dgMin > dgRec.get(temp.get(i))) {
					dgMin = dgRec.get(temp.get(i));
					indDgMin = i;
				}
			}
			dgSortList.add(temp.get(indDgMin));
			temp.remove(indDgMin);
		}
		return dgSortList;
	}
	
	private Integer findMinDegree(HashMap<Integer,Integer> dgRec, HashMap<Integer,Integer> serRec) {
		Integer indMin = 0;
		Integer minDg = dgRec.size()+1;
		for (int i = 1; i <= dgRec.size();i++) {
			if (minDg > dgRec.get(i) && serRec.get(i) == 0) {
				minDg = dgRec.get(i);
				indMin = i;
			}
		}
		return indMin;
	}
	
	private int[] matSum(int[][] mat) {
		int[] sumRe = new int[mat.length];
		for (int i = 0;i < mat.length;i++) {
			for (int j = 0;j < mat.length;j++) {
				sumRe[j] = sumRe[j]+mat[i][j];
			}
		}
		return sumRe;
	}
	

}
