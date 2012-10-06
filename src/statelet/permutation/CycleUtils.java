package statelet.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jeep.lang.Diag;

//////////////////////////////////////////////////////////////////////

public final class CycleUtils {

	public static Set< Cycle > 
	fromArray( int ... perm )
	{
		assert( ArrayUtils.isPermutation( perm ) );
		
		BitSet unchecked = new BitSet( perm.length );
		unchecked.set( 0, perm.length );
	
		int [] cycle = new int [ perm.length ];		
		Set< Cycle > result = new HashSet< Cycle >();
		for( int i=0; i<=perm.length; ++i )
		{
			int len = 0;
			if( unchecked.get( i ) )
			{
				cycle[ len++ ] = i;
	            unchecked.clear( i );
	            int j = i;
	            while( unchecked.get( perm[ j ] ) )
	            {
	            	j = perm[ j ];
	            	cycle[ len++ ] = j;	                    
	            	unchecked.clear( j );
	            }
	            
	            assert( len >= 1 );
	            if( len > 1 )
	            	result.add( new Cycle( Arrays.copyOf( cycle, len ) ) );
			}
		}
	
		assert( cyclesAreDisjoint( result ) );
		return result;
	}

	///////////////////////////////

	public static boolean cyclesAreDisjoint( Cycle c1, Cycle c2 )
	{
		List< Cycle > cycles = new ArrayList< Cycle >();
		cycles.add( c1 );
		cycles.add( c2 );
		return cyclesAreDisjoint( cycles ); 
	}

	public static boolean cyclesAreDisjoint( Collection< Cycle > cycles )
	{
		Set< Integer > allValues = new HashSet< Integer >();
		for( Cycle c : cycles )
		{
			Set< Integer > currentValues = c.preimage();
			final int currentSize = currentValues.size();			
			final int oldSize = allValues.size();
			allValues.addAll( currentValues );
			if( allValues.size() != oldSize + currentSize )
				return false;
		}
		return true;
	}
	
	///////////////////////////////
	
	public static Set< Cycle > 
	multiply( Cycle c1, Cycle c2 ) 
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

