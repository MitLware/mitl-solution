package org.mitlware.solution.permutation;

import java.util.HashSet;
import java.util.Set;


//////////////////////////////////////////////////////////////////////

public final class DisjointCycleForm
// implements Comparable< PermutationCycle >
implements Permutation
{
	private Set< Cycle > disjointCycles_;
	
	///////////////////////////////
	
	public DisjointCycleForm( int ... perm )
	{
		if( !ArrayUtilsUnchecked.isPermutation( perm ) )
			throw new IllegalArgumentException();
		
		disjointCycles_ = CycleUtilsUnchecked.fromArray( perm );
		assert( invariant() );
	}

	public DisjointCycleForm( Cycle cycle )
	{
		disjointCycles_ = java.util.Collections.singleton( cycle );
		assert( invariant() );
	}
	
	public DisjointCycleForm( Set< Cycle > cycles )
	{
		if( !CycleUtilsUnchecked.cyclesAreDisjoint( cycles ) )
			throw new IllegalArgumentException();
		
		disjointCycles_ = new HashSet< Cycle >( cycles );
		assert( invariant() );
	}
	
	public DisjointCycleForm( DisjointCycleForm rhs )
	{
		disjointCycles_ = new HashSet< Cycle >( rhs.disjointCycles_ );
		assert( invariant() );
	}

	///////////////////////////////

	@Override
	public int minPreimage() 
	{
		if( disjointCycles_.isEmpty() )
			return -1;
		else
		{
			Integer best = null; 
			for( Cycle c : disjointCycles_ )
			{
				int m = c.minPreimage();
				if( best == null || m < best )
					best = m;
			}
			return best;
		}
	}

	@Override
	public int maxPreimage() 
	{
		if( disjointCycles_.isEmpty() )
			return 0;
		else
		{
			Integer best = null; 
			for( Cycle c : disjointCycles_ )
			{
				int m = c.maxPreimage();
				if( best == null || m > best )
					best = m;
			}
			return best;
		}
	}
	
	@Override
	public Set< Integer > preimage()
	{
		Set< Integer > result = new HashSet< Integer >();
		for( Cycle c : disjointCycles_ )
			result.addAll( c.preimage()  );
		return result;
	}

	@Override
	public int image( int point ) {
		if( point < 0 )
			throw new IllegalArgumentException();
		
		for( Cycle c : disjointCycles_ )
		{
			final int image = c.image( point );
			if( image != point )
				return image;
		}
		
		return point;
	}

	public int order()
	{
		int [] orders = new int [ disjointCycles_.size() ];
		int i = 0;
		for( Cycle c : disjointCycles_ )
			orders[ i++ ] = c.length();
		
		return org.mitlware.solution.util.Math.lcm( orders );		
	}
	
	///////////////////////////////
	
	public int [] toPermutationArray()
	{
		final int size = maxPreimage() + 1;
		int [] perm = new int [ size ];
		
		for( int i=0; i<size; ++i )		
			perm[ i ] = image( i );

		assert( ArrayUtilsUnchecked.isPermutation( perm ) );
		return perm;
	}
	
	///////////////////////////////

	@Override
	public DisjointCycleForm clone()
	{
		return new DisjointCycleForm( this );
	}

	@Override
	public int hashCode()
	{
		return disjointCycles_.hashCode();
	}
	
	@Override	
	public boolean equals( Object o )
	{
		if( !( o instanceof DisjointCycleForm ) )
			return false;
			
		DisjointCycleForm rhs = (DisjointCycleForm)o;
		return disjointCycles_.equals( rhs.disjointCycles_ );
	}
	
//	@Override
//	public int compareTo( PermutationCycle o ) 
//	{
//		return disjointCycles_.compareTo( o.disjointCycles_ );
//	}

	
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		for( Cycle c : disjointCycles_ )
			result.append( c );			

		return result.toString();
	}

	///////////////////////////////
	
	public boolean invariant()
	{
		return CycleUtilsUnchecked.cyclesAreDisjoint( 
				disjointCycles_ );
	}
}

// End ///////////////////////////////////////////////////////////////

