package statelet.permutation.examples;

import hyperion3.Mutate;

import java.util.Random;

import statelet.permutation.ArrayForm;

/**
 * HyFlex TSP\Flow shop heuristic H1
 * @author Jerry Swan
 */

public class H1 implements Mutate< ArrayForm > {

	private Random random;
	
	///////////////////////////////
	
	public H1( Random random ) {
		this.random = random;
	}
	
	@Override
	public ArrayForm apply( ArrayForm s ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomInsert( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

