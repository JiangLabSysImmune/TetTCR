
import java.util.ArrayList;
import java.util.HashMap;

public class RcmGroup {
	int clusThresh;
	
	public ArrayList<int[]> clust (int[][] orgMat) {
		//transform into sparse matrix
		int[][] orgSp = spars(orgMat);
		//int[][] orgSp = orgMat;
		int[][] tmpSp;
		
		ArrayList<int[]> clustRe = new ArrayList<int[]>();

		int[] usedRe = iniRe(orgMat.length);
		int count = 0;
		while (sum(usedRe) > 0) {
			HashMap<Integer,Integer> tmpRe = new HashMap<Integer,Integer>();
			count++;
			int[][] orgSp2 = new int[orgSp.length][orgSp.length];
			for (int i = 0;i < orgSp.length;i++) {
				for (int j = 0;j < orgSp.length;j++) {
					orgSp2[i][j] = orgSp[i][j];
				}
			}

			tmpSp = formTmpSp(usedRe,orgSp2,tmpRe);
			int st = findStart(tmpSp);
			
			ArrayList<Integer> subGp = new ArrayList<Integer>();
			for (int i = st; i >= 0;i--) {
				int sumI = 0;
				for (int j = st;j >= i;j--) {
					sumI = sumI+tmpSp[i][j];
				}

				if (sumI == st-i+1) {
					usedRe[tmpRe.get(i)] = 0;
					subGp.add(tmpRe.get(i));
				}

			}

			for (int i = st; i < tmpSp.length;i++) {
				int sumI = 0;
				for (int j = st;j <= i;j++) {
					sumI = sumI+tmpSp[i][j];
				}
				
				if (i != st && sumI == i-st+1) {
					usedRe[tmpRe.get(i)] = 0;
					subGp.add(tmpRe.get(i));
				}
			}			
			
			int[] tmpAdd = new int[subGp.size()];
			for (int i = 0;i < subGp.size();i++) {
				tmpAdd[i] = (Integer)subGp.get(i);
			}
			
			clustRe.add(tmpAdd);
			
		}
		return clustRe;
	}
	
	
	private int findStart(int[][] tmpSp) {
		int[] maxInfo = new int[] {0,0};
		for (int i = 0;i < tmpSp.length;i++) {
			int sumI = 0;
			
			int j = 0;
			while (j <= i && j < tmpSp.length-i) {
				sumI = sumI+tmpSp[i-j][i+j];
				
				if (sumI > maxInfo[0]) {
					maxInfo[0] = sumI;
					maxInfo[1] = i;
				}			
				j++;
			}	

		}

		return maxInfo[1];
	}

	static int[][] formTmpSp(int[] usedRe, int[][] orgSp2, HashMap<Integer,Integer> tmpRe) {
		int len = sum(usedRe);
		
		int[][] tmpSp = new int[len][len];
		
		int[][] orgDp = orgSp2;
		for (int i = 0;i < usedRe.length;i++) {
			if (usedRe[i] == 0) {
				for (int j = 0;j < orgDp.length;j++) {
					orgDp[i][j] = 2;
					//orgDp[j][i] = 2;
				}
			}

                        for (int j = 0;j <orgDp.length;j++) {
                                if (usedRe[j] == 0) {
                                        orgDp[i][j] = 2;
                                }
                        }
			
		}
		

		int count = -1;
		for (int i = 0;i < orgDp.length;i++) {
			if (sum(orgDp[i]) != 2*orgDp.length) {
				count++;
				int count2 = -1;
				for (int j = 0;j < orgDp[0].length;j++) {
					if (orgDp[i][j] != 2) {
						count2++;
						tmpSp[count][count2] = orgDp[i][j];
					}
				}
				tmpRe.put(count, i);
			}			
		}

		return tmpSp;
	}
	
	
	static int[] iniRe(int len) {
		int[] usedRe = new int[len];
		for (int i = 0; i < len;i++) {
			usedRe[i] = 1;
		}
		return usedRe;
	}
	
	
	static int sum(int[] toSum) {
		int re = 0;
		for (int i = 0; i < toSum.length;i++) {
			re = re+toSum[i];
		}
		return re;
	}
	
	private int[][] spars (int[][] orgMat) {
		int[][] orgSp = new int[orgMat.length][orgMat[0].length];
		for (int i = 0; i < orgMat.length;i++) {
			for (int j = 0;j < orgMat[0].length;j++) {
				if (orgMat[i][j] <= clusThresh) {
					orgSp[i][j] = 1;
				}
				else {
					orgSp[i][j] = 0;
				}
			}
		}
		return orgSp;
	}
}
