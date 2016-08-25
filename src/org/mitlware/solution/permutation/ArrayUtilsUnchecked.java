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
    	
package org.mitlware.solution.permutation;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import jeep.lang.Diag;

//////////////////////////////////////////////////////////////////////

final class ArrayUtilsUnchecked {

	public static int [] identityPermutation( int n ) {
		int [] result = new int [ n ];
		for( int i=0; i<n; ++i )
			result[ i ] = i;
		
		assert( isPermutation( result ) );
		return result;
	}
	
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

		assert( isPermutation( result ) );
		return result;
	}
	
	///////////////////////////////
	
	public static int []
	divide( int [] p1, int [] p2 )
	{
		return multiply( p1, invert( p2 ) );
	}

	///////////////////////////////
	
	public static int [] 
	mod( int [] p1, int [] p2 )
	{
		return multiply( invert( p1 ), p2 );
	}
	
	///////////////////////////////
	
	public static int [] 
	invert( int [] p )
	{
		assert( isPermutation( p ) );
		
		int [] result = new int [ p.length ]; 
		for( int i=0; i<p.length; ++i )
			result[ p[ i ] ] = i;

		assert( isPermutation( result ) );
		return result;
	}
	
	///////////////////////////////
	
	public static int [] 
	add( int [] p1, int [] p2 )
	{
		assert( p1.length == p2.length );
		
		int [] a = inversionVector( p1 );
		int [] b = inversionVector( p2 );

		int [] inversionVector = new int [ p1.length - 1 ];
		for( int i=0; i<inversionVector.length; ++i )
			inversionVector[ i ] = (a[i] + b[i]) % ( p1.length - i );

		return fromInversionVector( inversionVector );
	}

	///////////////////////////////
	
	public static int []
	subtract( int [] p1, int [] p2 )
	{
		assert( isPermutation( p1 ) );
		assert( isPermutation( p2 ) );
		assert( p1.length == p2.length );		
		
		int [] a = inversionVector( p1 );
		int [] b = inversionVector( p2 );

		int [] inversionVector = new int [ p1.length - 1 ];
		for( int i=0; i<inversionVector.length; ++i )
		{
			int val  = ( a[i] - b[i] ) % ( p1.length - i );
			if( val < 0 )
				val += p1.length - i;
			
			inversionVector[ i ] = val;				
		}

		return fromInversionVector( inversionVector );
	}
	
	///////////////////////////////
	
	public static int []
	fromInversionVector( int ... inversionVector )
	{
		final int size = inversionVector.length + 1;
		List< Integer > N = new ArrayList< Integer >();
		for( int i=0; i<size; ++i )
			N.add( i );

		List< Integer > perm = new ArrayList< Integer >();
		for( int k=0; k<size - 1; ++k )
		{
			final int value = N.get( inversionVector[ k ] );
			perm.add( value );
 			final int index = N.indexOf( value );
 			assert( value != -1 );
 			N.remove( index );
		}

		for( Integer v : N )
			perm.add( v );
		
		int [] result = org.mitlware.solution.util.Collections.asArray( perm );
		assert( isPermutation( result ) );
		return result;		
	}
	
	///////////////////////////////
	
    public static int [] 
    conjugate( int [] a, int [] b )
    {
		assert( isPermutation( a ) );
		assert( isPermutation( b ) );
		assert( a.length == b.length );		
		
        int [] bInverse =  new int [ a.length ];
        for( int i=0; i<a.length; ++i )
            bInverse[ b[ i ] ] = i;

        int [] result = new int [ a.length ];
        for( int i=0; i<b.length; ++i )
            result[ i ] = bInverse[ a[ b[ i ] ] ];
        
        assert( isPermutation( result ) );
        return result;
    }

    ///////////////////////////////
    
    public static int [] 
    commutator( int [] a, int [] b )
    {
		assert( isPermutation( a ) );
		assert( isPermutation( b ) );
		assert( a.length == b.length );		
    
        int [] inva = new int [ a.length ];
        for( int i=0; i<a.length; ++i )
            inva[ a[ i ] ] = i;
        int [] invb = new int [ b.length ];
        for( int i=0; i<b.length; ++i )
            invb[ b[ i ] ] = i;
        
        int [] result = new int [ a.length ];
        for( int i=0; i<b.length; ++i )
            result[ i ] = inva[ invb[ a[ b[ i ] ] ] ];
        
        assert( isPermutation( result ) );
        return result;
    }

    ///////////////////////////////
    
    public static int []
    power( int [] a, int n )
    {
		assert( isPermutation( a ) );
		
        if( n == 0 )
            return identityPermutation( a.length );
        else if( n == 1 )
            return a.clone();
        else if( n < 0 )
            return power( invert( a ), -n );
        else
        {
        	// int [] b = new int [ a.size() ];
        	// Permutation result = new Permutation( a.size() );
        	int [] result = new int [ a.length ];        	
        		
            if( a.length == 2 )
            {
            	for( int i=0; i<a.length; ++i )            	
            		result[ i ] = a[ a[ i ] ];
            }
            else if( n == 3 )
            {
                // b = [a[a[i]] for i in a];
            	for( int i=0; i<a.length; ++i )            	
            		result[ i ] = a[ a[ a[ i ] ] ];
            }
            else if( n == 4 )
            {
                // b = [a[a[a[i]]] for i in a];
            	for( int i=0; i<a.length; ++i )            	
            		result[ i ] = a[ a[ a[ a[ i ] ] ] ];
            }
            else
            {
                // b = range(len(a));
                for( ; ; )
                {
                	if( n % 2 != 0 )
                	{
                		// b = [b[i] for i in a];
                    	for( int i=0; i<a.length; ++i )            	
                    		result[ i ] = result[ a[ i ] ];
                		
                    	if( --n == 0 )
                    		break;
                	}
                	if( n % 4 == 0 )
                	{
                        // a = [a[a[a[i]]] for i in a];                		
                		for( int i=0; i<a.length; ++i )            	
                			result[ i ] = a[ a[ a[ a[ i ] ] ] ];

                		n /= 4;
                	}
                	else if( n % 2 == 0 )
                	{
                		// a = [a[i] for i in a];
                    	for( int i=0; i<a.length; ++i )            	
                    		result[ i ] = a[ a[ i ] ];
                		n /= 2;
                	}
                }
            }
            
            assert( isPermutation( result ) );
            return result;
        }
    }
	
	///////////////////////////////
	
	public static BitSet getAscents( int [] perm ) {
		assert( isPermutation( perm ) );
		if( perm.length == 0 )
			return new BitSet();

		BitSet result = new BitSet( perm.length );		
		for( int i=0; i<perm.length - 1; ++i )
			if( perm[i] < perm[i+1] )
				result.set( i );
		
		return result;
	}

	/**
	 * The inversion vector consists of elements whose value
	 * indicates the number of elements in the statelet.permutation
	 * that are lesser than it and lie on its right hand side.
        Examples
        ========

        >>> from sympy.combinatorics.permutations import Permutation
        >>> p = Permutation([4,8,0,7,1,5,3,6,2])
        >>> p.inversion_vector()
        [4, 7, 0, 5, 0, 2, 1, 1]
        >>> p = Permutation([3,2,1,0])
        >>> p.inversion_vector()
        [3, 2, 1]
        """
	 * 
	 */
	
	public static int [] inversionVector( int [] perm ) {
        
		assert( isPermutation( perm ) );
		
        int [] result = new int [ perm.length - 1 ];

        for( int i=0; i<perm.length - 1; ++i )
        {
            int val = 0;
            for( int j=i+1; j<perm.length; ++j )
                if( perm[j] < perm[i] )
                	++val;
            result[i] = val;
        }
        
        return result;
	}
	
	///////////////////////////////
	
	static void transpose( int [] perm, int index1, int index2 ) 
	{
		assert( isPermutation( perm ) );
		
		final int temp = perm[ index1 ];
		perm[ index1 ] = perm[ index2 ];
		perm[ index2 ] = temp;
		
		assert( isPermutation( perm ) );		
	}
	
	///////////////////////////////
	
	static void insert( int [] values, int sourceIndex, int destIndex ) 
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
			
		ArrayUtilsUnchecked.randomShuffleArray( indicesToShuffle, random );

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

	///////////////////////////////
	
	public static int hammingDistance( int [] a, int [] b ) {
		if( a.length != b.length )
			throw new IllegalArgumentException();

		int result = 0;
		for( int i=0; i<a.length; ++i )
			if( a[ i ] == b[ i ] )
				++result;
		
		return result;
	}
}

// End ///////////////////////////////////////////////////////////////

