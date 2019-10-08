package trie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() { }

	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	//static Scanner stdin = new Scanner(System.in);


	//Gives a similarity value
	private static int matchValue(TrieNode existing, TrieNode toAdd, String[] allWords){
		System.out.println(allWords[existing.substr.wordIndex]);
		System.out.println(allWords[toAdd.substr.wordIndex]);

		String first=allWords[existing.substr.wordIndex].substring(existing.substr.startIndex,existing.substr.endIndex+1);
		String second=allWords[toAdd.substr.wordIndex].substring(toAdd.substr.startIndex,toAdd.substr.endIndex+1);
		System.out.println("Compare: "+first+ "\twith" +"\t"+ second);
		
		int count=1;
		int matchValue=0;
	/*	if(first.charAt(0)!=second.charAt(0)){
			return 0;
		}*/
		
		//System.out.println("From: "+ existing.substr.startIndex+" To: "+existing.substr.endIndex);
		if(first.length()<=second.length()){
			int imax=(short)(existing.substr.endIndex-existing.substr.startIndex+1);
		for(int i=0;i<imax;i++){
			
			//System.out.println("UM WE RUN COUNT: " );
			if(first.charAt(i)==second.charAt(i)){
				matchValue++;
				
			}
			else if(first.charAt(i)!=second.charAt(i)){
				
				break;
			}
		}
		}
		else{
			int imax=(short)(toAdd.substr.endIndex-toAdd.substr.startIndex+1);
			for(int i=0;i<imax;i++){
				
				
				if(first.charAt(i)==second.charAt(i)){
					matchValue++;
					
				}
				else if(first.charAt(i)!=second.charAt(i)){
					
					break;
				}
			
		}	
		}
		return matchValue;
	}
	private static int matchValue(TrieNode existing, String toAdd, String[] allWords){
		System.out.println(allWords[existing.substr.wordIndex]);
		System.out.println(toAdd);

		String first=allWords[existing.substr.wordIndex].substring(existing.substr.startIndex,existing.substr.endIndex+1);
		String second=toAdd;
		System.out.println("Compare: "+first+ "\twith" +"\t"+ second);
		
		int count=1;
		int matchValue=0;
	/*	if(first.charAt(0)!=second.charAt(0)){
			return 0;
		}*/
		
		System.out.println("From: "+ existing.substr.startIndex+" To: "+existing.substr.endIndex);
		if(first.length()<=second.length()){
			int imax=(short)(existing.substr.endIndex-existing.substr.startIndex+1);
		for(int i=0;i<imax;i++){
			
			//sSystem.out.println("UM WE RUN COUNT: " );
			if(first.charAt(i)==second.charAt(i)){
				matchValue++;
				
			}
			else if(first.charAt(i)!=second.charAt(i)){
				
				break;
			}
		}
		}
		else{
			int imax=(short)(toAdd.length());
			for(int i=0;i<imax;i++){
				
				
				if(first.charAt(i)==second.charAt(i)){
					matchValue++;
					
				}
				else if(first.charAt(i)!=second.charAt(i)){
					
					break;
				}
			
		}	
		}
		return matchValue;
	}
	

	
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root=new TrieNode(null,null,null);
	
		TrieNode rootChild=new TrieNode(new Indexes(0,(short)0,(short)(allWords[0].length()-1)),null,null);
		root.firstChild=rootChild;
		TrieNode curr=root.firstChild;
		TrieNode prev=root;
		for(int i=1;i<allWords.length;i++){

			TrieNode passIn=new TrieNode(new Indexes(i,(short)0,(short)(allWords[i].length()-1)),null,null);

			buildTrie(rootChild,passIn,prev,allWords);
			
	}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

		return root;
	}

	private static void buildTrie(TrieNode existing,TrieNode toAdd,TrieNode prev,String[]allWords){
		
		TrieNode pointer=existing;
		TrieNode dad=prev;
		
	
		
		int compare=(existing.substr.endIndex-existing.substr.startIndex)+1;
		

		int matchValue=matchValue(pointer,toAdd,allWords );
		//System.out.println("Compare Val= "+compare);
		//System.out.println("MATCH VALUE " + matchValue);

		if(matchValue==0){

			if(pointer.sibling==null){
				
				//System.out.println("WE ARE GONN ADD:" +allWords[toAdd.substr.wordIndex].substring(toAdd.substr.startIndex,toAdd.substr.endIndex+1));
				pointer.sibling=toAdd;
			}else{
				buildTrie(pointer.sibling,toAdd,dad,allWords);
			}
		}
		else if(matchValue<=compare){

			if(pointer.firstChild!=null && matchValue==compare){
				toAdd.substr.startIndex=(short)(pointer.substr.endIndex+1);
				pointer=pointer.firstChild;
				
				buildTrie(pointer,toAdd,dad.firstChild,allWords); 
				
			}
			else{
				TrieNode newChild=null;
				//System.out.println("WE CHANGE STUFF UP");
				if(dad.substr!=null){ 
					
					//System.out.println(dad.substr.startIndex);
					newChild=new TrieNode(new Indexes(pointer.substr.wordIndex,(short) (pointer.substr.startIndex+matchValue),(short) (pointer.substr.endIndex)),null,null);}

				else{
					newChild=new TrieNode(new Indexes(pointer.substr.wordIndex,(short) matchValue,(short) (pointer.substr.endIndex)),null,null);}	
				if(pointer.firstChild!=null){
				newChild.firstChild=pointer.firstChild;
				}
				pointer.firstChild=newChild;

				pointer.substr.endIndex=(short)(newChild.substr.startIndex-1);
				System.out.println(pointer.substr.endIndex);
				toAdd.substr.startIndex=(short) (pointer.substr.endIndex+1);
				dad=pointer;
				pointer=pointer.firstChild;
				
				buildTrie(pointer,toAdd,dad.firstChild,allWords);

			}
		}
		return;

	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
			String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		ArrayList<TrieNode> ret=new ArrayList<TrieNode>();
		
		TrieNode pointer=root.firstChild;
		
		
		
		ret=completionList(pointer,prefix,allWords,ret);
		return ret;
	}
	
	private static ArrayList<TrieNode> completionList(TrieNode pointer,String prefix,String[]allWords,ArrayList<TrieNode> ret){
		int matchValue=matchValue(pointer,prefix,allWords);
		TrieNode existing=pointer;
		int compare=prefix.length();
		TrieNode vertical=pointer;
		//System.out.println("MATCH VALUE BABY: "+matchValue +"COMPARED TO: "+ compare);
		 if(matchValue==0){
				//System.out.println("TREY WAY");
				if(pointer.sibling!=null){
					pointer=pointer.sibling;
					ret=completionList(pointer,prefix,allWords,ret);
				}else{
					return ret;
				}
			}
		
		 else if(matchValue==compare){
			if(pointer.firstChild==null){
				ret.add(pointer);
				return ret;
			}
			else{
				
				pointer=pointer.firstChild;
				
				
				while(pointer!=null){
					if(pointer.firstChild==null){
					ret.add(pointer);
					}
					else{
						ret=completionList(pointer,allWords[pointer.substr.wordIndex].substring(pointer.substr.startIndex, pointer.substr.endIndex+1),allWords,ret);
					}
				//	System.out.println("TIME TO ADD STUFF");
					pointer=pointer.sibling;

					
				}
				
				
			
				
				return ret;

			}
		}
		else {
				if(pointer.firstChild!=null){
				prefix=prefix.substring(pointer.substr.endIndex+1, prefix.length());
				pointer=pointer.firstChild;
				//System.out.println("RECURSION TIME BAAABY");
				 return ret=completionList(pointer,prefix,allWords,ret);
				}
			}
	
	
		
		return ret;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
					.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}

		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
}
