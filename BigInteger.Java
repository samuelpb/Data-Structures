package math;

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
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer)  
		throws IllegalArgumentException {
		
		BigInteger thing = new BigInteger();
		
		
		
		if (integer.length()==0) {
			throw new IllegalArgumentException();
		}
		
		int length = integer.length(); //temporary length variable for this loop
		for (int i=0; i<length; i++) { ///tests for spaces in the beginning
			if (integer.charAt(0)==' '&& integer.length()>1) {
				integer = integer.substring(1);
			} else {
				break;
			}
		}
		
		length = integer.length();
		for (int i=0; i<length; i++) { ///tests for spaces at the end
			if (integer.charAt(integer.length()-1)==' '&& integer.length()>1) {
				integer = integer.substring(0, integer.length()-1);
			} else {
				break;
			}
		}
		
		
		//changes negative to match the value of the entered integer
		if (integer.charAt(0) == '-') {
			thing.negative = true;
		}
		
		//removes the sign from the front of the number so it can be parsed
		
		if ((integer.charAt(0) == '-')|| (integer.charAt(0)=='+')) {
			integer= integer.substring(1, integer.length());
		}
		
		if (integer.length()==0) {
			throw new IllegalArgumentException();
		}
		
		//checks for illegal input
		for (int i=0; i<integer.length(); i++) {
			if ((48 > (int) integer.charAt(i)) || (57 < (int) integer.charAt(i))) {
				throw new IllegalArgumentException();
			}
		}
		
		
		//removes insignificant zeros
		for (int i=0; i<length; i++) {
			if ((integer.charAt(0)=='0')&&(integer.length()>1)) {
				integer = integer.substring(1);
				continue;
			}
			break;
		}
	
		thing.numDigits= integer.length();
		//iterates through the list and adds the first digit of the number to the front of the list
		for (int i=0; i<thing.numDigits; i++) {
			
			DigitNode temp = new DigitNode ((int)(integer.charAt(i))-48, thing.front);
			thing.front=temp;
			
		}
		
		//this is a check to make sure the integer is stored in reverse order
		
		/*for (DigitNode i = thing.front; i!=null; i=i.next) {  
			System.out.println(i.digit);
		}*/
		
		return thing;
	}
	
	/**
	 * Adds an integer to this integer, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY this integer.
	 * NOTE that either or both of the integers involved could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param other Other integer to be added to this integer
	 * @return Result integer
	 */
	public BigInteger add(BigInteger other) {
		
		BigInteger answer = new BigInteger();
		
		BigInteger larger = new BigInteger();
		BigInteger smaller = new BigInteger();
		
		
		
		int carry=0;
		String rep="";
		int temp=0;
		
		//figures out the longer or larger absolute value of the two
		if (this.toStringAbsoluteValue().length()>other.toStringAbsoluteValue().length()) {
			larger=parse(this.toStringAbsoluteValue());
			smaller=parse(other.toStringAbsoluteValue());
		} else if (this.toStringAbsoluteValue().length()<other.toStringAbsoluteValue().length()) {
			larger=parse(other.toStringAbsoluteValue());
			smaller=parse(this.toStringAbsoluteValue());
		}else {

			int order = this.toStringAbsoluteValue().compareTo(other.toStringAbsoluteValue());
		/* Sets which Big Integer is larger
		 * is negative if other is larger, positive if this is larger (has more/higher digits)
		 */
		
		//this if/else sets the objects to the larger and smaller, to use for place holding
		//and to figure out which number to stop adding
			if (order <0) {
				larger = parse(other.toStringAbsoluteValue());
				smaller = parse(this.toStringAbsoluteValue());
			} else {
				larger = parse(this.toStringAbsoluteValue());
				smaller = parse(other.toStringAbsoluteValue());
			}
		}
		

		
		
		if (this.negative == other.negative) {
			for (int i=0; i<larger.numDigits; i++) {
					temp = larger.front.digit + carry + smaller.front.digit;
					carry = temp/10;
					rep= temp%10 + rep;
					larger.front = larger.front.next;
					if (smaller.front.next==null) {
						smaller.front.digit=0;
					}else {
					smaller.front = smaller.front.next;
					}
				}
			answer = parse(carry + rep);
			answer.negative=this.negative;
			} else {
				for (int i=0; i<larger.numDigits; i++) {
					temp = larger.front.digit - carry - smaller.front.digit;
					carry=0;
					if (temp<0) {
						carry=1;
						temp = (10 - temp*-1);
					}
					rep = temp+rep;
					larger.front = larger.front.next;
					if (smaller.front.next==null) {
						smaller.front.digit=0;
					}else {
						smaller.front = smaller.front.next;
					}
				}
				
				answer=parse(carry + rep);
				answer.negative = larger.negative;
			}
			
		
		
		
		
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		return answer;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the given BigInteger
	 * with this BigInteger - DOES NOT MODIFY this BigInteger
	 * 
	 * @param other BigInteger to be multiplied
	 * @return A new BigInteger which is the product of this BigInteger and other.
	 */
	public BigInteger multiply(BigInteger other) {
		
		
		BigInteger answer = new BigInteger();
		answer = parse("0");
		boolean negOther = other.negative;
		other = parse(other.toStringAbsoluteValue());
		BigInteger temp= new BigInteger();
		temp = parse("0");
		String step = "";
		long counter=0;;
		
		
		for (DigitNode i=this.front; i!=null; i=i.next) {//iterates through one number
			for (int k=0; k<i.digit; k++) {//adds the other number to itself as much as the digit says
				temp = temp.add(other);
			}
			step = temp.toString();
			//System.out.println(temp);
				for (long m= counter; m>0; m--){//adds a zero if it is in the tens place or higher
				step= step + "0";
				}
			//System.out.println(step);
			answer = answer.add(parse(step));//increases the answer by the correct amount
			//System.out.println(answer); //just a check for myself
			temp=parse("0"); //resets temp so that you add the correct number each time
			counter++;
		}
		
		
		if (negOther != this.negative) {
			answer.negative=true;
		}
		else {
			answer.negative=false;
		}
		
		return answer;
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
		if ( !retval.equals("0")) {
		if (negative) {
			retval = '-' + retval;
		}
		}
		
		return retval;
	}
	
	private String toStringAbsoluteValue() {
		if (front == null) {
			return "0";
		}
		
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		
		
	}
		return retval;
	}
	
	
	
	
	
	
}
