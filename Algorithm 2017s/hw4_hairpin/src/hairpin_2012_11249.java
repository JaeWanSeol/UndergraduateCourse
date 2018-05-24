import java.util.*;
import java.io.*;


public class hairpin_2012_11249 {
	public static void main(String args[]) {
		HashMap<String,ArrayList<Integer>> dic = new HashMap<String, ArrayList<Integer>>();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		File inFile = new File(args[0]);
		
		String seq_id = "";
		String seq = "";
		
		
		try {
			fis = new FileInputStream(inFile);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			
			seq_id = br.readLine();
			seq = br.readLine();	
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		//open file
		//////////////////////////////////////////////////
		//test sequence
		
		
		makeDic(dic, seq);
		
		findHairpin(dic, seq);
		
		
		
		//close file
		////////////////////////////////////////////////
		
		try {
			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			isr.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	//value has the position of reverse(key), valus has sorted position
	static void makeDic(HashMap<String, ArrayList<Integer>> dic, String seq) {
		//phase1 : add all position to hash
		for(int i = 0 ; i < seq.length()-10 ; i++) {
			String tenMer = seq.substring(i, i+10);
			tenMer = reverse(tenMer);
			
			//i is 10-mer start position
			if(dic.get(tenMer) == null) {
				ArrayList<Integer> ar = new ArrayList<Integer>();
				ar.add(i);
				dic.put(tenMer, ar);
			}
			else {
				dic.get(tenMer).add(i);
			}
		}
		
		//sort position
		for(int i = 0 ; i < seq.length()-10 ; i++) {
			String tenMer = seq.substring(i, i+10);
			
			if(dic.get(tenMer) != null) {
				ArrayList<Integer> pos = dic.get(tenMer);
				Ascending asc = new Ascending();
				Collections.sort(pos, asc);
			}	
		}
		
		return;
	}
	
	static int MIN(int x, int y) {
		if(x > y) return y;
		else return x;
	}
	
	static String reverse(String s) {
		return new StringBuffer(s).reverse().toString();
	}
	
	static void findHairpin(HashMap<String, ArrayList<Integer>> dic, String seq) {
		int dummyCnt=0;
		for(int i = 0 ; i < seq.length()-10 ; i++) {
			
			
			String tenMer = seq.substring(i, i+10);
			//arr has position of reverse(tenMer)
			ArrayList<Integer> arr = dic.get(tenMer);
			boolean hairpin;
			
			if(arr == null) continue;
			
			for(int j = 0 ; j < arr.size() ; j++) {
				int X = arr.get(j) - i - 10;
				int D = X+10;
				int arm = (400 - X - 20)/2;
				if(i-arm < 0 || arr.get(j)+10+arm >= seq.length()) {
					arm = MIN(i, seq.length()-arr.get(j)-10);
				}
				

				if(D < 50) continue;
				
				else if(D <= 400) {
					String sL = seq.substring(i-arm, i);
					String sR = seq.substring(arr.get(j)+10, arr.get(j)+10 + arm);
					String sM = seq.substring(i+10, arr.get(j));
					Hairpin h = new Hairpin(tenMer, sL, sR, sM);
					hairpin = h.print();
					if(hairpin) { 
						i = i + 10 + X + 10 + arm - 1;
						if(dummyCnt != 0)  {
							System.out.println();
							
						}
						System.out.println(h.hairpin);
						System.out.println(h.lcs);
						System.out.println(h.loop);
						
						dummyCnt++;
						break;
					}
				}
				else break;
			}
			
			
		}
		return;
	}
	
}

class Ascending implements Comparator<Integer> {
	@Override
	public int compare(Integer i1, Integer i2) {
		return i1.compareTo(i2);
	}
}

class Hairpin {
	int leftstop;
	int rightstop;
	int leftbound;
	int rightbound;
	
	String kmer;
	String sL;
	String sR;
	String sM;
	String sMp;
	String sRp;
	String alignS;
	String alignR;
	String alignM;
	String alignMp;
	String alignSum;
	String alignSump;
	
	String hairpin;
	String lcs;
	String loop;
	
	int[][] slsrb;
	int[][] slsrc;
	int[][] mmb;
	int[][] mmc;
	
	public Hairpin(String s1, String s2, String s3, String s4) {
		kmer = s1; sL = s2; sR = s3; sM = s4;
	}
	
	public boolean print() {
		init();
		makeTable();
		makeAlign();
		selectStop();
		selectBound();
		return isHairpin();
	}
	
	boolean isMatch(int idx) {
		if(alignSum.charAt(idx) == '-') return false;
		else if(alignSump.charAt(idx) == '-') return false;
		return true;
	}
	
	public void helpPrint() {
		System.out.println("kmer : " + kmer);
		System.out.println("sL : " + sL);
		System.out.println("sRp : " + sRp);
		System.out.println("sM : " + sM);
		System.out.println("sMp : " + sMp);
		System.out.println("alignS : " + alignS);
		System.out.println("alignR : " + alignR);
		System.out.println("alignM : " + alignM);
		System.out.println("alignMp : " + alignMp);
		
	}
	
	void init() {
		sMp = reverse(sM);
		sRp = reverse(sR);
		leftstop = -2;
		rightstop = -2;
		leftbound = -2;
		rightbound = -2;
		alignS = "";
		alignR = "";
		alignM = "";
		alignMp = "";
	}
	
	void makeAlign() {
		int m = sL.length();
		int n = sRp.length();
		int p = sM.length();
		int q = sMp.length();
		
		while(m != 0 || n != 0) {
			if(slsrb[m][n] == 0) {
				alignS = sL.charAt(m-1) + alignS;
				alignR = sRp.charAt(n-1) + alignR;
				m--; n--;
			}
			else if(slsrb[m][n] == 1) {
				alignS = sL.charAt(m-1) + alignS;
				alignR = '-' + alignR;
				m--;
			}
			else {
				alignS = '-' + alignS;
				alignR = sRp.charAt(n-1) + alignR;
				n--;
			}
		}
		
		while(p != 0 || q != 0) {
			if(mmb[p][q] == 0) {
				alignM = sM.charAt(p-1) + alignM;
				alignMp = sMp.charAt(q-1) + alignMp;
				p--; q--;
			}
			else if(mmb[p][q] == 1) {
				alignM = sM.charAt(p-1) + alignM;
				alignMp = '-' + alignMp;
				p--;
			}
			else {
				alignM = '-' + alignM;
				alignMp = sMp.charAt(q-1) + alignMp;
				q--;
			}
		}
		alignSum = alignS + kmer + alignM;
		alignSump = alignR + kmer + alignMp;
	}
	
	void makeTable() {
		int m = sL.length();
		int n = sRp.length();
		int p = sM.length();
		int q = sMp.length();
		slsrb = new int[m+1][n+1];
		slsrc = new int[m+1][n+1];
		mmb = new int[p+1][q+1];
		mmc = new int[p+1][q+1];
		
		for(int i = 1 ; i <= m ; i++)
			slsrc[i][0] = 0;
		for(int j = 0 ; j <= n ; j++) {
			slsrc[0][j] = 0;
		}
		for(int i = 1 ; i <= m ; i++) {
			slsrb[i][0] = 1;
		}
		for(int i = 1 ; i <= n ; i++) {
			slsrb[0][i] = 2;
		}
		for(int i = 1 ; i <= m ; i++) {
			for(int j = 1 ; j <= n ; j++) {
				//0 : diagonal, 1:up, 2:left
				if(sL.charAt(i-1) == sRp.charAt(j-1)) {
					slsrc[i][j] = slsrc[i-1][j-1] + 1;
					slsrb[i][j] = 0;
				}
				else if(slsrc[i-1][j] >= slsrc[i][j-1]) {
					slsrc[i][j] = slsrc[i-1][j];
					slsrb[i][j] = 1;
				}
				else {
					slsrc[i][j] = slsrc[i][j-1];
					slsrb[i][j] = 2;
				}
			}
		}
		for(int i = 1 ; i <= p ; i++)
			mmc[i][0] = 0;
		for(int j = 0; j <= q ; j++)
			mmc[0][j] = 0;
		for(int i = 1 ; i <= p ; i++)
			mmb[i][0] = 1;
		for(int i = 1 ; i <= q ; i++) {
			mmb[0][i] = 2;
		}
		for(int i = 1 ; i <= p ; i++) {
			for(int j = 1; j <= q ; j++) {
				if(sM.charAt(i-1) == sMp.charAt(j-1)) {
					mmc[i][j] = mmc[i-1][j-1] + 1;
					mmb[i][j] = 0;
				}
				else if(mmc[i-1][j] >= mmc[i][j-1]) {
					mmc[i][j] = mmc[i-1][j];
					mmb[i][j] = 1;
				}
				else {
					mmc[i][j] = mmc[i][j-1];
					mmb[i][j] = 2;
				}
			}
		}
		
	}
	
	void selectStop() {
		leftstop = -1;
		rightstop = alignSum.length();
		
		for(int i = alignS.length()-1 ; i >= 0 ; i--) {
			if(isMatch(i) && !isMatch(i+1)) {
				int num = 0;
				for(int j = i+4 ; j >= 0 && j >= i-4 ; j--) {
					if(!isMatch(j)) num++;
				}
				if(num >= 4) {
					leftstop = i;
					break;
				}
			}
		}
		
		for(int i = alignS.length()+10 ; i < alignSum.length() ; i++) {
			if(isMatch(i) && !isMatch(i-1)) {
				int num = 0;
				for(int j = i-4 ; j < alignSum.length() && j <= i+4 ; j++) {
					if(!isMatch(j)) num++;
				}
				if(num >= 4) {
					rightstop = i;
					break;
				}
			}
		}
		
	}
	void selectBound() {
		for(int i = leftstop+1 ; i <= alignS.length() ; i++) {
			if(isMatch(i)) {
				leftbound = i;
				break;
			}
		}
		for(int i = rightstop-1 ; i >= alignS.length()+9 ; i--) {
			if(isMatch(i)) {
				rightbound = i;
				break;
			}
		}
			
	}
	boolean isHairpin() {
		//System.out.println("kmer : " + kmer);
		//System.out.println("sL : " + sL);
		//System.out.println("sM : " + sM);
		//System.out.println("sR : " + sR);
		//System.out.println("leftbound : " + leftbound);
		//System.out.println("rightboud : " + rightbound);
		int length_temp = rightbound -(alignS.length()+10) +1;
		loop = "";
		for(int i = rightbound+1 ; i < alignSum.length() - length_temp ; i++) {
			if(alignSum.charAt(i) != '-') loop += alignSum.charAt(i);
		}
		lcs = "";
		for(int i = leftbound ; i <= rightbound ; i++) {
			if(isMatch(i)) lcs += alignSum.charAt(i);
		}
		hairpin ="";
		for(int i = leftbound ; i <= rightbound ; i++) {
			if(alignSum.charAt(i) != '-') hairpin += alignSum.charAt(i);
		}
		hairpin += loop;
		for(int i = rightbound ; i >= leftbound ; i--) {
			if(alignSump.charAt(i) != '-') hairpin += alignSump.charAt(i);
		}
		
		//System.out.println("hairpin length : " + hairpin.length());
		//System.out.println("hairpin : " + hairpin);
		//System.out.println("lcs length : " + lcs.length());
		//System.out.println("lcs : " + lcs);
		//System.out.println("loop length : " + loop.length());
		//System.out.println("loop : " + loop);
				
		
		if(hairpin.length() >= 200 && hairpin.length() <= 400 && loop.length() <= 50) {
			//true hairpin
			//System.out.println("hairpin length : " + hairpin.length());
			//System.out.println(hairpin); //hairpin
			//System.out.println("lcs length : " + lcs.length());
			//System.out.println(lcs); //lcs
			//System.out.println("loop length : " + loop.length());
			//System.out.println(loop); //loop
			//System.out.println("alignSum");
			//System.out.println(alignSum);
			//System.out.println("alignSump");
			//System.out.println(alignSump);
			//System.out.println();
			//System.out.println("Stop : " + leftstop);
			//System.out.println("boud : " + leftbound);
			return true;
		}
		else {
			//not hairpin
			return false;
		}
	}
	String reverse(String s) {
		return new StringBuffer(s).reverse().toString();
	}
}
