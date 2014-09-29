package statelet.permutation.examples;

import java.util.Random;

import heu4j.Mutate;
import statelet.permutation.ArrayForm;

/**
 * HyFlex TSP\Flow shop heuristic H3
 * @author Jerry Swan
 */

public class H3 implements Mutate< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, Random random ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomShuffle( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

