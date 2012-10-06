package statelet.permutation.mutation;

import java.util.Random;

import statelet.Mutator;
import statelet.permutation.Permutation;
import statelet.permutation.ArrayForm;


import jeep.math.UnitInterval;

/**
 * HyFlex TSP\Flow shop heuristic H1
 * @author Jerry Swan
 */

public class H1 implements Mutator< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, UnitInterval mutationDegree, Random random ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomInsert( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

