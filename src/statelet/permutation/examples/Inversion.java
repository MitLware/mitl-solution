package statelet.permutation.examples;

import heu4j.Mutate;

import java.util.Random;

import statelet.permutation.ArrayForm;

//////////////////////////////////////////////////////////////////////

public class Inversion 
implements Mutate< ArrayForm > {

	@Override
	public ArrayForm apply( ArrayForm s, Random random ) {
		
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
