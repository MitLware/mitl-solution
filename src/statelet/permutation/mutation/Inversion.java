package statelet.permutation.mutation;

import java.util.Random;

import jeep.math.UnitInterval;
import statelet.Mutator;
import statelet.permutation.Permutation;
import statelet.permutation.ArrayForm;

//////////////////////////////////////////////////////////////////////

public class Inversion 
implements Mutator< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, 
			UnitInterval mutationDegree,
			Random random ) 
	{
		final int from = random.nextInt( s.size() - 1 );
		int to;
		while( ( to = random.nextInt( s.size() ) ) <= from )
			;

		ArrayForm result = new ArrayForm( s );
		result.invert( from, to );
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////
