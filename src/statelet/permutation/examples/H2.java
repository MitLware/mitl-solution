package statelet.permutation.examples;

import java.util.Random;

import statelet.Mutator;
import statelet.permutation.core.ArrayForm;

import jeep.math.UnitInterval;

/**
 * HyFlex TSP\Flow shop heuristic H2
 * @author Jerry Swan
 */

public class H2 implements Mutator< ArrayForm > 
{
	@Override
	public ArrayForm apply( ArrayForm s, UnitInterval mutationDegree, Random random ) 
	{
		if( s.size() <= 1 )
			return new ArrayForm( s );
		else
		{
			final int r1 = random.nextInt( s.size() );
			int r2;
			while( ( r2 = random.nextInt( s.size() ) ) == r1 )
				;
			
			ArrayForm result = new ArrayForm( s );
			result.swap( r1, r2 );
			return result;
		}
	}
}

// End ///////////////////////////////////////////////////////////////

