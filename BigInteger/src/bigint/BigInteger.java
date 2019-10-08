package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;

	/**
	 * Number of digits in this integer
	 */
	int numDigits;

	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */

	DigitNode front;

	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}

	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
			throws IllegalArgumentException {
		BigInteger n=new BigInteger();
		//Place holder for when the actual integer starts
		int endOfLeadingChars=0;

		//returns null if 0
		if(integer.equals("0")){
			n.front=null;
			return n;
		}


		//Remove trailing spaces
		integer=integer.trim();

		char[] digits=integer.toCharArray();


		//Check for positive or negative
		if(digits[0]=='-'){
			n.negative=true;
			endOfLeadingChars=1;
		}
		if(digits[0]=='+'){
			endOfLeadingChars=1;

		}

		//Gets rid of and finds the end of the leading zeroes.
		for(int i=endOfLeadingChars;i<digits.length;i++){

			if(digits[i]!='0'){
				endOfLeadingChars=i;
				break;
			}

		}
		//if it is an actual digit, add it to the BigInteger. If not, 
		for(int i=endOfLeadingChars;i<digits.length;i++){
			if(Character.isDigit(digits[i])){

				n.addToFront(Character.getNumericValue(digits[i]));

			}		
			else{
				throw new IllegalArgumentException("Incorrect Format");
			}
		}

		return n;
	}




	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	private void  addToEnd(int data){
		DigitNode k;
		k=front;
		if(k==null){
			front=new DigitNode(data,null);
			numDigits++;
		}

		else{

			while(k.next != null) {
				k=k.next;
			}

			k.next=new DigitNode(data,null);;
			numDigits++;
		}
	}

	private static BigInteger reverse(BigInteger x){
		BigInteger ret=new BigInteger();
		ret.negative=x.negative;
		for(DigitNode origptr=x.front;origptr!=null;origptr=origptr.next){
			ret.addToFront(origptr.digit);

		}

		return ret;

	}
	private boolean isLargerThan(BigInteger x){
		boolean ret=false;
		BigInteger orig=new BigInteger();
		BigInteger second=new BigInteger();

		//Reverse the two linked lists
		for(DigitNode origptr=front;origptr!=null;origptr=origptr.next){
			orig.addToFront(origptr.digit);

		}
		for(DigitNode secondpoint=x.front;secondpoint!=null;secondpoint=secondpoint.next){
			second.addToFront(secondpoint.digit);

		}




		DigitNode secondptr=second.front;
		if(numDigits>x.numDigits){
			return true;
		}
		else if(numDigits<x.numDigits){

			return false;
		}
		else{
			for(DigitNode firstptr=orig.front;firstptr!=null;firstptr=firstptr.next){


				if(firstptr.digit>secondptr.digit){
					return true;

				}else{

					secondptr=secondptr.next;
				}



			}
			return false;

		}
	}
	private boolean equals(BigInteger x){
		boolean ret=false;

		BigInteger orig=new BigInteger();
		BigInteger second=new BigInteger();

		//Reverse the two linked lists
		for(DigitNode origptr=front;origptr!=null;origptr=origptr.next){
			orig.addToFront(origptr.digit);

		}
		for(DigitNode secondpoint=x.front;secondpoint!=null;secondpoint=secondpoint.next){
			second.addToFront(secondpoint.digit);

		}
		DigitNode secondptr=second.front;
		if(numDigits>x.numDigits){
			return false;
		}
		else if(numDigits<x.numDigits){

			return false;
		}
		else{
			for(DigitNode firstptr=orig.front;firstptr!=null;firstptr=firstptr.next){

				if(firstptr.digit!=secondptr.digit){
					return false;

				}else{

					secondptr=secondptr.next;
				}



			}
			return true;

		}
	}
	private void addToFront(int data){
		if(front==null){
			front=new DigitNode(data,null);
			numDigits++;
		}
		else{front=new DigitNode(data,front);
		numDigits++;
		}


	}
	private static BigInteger subtract(BigInteger bigger, BigInteger smaller) {
		BigInteger ret=new BigInteger();
		DigitNode smallptr=smaller.front;
		if(!bigger.negative && smaller.negative){
			ret.negative=false;
		}
		if(bigger.negative && !smaller.negative){
			ret.negative=true;
		}



		

		for(DigitNode bptr=bigger.front;(bptr!=null || smallptr!=null);bptr=bptr.next){

			boolean carry=false;
			if(smallptr==null||bptr==null){

				break;
			}
			
			carry=bptr.digit<smallptr.digit;
			

			if(carry){
				if(bptr!=null){
					ret.addToFront(bptr.digit+10);

				}

				if(smallptr.next==null){
					smallptr.next=new DigitNode(1,null);

				}
				ret.front.digit=ret.front.digit-smallptr.digit;
				smallptr=smallptr.next;


			}
			else{
				if(bptr!=null){
					ret.addToFront(bptr.digit);

				}			
				if(smallptr!=null){

					if(smallptr.next==null && bptr!=null){
						smallptr.next=new DigitNode(0,null);
					}
					ret.front.digit=ret.front.digit-smallptr.digit;
					smallptr=smallptr.next;

				}







			}
		}

		if(ret.front.digit==0){
			ret.front=ret.front.next;
			ret.numDigits--;
		}






		return reverse(ret);
	}




	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger ret=new BigInteger();

		//diff=difference in size between the two integers
		int diff=0;

		//0+0=0
		if(first==null && second==null){
			ret.front=null;
			return ret;
		}

		if(first.negative && second.negative){
			ret.negative=true;
		}

		if((first.negative && !second.negative)||(!first.negative && second.negative)){




			if(first.equals(second)){
				ret.front=null;
				return ret;		
			}
			if(first.isLargerThan(second)){

				return subtract(first,second);

			}
			else{

				return subtract(second,first);

			}




		}



		//Makes sure that both of the integers are the same size by adding necessary 0's
		if(first.numDigits>second.numDigits){
			diff=first.numDigits-second.numDigits;
			for(int i=0;i<diff;i++){
				second.addToEnd(0);

			}
		}
		else{

			diff=second.numDigits-first.numDigits;
			for(int i=0;i<diff;i++){
				first.addToEnd(0);

			}
		}


		DigitNode shortptr=second.front;
		int carry=0;

		for(DigitNode longptr=first.front;(longptr!=null&&shortptr!=null);longptr=longptr.next){

			int sum=longptr.digit+shortptr.digit+carry;

			if(sum>9){
				ret.addToEnd((sum%10));
				carry=1;
				shortptr=shortptr.next;
			}
			else if(sum<=-10){
				ret.addToEnd((sum%10));
				carry=1;
				shortptr=shortptr.next;
			}
			else{
				ret.addToEnd(sum);
				carry=0;
				shortptr=shortptr.next;
			}

		}
		//Whatever leftover carry numbers there are
		if(carry!=0){
			ret.addToEnd(carry);
		}

		return ret; 
	}

	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {


		BigInteger ret=new BigInteger();
		int diff=0;
		//Decides whether the final product is negative or not
		boolean retneg=false;
		//If either of the numbers are 0, return the product (0)
		if(first.front==null || second.front==null){
			ret.front=null;
			return ret;
		}


		if((first.negative && !second.negative)||(!first.negative && second.negative)){
			retneg=true;
		}

		if(first.numDigits==1 && first.front.digit==1){
			ret.front=second.front;
			ret.numDigits++;
			return ret;
		}
		if(second.numDigits==1 && second.front.digit==1){
			ret.front=first.front;
			ret.numDigits++;
			return ret;

		}


		//Checks whether the final product will be negative or not
		int zeros=0;
		int carry=0;


		for(DigitNode firstptr=first.front;firstptr!=null;firstptr=firstptr.next){

			BigInteger temp=new BigInteger();
			int product;
			int last;
			//System.out.println("Amount of zeros to addFIRST:" + zeros);
			for(int i=0;i<zeros;i++){
				temp.addToFront(0);
			}
			//System.out.println("After adding 0\'s"+temp);
			for(DigitNode secondptr=second.front;secondptr!=null;secondptr=secondptr.next){

				product=(firstptr.digit*secondptr.digit)+carry;


				if(product>10){

					temp.addToEnd((product%10));	
					carry=((product)%100)/10;
					System.out.println("Carry is: "+carry);
				}



				else if (product!=0 ){

					temp.addToEnd(product);
					carry=0;
				}
				else if(carry!=0){

					temp.addToEnd(carry);
					carry=0;
				}

				else if (secondptr.next!=null && product!=0){

					temp.addToEnd(product);
				}


				else if(secondptr.next!=null){

					temp.addToEnd(product);
					carry=0;
				}
				else{

					temp.addToEnd(product);
					carry=0;
				}




			}

			if(carry!=0){

				temp.addToEnd(carry);
				carry=0;
			}





			ret=add(ret,temp);

			zeros++;
		}



		ret.negative=retneg;
		return ret; 

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}


		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
			retval = curr.digit + retval;
		}


		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}

}
