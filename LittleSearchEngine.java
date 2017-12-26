package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

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

		HashMap<String, Occurrence> Map = new HashMap<>();
		Scanner sc = new Scanner(new File(docFile));

		while (sc.hasNext()) {
			String word = sc.next();
			//checks for a valid keyword
			if (getKeyword(word)!=null) {
				word = getKeyword(word);
				//checks if word is there, if so adds to frequency
				if (Map.containsKey(word)) {
					Map.get(word).frequency ++;
					//System.out.println(word + ": " + Map.get(word).frequency);
					//if not, it sets the info 
				} else {
					//System.out.println(word + ": 1");
					Occurrence info = new Occurrence (docFile, 1);
					Map.put(word,  info);
				}
			}
		}

		sc.close();


		return Map;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 * 
	 * puts it into the master hashmap
	 * 	
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {

		Set<String> words = kws.keySet();

		for (String s: words) {
			//if the word is there, add last occurrence
			if (keywordsIndex.containsKey(s)) {
				ArrayList<Occurrence> occs = keywordsIndex.get(s);
				occs.add(kws.get(s));
				insertLastOccurrence(occs);
				//if it is not there, create an occurrence for it and add it to the list
			} else {
				ArrayList<Occurrence> occs = new ArrayList<>();
				Occurrence info = kws.get(s);
				occs.add(info);
				keywordsIndex.put(s, occs);
			}
		}


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

		//sets word to lower case and removes space around it
		word=word.toLowerCase();
		word=word.trim();

		//finds where the last regular character is
		int i;
		for (i = word.length()-1; i>=0; i--) {
			if (word.charAt(i) < 'a' || word.charAt(i) > 'z') {
				word = word.substring(0, i);
				continue;
			}
			break;
		}

		//tests to see if there are illegal characters within the word, returns null if so
		for (int c = 0; c<word.length(); c++) {
			if (word.charAt(c)<'a' || word.charAt(c)>'z') {
				return null;
			}
		}

		//test to see if it is a noise word
		if (noiseWords.contains(word)) {
			return null;
		}

		if (word.isEmpty()) {
			return null;
		}
		//if it reaches here, it is a word without illegal characters, and not a noise word, so can be added
		return word;
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
	 *         
	 *         
	 *       
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {


		//if the only occurnece, return null for no comparisons
		if (occs.size()==1) {
			return null;
		}

		Occurrence target = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int targetFrequency = target.frequency;

		ArrayList <Integer> indexes = new ArrayList<>();
		int low = 0; 
		int high = occs.size()-1;





		while (high>=low) {

			int middle = (high + low)/2;
			indexes.add(middle);

			//if it equals one, insert it right there
			if (occs.get(middle).frequency == targetFrequency) {
				occs.add(middle+1, target);
				break;
				//if greater than, reduce high to the half -1;
			} else if (occs.get(middle).frequency < targetFrequency) {
				high=middle-1;
				//if less than, check the second half (descending order)
			} else {
				low = low+1;
			}

		}

		//if the loop has ended, and low>high, it must be inserted into it
		//inserted at the point where it no longer belonged
		if (low>high) {
			occs.add(low, target);
		}


		//System.out.println(indexes);

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

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code

		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		//System.out.println("I am in the method");
		
		
		//has a list of all occurrences of kw1, in descending order
		ArrayList<Occurrence> files1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> files2 = keywordsIndex.get(kw2);
		ArrayList<Occurrence> topFiles = new ArrayList<Occurrence>();
		ArrayList<String> answer = new ArrayList<String>();

		//runs through each file for kw1
		if (files1!=null) {
			for (Occurrence occ : files1) {
				int total = occ.frequency;
				//the total is the amount in the first document
				if (files2 != null){
					for (Occurrence o : files2) {
						//adds the amount for the second word to the total
						if (o.document.equals(occ.document)) {
							total += o.frequency;
							break;
						}
					}
				}
				//then creates the occurrence, and adds it to top files
				Occurrence e = new Occurrence (occ.document, total);
				topFiles.add(e);
				//uses the already made function to add it to the right place in the list
				insertLastOccurrence(topFiles);
			}
		}

		boolean already = false;
		//does the same thing, but checks the files2
		if (files2!=null){
			for (Occurrence occ : files2) {
				already = false;
				//System.out.println(occ.document);
				int total = occ.frequency;
				if (files1!=null){
					for (Occurrence o : files1) {
						if (o.document.equals(occ.document)) {
							already = true;
							break;
						}
					}
				}
				//System.out.println(already);
				//if the file was counted from file 1, then dont count it
				if (already) continue;
				Occurrence e = new Occurrence (occ.document, total);
				topFiles.add(e);
				insertLastOccurrence(topFiles);
			}
		}

		

		if (topFiles.size()>5) {
			for (int i=0; i<5; i++) {
				answer.add(topFiles.get(i).document);

			}
		}else {
			for (Occurrence occ : topFiles) {
				answer.add(occ.document);
			}
		}


		//basically need to add the array names of files and how much they appear




		if (answer.isEmpty()) {
			return null;
		}
		return answer;

	}






}
