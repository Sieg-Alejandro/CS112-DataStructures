package friends;

import structures.Queue;
import structures.Stack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Friends {


	public static void main(String[]args) throws FileNotFoundException{
		Scanner sc = new Scanner(new File("doc4.txt"));

		Graph tester=new Graph(sc);
		for(int i=0;i<tester.members.length;i++){
			System.out.println(i +": "+tester.members[i].name +" goes to: " +tester.members[i].school) ;

		}

		ArrayList<ArrayList<Integer>> y=getAdjacencyList(tester);
		for(ArrayList<Integer> x: getAdjacencyList(tester)){
			System.out.println(x);
		}
		System.out.println(shortestChain(tester,"sam","aparna"));
		System.out.println(connectors(tester));
		System.out.println(cliques(tester,"rutgers"));
	}

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

		/** COMPLETE THIS METHOD **/
		//Get the person number for each person
		int pnumber1=g.map.get(p1);
		int pnumber2=g.map.get(p2);

		//Load the appropriate Adjacency List
		ArrayList<ArrayList<Integer>> adj=getAdjacencyList(g);

		//Load the array containing the shortest path to every
		//other person starting from a given person
		int[] shortestPath=loadShortestPath(pnumber1,adj,g);

		//Used stacks to maintain order
		Stack<Integer> shortnums=new Stack<Integer>();

		//String version for return
		ArrayList<String>  ret=new ArrayList<String>();

		//Back trace the path using shortestPath that we just loaded in
		int curr=pnumber2;

		while(shortestPath[curr]!=-1){

			shortnums.push(curr);
			curr=shortestPath[curr];

		}
		if(curr==pnumber1){
			shortnums.push(curr);	
		}

		//Unload our stack into the final ArrayList
		while(!shortnums.isEmpty()){

			ret.add(g.members[shortnums.pop()].name);
		}


		//Return
		return ret;
	}


	private static int[] loadShortestPath(int source,ArrayList<ArrayList<Integer>> adj, Graph g ){
		//Stores which nodes have been visited or not
		boolean[] visited=new boolean[g.members.length];

		//This is what stores the shortest paths using predecessor
		int[] prev=new int[g.members.length];

		//Set all of them to -1. 
		for(int i=0;i<prev.length;i++){
			prev[i]=-1;
		}

		//Will hold the people who are to be checked
		Queue<Integer> fringe=new Queue<Integer>();

		//Mark the source as visited
		visited[source]=true;

		fringe.enqueue(source);


		//BFS Stuff
		while(!fringe.isEmpty()){
			int v=fringe.dequeue();


			for(int w : adj.get(v)){

				if(visited[w]==false){

					visited[w]=true;
					prev[w]=v;
					fringe.enqueue(w);

				}
			}
		}
		return prev;
	}

	//Creates Adjacency List for a given graph
	private static ArrayList<ArrayList<Integer>>getAdjacencyList(Graph g){
		ArrayList<ArrayList<Integer>> ret=new ArrayList<ArrayList<Integer>>();

		for(Person i : g.members){

			ArrayList<Integer> temp=new ArrayList<Integer>();

			Friend tempfriend=i.first;

			while(tempfriend!=null){
				temp.add(tempfriend.fnum);
				tempfriend=tempfriend.next;
			}

			ret.add(temp);
		}
		return ret;
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
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school ) {

		/** COMPLETE THIS METHOD **/

		ArrayList<ArrayList<String>> ret=new ArrayList<ArrayList<String>>();

		ArrayList<ArrayList<Integer>> adj=getAdjacencyList(g);


		for(int i=0; i<adj.size();i++){

			boolean[] visited=new boolean[g.members.length];
			ArrayList<String> tempie= new ArrayList<String>();

			//Return cliques using DFS starting for each person
			tempie=cliqueDFS(school,g,adj, i ,visited,tempie);
			//System.out.println("NUMBER: "+ i  + " - " + tempie);
			//Sometimes null is returned
			if(tempie.isEmpty()){
				continue;
			}

			//Cleanup work to ensure no duplicates
			else{

				boolean inhere=false;
				for(int j=0;j<ret.size();j++){
					for(String x: tempie){
						if(ret.get(j).contains(x)){
							inhere=true;
							break;
						}
					}
					if(inhere==true){
						break;
					}
				}

				if(inhere==false){
					ret.add(tempie);
				}


			}
		}

		return ret;

	}

	private  static ArrayList<String> cliqueDFS(String school, Graph g, ArrayList<ArrayList<Integer>> adj,
			int person, boolean[] visited, ArrayList<String> click){

		if(g.members[person].school==null){
			return click;
		}
		else if(!g.members[person].school.equals(school)){
			return click;
		}
		else if (g.members[person].school.equals(school)){
			click.add(g.members[person].name);

			for(int i: adj.get(person)){


				if(visited[i]==true){
					continue;
				}

				else{
					visited[i]=true;
					click.addAll(cliqueDFS(school, g, adj, i, visited, click));

					ArrayList<String> temp=new ArrayList<String>();
					for(String s: click){
						if(!temp.contains(s)){
							temp.add(s);
						}
					}
					click=temp;

				}

			}

		}	
		return click;
	}
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be feound.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		/** COMPLETE THIS METHOD **/
		ArrayList<String> ret=new ArrayList<String>();
		final int COUNT=0;

		ArrayList<ArrayList<Integer>> adj=getAdjacencyList(g);
	//	int[][] vertexnums=new int[g.members.length][2];



		for(int i=0; i<adj.size();i++){
			int[][] vertexnums=new int[g.members.length][2];

			boolean[] visited=new boolean[g.members.length];
			ArrayList<String>temp =dfsConnect2(i,visited,adj,COUNT,g,vertexnums);

		

			

			for(String x : temp){
				if(!ret.contains(x)){
					ret.add(x);
				}

			}

		}




		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return ret;

	}

	private static ArrayList<String> dfsConnect(int source,boolean[] visited,ArrayList<ArrayList<Integer>> adj, 
			int count,Graph g, int[][] vertexnums){
		ArrayList<String> ret=new ArrayList<String>();
		int dfscount=count+1;
		int back=dfscount;
		vertexnums[source][0]=dfscount;
		vertexnums[source][1]=back;

		//System.out.println("Source "+g.members[source].name +": "+"DFSNum= "+dfscount+"\tBack= "+back);


		visited[source]=true;


		for(int i: adj.get(source)){
			//System.out.println("\t"+g.members[source].name+ " Check neighbor: "+g.members[i].name);
			if(visited[i]){

				back=Math.min(back, vertexnums[i][0]);
				vertexnums[source][1]=back;

				//				System.out.println("\tSKIP "+g.members[i].name
				//						+"\tAnd set "+g.members[source].name+"'s back to: "+ back);
				//				System.out.println();

			}
			else{
				ret.addAll(dfsConnect(i,visited,adj,dfscount,g,vertexnums));
				visited[i]=true;
				if(dfscount>vertexnums[i][1]){
					back=Math.min(back,vertexnums[i][1] );
					vertexnums[source][1]=back;
					//	System.out.println("\tAnd set "+g.members[source].name+"'s back to: "+ back);
					//	System.out.println();

				}
				else if((dfscount<=vertexnums[i][1])&& adj.get(source).size()>1){
					//System.out.println();
					//	System.out.println("ATTENTION. "+g.members[source].name +" IS A POSSIBLE CONNECTER YA HEARD");
					if(dfscount!=1){

						if(!ret.contains(g.members[source].name)){
							ret.add(g.members[source].name);
						}

					}
				}

			}

		}



		return ret;
	}
	private static ArrayList<String> dfsConnect2(int source,boolean[] visited,ArrayList<ArrayList<Integer>> adj, 
			int count,Graph g, int[][] vertexnums){
		ArrayList<String> ret=new ArrayList<String>();
		int dfscount=count+1;
		int back=dfscount;
		vertexnums[source][0]=dfscount;
		vertexnums[source][1]=back;

		//System.out.println("Source "+g.members[source].name +": "+"DFSNum= "+dfscount+"\tBack= "+back);


		visited[source]=true;


		for(int i: adj.get(source)){
			//System.out.println("\t"+g.members[source].name+ " Check neighbor: "+g.members[i].name);
			if(visited[i]){

				back=Math.min(back, vertexnums[i][0]);
				vertexnums[source][1]=back;

				//				System.out.println("\tSKIP "+g.members[i].name
				//						+"\tAnd set "+g.members[source].name+"'s back to: "+ back);
				//				System.out.println();

			}
			else{
				ret.addAll(dfsConnect(i,visited,adj,dfscount,g,vertexnums));
				visited[i]=true;
				if(dfscount>vertexnums[i][1]){
					back=Math.min(back,vertexnums[i][1] );
					vertexnums[source][1]=back;
					//	System.out.println("\tAnd set "+g.members[source].name+"'s back to: "+ back);
					//	System.out.println();

				}
				else if((dfscount<=vertexnums[i][1])&& adj.get(i).size()==1){
					//System.out.println();
					if(adj.get(i).get(0)==source){


						ret.add(g.members[source].name);


					}
				}

			}

		}



		return ret;
	}
}

