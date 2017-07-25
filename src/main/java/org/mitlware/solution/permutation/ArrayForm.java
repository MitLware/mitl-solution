
//////////////////////////////////////////////////////////////////////

package org.mitlware.solution.permutation;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.mitlware.support.util.*;
import org.mitlware.support.math.UnitInterval;

//////////////////////////////////////////////////////////////////////

public final class ArrayForm
implements Permutation, Comparable< ArrayForm > {

	private int	[] perm_;
	
	///////////////////////////////
	
	public ArrayForm( int n ) {
		if( n < 0 )
			throw new IllegalArgumentException( "Invalid permutation size" );
		
		perm_ = new int [ n ];
		
		for( int i=0; i<perm_.length; ++i )
			perm_[ i ] = i;
		
		assert invariant();
	}

	public ArrayForm( int n, Random random ) {
		this( n );
		randomShuffle( random );
		
		assert invariant();		
	}
	
	public ArrayForm( int ... perm ) {
		if( !ArrayUtilsUnchecked.isPermutation( perm ) )
			throw new IllegalArgumentException();
		
		perm_ = new int [ perm.length ];
		System.arraycopy( perm, 0, perm_, 0, perm.length );
		
		assert invariant();		
	}

	public ArrayForm( ArrayForm rhs ) {
		perm_ = (int [])rhs.perm_.clone();
		assert invariant();		
	}

	///////////////////////////////

	@Override
	public int minPreimage() {
		return perm_.length - 1;
	}

	@Override
	public int maxPreimage() {
		return perm_.length;
	}
	
	@Override
	public Set< Integer > preimage() {
		return new HashSet< Integer >( MitlCollections.asList( perm_ ) );
	}

	@Override
	public int image( int point ) {
		if( point < 0 )
			throw new IllegalArgumentException();
		if( point >= perm_.length )
			return point;
		else
			return perm_[ point ];
	}
	
	///////////////////////////////
	
	public int [] toArray() { return perm_;	}
	public int [] copyToArray() { return perm_.clone();	}

	
	public void transpose( int index1, int index2 ) {
		if( index1 < 0 || index1 >= size() )
			throw new IllegalArgumentException();
		if( index2 < 0 || index2 >= size() )
			throw new IllegalArgumentException();
		
		ArrayUtilsUnchecked.transpose( perm_, index1, index2 );
		assert invariant();
	}

	/**
	 * Swap two randomly-chosen elements. 
	 * @book{Eiben:2003:IEC:954563,
	 * 	 author = {Eiben, Agoston E. and Smith, J. E.},
	 * 	 title = {Introduction to Evolutionary Computing},
	 * year = {2003},
	 * isbn = {3540401849},
	 * publisher = {SpringerVerlag},
	 * }
	 */
	
	public void randomSwap( Random random ) 
	{
		final int r1 = random.nextInt( perm_.length );
		int r2;
		while( ( r2 = random.nextInt( perm_.length ) ) != r1 )
			;
		
		ArrayUtilsUnchecked.transpose( perm_, r1, r2 );
		assert invariant();
	}

	/**
	 * Syswerda, G. (1991). 
	 * Schedule Optimization Using Genetic Algorithms. 
	 * In Davis, L. (ed.) Handbook of Genetic Algorithms, 332-349. 
	 * New York: Van Nostrand Reinhold. 
	 */

	public void randomShuffle( Random random ) {
		ArrayUtilsUnchecked.randomShuffleArray( perm_, random );
		assert( invariant() );
	}

	/**
	 * Move the element at sourceIndex to destIndex,
	 * shifting elements as required.
	 */
	public void insert( int sourceIndex, int destIndex ) {
		if( sourceIndex < 0 || sourceIndex >= perm_.length )
			throw new IllegalArgumentException();
		if( destIndex < 0 || destIndex >= perm_.length )
			throw new IllegalArgumentException();
		
		ArrayUtilsUnchecked.insert( perm_, sourceIndex, destIndex );
		assert( invariant() );
	}
	
	/**
	 * Insert a randomly selected element 
	 * into a randomly selected position in the statelet.permutation, 
	 * shifting elements as required.
	 */
	
	public void randomInsert( Random random ) {
		int r1 = random.nextInt( perm_.length );

		int r2;
		while( ( r2 = random.nextInt( perm_.length ) ) == r1 )
			;

		ArrayUtilsUnchecked.insert( perm_, r1, r2 );
		assert invariant();
	}

	
	public void rotate( int amount ) {
		ArrayUtilsUnchecked.rotateArray( perm_, amount );
		assert invariant();
	}
	
	
	/**
	 * Permute a number of randomly selected elements.
	 * @param mutationDegree
	 * @param random
	 */
	public void randomShuffleSubset( UnitInterval mutationDegree, Random random ) {
		final int numShuffles = 2 + (int) ( mutationDegree.getValue() * ( size() - 2 ) );

		int [] indicesToShuffle = Sampling.randomSubsetArray( perm_.length, numShuffles, random );
		assert indicesToShuffle.length == numShuffles;

		ArrayUtilsUnchecked.shuffleSubset( perm_, indicesToShuffle, random );
		
		assert invariant();
	}

	///////////////////////////////
	
	public BitSet getAscents() {
		/***
		if( perm_.length == 0 )
			return new BitSet();

		BitSet result = new BitSet( perm_.length );		
		for( int i=0; i<perm_.length - 1; ++i )
			if( perm_[i] < perm_[i+1] )
				result.set( i );
		return result;
		***/
		return ArrayUtilsUnchecked.getAscents( perm_ );
	}
	
	///////////////////////////////
	
	public int hammingDistance( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();

		return ArrayUtilsUnchecked.hammingDistance( perm_, other.perm_ );
	}
	
	///////////////////////////////
	
	public int size() { return perm_.length; }
	
	public int get( int i ) { return perm_[ i ]; }
	
	///////////////////////////////	
	
	public int compareTo( ArrayForm rhs ) {
		return org.mitlware.support.util.MitlArrays.lexicographicalCompare( perm_, rhs.perm_ );
	}

	public int hashCode() {
		return Arrays.hashCode( perm_ );
	}
	
	public boolean equals( Object rhs ) {
		return rhs instanceof ArrayForm 
			&& Arrays.equals( perm_, ((ArrayForm)rhs).perm_ );
	}
	
	public ArrayForm clone() {
		return new ArrayForm( this );
	}
	
	///////////////////////////////
	
	public String toString() {
		return Arrays.toString( perm_ );
	}
	
	public boolean invariant() {
		return ArrayUtilsUnchecked.isPermutation( perm_ );
	}
	
	///////////////////////////////

	public void add( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.add( perm_, other.perm_ );
		assert invariant();
	}

	public void subtract( ArrayForm other )	{
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.subtract( perm_, other.perm_ );
		assert invariant();
	}
	
	public void multiply( ArrayForm other )	{
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.multiply( perm_, other.perm_ );
		assert invariant();
	}

	public void divide( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.divide( perm_, other.perm_ );
		assert invariant();
	}

	public void mod( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.mod( perm_, other.perm_ );
		assert invariant();
	}

	public void conjugate( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.conjugate( perm_, other.perm_ );
		assert invariant();
	}

	public void commutatorWith( ArrayForm other ) {
		if( size() != other.size() )
			throw new IllegalArgumentException();
		
		perm_ = ArrayUtilsUnchecked.commutator( perm_, other.perm_ );
		assert invariant();
	}

	public void power( int n ) {
		perm_ = ArrayUtilsUnchecked.power( perm_, n );
		assert invariant();
	}

	//////////////////////////////
	
	public void invert( int from, int to ) {
		if( from < 0 || from >= perm_.length )
			throw new IllegalArgumentException();
		if( to < 0 || to >= perm_.length )
			throw new IllegalArgumentException();			
		if( from > to )
			throw new IllegalArgumentException();			
		
		ArrayUtilsUnchecked.invertArrayRange( perm_, from, to );
		assert invariant();
	}

	public void invert() {
		perm_ = ArrayUtilsUnchecked.invert( perm_ );
		assert invariant();
	}
}

// End ///////////////////////////////////////////////////////////////
