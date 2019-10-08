package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is created 
	 * and stored, even if it appears more than once in the expression.
	 * At this time, values for all variables and all array items are set to
	 * zero - they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr The expression
	 * @param vars The variables array list - already created by the caller
	 * @param arrays The arrays array list - already created by the caller
	 */

	public static void main(String[] args){
		//System.out.println("Hello");
		int count=0;


		String tester= ("varx + vary*varz[(vara+varb[(a+b)*33])])/55").replaceAll(" ", "");
		//System.out.println(tester.makeVariableLists());
		//		while(tester.hasMoreTokens()){
		//		System.out.println("Count"+count);
		//		System.out.println(tester.nextToken());
		//		System.out.println(tester.nextToken());
		//		System.out.println("______________________________");
		//		count++;
		//		}
	}


	public static void 
	makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/** DO NOT create new vars and arrays - they are already created before being sent in
		 ** to this method - you just need to fill them in.
		 **/

		expr=expr.replaceAll("\\s+","");

		StringTokenizer expres=new StringTokenizer(expr,delims,true);

		while(expres.hasMoreTokens()){
			String item=expres.nextToken();
			Variable temp1=new Variable(item);
			Array temp2=new Array(item);
			if(expres.hasMoreTokens()){
				if(item.matches("[a-zA-Z]+")){
					String next=expres.nextToken();
					if(next.equals("[")){
						if(!arrays.contains(temp2)){
						Array newArray=new Array(item);
						arrays.add(newArray);
						}
					}
					else{
						if(!vars.contains(temp1)){
						Variable newVariable=new Variable(item);
						vars.add(newVariable);
						}
					}

				}



			}	else if(item.matches("[a-zA-Z]+")){
				if(!vars.contains(temp1)){
					Variable newVariable=new Variable(item);
					vars.add(newVariable);
					}
			}




		}
		System.out.println(vars);
		System.out.println(arrays);

	}

	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc Scanner for values input
	 * @throws IOException If there is a problem with the input 
	 * @param vars The variables array list, previously populated by makeVariableLists
	 * @param arrays The arrays array list - previously populated by makeVariableLists
	 */
	public static void 
	loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				// following are (index,val) pairs
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok," (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;              
				}
			}
		}
	}
	/**
	 * Evaluates the expression.
	 * 
	 * @param vars The variables array list, with values for all variables in the expression
	 * @param arrays The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */
	//The main evaluate
	public static float 
	evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		//System.out.println("Variable Values: " +vars);
		//System.out.println("Array Values: "+arrays);

		expr=expr.replaceAll(" ","");
		expr=expr.replaceAll("\\t+","");

		StringTokenizer expres=new StringTokenizer(expr,delims,true);

		//Put the expression into an ArrayList
		ArrayList<String>	expresArr=new ArrayList<String>();

		while(expres.hasMoreTokens()){
			expresArr.add(expres.nextToken());
		}
		return evaluate(expresArr,vars,arrays);

	}

		//recursive version
	private static float 
	evaluate(ArrayList<String> expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
	//	System.out.println("START METHOD------------------------------------------------------------------");
	//	System.out.println("VARIABLES: "+vars);
	//	System.out.println(expr);
	//	System.out.println("HAS MAX SIZE OF:" +expr.size());
		Stack<String>operators=new Stack<String>();
		Stack<Float> values=new Stack<Float>(); 
		float tempnum=0;
		int openIndex=0;
		int closeIndex=0;
		int pCounter=0;
		String arrName="";
		
		// If expression="3" or "n" , just return that one float
		if(expr.size()==1 || values.size()==1){
			if(isAlpha(expr.get(0))){
				Variable temp=new Variable(expr.get(0));
				if(vars.contains(temp)){
					return (getVar(expr.get(0),vars));
				}
			}	
			if(isNum(expr.get(0))){
				return Float.parseFloat(expr.get(0));

			}
		}
		//loop through the whole expression
		for(int i=0;i<expr.size();i++){
			//If operator, push onto operator stack
			if(isOperator(expr.get(i)) || expr.get(i).equals("*")){
				operators.push(expr.get(i));
				//System.out.println("WE FINNA PUSH THE OPERATION: "+ expr.get(i));
			}
			//If just a number, push it into the values stack
			if(isNum(expr.get(i))){
				values.push(Float.parseFloat(expr.get(i)));
			//	System.out.println("WE FINNA PUSH THE NUMBER: "+expr.get(i));
			}
			
			
			//If a variable name AND ONLY A VARIABLE NAME NOT AN ARRAY NAME, push the value of that var
			if(isAlpha(expr.get(i))){
				Variable temp=new Variable(expr.get(i));
				if(vars.contains(temp)){
					//System.out.println("WE FINNA PUSH THE VARIABLE WITH VALUE: "+expr.get(i)+"="+ getVar(expr.get(i),vars));
					
					values.push(getVar(expr.get(i),vars));
				}
			}

			//If (, set open index to i,
			//iterate over to next part of expression
			//Increment pCounter
			if(expr.get(i).equals("(")){
				openIndex=i;
				//System.out.println("OPEN INDEX IS" + i);
				i++;
				pCounter++;

				//Loop through the REST of the expression and find the closing )
				//If (, increase pCounter
				//If ), AND Pcounter=1, its gonna close and be 0 so store that index
				//If ), incREMENT PcOUNTER
				for(int j=i;j<expr.size();j++){
					if(expr.get(j).equals("(")){
						pCounter++;
					}
					if(expr.get(j).equals(")" )&& pCounter==1){
						closeIndex=j;
						i=j;
						break;
					}
					if(expr.get(j).equals(")")){
						pCounter--;
					}
				}

				//Create a subexpression using the open and close index of your parentheses
				//Push a the value of that subexpression by calling evaluate again
				//Reset all your counters and index value 
				

				ArrayList<String> temp=new ArrayList<String>(expr.subList((openIndex+1), closeIndex));

				//System.out.println("SUBLIST WITH INDEXES : "+(openIndex+1)+ "," +closeIndex+": "+ temp);

				values.push(evaluate(temp,vars,arrays));
				openIndex=0;
				closeIndex=0;
				pCounter=0;

			}
			//--------------------------------------------------------------
			// Use same logic as parentheses for brackets but with a couple tweaks to get the arrayVal.
			if(expr.get(i).equalsIgnoreCase("[")){
				arrName=expr.get(i-1);
				System.out.println(arrName);
				openIndex=i;
				i++;
				pCounter++;
				for(int j=i;j<expr.size();j++){
					if(expr.get(j).equals("[")){
						pCounter++;
					}
					if(expr.get(j).equals("]" )&& pCounter==1){
						closeIndex=j;
						i=j;
						break;
					}
					if(expr.get(j).equals("]")){
						pCounter--;
					}
				}
				ArrayList<String> temp=new ArrayList<String>(expr.subList((openIndex+1), closeIndex));
				System.out.println("Temp: " + temp);
					
				values.push(getArrayValue(arrName,(int) evaluate(temp,vars,arrays),arrays));
				openIndex=0;
				closeIndex=0;
				pCounter=0;

			}

		}

		if(expr.size()==1 || values.size()==1){
			if(isAlpha(expr.get(0))){
				Variable temp=new Variable(expr.get(0));
				if(vars.contains(temp)){
					return (getVar(expr.get(0),vars));
				}
			}	
			if(isNum(expr.get(0))){
				return Float.parseFloat(expr.get(0));

			}
		}
		//------------------------------------------------------------------------------
		//DO MATH HERE USING THE STACKS AND VALUES!!

		operators=reverseOps(operators);
		values=reverseVals(values);
		String currentOp="";
		Stack<Float> tempVals=new Stack<Float>(); 
		Stack<String>tempOps=new Stack<String>();
 

		if(values.size()==1){
			//System.out.println("UMMMLMAO");
			return values.peek();
		}

		while(values.size()>1){

			if(!operators.isEmpty()){
				currentOp=operators.pop();
			}
		//	System.out.println("Can we enter here: YES WE CAN BECAUSE I PASSED THROUGH WITH A STACK SIZED: " + values.size());

			if(operators.isEmpty()){


			}

			if(currentOp.equals("*") ||currentOp.equals("/") ){

				float val1=values.pop();
				float val2=values.pop();
				//System.out.println("GOTTA DO MULTIPLEDIVIDE THOUGH");
				tempnum=doMath(currentOp,val1,val2);
				//System.out.println(tempnum);
				values.push(tempnum);

			}

			else if(currentOp.equals("+") || currentOp.equals("-")){
				tempVals.push(values.pop());
				tempOps.push(currentOp);

			}

			if(operators.isEmpty()){
				while (!tempOps.isEmpty()){
					operators.push(tempOps.pop());

				}
				while(!tempVals.isEmpty()){

					values.push(tempVals.pop());

				}

				while(!operators.isEmpty()){
					//System.out.println("GONNA DO LAST MATH");
					values.push(doMath(operators.pop(),values.pop(),values.pop()));

				}
			}
			
		}

		//System.out.println("IM GONNA RETURN: "+ values.peek());
		//System.out.println("END METHOD-------------------------------------------------------------------------");

		return values.peek();
	}
	
	private static float getArrayValue(String name, int index,ArrayList<Array> arrays ){

		float ret=0;

		for(int i=0;i<arrays.size();i++){

			if(arrays.get(i).name.equals(name)){
				ret= arrays.get(i).values[index];
			}
		}




		return ret;
	}

	private static float doMath(String op,float first,float second){
		if(op.equals("+")){
			return (first+second);
		}
		else if(op.equals("-")){
			return (first-second);
		}
		else if(op.equals("/")){
			return (first/second);
		}
		else{
			return (first*second);
		}

	}
	public static boolean isNum(String strNum) {
		try {
			float d =Float.parseFloat(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}
	public static boolean isOperator(String x){

		if(x.equals("+") || x.equals("-") || x.equals("*") || x.equals("/")){
			return true;
		}
		else {
			return false;
		}


	}
	public static boolean isAlpha(String x){
		if (x.matches("[a-zA-Z]+")){
			return true;
		}
		else
			return false;

	}
	public static float getVar(String name, ArrayList<Variable> vars){
		float ret=0;
		
		for(int i=0;i<vars.size();i++){
			if(vars.get(i).name.equals(name)){
				ret=(float)vars.get(i).value;
			}
		}

		return ret;

	}
	public static Stack<Float> reverseVals(Stack<Float> y)
	{
		Stack<Float> ret=new Stack<Float>();
		while(!y.isEmpty()){
			ret.push(y.pop());

		}

		return ret;

	}
	public static Stack<String> reverseOps(Stack<String> x)
	{
		Stack<String> ret=new Stack<String>();
		while(!x.isEmpty()){
			ret.push(x.pop());

		}

		return ret;

	}
}
