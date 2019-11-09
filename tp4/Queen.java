package tp4;

	
	import org.chocosolver.solver.ResolutionPolicy;
	import org.chocosolver.solver.Solver;
	import org.chocosolver.solver.constraints.Constraint;
	import org.chocosolver.solver.constraints.IntConstraintFactory;
	import org.chocosolver.solver.constraints.IntConstraintFactory.*;
	import org.chocosolver.solver.search.strategy.IntStrategyFactory;
	import org.chocosolver.solver.variables.IntVar;
	import org.chocosolver.solver.variables.BoolVar;
	import org.chocosolver.solver.variables.VariableFactory;
	import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
	import org.chocosolver.util.ESat;
	import org.chocosolver.util.tools.ArrayUtils;

	public class Queen extends AbstractProblem {

	  @Option(name = "-n", usage = "n (size of problem, default 8).", required = false)
	  int n = 8;

	  @Option(name = "-solutions", usage = "number of solutions (default 0, all).", required = false)
	  int solutions = 0;

	  IntVar[] queens;


	  @Override
	  public void buildModel() {

	    queens = VariableFactory.enumeratedArray("queens", n, 1, n, solver);
	    IntVar[] diag1 = new IntVar[n];
	    IntVar[] diag2 = new IntVar[n];

	    for (int i = 0; i < n; i++) {
	      diag1[i] = VariableFactory.offset(queens[i], i);
	      diag2[i] = VariableFactory.offset(queens[i], -i);
	    }

	    solver.post(IntConstraintFactory.alldifferent(queens, "BC"));
	    solver.post(IntConstraintFactory.alldifferent(diag1, "BC"));
	    solver.post(IntConstraintFactory.alldifferent(diag2, "BC"));

	  }

	  @Override
	  public void createSolver() {
	    solver = new Solver("Queen");
	  }

	  @Override
	  public void configureSearch() {
	    
	    solver.set(IntStrategyFactory.firstFail_InDomainMin(queens));
	    // solver.set(IntStrategyFactory.firstFail_InDomainMiddle(queens));
	    // solver.set(IntStrategyFactory.domOverWDeg_InDomainMin(queens, seed));
	  }

	  @Override
	  public void solve() {
	    // System.out.println(solver); // Solver/model before solve.
	    solver.findSolution();
	  }


	  @Override
	  public void prettyOut() {
	 
	    if (solver.isFeasible() == ESat.TRUE) {
	      int num_solutions = 0;
	      do {
	        for(int i = 0; i < n; i++) {
	          System.out.print(queens[i].getValue() + " ");
	        }
	        System.out.println();
	        num_solutions++;
	        
	        if (solutions > 0 && num_solutions >= solutions) {
	          break;
	        }

	      } while (solver.nextSolution() == Boolean.TRUE);


	      System.out.println("\nIt was " + num_solutions + " solutions.");

	    }  else {
	      System.out.println("No solution.");
	    }

	  }


	  public static void main(String args[]) {

	    new Queen().execute(args);

	  }


	}


