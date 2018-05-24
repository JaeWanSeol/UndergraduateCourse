package FindHairpin;

import java.io.*;
import java.util.*;

public class FindHairpin {
	public static String DNAseq;
	public static HashMap<String, Vector<Integer>> KmerMap = new HashMap<String, Vector<Integer>>();
	
	public static Integer Min_Hairpin_Length = 200;
	public static Integer Max_Hairpin_Length = 400;
	public static Integer Max_Loop_Length = 50;
	
	public static class Candidate{
		String Left;
		String Right;
		String Middle;
		
		
		Candidate(String l, String r, String m){
			Left = l;
			Right = r;
			Middle = m;
		}
		Candidate(int Loc1, int Loc2){
			
			int Middlelen = Loc2 - Loc1 - 10;
			int Leftover = Max_Hairpin_Length - Middlelen - 20;
			int Leftlen = (Leftover / 2) + (Leftover % 2);
			int Rightlen = Leftlen;
			
			boolean cond1 = Loc1-Leftlen >= 0;
			boolean cond2 = Loc2 + 10 + Rightlen < DNAseq.length();
			
			if(cond1 && cond2){
				Left = DNAseq.substring(Loc1-Leftlen, Loc1);
				Middle = DNAseq.substring(Loc1+10, Loc2);
				Right = DNAseq.substring(Loc2+10, Loc2+10 + Rightlen);
			}
			else if(cond1 && !cond2){
				Left = DNAseq.substring(0, Loc1);
				Middle = DNAseq.substring(Loc1+10, Loc2);
				Right = DNAseq.substring(Loc2+10, Loc2+10 + Loc1);
			}
			else if(!cond1 && cond2){
				Left = DNAseq.substring(Loc1 - (DNAseq.length() - (Loc2+10)), Loc1);
				Middle = DNAseq.substring(Loc1+10, Loc2);
				Right = DNAseq.substring(Loc2+10, DNAseq.length());
			}
			else{
				if((Rightlen + Loc2 + 11 - DNAseq.length())  <  (Leftlen - Loc1)){
					Left = DNAseq.substring(0, Loc1);
					Middle = DNAseq.substring(Loc1+10, Loc2);
					Right = DNAseq.substring(Loc2+10, Loc2+10 + Loc1);
				}
				else{
					Left = DNAseq.substring(Loc1 - (DNAseq.length() - (Loc2+10)), Loc1);
					Middle = DNAseq.substring(Loc1+10, Loc2);
					Right = DNAseq.substring(Loc2+10, DNAseq.length());
				}
				
			}
		
		}
		
		void printCandidate(){
			System.out.println("Candidate ");
			System.out.println("left     : " + Left);
			System.out.println("Right    : " + Right);
			System.out.println("Middle   : " + Middle);
			System.out.println("");
		}
	}
	
	public static class LCS{
		String X;
		String Y;
		int[][] b;
		int[][] c;
		String Aligned_X ;
		String Aligned_Y ;
		String LCS_output;
		String LCS_output_rev;
		
		LCS(String x, String y){
			X = x;
			Y = y;
			Aligned_X = "";
			Aligned_Y = "";
			b = new int[x.length()+1][y.length()+1];
			c = new int[x.length()+1][y.length()+1];
			compute_LCStable(x,y);
			LCS_Align(x,y);
		}
		
		void compute_LCStable(String X, String Y){
			int m = X.length();
			int n = Y.length();
			for(int i = 1; i <= m; i++){
				c[i][0] = 0;
				b[i][0] = 1;
			}
			for(int j = 1; j <= n; j++){
				c[0][j] = 0;
				b[0][j] = 2;
			}
			c[0][0] = 0;
			b[0][0] = 0;
			
			for(int i = 1; i <= m; i++){
				for(int j = 1; j <= n; j++){
					if(X.charAt(i-1) == Y.charAt(j-1)){
						c[i][j] = c[i-1][j-1] + 1;
						b[i][j] = 0;
					}
					else if(c[i-1][j] >= c[i][j-1]){
						c[i][j] = c[i-1][j];
						b[i][j] = 1;
					}
					else{
						c[i][j] = c[i][j-1];
						b[i][j] = 2;
					}
				}
			}
			return;
		}
		
		void LCS_Align(String X, String Y){
			
			int i = X.length();
			int j = Y.length();
			LCS_output = "";
			LCS_output_rev = "";
			while(i != 0 || j != 0){
				if(b[i][j] == 0){
					Aligned_X = X.charAt(i-1) + Aligned_X;
					Aligned_Y = Y.charAt(j-1) + Aligned_Y;
					i--;
					j--;
					
					if(i != 0 && j != 0){
						LCS_output += X.charAt(i-1);
						LCS_output_rev = X.charAt(i-1) + LCS_output_rev; 
						}
				}
				else if(b[i][j] == 1){
					Aligned_X = X.charAt(i-1) + Aligned_X;
					Aligned_Y = '-' + Aligned_Y;
					i--;
				}
				else if(b[i][j] == 2){
					Aligned_X = '-' + Aligned_X;
					Aligned_Y =  Y.charAt(j-1) + Aligned_Y;
					j--;
				}
				else{
					return;
				}	
			}
			
		}
		void printLCS(){
			System.out.println("String x: " + X);
			System.out.println("String y: " + Y);
			System.out.println("Aligned x: " + Aligned_X);
			System.out.println("Aligned y: " + Aligned_Y);
		}
	}
	
	public static class Aligned_Candidate{
		String LeftAligned;
		String RightAligned;
		String LeftMiddleAligned;
		String RightMiddleAligned;
		
		Aligned_Candidate(String L, String R, String M, String MR){
			LeftAligned = L;
			RightAligned = R;
			LeftMiddleAligned = M;
			RightMiddleAligned = MR;
		}
		void printAlignedCandidate(){
			System.out.println("Left Aligned        : " + LeftAligned);
			System.out.println("Right Aligned       : " + RightAligned);
			System.out.println("LeftMiddle Aligned  : " + LeftMiddleAligned);
			System.out.println("RightMiddle Aligned : " + RightMiddleAligned);
		}

	}
	
	public static class Hairpin_Pieces{
		String Left;
		String Right;
		String Loop;
		String MiddleL;
		String MiddleR;
		
		Hairpin_Pieces(String L, String R, String M, String MR){
			Left = Reversed(IndelErasedSubstring(L, BoundaryPosition(L, R)));
			Right = IndelErasedSubstring(R, BoundaryPosition(L, R));
			
			Integer mid_boundary = BoundaryPosition(M, MR);
			
			MiddleL = IndelErasedSubstring(M, mid_boundary);
			MiddleR = Reversed(IndelErasedSubstring(MR, mid_boundary));
			Loop = "";
			for(int i = mid_boundary; i < (M.length() - mid_boundary); i++)
				if(M.charAt(i) != '-')
					Loop += M.charAt(i);
			
		}
	}
	
	public static class Hairpin{
		String LeftArm;
		String RightArm;
		String Loop;
		Integer Length;
		
		Hairpin(String L, String R, String ML, String MR, String LP, String Kmer){
			Loop = LP;
			LeftArm = L + Kmer + ML;
			RightArm = MR + Reversed(Kmer) + R;
			Length = Loop.length() + RightArm.length() + LeftArm.length();
		}
		
		boolean isHairpin(){
			if(Loop.length() > 50)
				return false;

			if(Length < 200 || Length > 400)
				return false;
			
			return true;
		}
		
		void printHairpin(){
			LCS lcs = new LCS(LeftArm, Reversed(RightArm));
			
			System.out.println(LeftArm + Loop + RightArm);
			System.out.println(lcs.LCS_output);
			System.out.println(Loop);
		}
	}
	
	public static void setHashMap(){

		
		String Kmer = "";
		Integer KmerLoc = 0;
		while(KmerLoc + 10 <= DNAseq.length()){
			Kmer = DNAseq.substring(KmerLoc, KmerLoc+10);

			//if hash contains null vector, create vector and place it  
			if(KmerMap.get(Kmer) == null){
				Vector<Integer> vec = new Vector<Integer>();
				vec.addElement(KmerLoc);
				KmerMap.put(Kmer, vec);
			}
			//else, push in Hash Map
			else{
				KmerMap.get(Kmer).add(KmerLoc);
			}
			KmerLoc++;
		}
	}

	public static boolean Distance_Satisfied(Integer a, Integer b){
		return (b-a <= 390) && (b-a >= 60);
	}

	public static String Reversed(String s){
		String reversed = "";
		for(int i = s.length()-1; i >= 0; i--)
			reversed += s.charAt(i);
		return reversed;
	}
	
	
	public static int BoundaryPosition(String X, String Y){
		int stopPosition = 0;
		for(int pos = 0; pos < X.length(); pos++){
			
			if(isMatch(X,Y,pos) && isprevIndel(X,Y,pos) && CountNumberofIndels(X, Y, pos) >= 4){
				stopPosition = pos;
				pos = X.length();
			}
		}
		for(int i = stopPosition-1; i >= 0; i--)
		{
			if(isMatch(X,Y,i)){					
				return i+1;
				}
		}
		return 0;
		
	}
	public static boolean isMatch(String X, String Y, int pos){
		if(pos < 0)
			return true;
		if(pos >= X.length())
			return true;
		if((X.charAt(pos) == '-') || (Y.charAt(pos) == '-'))
			return false;
		if(X.charAt(pos) == Y.charAt(pos))
			return true;
		
		System.out.println("isMatch wrong");
		return false;
	}
	public static boolean isprevIndel(String X, String Y, int pos){
		if(pos-1 <= 0)
			return false;
		if((X.charAt(pos-1) == '-') || (Y.charAt(pos-1) == '-'))
			return true;
		if(X.charAt(pos-1) == Y.charAt(pos-1))
			return false;
		System.out.println("isprevIndel wrong");
		return false;
	}
	public static int CountNumberofIndels(String X, String Y, int pos){
		int cnt = 0;
		for(int i = pos - 4; i <= pos + 4; i++){
			if(i == pos)
				;
			else{
				if(!isMatch(X,Y,i))
					cnt++;
			}
		}
		return cnt;
	}
	
	public static String IndelErasedSubstring(String X, int pos){
		String result = "";
		for(int i = 0; i < pos; i++){
			if(X.charAt(i) != '-')
				result += X.charAt(i);
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		
		
		//Open File
		BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		String s;
		while((s = reader.readLine()) != null)
			DNAseq = s;
		
		//Set Hash Map
		setHashMap();
		
		//variants
		String Kmer1;
		String Kmer2;
		int Kmer1_pos = 0;
		int Kmer2_pos;
		Vector<Integer> Kmer2Found = null;	
		
		while(Kmer1_pos + 10 <= DNAseq.length()){
			Kmer1 = DNAseq.substring(Kmer1_pos, Kmer1_pos+10);
			
			//Kmer2 is reversed Kmer1
			Kmer2 = "";
			for(int i = 9; i >= 0; i--){
				Kmer2 += Kmer1.charAt(i);
			}
			Kmer2Found = KmerMap.get(Kmer2);
			
			//Reversed Kmer is found on Hash Table
			if(Kmer2Found != null){
				
				//For every reversed Kmer found
				for(int i = 0; i < Kmer2Found.size(); i++){
					Kmer2_pos = Kmer2Found.get(i);
					
					// 50 <= KmerDistance <= 400
					if(Distance_Satisfied(Kmer1_pos, Kmer2_pos)){
						
						//Get Candidate Region range of 400 by Kmer positions
						Candidate C = new Candidate(Kmer1_pos, Kmer2_pos);
						
						//Regions Outside Kmers, Between Kmers -> Compute LCS by Extending Direction
						LCS LeftRight = new LCS(Reversed(C.Left), C.Right);
						LCS Middle = new LCS(C.Middle, Reversed(C.Middle));
						
						//Aligned form of Left, Right, Middle, Reversed_Middle
						Aligned_Candidate AC = new Aligned_Candidate(LeftRight.Aligned_X, LeftRight.Aligned_Y, Middle.Aligned_X, Middle.Aligned_Y);
						
						//Pieces are : (1.left) kmer1 (2.middleleft) (3.loop) (4.middleright) kmer2 (5.right)
						Hairpin_Pieces HP = new Hairpin_Pieces(AC.LeftAligned, AC.RightAligned, AC.LeftMiddleAligned, AC.RightMiddleAligned);
						
						//Compute Hairpin with Pieces
						Hairpin H = new Hairpin(HP.Left, HP.Right, HP.MiddleL, HP.MiddleR, HP.Loop, Kmer1);
						
						//print hairpin if >= 200 <=400 && Loop <= 50 , 
						if(H.isHairpin()){
							H.printHairpin();
							i = Kmer2Found.size();
							//Jump to Next Part
							Kmer1_pos += H.Length;
						
						}
					}			
				}
			}
			Kmer1_pos++;
		}
		reader.close();
	}
}
