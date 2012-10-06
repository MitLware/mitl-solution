/**
 * 
[1] Skiena, S. 'Permutations.' 1.1 in Implementing Discrete Mathematics
           Combinatorics and Graph Theory with Mathematica.  Reading, MA:
           Addison-Wesley, pp. 3-16, 1990.

    [2] Knuth, D. E. The Art of Computer Programming, Vol. 4: Combinatorial
           Algorithms, 1st ed. Reading, MA: Addison-Wesley, 2011.

     [3] Wendy Myrvold and Frank Ruskey. 2001. Ranking and unranking
           permutations in linear time. Inf. Process. Lett. 79, 6 (September 2001),
           281-284. DOI=10.1016/S0020-0190(01)00141-7

     [4] D. L. Kreher, D. R. Stinson 'Combinatorial Algorithms'
           CRC Press, 1999

     [5] Graham, R. L.; Knuth, D. E.; and Patashnik, O.
           Concrete Mathematics: A Foundation for Computer Science, 2nd ed.
           Reading, MA: Addison-Wesley, 1994.

     [6] http://en.wikipedia.org/wiki/Permutation#Product_and_inverse

     [7] http://en.wikipedia.org/wiki/Lehmer_code

*********************************************************************/
    	
package statelet.permutation;


import java.util.BitSet;
import java.util.Random;

import jeep.lang.Diag;

//////////////////////////////////////////////////////////////////////

public final class ArrayUtils {

	public static boolean isPermutation( int ... perm ) {
		
		BitSet check = new BitSet( perm.length );
		for( int i=0; i<perm.length; ++i ) {
			if( perm[ i ] < 0 || perm[ i ] >= perm.length )
				return false;
			
			check.set( perm[ i ] );
		}
		return check.cardinality() == perm.length;
	}

	///////////////////////////////

	public static int [] multiply( int [] p1, int [] p2 )
	{
		assert( isPermutation( p1 ) );
		assert( isPermutation( p2 ) );
		assert( p1.length == p2.length );		
		
		int [] result = p1.clone();
		for( int i=0; i<p2.length; ++i )
			result[ i ] = p2[ p1[ i ] ];
		return result;
	}
	
	///////////////////////////////
	
	static void swapUnchecked( int [] perm, int index1, int index2 ) 
	{
		assert( isPermutation( perm ) );
		
		final int temp = perm[ index1 ];
		perm[ index1 ] = perm[ index2 ];
		perm[ index2 ] = temp;
		
		assert( isPermutation( perm ) );		
	}
	
	static void insertUnchecked( int [] values, int sourceIndex, int destIndex ) 
	{
		assert( sourceIndex >= 0 && sourceIndex < values.length );
		assert( destIndex >= 0 && destIndex < values.length );

		final int sourceValue = values[ sourceIndex ];
		
		if( sourceIndex < destIndex )
		{
			System.arraycopy( values, sourceIndex + 1, 
					values, sourceIndex, 
					destIndex - sourceIndex );		
			values[ destIndex ] = sourceValue;
		}
		else
		{
			assert( sourceIndex >= destIndex );
			
			System.arraycopy( values, destIndex, 
					values, destIndex + 1, 
					sourceIndex - destIndex );		
			
			values[ destIndex ] = sourceValue;			
		}
	}

	///////////////////////////////
	
	/**
	 * Randomly permute the elements at the specified indices. 	
	 * @param indicesToShuffle
	 * @param random
	 */
	static void shuffleSubset( int [] permutation, int [] indicesToShuffle, Random random )
	{
		int [] valuesForShuffleIndices = new int [ indicesToShuffle.length ];
		for( int i=0; i<indicesToShuffle.length; ++i )
			valuesForShuffleIndices[ i ] = permutation[ indicesToShuffle[ i ] ]; 
			
		ArrayUtils.randomShuffleArray( indicesToShuffle, random );

		for( int i=0; i<indicesToShuffle.length; ++i )
			permutation[ indicesToShuffle[ i ] ] = valuesForShuffleIndices[ i ];
	}
	
	static void randomShuffleArray( int [] array, Random random ) 
	{
		// http://en.wikipedia.org/wiki/Knuth_shuffle
		for( int i = array.length; i > 0; )
		{
			final int j = random.nextInt( i-- );
			
			final int temp = array[ i ];
			array[ i ] = array[ j ];
			array[ j ] = temp;
		}
	}
	
	static void invertArrayRange( int [] array, int from, int to )
	{
		assert( isPermutation( array ) );
		assert( from >= 0 && from < array.length );
		assert( to >= 0 && to <= array.length );		
		assert( from <= to );
		
		int [] inverse = new int[ array.length ];
		for( int i=from; i<to; ++i )
			inverse[ array[ i ] ] = i;
		for( int i=from; i<to; ++i )
			array[ i ] = inverse[ i ];
	}
	
	static void rotateArray( int[] array, int ammount ) {

		// size of the array
		final int len = array.length;
		int[] newArray = new int[len];

		ammount %= len;
		if( ammount < 0 )
			ammount += len;

		// The offset is the amount from
		// end of the original array. This is where
		// we start the copy.
		final int offset = len - ammount;

		// Copy from offset to end
		System.arraycopy(array, offset, newArray, 0, ammount);

		// Copy from beginning to offset
		System.arraycopy(array, 0, newArray, ammount, offset);

		// Copy the temp back to the original
		System.arraycopy( newArray, 0, array, 0, len );
	}
	
	// TODO: sort this out
	static int [] flip( int [] perm, int a, int b )
	{
		assert( isPermutation( perm ) );
		if( a > b )
		{
			final int temp = a;
			a = b + 1;
			b = temp - 1;
		}
		
		if( a == b )
			return perm;			
		else
		{
			int [] newPerm = new int[ perm.length ];
			System.arraycopy( perm, 0, newPerm, 0, a );
		
			int count = a;
			for( int i=b; i>=a; --i, ++count )
				newPerm[ count ] = perm[ i ];

			System.arraycopy( perm, b + 1, newPerm, b+1, perm.length - b - 1 );
			assert( isPermutation( newPerm ) );		
			return newPerm;
		}
	}	
}

// End ///////////////////////////////////////////////////////////////

