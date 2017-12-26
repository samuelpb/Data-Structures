package trie;

import java.util.ArrayList;

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
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

		TrieNode front = new TrieNode (null, null, null );
		
		if (allWords.length == 0) return front;

		//sets the original node if the trie is empty
		if (front.firstChild==null) {
			TrieNode newLeaf;
			Indexes substr = new Indexes (0, (short) 0, (short) (allWords[0].length()-1));
			newLeaf = new TrieNode (substr, null, null);
			front.firstChild = newLeaf;
		}

		for (int i=1; i<allWords.length; i++) {

			buildTrie(front.firstChild, allWords, i, front);
		}






		return front;
	}


	private static void buildTrie (TrieNode curr, String[] allWords, int index, TrieNode parent) {



		int compared = compareNode(curr, allWords[index], allWords);

		if (compared==0) {
			if (curr.firstChild==null) {
				//create an over arching parent
				//shouldnt happen
			}else {
				buildTrie(curr.firstChild, allWords, index, curr);
			}
		} else if (compared == -1) {
			//add a new adjacent Leaf since there is no sibling
			if (curr.sibling==null) {
				Indexes substr;
				if (parent.substr==null) {
					substr = new Indexes (index, (short)0, (short)(allWords[index].length()-1));
				}else {
					if (parent.sibling == curr) {
						substr = new Indexes (index, (short)parent.substr.startIndex, (short)(allWords[index].length()-1));
					}else{
						substr = new Indexes (index, (short)(parent.substr.endIndex+1), (short)(allWords[index].length()-1));
					}
				}
				//if (alphabetical ( allWords[curr.substr.wordIndex], allWords[index])) {
				TrieNode newLeaf = new TrieNode (substr, null, null);
				curr.sibling = newLeaf;

			} else {
				//if there is a sibling, just build tree from it if alphabetical
				if (curr.sibling != null) {
					buildTrie(curr.sibling, allWords, index, curr);
				} else {
					//if not alphabetical, it wont match siblings, so insert leaf  node before;
					Indexes substr;
					if (parent.substr==null) {
						substr = new Indexes (index, (short)0, (short)(allWords[index].length()-1));
					}else {
						if (parent.sibling == curr) {
							substr = new Indexes (index, (short)parent.substr.startIndex, (short)(allWords[index].length()-1));
						}else{
							substr = new Indexes (index, (short)(parent.substr.endIndex+1), (short)(allWords[index].length()-1));
						}					}
					TrieNode newLeaf = new TrieNode (substr, null, null);
					curr.sibling = newLeaf;

				}
			}

		} else if (compared == 1) {
			//System.out.println("Partial");
			partialRelationBuild (parent, curr, index, allWords);
		} else {
			buildTrie(curr.firstChild, allWords, index, curr);
		}


	}

	/* Cases to Deal With:
	 * 		First Node in Tree
	 * 		Creating a new Leaf
	 * 		Creating a new Parent and Leaf
	 * 		Creating a new Leaf as a sibling (before or after)
	 * 		Crating a new Parent as a sibling 
	 */

	/*
	 * THIS COMPARE METHOD IS TO COMPARE a word TO A NODE
	 * return values: 0--belongs underneath node
	 * 				 -1 -- does not belong underneath node
	 * 				  1--is partly related to the substring at the node
	 * 				  2--is more than related
	 * 
	 * 
	 * 
	 */

	private static int compareNode (TrieNode node, String word, String[] allWords) {

		if (node.substr==null) return -1;
		String nodeWord = allWords[node.substr.wordIndex];

		int startIndex = node.substr.startIndex;
		int counter=node.substr.startIndex;
		int endIndex = node.substr.endIndex;


		//counts the character relations from beginning
		for (int i=startIndex; i< Math.min(nodeWord.length(), word.length()); i++) {
			if (nodeWord.charAt(i)==word.charAt(i)) {
				counter++;
				continue;
			}
			break;
		}

		//System.out.println("Counter : " + counter);
		//System.out.println("Word: "+ word+ "\t" + "NodeWord: " + allWords[node.substr.wordIndex]);
		//System.out.println(counter-startIndex);
		//System.out.println(counter-endIndex);
		if (counter==startIndex) {
			return -1;
		} else if (counter<endIndex+1) {
			return 1;
		} else if (counter == endIndex+1) {
			return 0;
		} else {
			return 2;
		}

	}


	/*
	 * will create a new parent node on top of one
	 * then changes the old two nodes to leaves under it, while not touching other nodes around
	 */
	private static void partialRelationBuild (TrieNode parent, TrieNode child, int index, String[] allWords ) {

		int firstAppearance = Math.min(child.substr.wordIndex, index);
		int startIndex = child.substr.startIndex;
		int i=0;

		//tests for how long the relation is
		for (i=startIndex; i<child.substr.endIndex; i++) {
			if (allWords[child.substr.wordIndex].charAt(i) == allWords[index].charAt(i) ) {
				continue;
			}break;
		}

		//creates a new parent node named temp under the parent above the child
		Indexes tempIndexes = new Indexes (firstAppearance, (short) startIndex, (short)(i-1));
		TrieNode temp = new TrieNode (tempIndexes, child, child.sibling);
		//edits parent to point towards the temp
		if (parent.firstChild == child) {
			parent.firstChild=temp; 
		} else {
			parent.sibling=temp;
		}
		//edits child's indexes
		child.substr.startIndex = (short) (i);
		child.sibling = null;

		//builds a node to attach to the sibling
		TrieNode newLeaf;
		Indexes substr = new Indexes (index, (short)i, (short)(allWords[index].length()-1));
		newLeaf = new TrieNode (substr, null, null);
		child.sibling = newLeaf;


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
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		ArrayList<TrieNode> list= new ArrayList<>();

		if (prefix.length()==0) {
			return getIndexes(root, allWords, prefix);
		}
		
		//if the main root, move onto frist child
		if (root.substr==null) {
			//if no child, return null
			if (root.firstChild==null) {return null;}
			//if there are, start completion list at child
			return completionList(root.firstChild, allWords, prefix);
		}

		int compared = compareForCompletion(root, prefix, allWords);
		
		
		
		
		if (compared  == -1) {
			if (root.sibling==null) {
				return null;
			} else {
				return completionList(root.sibling, allWords, prefix);
			}
		}

		//if the prefix is a prefix to the noe/is the node
		if (compared == 1) {
			if (root.firstChild!=null) {
				list.addAll(getIndexes(root.firstChild, allWords, prefix));
			} else {
				list.add(root);
			}
		} 
		//have a way to check other siblings
		
		//if the prefix is longer than the node, have to check each child
		

		if (compared == 2) {
			if (root.firstChild==null) {return null;}
			return completionList (root.firstChild, allWords, prefix);
		}

		if (list.isEmpty()) {
			return null;
		} else {
			return list;
		}
	
	}

	
	///it doesnt go and check the last sibling if it returns 0
	
	/*
	 * return values: 0--belongs underneath node
	 * 				 -1 -- does not belong underneath node
	 * 				  1--is partly related to the substring at the node
	 * 				  2--is more than related
	 * 
	 * 
	 * 
	 */
	private static int compareForCompletion (TrieNode node, String prefix, String[] allWords) {

		String nodeWord = allWords[node.substr.wordIndex];
		int startIndex = node.substr.startIndex;
		int endIndex = node.substr.endIndex;

		
		
		
		if (prefix.equals(nodeWord.substring(0, Math.min(endIndex+1, prefix.length())))) {
			//return all nodes underneath this one if the prefix is the node/a prefix for the node
			
			return 1;
		}else {
			//if the node's substring is contained in the prefix
			if (prefix.length()-1 > endIndex) {
				if ((prefix.substring(0, endIndex+1).equals(nodeWord.substring(0, endIndex+1)))) {
					return 2;
				}
			}
			return -1;
		}



		



	}

	/*
	 * Need a method to find which nodes
	 * returns a list of nodes
	 */

	private static ArrayList<TrieNode> getIndexes (TrieNode curr, String[] allWords, String prefix) {

		ArrayList<TrieNode> nodes = new ArrayList<>();;

		if (curr.sibling==null) {
			//if no sibling, dont owrry about its siblings
		} else {
			//if theres a sibling, get the indexes from the sibling as well
			nodes.addAll(getIndexes(curr.sibling, allWords, prefix));
		}

		if (curr.firstChild==null) {
			//means it is a leaf node, so add it
			nodes.add(curr);
			return nodes;
		}else {
			//a parent node, get its children
			nodes.addAll(getIndexes(curr.firstChild, allWords, prefix));
		}


		return nodes;
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
