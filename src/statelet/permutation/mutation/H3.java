package statelet.permutation.mutation;

import java.util.Random;

import statelet.Mutator;
import statelet.permutation.ArrayForm;


import jeep.math.UnitInterval;

/**
 * HyFlex TSP\Flow shop heuristic H3
 * @author Jerry Swan
 */

public class H3 implements Mutator< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, UnitInterval mutationDegree, Random random ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomShuffle( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

