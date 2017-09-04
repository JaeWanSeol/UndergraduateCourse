//java version "1.7.0_111"
//OpenJDK Runtime Environment (IcedTea 2.6.7) (7u111-2.6.7-0ubuntu0.14.04.3)
//OpenJDK 64-Bit Server VM (build 24.111-b01, mixed mode)

import java.util.*;
// LongInt ADT for unbounded integers
class LongInt {
	private char sign = '+';
	private ArrayList<Integer> number = new ArrayList<Integer>();

	public char getSign(){
		return sign;
	}

	public ArrayList<Integer> getNumber(){
		return number;
	}

	
	
	// constructor
	public LongInt(String s) {
		if(s.length() == 1){
			if(s.charAt(0) == '0'){
				sign = '0';
				number.add(0);
			}
			else{
				sign = '+';
				number.add(s.charAt(0) - '0');
			}

		}
		else{
			if(s.charAt(0) == '+'){
				if(s.charAt(1) == '0'){
					sign = '0';
					number.add(0);
				}
				else{
					sign = '+';
					for(int i = 1 ; i < s.length() ; i++) number.add(s.charAt(i) - '0');
				}
			}
			else if(s.charAt(0) == '-'){
				if(s.charAt(1) == '0'){
					sign = '0';
					number.add(0);
				}
				else{
					sign = '-';
					for(int i = 1; i < s.length() ; i++) number.add(s.charAt(i) - '0');
				}
			}
			else{
				sign = '+';
				for(int i = 0 ; i < s.length() ; i++) number.add(s.charAt(i) - '0');
			}
		}


 	 }

	//constructor 2
	public LongInt(char sign, ArrayList<Integer> number){
		this.sign = sign;
		this.number = number;
	}

	// simple_add : bigger one + smaller one
	private ArrayList<Integer> simple_add(ArrayList<Integer> num1, ArrayList<Integer> num2){
		ArrayList<Integer> result_temp = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> temp2 = new ArrayList<Integer>();

		int carry = 0;
		
		for(int i = num1.size()-1 ; i >= 0 ; i--) temp1.add(num1.get(i));
		for(int i = num2.size()-1 ; i >= 0 ; i--) temp2.add(num2.get(i));
		
		for(int i = 0 ; i < num1.size() - num2.size() ; i++) temp2.add(0);

		for(int i = 0 ; i < temp1.size() ; i++){
			int sum = carry + temp1.get(i) + temp2.get(i);

			carry = sum / 10;
			result_temp.add(sum%10);
		}

		if(carry != 0) result_temp.add(carry);

		for(int i = result_temp.size()-1 ; i >= 0 ; i--) result.add(result_temp.get(i));

		return result;
	}
	// simple_subtract : bigger one - smaller one
	private ArrayList<Integer> simple_subtract(ArrayList<Integer> num1, ArrayList<Integer> num2){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> result_temp = new ArrayList<Integer>();
		ArrayList<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> temp2 = new ArrayList<Integer>();

		for(int i = num1.size()-1 ; i >= 0 ; i--) temp1.add(num1.get(i));
		for(int i = num2.size()-1 ; i >= 0 ; i--) temp2.add(num2.get(i));
		
		for(int i = 0 ; i < num1.size() - num2.size() ; i++) temp2.add(0);

		for(int i = 0 ; i < temp1.size() ; i++){	
			int sub;

			if(temp1.get(i) - temp2.get(i) < 0){
				temp1.set(i+1 , temp1.get(i+1) - 1);
				sub = 10 + temp1.get(i) - temp2.get(i);
			}
			else{
				sub = temp1.get(i) - temp2.get(i);
			}

			result_temp.add(sub);
		}

		int j = 0;

		for(int i = result_temp.size()-1 ; i >= 0 ; i--){
			if(result_temp.get(i) != 0){
				j = i;
				break;
			}
		}

		for(int i = j ; i >= 0 ; i--) result.add(result_temp.get(i));

		return result;

	}

	// simple_multiply : bigger one * smaller one
	private ArrayList<Integer> simple_multiply(ArrayList<Integer> num1, ArrayList<Integer> num2){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> result_temp = new ArrayList<Integer>();
		ArrayList<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> temp2 = new ArrayList<Integer>();
		ArrayList<Integer> result_temp_temp = new ArrayList<Integer>();

		for(int i = num1.size()-1 ; i >= 0 ; i--) temp1.add(num1.get(i));
		for(int i = num2.size()-1 ; i >= 0 ; i--) temp2.add(num2.get(i));

		for(int i = 0 ; i < num1.size() - num2.size() ; i++) temp2.add(0); 

		for(int i = 0 ; i < temp1.size() ; i++){
			int mul = 0;
			
			for(int j = 0 ; j <= i ; j++) mul += temp1.get(j) * temp2.get(i-j);

			result_temp.add(mul);
		}

		for(int i = temp1.size() ; i < 2 * temp1.size() - 1 ; i++){
			int mul = 0 ;
			
			for(int j = temp1.size()-1 ; j >= i - temp1.size() + 1 ; j--) mul+= temp1.get(j) * temp2.get(i-j);

			result_temp.add(mul);
		}

		int carry = 0;

		for(int i = 0 ; i < result_temp.size() ; i++){
			int sum = carry + result_temp.get(i);

			carry = sum / 10;
		
			result_temp_temp.add(sum%10);
		}

		if(carry != 0) result_temp_temp.add(carry);

		int j = 0;

		for(int i = result_temp_temp.size()-1 ; i >= 0 ; i--){
			if(result_temp_temp.get(i) != 0){
				j = i;
				break;
			}
		}

		for(int i = j ; i >= 0 ; i--) result.add(result_temp_temp.get(i));

		return result;
	}




	// compare abs_value , if l1>l2 return 1, if l1<l2 return -1, if l1=l2 return 0
	private int compare_abs(LongInt l1, LongInt l2){
		if(l1.getNumber().size() > l2.getNumber().size()) return 1;
		else if(l1.getNumber().size() < l2.getNumber().size()) return -1;
		else{
			for(int i = 0 ; i < l1.getNumber().size() ; i++){
				if(l1.getNumber().get(i) > l2.getNumber().get(i)) return 1;
				else if(l1.getNumber().get(i) < l2.getNumber().get(i)) return -1;
			}

			return 0;
		}
	}
	
	// find the result value's sign
	private char find_sign(LongInt l1, LongInt l2, char op){
		if(op == '+'){
			if(l1.getSign() == '+'){
				if(l2.getSign() == '+' || l2.getSign() == '0') return '+';
				else{
					int check = compare_abs(l1, l2);
					if(check == 1) return '+';
					else if(check == -1) return '-';
					else return '0';
				}
			}
			else if(l1.getSign() == '-'){
				if(l2.getSign() == '+'){
					int check = compare_abs(l1, l2);
					if(check == 1) return '-';
					else if(check == -1) return '+';
					else return '0';
				}
				else return '-';
			}
			else return l2.getSign();
		}
		else if(op == '-'){
			if(l1.getSign() == '+'){
				if(l2.getSign() == '+'){
					int check = compare_abs(l1, l2);
					if(check == 1) return '+';
					else if(check == -1) return '-';
					else return 0;
				}
				else return '+';
			}
			else if(l1.getSign() == '-'){
				if(l2.getSign() == '-'){
					int check = compare_abs(l1, l2);
					if(check == 1) return '-';
					else if(check == -1) return '+';
					else return '0';
				}
				else return '-';
			}
			else{
				if(l2.getSign() == '+') return '-';
				else if(l2.getSign() == '-') return '+';
				else return '0';
			}
		}
		else{
			if(l1.getSign() == '+') return l2.getSign();
			else if(l1.getSign() == '-'){
				if(l2.getSign() == '+') return '-';
				else if(l2.getSign() == '-') return '+';
				else return '0';
			}
			else return 0;
		}
	}


	
	// returns 'this' + 'opnd'; Both inputs remain intact.
	public LongInt add(LongInt opnd) {
		char result_sign;
		ArrayList<Integer> result_number = new ArrayList<Integer>();
		
		result_sign = find_sign(this, opnd, '+');

		if(result_sign == '0'){
			result_number.add(0);
			LongInt result = new LongInt(result_sign, result_number);
			return result;
		}
		else{
			if(this.getSign() == '0'){
				result_sign = opnd.getSign();
				for(int i = 0 ; i < opnd.getNumber().size() ; i++) result_number.add(opnd.getNumber().get(i));
				LongInt result = new LongInt(result_sign , result_number);
				return result;
			}
			else if(opnd.getSign() == '0'){
				result_sign = this.getSign();
				for(int i = 0 ; i < this.getNumber().size() ; i++) result_number.add(this.getNumber().get(i));
				LongInt result = new LongInt(result_sign , result_number);
				return result;
			}
			else{
				if(this.getSign() == opnd.getSign()){
					int check = compare_abs(this, opnd);
					if(check >= 0) result_number = simple_add(this.getNumber(), opnd.getNumber());
					else result_number = simple_add(opnd.getNumber(), this.getNumber());
				}
				else{
					int check = compare_abs(this, opnd);
					if(check >= 0 ) result_number = simple_subtract(this.getNumber(), opnd.getNumber());
					else result_number = simple_subtract(opnd.getNumber(), this.getNumber());
				}

				LongInt result = new LongInt(result_sign, result_number);
				return result;
			}
		}
 	 }
  
	// returns 'this' - 'opnd'; Both inputs remain intact.
	public LongInt subtract(LongInt opnd) {
		char result_sign;
		ArrayList<Integer> result_number = new ArrayList<Integer>();
		
		result_sign = find_sign(this, opnd, '-');

		if(result_sign == '0'){
			result_number.add(0);
			LongInt result = new LongInt(result_sign, result_number);
			return result;
		}
		else{
			if(this.getSign() == '0'){
				for(int i = 0 ; i < opnd.getNumber().size() ; i++) result_number.add(opnd.getNumber().get(i));
				LongInt result = new LongInt(result_sign , result_number);
				return result;
			}
			else if(opnd.getSign() == '0'){
				for(int i = 0 ; i < this.getNumber().size() ; i++) result_number.add(this.getNumber().get(i));
				LongInt result = new LongInt(result_sign , result_number);
				return result;
			}
			else{
				if(this.getSign() == opnd.getSign()){
					int check = compare_abs(this, opnd);
					if(check >= 0) result_number = simple_subtract(this.getNumber(), opnd.getNumber());
					else result_number = simple_subtract(opnd.getNumber(), this.getNumber());
				}
				else{
					int check = compare_abs(this, opnd);
					if(check >= 0 ) result_number = simple_add(this.getNumber(), opnd.getNumber());
					else result_number = simple_add(opnd.getNumber(), this.getNumber());
				}

				LongInt result = new LongInt(result_sign, result_number);
				return result;
			}
		}	
  	}
	
	// returns 'this' * 'opnd'; Both inputs remain intact.
	public LongInt multiply(LongInt opnd) {
		char result_sign;
		ArrayList<Integer> result_number = new ArrayList<Integer>();

		result_sign = find_sign(this, opnd, '*');

		if(result_sign == '0'){
			result_number.add(0);

			LongInt result = new LongInt(result_sign, result_number);
			return result;
		}
		else{
			int check = compare_abs(this, opnd);

			if(check >= 0) result_number = simple_multiply(this.getNumber(), opnd.getNumber());
			else result_number = simple_multiply(opnd.getNumber(), this.getNumber());

			LongInt result = new LongInt(result_sign, result_number);

			return result;
		}
  	}

	// print the value of 'this' element to the standard output.
  	public void print() {
  		if(sign == '-'){
			System.out.print("-");

			for(int i = 0 ; i < number.size() ; i++){
				System.out.print(number.get(i));
			}
		}
		else if(sign == '+'){
			for(int i = 0 ; i < number.size() ; i++){
				System.out.print(number.get(i));
			}
		}
		else{
			System.out.print(0);
		}
 	 
  	}			

}

