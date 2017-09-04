// (Nearly) Optimal Binary Search Tree
// Bongki Moon (bkmoon@snu.ac.kr), Sep/23/2014

public class BST { // Binary Search Tree implementation
	private Node root;
	private int numOfElement; 
	
	protected boolean NOBSTified = false;
	protected boolean OBSTified = false;
  
	public BST() { 
		root = null;
		numOfElement = 0;
	}
	public int size() { return numOfElement; }
	public void insert(String key) {
		if(search(root, key) != null){
			search(root, key).incFreq();
			return;
		}
		root = insert(root, key);
	}
	private Node insert(Node t, String s) {
		if(t == null) { numOfElement++; return new Node(s); }
		else if(t.getKey().compareTo(s) > 0) { t.setLeft(insert(t.getLeft(), s)); }
		else { t.setRight(insert(t.getRight(), s)); }
		return t;
	}
	public boolean find(String key) {
		return find(root, key);
	}
	private boolean find(Node t, String s) {
		if(t == null) return false;
		else{
			t.incAccess_count();
			if(t.getKey().compareTo(s) == 0){
				return true;
			}
			else if(t.getKey().compareTo(s) > 0){
				return find(t.getLeft(), s);
			}
			else{
				return find(t.getRight(), s);
			}
		}
	}
	private Node search(Node t, String s){
		if(t == null) return null;
		else{
			if(t.getKey().compareTo(s) == 0) { return t; }
			else if(t.getKey().compareTo(s) > 0) { return search(t.getLeft(), s); }
			else { return search(t.getRight(), s); }
		}
	}
	public int sumFreq() { 
		int sum = 0;
		sum = sumFreq(root);
		return sum;
	}
	private int sumFreq(Node t){
		if(t == null) return 0;
		int sum = 0;
		sum += sumFreq(t.getLeft());
		sum += sumFreq(t.getRight());
		sum += t.getFreq();
		return sum;
	}
	public int sumProbes() { 
		int sum = 0;
		sum = sumProbes(root);
		return sum;
	}
	private int sumProbes(Node t){
		if(t == null) return 0;
		int sum = 0;
		sum += sumProbes(t.getLeft());
		sum += sumProbes(t.getRight());
		sum += t.getAccess_count();
		return sum;
	}
	public int sumWeightedPath() { 
		int sum = 0;
		sum = sumWeightedPath(root, 1);
		return sum;
	}
	private int sumWeightedPath(Node t, int li){
		if(t == null) return 0;
		int sum = 0;
		sum += sumWeightedPath(t.getLeft(), li+1);
		sum += sumWeightedPath(t.getRight(), li+1);
		sum += li * t.getFreq();
		return sum;
	}
	public void resetCounters() { 
		resetCounters(root);
	}
	private void resetCounters(Node t){
		if(t == null) return;
		t.resetCounters();
		resetCounters(t.getLeft());
		resetCounters(t.getRight());
		return;
	}
	public void print() { 
		print(root);
	}
	private void print(Node t){
		if(t == null) return;
		print(t.getLeft());
		System.out.println("[" + t.getKey() + ":" + t.getFreq() + ":" + t.getAccess_count() + "]");
		print(t.getRight());
		return;
	}
	public void nobst() { 
		NOBSTified = true;
		int weightSumArray[] = new int[numOfElement];
		Node arr[] = new Node[numOfElement];
		
		makeArray(root, arr, 0);
		
		for(int i = 0 ; i < numOfElement ; i ++) {
			arr[i].setLeft(null);
			arr[i].setRight(null);
		}
		int weightSum = 0;
		for(int i = 0 ; i < numOfElement ; i++) {
			weightSum += arr[i].getFreq();
			weightSumArray[i] = weightSum;
		}

		root = selectRootNobst(arr, 0, numOfElement-1, weightSumArray);
		
	}	// Set NOBSTified to true. 
	
	public void obst() {
		OBSTified = true;
		Node arr[] = new Node[numOfElement];
		makeArray(root, arr, 0);
		for(int i = 0 ; i < numOfElement ; i++) {
			arr[i].setLeft(null);
			arr[i].setRight(null);
		}
		
		int costTable[][] = new int[numOfElement+2][numOfElement+1];
		int rootTable[][] = new int[numOfElement+2][numOfElement+1];
		//int piTable[] = new int[numOfElement];
		//int freqtemp = 0;
		//for(int i = 0 ; i < numOfElement ; i++) {
		//	freqtemp += arr[i].getFreq();
		//	piTable[i] = freqtemp;
		//}
		/*int piTable[][] = new int[numOfElement][numOfElement];
		//rootTable initial
		for(int i = 1 ; i <= numOfElement ; i++) {
			rootTable[i][i] = i;
		}
		//make pi table n x n matrix
		for(int i = 0 ; i < numOfElement ; i++) { 
			int temp = 0;
			for(int j = i ; j < numOfElement ; j++) {
				temp += arr[j].getFreq();
				piTable[i][j] = temp;
			}
		}*/
				//make cost table and root table n+2 x n+1 matrix
		for(int low = numOfElement+1 ; low >= 1 ; low--){
			int sumTemp;
			if(low == numOfElement+1) sumTemp = 0;
			else sumTemp = arr[low-1].getFreq();
			
			for(int high = low-1 ; high <= numOfElement ; high++){
				if(low > high){
					costTable[low][high] = 0;
					rootTable[low][high] = 0;
				}
				else if(low == high) {
					//costTable[low][high] = piValue(piTable, low-1, high-1);
					costTable[low][high] = sumTemp;
					rootTable[low][high] = low;
				}
				else {
					sumTemp += arr[high-1].getFreq();
					int temp = minCost(costTable, low, high, rootTable[low][high-1], rootTable[low+1][high]);
					//costTable[low][high] = piValue(piTable, low-1, high-1) + costTable[low][temp-1] + costTable[temp+1][high];
					costTable[low][high] = sumTemp + costTable[low][temp-1] + costTable[temp+1][high];
					rootTable[low][high] = temp;
				}
			}
		}/*
		System.out.println("this is costs table");
		for(int i = 0 ; i < numOfElement+2 ; i++) {
			for(int j = 0 ; j < numOfElement+1 ; j++) {
				System.out.print(costTable[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println("this is root table");
		for(int i = 0 ; i < numOfElement+2 ; i++) {
			for(int j = 0 ; j < numOfElement+1 ; j ++){
				System.out.print(rootTable[i][j] + " ");
			}
			System.out.println();
		}
		*/
		root = selectNodeObst(arr, rootTable, 1, numOfElement);
	}	// Set OBSTified to true.
	int piValue(int[] arr, int beg, int end) {
		if(beg > end) return 0;
		if(beg-1 < 0) return arr[end];
		return arr[end] - arr[beg-1];
	}
	private int minCost(int[][] arr, int low, int high, int rstart, int rend){
		int temp = arr[low][rstart-1] + arr[rstart+1][high];
		int r = rstart;
		for(int i = rstart+1 ; i <= rend ; i++){
			if(arr[low][i-1]+arr[i+1][high] < temp){
					r = i;
					temp = arr[low][i-1] + arr[i+1][high];
			}
		}
		return r;
	}
	//convert binary search tree to sorted(by key) array
	private int makeArray(Node t, Node[] arr, int i){
		if(t.getLeft() != null) {
			i = makeArray(t.getLeft(), arr, i);
		}
		arr[i] = t;
		i++;
		if(t.getRight() != null) {
			i = makeArray(t.getRight(), arr, i);
		}
		return i;
	}
	private int abs(int a){
		if(a >= 0) return a;
		else return -1*a;
	}
	//select node that minimize difference between leftweight and rightweight
	private Node selectRootNobst(Node[] arr, int beg, int end, int[] sumArray) {
		if(beg > end) return null;
		if(beg == end) return arr[beg];
		
		int idx, diff;
		int leftWeight = 0;
		int rightWeight = 0;
		idx = beg;
		for(int i = beg+1 ; i <= end ; i++) {
			rightWeight += arr[i].getFreq();
		}
		diff = rightWeight - leftWeight;
		//idx for target node(root)'s index, diff for difference between left weight and right weight
		//set idx and diff for arr[beg] above
		for(int i = beg+1 ; i <= end; i++) { // if i is root 
			int leftWeighttemp = 0;
			int rightWeighttemp = 0;
			int difftemp = 0;
			if(beg-1 < 0) leftWeighttemp = sumArray[i-1];
			else leftWeighttemp = sumArray[i-1] - sumArray[beg-1];
			rightWeighttemp = sumArray[end] - sumArray[i];
			difftemp = abs(leftWeighttemp - rightWeighttemp);
			if(difftemp < diff) { 
				diff = difftemp;
				idx = i;
			}
			else if(difftemp == diff) {
				if(rightWeighttemp > leftWeighttemp){
					idx = i;
					diff = difftemp;
				}
			}
		}
		
		arr[idx].setLeft(selectRootNobst(arr, beg, idx-1, sumArray));
		arr[idx].setRight(selectRootNobst(arr, idx+1, end, sumArray));
		return arr[idx];
	}
	private Node selectNodeObst(Node[] arr, int[][] rootTable, int low, int high){
		if(low > high) return null;
		
		int idx = rootTable[low][high]-1;
		arr[idx].setLeft(selectNodeObst(arr, rootTable, low, idx));
		arr[idx].setRight(selectNodeObst(arr, rootTable, idx+2, high));
		
		return arr[idx];
	}

}

class Node{
	public Node(Node t){
		key = t.getKey();
		freq = t.getFreq();
		access_count = t.getAccess_count();
		left = null;
		right = null;
	}
	public Node(){
		key = null;
		freq = 0;
		access_count = 0;
		left = null;
		right = null;
	}
	public Node(String s){
		key = s;
		freq = 1;
		access_count = 0;
		left = null;
		right = null;
	}
	public String getKey() { return key;	}
	public int getFreq() { return freq; }
	public int getAccess_count() { return access_count; }
	public Node getLeft() { return left; }
	public Node getRight() { return right; }
	public void setLeft(Node t) { left = t; }
	public void setRight(Node t) { right = t; }
	public void incFreq() { freq++; }
	public void incAccess_count() { access_count++; }
	public void resetCounters(){
		freq = 0;
		access_count = 0;
	}

	private Node left;
	private Node right;
	private String key;
	private int freq;
	private int access_count;
}
