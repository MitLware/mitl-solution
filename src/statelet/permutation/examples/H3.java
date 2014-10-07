package statelet.permutation.examples;

import java.util.Random;

import hyperion3.Mutate;
import statelet.permutation.ArrayForm;

/**
 * HyFlex TSP\Flow shop heuristic H3
 * @author Jerry Swan
 */

public class H3 implements Mutate< ArrayForm > {

	private Random random;
	
	///////////////////////////////
	
	public H3( Random random ) {
		this.random = random;
	}
	
	///////////////////////////////
	
	@Override
	public ArrayForm apply( ArrayForm s ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomShuffle( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

