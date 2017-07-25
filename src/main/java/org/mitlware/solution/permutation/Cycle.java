package org.mitlware.solution.permutation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mitlware.support.util.*;

import org.mitlware.Diag;

//////////////////////////////////////////////////////////////////////

public final class Cycle
implements Permutation, Comparable< Cycle > {

	private int [] cycle_;
	
	///////////////////////////////
	
	public Cycle( int ... cycle ) {
		if( !isCycle( cycle ) )
			throw new IllegalArgumentException();
		
		cycle_ = cycle.clone();
		final int minIndex = MitlArrays.minIndex( cycle_ );
		ArrayUtilsUnchecked.rotateArray( cycle_, -minIndex );
		
		assert( invariant() );
	}

	public Cycle( List< Integer > cycle ) {
		this( MitlCollections.asArray( cycle ) );
	}
	
	public Cycle( Cycle rhs ) {
		cycle_ = rhs.cycle_.clone();
		assert( invariant() );
	}
	
	///////////////////////////////

	@Override
	public int minPreimage() {
		return cycle_[ 0 ];
	}

	@Override
	public int maxPreimage() {
		return cycle_[ cycle_.length - 1 ];
	}
	
	@Override
	public Set< Integer > preimage() {
		return new HashSet< Integer >( MitlCollections.asList( cycle_ ) );
	}

	@Override
	public int image( int point ) {
		final int index = MitlCollections.asList( cycle_ ).indexOf( point );
		if( index == -1 )
			return point;
		else
			return cycle_[ ( index + 1 ) % cycle_.length ];
	}
	
	///////////////////////////////
	
	@Override
	public Cycle clone() {
		return new Cycle( this );
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( cycle_ );
	}
	
	@Override	
	public boolean equals( Object o ) {
		if( !( o instanceof Cycle ) )
			return false;
			
		Cycle rhs = (Cycle)o;
		return Arrays.equals( cycle_, rhs.cycle_ );
	}

	@Override
	public int compareTo( Cycle o ) {
		return org.mitlware.support.util.MitlArrays.lexicographicalCompare( cycle_, o.cycle_ );
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer( "(" );
		for( int i=0; i<cycle_.length; ++i ) {
			result.append( cycle_[ i ] );			
			if( i < cycle_.length - 1 )
				result.append( ',' );
		}
		result.append( ')' );		
		return result.toString();
	}

	///////////////////////////////
	
	int order() { return cycle_.length; }
	int length() { return cycle_.length; }
	
	int [] toArray() { return cycle_; }
	int [] copyToArray() { return cycle_.clone(); }
	
	///////////////////////////////
	
	public int [] toPermutationArray() {
		final int size = maxPreimage() + 1;
		int [] perm = new int [ size ];
		
		for( int i=0; i<size; ++i )		
			perm[ i ] = image( i );

		assert( ArrayUtilsUnchecked.isPermutation( perm ) );
		return perm;
	}
	
	///////////////////////////////

	public boolean invariant() {
		return isCycle( cycle_ ) && MitlArrays.minIndex( cycle_ ) == 0;				
	}
	
	///////////////////////////////
	
	public static boolean isCycle( int ... array ) {
		Set< Integer > values = new HashSet< Integer >();
		for( int i : array ) {
			values.add( i );
			if( i < 0 )
				return false;
		}
		return values.size() == array.length;
	}
}

// End ///////////////////////////////////////////////////////////////

