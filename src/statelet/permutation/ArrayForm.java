/*
 * Copyright (C) Jerry Swan, 2010-2012.
 * 
 * This file is part of Hyperion, a hyper-heuristic solution-domain framework.
 * 
 * Hyperion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hyperion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hyperion. If not, see <http://www.gnu.org/licenses/>.
 *
 */

//////////////////////////////////////////////////////////////////////

package statelet.permutation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jeep.math.UnitInterval;
import powerchords.RandomSamplingUtil;

//////////////////////////////////////////////////////////////////////

public final class ArrayForm
implements Permutation, Comparable< ArrayForm >
{
	private int	[] permutation_;
	
	///////////////////////////////
	
	public ArrayForm( int n ) 
	{
		if( n < 0 )
			throw new IllegalArgumentException( "Invalid permutation size" );
		
		permutation_ = new int [ n ];
		
		for( int i=0; i<permutation_.length; ++i )
			permutation_[ i ] = i;
		
		assert invariant();
	}

	public ArrayForm( int n, Random random ) 
	{
		this( n );
		randomShuffle( random );
		
		assert invariant();		
	}
	
	public ArrayForm( int ... perm ) 
	{
		if( !ArrayUtils.isPermutation( perm ) )
			throw new IllegalArgumentException();
		
		permutation_ = new int [ perm.length ];
		System.arraycopy( perm, 0, permutation_, 0, perm.length );
		
		assert invariant();		
	}

	public ArrayForm( ArrayForm rhs ) 
	{
		permutation_ = (int [])rhs.permutation_.clone();
		assert invariant();		
	}

	///////////////////////////////

	@Override
	public int minPreimage() 
	{
		return permutation_.length - 1;
	}

	@Override
	public int maxPreimage() 
	{
		return permutation_.length;
	}
	
	@Override
	public Set< Integer > preimage()
	{
		return new HashSet< Integer >( 
				statelet.util.Collections.asList( permutation_ ) );
	}

	@Override
	public int image( int point ) 
	{
		if( point < 0 )
			throw new IllegalArgumentException();
		if( point >= permutation_.length )
			return point;
		else
			return permutation_[ point ];
	}
	
	///////////////////////////////
	
	public void swap( int index1, int index2 ) 
	{
		ArrayUtils.swapUnchecked( permutation_, index1, index2 );
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
		final int r1 = random.nextInt( permutation_.length );
		int r2;
		while( ( r2 = random.nextInt( permutation_.length ) ) != r1 )
			;
		
		ArrayUtils.swapUnchecked( permutation_, r1, r2 );
		assert invariant();
	}

	/**
	 * Syswerda, G. (1991). 
	 * Schedule Optimization Using Genetic Algorithms. 
	 * In Davis, L. (ed.) Handbook of Genetic Algorithms, 332-349. 
	 * New York: Van Nostrand Reinhold. 
	 */

	public void randomShuffle( Random random ) 
	{
		ArrayUtils.randomShuffleArray( permutation_, random );
		assert( invariant() );
	}

	/**
	 * Move the element at sourceIndex to destIndex,
	 * shifting elements as required.
	 */
	public void insert( int sourceIndex, int destIndex ) 
	{
		if( sourceIndex < 0 || sourceIndex >= permutation_.length )
			throw new IllegalArgumentException();
		if( destIndex < 0 || destIndex >= permutation_.length )
			throw new IllegalArgumentException();
		
		ArrayUtils.insertUnchecked( permutation_, sourceIndex, destIndex );
		assert( invariant() );
	}
	
	/**
	 * Insert a randomly selected element 
	 * into a randomly selected position in the permutation, 
	 * shifting elements as required.
	 */
	
	public void randomInsert( Random random ) 
	{
		int r1 = random.nextInt( permutation_.length );

		int r2;
		while( ( r2 = random.nextInt( permutation_.length ) ) == r1 )
			;

		ArrayUtils.insertUnchecked( permutation_, r1, r2 );
		assert invariant();
	}

	
	public void rotate( int amount ) 
	{
		ArrayUtils.rotateArray( permutation_, amount );
		assert invariant();
	}
	
	
	/**
	 * Permute a number of randomly selected elements.
	 * @param mutationDegree
	 * @param random
	 */
	public void randomShuffleSubset( UnitInterval mutationDegree, Random random )
	{
		final int numShuffles = 2 + (int) ( mutationDegree.getValue() * ( size() - 2 ) );

		int [] indicesToShuffle = RandomSamplingUtil.randomSubsetArray( permutation_.length, numShuffles, random );
		assert indicesToShuffle.length == numShuffles;

		ArrayUtils.shuffleSubset( permutation_, indicesToShuffle, random );
		
		assert invariant();
	}

	
	///////////////////////////////
	
	/**
	 * N-Opt move, selects N arches and substitutes them with new randomly selected ones.
	 * The value of N depends on the intensityOfMutation parameter:
	 * N = 2 if        intensityOfMutation <= 0.25
	 * N = 3 if 0.25 < intensityOfMutation <= 0.50
	 * N = 4 if 0.50 < intensityOfMutation <= 0.75
	 * N = 5 if 0.75 < intensityOfMutation <= 1.00
	 * 
	 */
	
	public void nOpt( UnitInterval mutationDegree, Random random )
	{
		int n = 2;
		if( mutationDegree.getValue() >= 0.25 )
			n = 3;
		else if( mutationDegree.getValue() >= 0.5 )
			n = 4;
		else if( mutationDegree.getValue() >= 0.75 )
			n = 5;

		nOpt( n, random );
	}
	
	public void nOpt( int n, Random random )
	{
		if( n < 1 )
			throw new IllegalArgumentException();

		int [] newValues = permutation_.clone();
		for( int i=0; i<n-1; ++i )
		{
			final int r1 = random.nextInt( permutation_.length );
			int r2;
			while( ( r2 = random.nextInt( permutation_.length ) ) == r1 )
				;
			
			newValues = ArrayUtils.flip( newValues, r1, r2 );
		}

		permutation_ = newValues;
		assert invariant();
	}
	
	
	///////////////////////////////

	public int size() { return permutation_.length; }
	
	public int get( int i ) { return permutation_[ i ]; }
	
	///////////////////////////////	
	
	public int compareTo( ArrayForm rhs ) {
		return jeep.util.Arrays.lexicographicalCompare( permutation_, rhs.permutation_ );
	}

	public int hashCode() {
		return Arrays.hashCode( permutation_ );
	}
	
	public boolean equals( Object rhs ) {
		return rhs instanceof ArrayForm 
			&& Arrays.equals( permutation_, ((ArrayForm)rhs).permutation_ );
	}
	
	public ArrayForm clone() {
		return new ArrayForm( this );
	}
	
	///////////////////////////////
	
	public String toString()
	{
		return Arrays.toString( permutation_ );
	}
	
	public boolean invariant() {
		return ArrayUtils.isPermutation( permutation_ );
	}
	
	///////////////////////////////

	public void invert( int from, int to )
	{
		if( from < 0 || from >= permutation_.length )
			throw new IllegalArgumentException();
		if( to < 0 || to >= permutation_.length )
			throw new IllegalArgumentException();			
		if( from > to )
			throw new IllegalArgumentException();			
		
		ArrayUtils.invertArrayRange( permutation_, from, to );
		assert invariant();
	}
}

// End ///////////////////////////////////////////////////////////////
