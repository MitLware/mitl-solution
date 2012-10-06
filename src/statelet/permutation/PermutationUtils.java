package statelet.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutationUtils {

	public static Set< Cycle > 
	multiply( Permutation c1, Permutation c2 ) 
	{
		Set< Integer > c1Preimage = c1.preimage();
		Set< Integer > c2Preimage = c2.preimage();
		final int maxPreimage = Math.max( c1.maxPreimage(), c2.maxPreimage() ); 
		BitSet available = new BitSet( maxPreimage + 1 );
		for( int i : c1Preimage )
			available.set( i );
		for( int i : c2Preimage )
			available.set( i );

		Set< Cycle > result = new HashSet< Cycle >();

		for( int smallest=available.nextSetBit(0); smallest>=0; smallest=available.nextSetBit( smallest+1 ) )
		{
			int i = smallest;
			List< Integer > currentCycle = new ArrayList< Integer >();
			currentCycle.add( i );
			available.clear( i );
				
			for( ; ; )
			{
				i = c2.image( c1.image( i ) );

				if( Arrays.asList( currentCycle ).contains( i ) 
					|| !available.get( i ) )
				{
					available.clear( i );					
					result.add( new Cycle( currentCycle ) );
					break;
				}
				else
				{
					available.clear( i );					
					currentCycle.add( i );
				}
			}
		}
		assert( CycleUtils.cyclesAreDisjoint( result ) );		
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

