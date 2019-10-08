package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {


	public static void main(String[] args) throws FileNotFoundException{

		
		
		LittleSearchEngine tester=new LittleSearchEngine();
		//tester.loadNoiseWords("noisewords.txt");
		tester.makeIndex("docs.txt", "noisewords.txt");
		System.out.println(tester.top5search("red", "car"));
		
		//System.out.println(tester.keywordsIndex.get("abcd"));
		
		//System.out.println(tester.keywordsIndex);
		
		System.out.println(tester.getKeyword("sWord"));
	}
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	//Simply checks if a String is only letters
	private boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}
	
	//Made for testing
	private void loadNoiseWords(String noiseWordsFile) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

	}
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
			throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		HashMap<String,Occurrence>	kws = new HashMap<String,Occurrence>(1000,2.0f);

		//System.out.println("KWS SIZE BOYY"+ kws.size());

		Scanner sc = new Scanner(new File(docFile));
		while (sc.hasNext()) {
			String word = sc.next();

			String kw= getKeyword(word);

			if(kw!=null){
				//System.out.println(kw);
				Occurrence curr=kws.get(kw);

				if(curr!=null){
					curr.frequency++;
					//System.out.println("Size WAS: " + kws.size());

					kws.put(kw, curr);
					//System.out.println("Update : " +kw);
				//	System.out.println("Size is now" + kws.size());
//System.out.println("------------------------------------------------");
				}
				else{
					kws.put(kw,new Occurrence(docFile,1));
					//System.out.println("Add: " + kw);
					//System.out.println("Size is now" + kws.size());
					//System.out.println("-------------------------------------------------------------------");
				}



			}


		}

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		//System.out.println("HELLO"+kws.size());
		return kws;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
	
		//For every key value pair in the hashmap
		for(Map.Entry<String, Occurrence> kw :kws.entrySet()){
			//Gets a parsed version of the word via getKey method
			String word=kw.getKey();

			//Get the occurence object
			Occurrence kwOcc=kw.getValue();

			//Grab the arrayList from the master hashmap using the word
			//The variable below will be null if it does not exist in the master occurence list
			ArrayList<Occurrence> masterOccurrenceList=keywordsIndex.get(word);

			//If the word already exists
			if(masterOccurrenceList!=null){

				//Add the Occurence object to the end of the ArrList
				masterOccurrenceList.add(kwOcc);

				//Call insertLastOccurence to properly place it in the ArrList
				//Makes sure it's in descending order
				insertLastOccurrence(masterOccurrenceList);

				//Finally, update the ArrList in the master hashmap
				keywordsIndex.put(word, masterOccurrenceList);
			}

			//Else, create a new ArrList of Occurrences for that word
			else{
				ArrayList<Occurrence> toPut=new ArrayList<Occurrence>();
				toPut.add(kwOcc);
				keywordsIndex.put(word, toPut);

			}

		}

		/** COMPLETE THIS METHOD **/
	}


	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		//Gets rid of all the punctuation
		
		//word.replaceAll("\"","");
		//word=word.replaceAll(")","");
		//System.out.println("Words is:"+ word);
		word=word.toLowerCase();
		int count = word.length()-1;
		while (0 < count)
		{
			char c = word.charAt(count);
			//System.out.println(word.charAt(count));
			
			if ((Character.isLetter(c))){
				break;
			}
			count--;
		}
		
		
		
		word= word.substring(0,count+1);
		
		//isAlpha checks to see if a string is ONLY letters or not.
		if (!isAlpha(word)){
			return null;
		}
		
		//Checks to see if its a noiseword
		if(noiseWords.contains(word)){
			return null;
		}
		else{
			return word;
		}

	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> indexes=new ArrayList<Integer>();
		int left=0;
		int right=(occs.size()-2);

		//Holds the last Occurrence object in the ArrList
		Occurrence last=occs.get(occs.size()-1);

		//Holds the frequency of the last Occurence 
		int lastOcc=last.frequency;

	
		int pos=0;

		//Binary Search
		while(left<=right){

			int mid = (left+right)/2;

			//Add mid index to return list for testing
			indexes.add(mid);


			//Check for equivalence
			if(lastOcc == occs.get(mid).frequency){
				pos= mid;

				//Add to the appropriate index- ArrayList automatically does the shifting
				occs.add(mid,last);
				//Remove the last index
				occs.remove(occs.size()-1);

	
				return indexes;
			}

			//If last occ is less than mid
			else if(lastOcc < occs.get(mid).frequency){
				left=mid+1;
			}

			//if last occ is greater than mid
			else {
				right=mid-1;
			}
		}

		//Insert at appropriate position- ArrayList automatically shifts the whole array
		occs.add(left,last);
		//Remove the last item
		occs.remove(occs.size()-1);


		//return now
		return indexes;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
			throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	
	//This is a private version of insertLastOccurence that returns 
	//An ArrayList rather than changing the master one.
	private ArrayList<Occurrence> fix(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> indexes=new ArrayList<Integer>();
		int left=0;
		int right=(occs.size()-2);

		//Holds the last Occurrence object in the ArrList
		Occurrence last=occs.get(occs.size()-1);

		//Holds the frequency of the last Occurence 
		int lastOcc=last.frequency;

		int pos=0;

		//Binary Search
		while(left<=right){

			int mid = (left+right)/2;

			//Add mid index to return list for testing
			indexes.add(mid);


			//Check for equivalence
			if(lastOcc == occs.get(mid).frequency){
				pos= mid;

				//Add to the appropriate index- ArrayList automatically does the shifting
				occs.add(mid,last);
				//Remove the last index
				occs.remove(occs.size()-1);

//				//Debug print
//				System.out.println("AFTER PROPER INSERTION");
//				for(Occurrence x : occs){
//					System.out.print(x.frequency+ ",");
//				}
				//Return now
//				System.out.println();
//				System.out.println("---------------------------------------------------------------");

				return occs;
			}

			//If last occ is less than mid
			else if(lastOcc < occs.get(mid).frequency){
				left=mid+1;
			}

			//if last occ is greater than mid
			else {
				right=mid-1;
			}
		}

		//Insert at appropriate position- ArrayList automatically shifts the whole array
		occs.add(left,last);
		
		//Remove the last item
		occs.remove(occs.size()-1);



		//return now
		return occs;
	}
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		//Holds the sorted list of Occurences
		ArrayList<Occurrence> ret=new ArrayList<Occurrence>();
		
		//Used for special cases where one is null
		ArrayList<String> nullret=new ArrayList<String>();
		
		//Get both ArrayLists for both words from the master hash table
		ArrayList<Occurrence> key1=keywordsIndex.get(kw1);
		ArrayList<Occurrence> key2=keywordsIndex.get(kw2);
		
		System.out.println(key1);
		System.out.println(key2+"\n");
		//Handles a words that don't exist in either 1 or none of the documents
//------------------------------------------------------------------------------------------
		if(key1==null && key2==null){
			return nullret;
		}
		if(key1==null){
			if(key2.size()<5){
				for(int i=0;i<key2.size();i++){
					nullret.add(key2.get(i).document);

				}
			}
			else{

				for(int i=0;i<5;i++){

					nullret.add(key2.get(i).document);
				}
			}
			return nullret;
		}
		
		if(key2==null){
			if(key1.size()<5){
				for(int i=0;i<key1.size();i++){
					nullret.add(key1.get(i).document);
				}
			}
			else{
				for(int i=0;i<5;i++){
					nullret.add(key1.get(i).document);
				}
			}
			return nullret;
		}
//-------------------------------------------------------------------------------------------

	
		//Finds the bigger list and stores accordingly.
		ArrayList<Occurrence> bigger;
		ArrayList<Occurrence> smaller;
		if(key1.size()>=key2.size()){
			smaller=key2;
			bigger=key1;

		}
		else{
			bigger=key2;
			smaller=key1;
		}

		
		//Instantiate the pointers
		Iterator<Occurrence> it1=smaller.iterator();
		Iterator<Occurrence> it2=bigger.iterator();
		
		//For every element in the smaller one, check for equivalence against the bigger one. 
		//IF there is a match, delete the smaller or equal one.
		//Uses pointer to avoid nullpointerexception during when something is removed;
		while(it1.hasNext()){
			Occurrence x=it1.next();
			while(it2.hasNext()){
				Occurrence y=it2.next();	
				
				if(x.document.equals(y.document)){
					if(x.frequency>y.frequency){
						it2.remove();
					}
					else{
						it1.remove();
					}

				}
			}
			it2=bigger.iterator();
		}
		
		//Take whatever is leftover and pass it into a final arraylist 
		//Use the private recycled sort method to put them in descending order
		for(Occurrence z : smaller){
			ret.add(z);

			ret=fix(ret);
		}
		for(Occurrence w : bigger){
			ret.add(w);
			ret=fix(ret);

		}
		
		//ArrayList lastly is what stores the strings of the documents
		ArrayList<String> lastly=new ArrayList<String>();

		//If list is less than size 5, get all of it
		if(ret.size()<5){
			for(int i=0;i<ret.size();i++){
				lastly.add(ret.get(i).document);

			}
		}
		//else only get 5
		else{

			for(int i=0;i<5;i++){

				lastly.add(ret.get(i).document);
			}
		}
		
		//Return
		return lastly;

	}
}
