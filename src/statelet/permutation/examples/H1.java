package statelet.permutation.examples;

import heu4j.Mutate;

import java.util.Random;

import jeep.math.UnitInterval;
import statelet.permutation.ArrayForm;

/**
 * HyFlex TSP\Flow shop heuristic H1
 * @author Jerry Swan
 */

public class H1 implements Mutate< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, Random random ) 
	{
		ArrayForm result = new ArrayForm( s );
		result.randomInsert( random );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

