package statelet.permutation.examples;

import hyperion3.Mutate;

import java.util.Random;

import statelet.permutation.ArrayForm;

//////////////////////////////////////////////////////////////////////

public class Inversion 
implements Mutate< ArrayForm > {

	private Random random;
	
	///////////////////////////////
	
	public Inversion( Random random ) {
		this.random = random;
	}
	
	@Override
	public ArrayForm apply( ArrayForm s ) {
		
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
