import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gurobi.*;

public class Main {
        public static void main(String[] args) throws IOException{
                List<List<String>> list = readValues();
                System.out.println(list.get(0));
		List<Integer> valueList = new ArrayList<Integer>();
		List<Integer> weightList = new ArrayList<Integer>();
		for(int i=1;i<list.size();i++) {
			valueList.add(Integer.parseInt(list.get(i).get(4))); 
			weightList.add(Integer.parseInt(list.get(i).get(5)));
			}
		List<List<String>> list1 = readSequential();
		List<ArrayList<Double>> sequential_data = new ArrayList<ArrayList<Double>>();
        
		for (int i=1;i<list1.size();i++){
			ArrayList<Double> row = new ArrayList<>();
			for (int j=1;j<list1.get(0).size();j++){
				row.add(Double.parseDouble(list1.get(i).get(j)));
			}
			sequential_data.add(row);
		}
		System.out.println(sequential_data.get(0).get(1));
		
		
		int maxWeight = 1800000;
		int[][] keep = new int[valueList.size()+1][maxWeight+1];
		List<Integer> knapsackSolution = new ArrayList<Integer>(); // new arraylist to store ids' of Knapsack problem
		Solver(weightList,valueList, maxWeight, weightList.size(), knapsackSolution);
		
		//double optimal = bottomUp(weightList,valueList,keep,maxWeight);
		//System.out.println("Optimal: "+optimal);
		
		//KnapsackSolver(i, valueList, weightList, maxWeight);
		//System.out.println(KnapsackSolver(i,valueList, weightList, maxWeight));
		
		//Knapsack(valueList, weightList, maxWeight, knapsackSolution );
		//System.out.println(knapsackSolution);
		int src = knapsackSolution.get(0);
    	int dest = knapsackSolution.get(0);
		AssignSrcDest(src, dest, knapsackSolution,valueList);
		
		TSP(sequential_data,knapsackSolution, src, dest);
		
		/* double[][] graph = sequential_data.stream()
	                .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
	                .toArray(double[][]::new);
	 
	        //System.out.println("ReadArrayList"+Arrays.deepToString(arr));
		 
		 for(int i=0; i<graph.length;i++) {
			 for(int j=0;j<graph.length;j++) {
				 System.out.print(graph[i][j]+ " ");
				 
			 }
			 System.out.println();
		 }
			
	        Dijkstra dijkstra = new Dijkstra(graph);
	        System.out.println(dijkstra.solve(src, dest));
	        dijkstra.printPath(src, dest);
		
		
		System.out.println();*/
		
		
		
        }
        
       



		/*private static int KnapsackSolver(int i, List<Integer> valueList, List<Integer> weightList, int maxWeight) {
			int take =0;
			int donottake=0;
			if(i==0) {
				return 0;
			}else {
				if(weightList.get(i)<=maxWeight) {
					take = valueList.get(i)+KnapsackSolver(i-1, valueList, weightList, maxWeight-weightList.get(i));
				}
				donottake=KnapsackSolver(i-1, valueList, weightList, maxWeight);
				return Math.max(take, donottake);
			}
			
			
		}*/





		/*private static double bottomUp(List<Integer> weightList, List<Integer> valueList, int[][] keep, int maxWeight) {
			// TODO Auto-generated method stub
			//int RemainingCapacity=0;
			//double ValRemCap = - RemainingCapacity*(0.02);
			int LastValue=0;
			
			double[][] V = new double[weightList.size()+1][maxWeight+1];
			int[] solution = new int[weightList.size()+1];
			int RemainingCapacity;
			for(int i=0;i<weightList.size();i++) {
				for(int j = 0;j<maxWeight;j++) {
					V[i][j]=0-j*0.02;
				}
			}
			
			for(int i = 1; i<=weightList.size();i++) {
				for(int j = 0; j<=maxWeight;j++) {
					int weight = weightList.get(i-1);
					if(weight<=j) {
						V[i][j]=Math.max(V[i-1][j], valueList.get(i-1)+V[i-1][j-weight]);
						keep[i][j]=1;
					}else {
						V[i][j]=V[i-1][j];
						keep[i][j]=0;
					}
				}
			}
			int K = maxWeight;
			for(int i = weightList.size();i>0;i--) {
				if(keep[i][K]==1) {
					solution[i]=1;
					K -= weightList.get(i-1);
					System.out.println(i);
				}
			}
			
			return V[weightList.size()][maxWeight];
		}*/





		private static void TSP(List<ArrayList<Double>> sequential_data, List<Integer> knapsackSolution, int src,
				int dest) {
			// TODO Auto-generated method stub
			//create new gap
			
			int[][] gap = new int[knapsackSolution.size()][knapsackSolution.size()];
			
			for(int i = 0;i<knapsackSolution.size();i++) {
				for(int j=0;j<knapsackSolution.size();j++) {
					gap[i][j]=sequential_data.get(knapsackSolution.get(i)).get(knapsackSolution.get(j));
				}
			}
			
			
		}





		private static void Solver(List<Integer> weightList, List<Integer> valueList, int maxWeight, int size, List<Integer> knapsackSolution) {
			// TODO Auto-generated method stub
			int NEGATIVE_INFINITY = Integer.MIN_VALUE;
			int[][] m =new int[weightList.size()+1][maxWeight+1];
			int[][] sol = new int[weightList.size()][maxWeight+1];
			
			for(int i = 1;i<weightList.size();i++) {
				for(int j = 0;j<=maxWeight;j++) {
					int m1=m[i-1][j];
					int m2=NEGATIVE_INFINITY;
					if(j>=weightList.get(i)) {
						m2=m[i-1][j-weightList.get(i)+valueList.get(i)];
						m[i][j]=Math.max(m1, m2);
						sol[i][j]=m2>m1?1:0;
					}
				}
				
				int[] selected = new int[weightList.size()+1];
				for(int n = weightList.size(),w=maxWeight;n>0;n--) {
					if(sol[n][w]!=0) {
						selected[n]=1;
						w=w-weightList.get(n);
					}
					else
						selected[n]=0;
				}
				
				
				for(int z = 1;z<weightList.size()+1;z++) {
					if(selected[z]==1)
						System.out.println(i + valueList.get(i));
						knapsackSolution.add(i);
						
				}
			}
		}





		private static void AssignSrcDest(int src, int dest, List<Integer> knapsackSolution, List<Integer> valueList) {
        	
        	//find Max and Second Valuable Songs And Assign as src and dest
        	
        	int SourceValue = Integer.MIN_VALUE;
        	int DestinationValue = Integer.MIN_VALUE;
        	
        	//Assign src
        	for(int i = 0; i<knapsackSolution.size(); i++) {
        		if(valueList.get(knapsackSolution.get(i))>SourceValue) {
        			SourceValue = valueList.get(knapsackSolution.get(i));
        			src = knapsackSolution.get(i);
        		}
        	}
        	System.out.println("src ID: " + src);
        	
        	//Assign dest
        	for(int i = 0; i<knapsackSolution.size();i++) {
        		if(valueList.get(knapsackSolution.get(i))>DestinationValue && valueList.get(knapsackSolution.get(i))!= SourceValue ) {
        			DestinationValue = valueList.get(knapsackSolution.get(i));
        			dest = knapsackSolution.get(i);
        		}
        	}
        	System.out.println("dest ID: " + dest);
		}
        
        
		/*private static void Knapsack(List<Integer> valueList, List<Integer> weightList, int maxWeight, List<Integer> knapsackSolution) {
        	 try {
				GRBEnv env = new GRBEnv(true);
		        env.set("logFile", "mip1.log");
		        env.start();
		        
		        GRBModel model = new GRBModel(env);
		        GRBVar[] X = new GRBVar[valueList.size()];
		        
		        for(int i=0; i<X.length;i++)
		        {
		        	X[i]=model.addVar(0, 1, 0, GRB.INTEGER, "X_"+i);
		        }
		        
		        GRBLinExpr expr = new GRBLinExpr();
		        
		        for(int i=0; i<X.length;i++)
		        {
		        	expr.addTerm(valueList.get(i),X[i]);		   
		        }
		        model.setObjective(expr, GRB.MAXIMIZE);
		        
		        expr = new GRBLinExpr();
		        
		        for(int i=0; i<X.length;i++)
		        {
		        	expr.addTerm(weightList.get(i),X[i]);
		        }
		        model.addConstr(expr, GRB.LESS_EQUAL, maxWeight, "Knapsack Capacity Constraint");
		        
		        model.optimize();
				System.out.println("OPTIMAL SOLUTION");		
				System.out.println("Obj " + model.get(GRB.DoubleAttr.ObjVal));
				System.out.println("Items selected");
				System.out.println("Id | Value | Weight");
				
				for(int i=0; i<X.length;i++)
		        {
		        	if(X[i].get(GRB.DoubleAttr.X)>.5)
		        	{
		        		System.out.println(i + " " +valueList.get(i)+" "+weightList.get(i));
		        		// Add List to Store Solution
		        		knapsackSolution.add(i); // storing ids
		        		
		        	}
		        }
				
				model.dispose();
		        env.dispose();
		        

		        
			} catch (GRBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
		}*/
        
        

     
        public static List<List<String>> readValues() throws IOException { 
                try
		{
			List< List<String> > data = new ArrayList<>();//list of lists to store data
			String file = "term_project_value_data.csv";//file path
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			//Reading until we run out of lines
			String line = br.readLine();
			while(line != null)
			{
				List<String> lineData = Arrays.asList(line.split(","));//splitting lines
				data.add(lineData);
				line = br.readLine();
			}
			
			//printing the fetched data
			for(List<String> list : data)
			{
				for(String str : list)
					System.out.print(str + " ");
				System.out.println();
			}
			br.close();
                        return data;
		}
		catch(Exception e)
		{
			System.out.print(e);
                        List< List<String> > data = new ArrayList<>();//list of lists to store data
                        return data;
		}
                
        }
	public static List<List<String>> readSequential() throws IOException { 
                try
		{
			List< List<String> > data = new ArrayList<>();//list of lists to store data
			String file = "term_project_sequential_data.csv";//file path
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			//Reading until we run out of lines
			String line = br.readLine();
			while(line != null)
			{
				List<String> lineData = Arrays.asList(line.split(","));//splitting lines
				data.add(lineData);
				line = br.readLine();
			}
			
			//printing the fetched data
			for(List<String> list : data)
			{
				for(String str : list)
					System.out.print(str + " ");
				System.out.println();
			}
			br.close();
                        return data;
		}
		catch(Exception e)
		{
			System.out.print(e);
                        List< List<String> > data = new ArrayList<>();//list of lists to store data
                        return data;
		}
                
        }
}
