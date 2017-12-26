package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		//test for names beign in the map

		/*
		 * DFS while returning a path and then compare path lengths	
		 */ArrayList<String> answer = new ArrayList<String>();
		 boolean[] visited = new boolean[g.members.length];

		 for (int i=0; i<visited.length; i++) {
			 visited[i] = false;
		 }

		 if (p1.equals(p2)) {//if the same person
			 answer.add(p1);
			 return answer;
		 }

		 answer.add(p1);

		 Stack<String> chain = findChain(g, p1, p2, visited);

		 if (chain ==  null) {
			 return null;
		 }

		 Stack<String> b = new Stack<String>();
		 while (!chain.isEmpty()) {
			 b.push(chain.pop());
		 }
		 while (!b.isEmpty()) {
			 answer.add(b.pop());
		 }

		 return answer;
	}

	/**
	 * 
	 * In this method, all paths from one person to another are found recursively
	 * Then, the paths are put into a list and compared, and the shortest is returned
	 * 			This is the recursive path for shortestChain
	 * 
	 * @param g is the graph we are searching
	 * @param p1 is the person we start at
	 * @param p2 is the target person
	 * @param visited is an boolean array for all friends
	 * @return the shortest path from p1 to p2
	 */

	private static Stack<String> findChain (Graph g, String p1, String p2, boolean[] visited) {

		int p1num = g.map.get(p1);
		int p2num = g.map.get(p2);
		Person person1 = g.members[p1num];


		ArrayList<Stack<String>> stacks = new ArrayList<>();

		visited[p1num] = true;

		if (person1.first != null) { //if they have a friend

			//for each friend, if not visited, do dfs of the friends and next friends
			for (Friend ptr = person1.first; ptr!=null; ptr = ptr.next) {
				int num = ptr.fnum;
				Stack<String> path = new Stack<String>();
				if (g.members[num].name.equalsIgnoreCase(p2)) { 
					//System.out.println("hey");//if equal to target
					path.push(p2);
					stacks.add(path); 
					break;//return just the taret if its it
				} else {
					//send findchain on the friend if friend hasnt been visited
					//if they have, end the loop and return null for that path
					//System.out.println(visited[num] + "|" + g.members[num].name);
					if (visited[num]) {
						//System.out.println("here");
						//return null;
					} else {
						visited[num] = true;
						path.push(g.members[ptr.fnum].name);
						Stack<String> b = new Stack<String>();
						Stack<String> a = findChain (g, g.members[ptr.fnum].name, p2, visited);

						if (a!=null) {
							while (!(a.isEmpty())) {
								//System.out.println("made it");//flips the order
								b.push(a.pop());
							}
							while (!b.isEmpty()){ //puts the order back in 
								path.push(b.pop());
							}
							//path.push(g.members[num].name); //gonna be a stack with top being p1
							//System.out.println("this should be it");
							stacks.add(path);
						}

					}

				}

			}


		} else {
			//System.out.println("no friend");
			return null;
		}



		if (stacks.isEmpty()) {
			//System.out.println("Stacks is empty");
			return null;
		}

		if (stacks.size()==1) {
			return stacks.get(0);
		}

		Stack<String> answer = stacks.get(0);
		int size = stacks.get(0).size();

		//compares sizes of the stacks in the arraylist
		for (Stack<String> s : stacks) {
			if (s.size()<size) {
				answer = s;
			}
		}
		//System.out.println(("answer" + answer));
		return answer;
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		boolean[] visited = new boolean[g.members.length];
		ArrayList<ArrayList<String>> answer = new ArrayList<ArrayList<String>>();


		for (int i=0; i<visited.length; i++) { //intitializes the unvisited
			visited[i]= false;
		}

		for (int i=0; i<visited.length; i++) {
			if (!visited[i]) {//if it hasnt been hit
				visited[i]=true;
				if (g.members[i].school!=null) {
					if (g.members[i].school.equalsIgnoreCase(school)) {//if that is the school in question
						answer.add(cliqueFinder(g.members[i].name, g, school, visited)); 
					}
				}
			}
		}


		if (answer.isEmpty()) {
			return null;
		}
		return answer;

	}


	/**
	 * Recursive Method to find the cliques
	 * Uses dfs to go to each friends and test to see if they are at the school
	 * 
	 * 
	 * @param p1 is the person's name you start at
	 * @param g is the graph
	 * @param school is the school where the clique is found
	 * @param visited is whether the person has been visited
	 * @return an array list with a clique
	 */

	private static ArrayList<String> cliqueFinder (String p1, Graph g, String school, boolean[] visited) {

		int pnum = g.map.get(p1);
		visited[pnum]=true;
		ArrayList<String> answer = new ArrayList<String>();//what will be returned

		answer.add(p1);


		if (g.members[pnum].first!=null) { //if there are friends

			for (Friend ptr = g.members[pnum].first; ptr!=null; ptr = ptr.next) {
				if (visited[ptr.fnum]) {
					continue;
				}
				visited[ptr.fnum] = true;
				Person curr = g.members[ptr.fnum];
				if (curr.school!=null) {
					if (curr.school.equalsIgnoreCase(school)) { //if they are in the clique
						answer.add(curr.name);
						ArrayList<String> a = cliqueFinder(g.members[ptr.fnum].name, g, school, visited);
						if (a!=null) {
							for (String s : a) {
								if(!answer.contains(s)) {
									answer.add(s);
								}
							}

						}
					}
				}


			}

		}

		if (answer.isEmpty()) {
			return null;
		}




		return answer;
	}



	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {


		ArrayList <String> answer = new ArrayList<String>();  //what I will return

		ArrayList<String> numbering = new ArrayList<String>(g.members.length);//the dfs numbering




		boolean [] visited = new boolean[g.members.length]; //visited for dfs

		for (int i=0; i<visited.length; i++) {
			visited[i]=false;
		}

		//dfs to assign numbers to every vertex in my order
		for (int i=0; i<visited.length; i++) {
			if (!visited[i]) {
				dfs(g, visited, numbering, g.members[i].name);
			}
		}

		//now numbering is the dfs order i want




		for (int i=0; i<visited.length; i++) {
			visited[i]=false;
		}



		for (int i=0; i<visited.length; i++) {
			if (!visited[i]) {
				findConnectors(g, visited, numbering.get(i), numbering, answer, i);
				//need to test to see if the vertex sent is a connector
			}
		}
		//test to see if the start vertex is a connector






		if (!answer.isEmpty()) return answer;
		return null;

	}

	/*
	 * I need a method that goes through and find the lowest dfs number that each vertex
	 * connects to. If this is not lower than its parent, then its parent connects it to the graph
	 * 
	 * to test vertices, if a vertex has ore than one friend, and one of them is not visited 
	 * through the first find connectors of its first friend, then it is a connector
	 */

	/**
	 * 
	 * @param g is the graph
	 * @param visited keeps track of what people have been hit
	 * @param curr is the name of the current person
	 * @param numbering is the dfs numbering
	 * @param answer is the array list of connectors
	 * @param i is the number of the vertex sent
	 * @return an int that is the lowest vertex that can be reached from that person
	 */

	private static int findConnectors (Graph g, boolean[] visited, String curr,
			ArrayList<String> numbering, ArrayList<String> answer, int i) {

		int num = numbering.indexOf(curr);

		if (!visited[num]) {
			visited[num] = true;

			Person p1 = g.members[g.map.get(curr)];
			int friendCounter = 0;
			int minConnect = num;

			if (p1.first!=null) {
				for (Friend ptr = p1.first; ptr!=null; ptr=ptr.next) {
					int back = findConnectors(g, visited, g.members[ptr.fnum].name, numbering, answer, i);
					if (back >=num) {
						if (!answer.contains(curr)) {
							if (num!=i) {
								answer.add(curr);
							} else {
								if (ptr.next!=null) {
									//System.out.println(curr);
									for (Friend f = ptr.next; f!=null; f=f.next) {
										if (!visited[numbering.indexOf(g.members[f.fnum].name)]) {
											if (!answer.contains(curr)) {
												answer.add(curr);
											}
										}
									}
								}
							}

						}
					}

					if (back<minConnect) {
						minConnect=back;
					}


				}
			}

			return minConnect;

		} else { //if it has been hit already, return that number
			return numbering.indexOf(curr);
		}





	}

	private static void dfs(Graph g, boolean[] visited, ArrayList<String> numbering, String p1) {


		int pnum = g.map.get(p1);//number of the person in the graph
		visited[pnum] = true; 


		Person curr = g.members[pnum];

		numbering.add(curr.name); 



		if (curr.first!=null){

			for (Friend first = curr.first; first!=null; first=first.next) {

				if (!visited[first.fnum]) {
					dfs(g, visited, numbering, g.members[first.fnum].name);
				}
			}
		}

	}



}

