package statelet;

import java.util.Random;

import jeep.math.UnitInterval;

public interface Mutator< State > {

	public State apply( State s, UnitInterval mutationDegree, Random random );
}

// End ///////////////////////////////////////////////////////////////

