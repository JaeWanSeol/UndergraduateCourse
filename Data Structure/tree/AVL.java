public class AVL extends BST
{
	public AVLNode root;
	private int numOfElement;
	
	public AVL() { 
		root = null;
		numOfElement = 0;
	}
	public int size() { return numOfElement; }
	public void insert(String key) {
		if(search(root, key) != null){
			search(root, key).freq += 1;
			return;
		}
		root = insert(root, key);
		numOfElement++;
	}
	private AVLNode insert(AVLNode t, String s){
		
		if(t == null) { t = new AVLNode(s); }
		else if(t.key.compareTo(s) > 0){
			t.left = insert(t.left, s);
			if(getHeight(t.left) - getHeight(t.right) == 2){
				if(t.left.key.compareTo(s) > 0){
					t = rotateR(t);
				}
				else{
					t = rotateLR(t);
				}
			}
		}
		else{
			t.right = insert(t.right, s);
			if(getHeight(t.right) - getHeight(t.left) == 2){
				if(t.right.key.compareTo(s) > 0){
					t = rotateRL(t);
				}
				else{
					t = rotateL(t);
				}
			}
		}
		int t1 = getHeight(t.left); int t2 = getHeight(t.right);
		t.height = (t1 > t2) ? t1+1 : t2+1;
		return t;
	}
	private AVLNode rotateR(AVLNode t){
		AVLNode temp = t.left;
		t.left = temp.right;
		temp.right = t;
		
		int t1 = getHeight(t.left); int t2 = getHeight(t.right);
		t.height = (t1 > t2) ? t1+1 : t2+1;
		t1 = getHeight(temp.left); t2 = getHeight(temp.right);
		temp.height = (t1 > t2) ? t1+1 : t2+1;
		return temp;
	}
	private AVLNode rotateL(AVLNode t){
		AVLNode temp = t.right;
		t.right = temp.left;
		temp.left = t;
		
		int t1 = getHeight(t.left);
		int t2 = getHeight(t.right);
		t.height = (t1 > t2) ? t1+1 : t2+1;
		t1 = getHeight(temp.left); t2 = getHeight(temp.right);
		temp.height = (t1 > t2) ? t1+1 : t2+1;
		return temp;
	}
	private AVLNode rotateLR(AVLNode t){
		t.left = rotateL(t.left);
		return rotateR(t);
	}
	private AVLNode rotateRL(AVLNode t){
		t.right = rotateR(t.right);
		return rotateL(t);
	}
	private AVLNode search(AVLNode t, String s){
		if(t == null) return null;
		else{
			if(t.key.compareTo(s) == 0) { return t; }
			else if(t.key.compareTo(s) > 0) { return search(t.left, s); }
			else { return search(t.right, s); }
		}
	}
	private int getHeight(AVLNode t){
		if(t == null) return -1;
		else return t.height;
	}
	public boolean find(String key) {
		return find(root, key);
	}
	private boolean find(AVLNode t, String s) {
		if(t == null) return false;
		else{
			t.access_count += 1;
			if(t.key.compareTo(s) == 0){
				return true;
			}
			else if(t.key.compareTo(s) > 0){
				return find(t.left, s);
			}
			else{
				return find(t.right, s);
			}
		}
	}
	public int sumFreq() { 
		int sum = 0;
		sum = sumFreq(root);
		return sum;
	}
	private int sumFreq(AVLNode t){
		if(t == null) return 0;
		int sum = 0;
		sum += sumFreq(t.left);
		sum += sumFreq(t.right);
		sum += t.freq;
		return sum;
	}
	public int sumProbes() { 
		int sum = 0;
		sum = sumProbes(root);
		return sum;
	}
	private int sumProbes(AVLNode t){
		if(t == null) return 0;
		int sum = 0;
		sum += sumProbes(t.left);
		sum += sumProbes(t.right);
		sum += t.access_count;
		return sum;
	}
	public int sumWeightedPath() { 
		int sum = 0;
		sum = sumWeightedPath(root, 1);
		return sum;
	}
	private int sumWeightedPath(AVLNode t, int li){
		if(t == null) return 0;
		int sum = 0;
		sum += sumWeightedPath(t.left, li+1);
		sum += sumWeightedPath(t.right, li+1);
		sum += li * (t.freq);
		return sum;
	}
	public void resetCounters() { 
		resetCounters(root);
	}
	private void resetCounters(AVLNode t){
		if(t == null) return;
		t.freq = 0;	t.access_count = 0;
		resetCounters(t.left);
		resetCounters(t.right);
		return;
	}
	public void print() { 
		print(root);
	}
	private void print(AVLNode t){
		if(t == null) return;
		print(t.left);
		System.out.println("[" + t.key + ":" + t.freq + ":" + t.access_count + "]");
		print(t.right);
		return;
	}
}

class AVLNode{
	public String key;
	public AVLNode left;
	public AVLNode right;
	public int freq;
	public int access_count;
	public int height;
	
	public AVLNode(){
		key = null;
		left = null;
		right = null;
		freq = 0;
		access_count = 0;
		height = 0;
	}
	public AVLNode(String s){
		key = s;
		left = null;
		right = null;
		freq = 1;
		access_count = 0;
		height = 0;
	}
}

