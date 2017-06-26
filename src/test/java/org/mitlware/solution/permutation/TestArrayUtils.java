package org.mitlware.solution.permutation;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

//////////////////////////////////////////////////////////////////////

public final class TestArrayUtils {

	@Test
	public void testInversionVector() {
        
		int [] p = new int [] { 4,8,0,7,1,5,3,6,2 };
        int [] v = ArrayUtilsUnchecked.inversionVector( p );
        assertArrayEquals( new int [] { 4, 7, 0, 5, 0, 2, 1, 1 }, v );
        
		p = new int [] { 3,2,1,0 };
        v = ArrayUtilsUnchecked.inversionVector( p );
        assertArrayEquals( new int [] { 3, 2, 1 }, v ); 
	}

	@Test
	public void testFromInversionVector() {
    
		int [] p = new int [] { 3, 2, 1, 0, 4, 5 };
		int [] inversionVector = new int [] { 3,2,1,0,0 };
		assertArrayEquals( inversionVector, 
				ArrayUtilsUnchecked.inversionVector( p ) );
        
		int [] q = ArrayUtilsUnchecked.fromInversionVector( 3,2,1,0,0 );
        assertArrayEquals( p, q );
	}

    @Test
    public void testConjugate() {
    	int [] a = new int []  { 0,2,1,3 };
    	int [] b = new int []  { 0,2,3,1 };
    	int [] conj = ArrayUtilsUnchecked.conjugate( a, b );
    	assertArrayEquals( new int [] { 0, 3, 2, 1 }, conj );
    }

    @Test
    public void testCommutator() {
    	int [] a = new int [] { 0,2,1,3 };
    	int [] b = new int [] { 0,2,3,1 };
    	int [] comm = ArrayUtilsUnchecked.commutator( a, b );
    	assertArrayEquals( new int [] { 0, 3, 1, 2 }, comm );
    }
    
    @Test
    public void testPower() {
    	int [] a = new int [] { 2,0,3,1 };
        
        assertArrayEquals( ArrayUtilsUnchecked.identityPermutation( a.length ), 
        		ArrayUtilsUnchecked.power( a, 0 ) );
        assertArrayEquals( a, ArrayUtilsUnchecked.power( a, 1 ) );
        assertArrayEquals( ArrayUtilsUnchecked.invert( a ), 
        	ArrayUtilsUnchecked.power( a, -1 ) );        
        assertArrayEquals( new int [] { 0, 1, 2, 3 }, 
        	ArrayUtilsUnchecked.power( a, 4 ) );        
    }

    @Test
    public void testAdd() {
    	int [] a = new int [] { 0, 3, 1, 2 };
    	int [] b = new int [] { 2, 1, 0, 3 };
    	assertArrayEquals( new int [] { 2, 0, 1, 3 }, 
    			ArrayUtilsUnchecked.add( a, b ) ); 
    }

    @Test
    public void testSubtract() {
    	int [] p = new int [] { 0,1,2,3 };
    	int [] q = new int [] { 2,1,3,0 };
    	assertArrayEquals( q, ArrayUtilsUnchecked.subtract( q, p ) ); 
    }

	/*****    
    ///////////////////////////////
    
    public boolean isLeftDistributive( Permutation x, Permutation y, Permutation z ) 
    {
    	Permutation lhs = Permutation.Multiply( x, Permutation.Add( y, z ) );
    	Permutation rhs = Permutation.Add( Permutation.Multiply( x, y ), 
    			Permutation.Multiply( x, z ) );
    	
    	if( !lhs.equals( rhs ) )
    	{
    		System.out.println( "x:" + x );
    		System.out.println( "y:" + y );
    		System.out.println( "z:" + z );
    		System.out.println( "lhs:" + lhs );
    		System.out.println( "rhs:" + rhs );    		
    	}
    	
    	return lhs.equals( rhs );
    }

    public boolean isRightDistributive( Permutation x, Permutation y, Permutation z ) 
    {
    	Permutation lhs = Permutation.Multiply( Permutation.Add( x, y ), z ); 
    	Permutation rhs = Permutation.Add( Permutation.Multiply( x, z), 
    			Permutation.Multiply( y, z) );
    	
    	if( !lhs.equals( rhs ) )
    	{
    		System.out.println( "x:" + x );
    		System.out.println( "y:" + y );
    		System.out.println( "z:" + z );
    		System.out.println( "lhs:" + lhs );
    		System.out.println( "rhs:" + rhs );    		
    	}
    	
    	return lhs.equals( rhs );    	
    }

    @Test
    public void testDistributivity() {
    	
    	final int permutationSize = 2;
    	final int numSamples = 1000;    	
    	
    	final long randomSeed = 0x12345678;
    	Random random = new Random( randomSeed );

    	for( int i=0; i<numSamples; ++i )
    	{
    		Permutation x = new Permutation( permutationSize, random );
    		Permutation y = new Permutation( permutationSize, random );
    		Permutation z  =  new Permutation( permutationSize, random );
    		assertTrue( isRightDistributive( x, y, z ) );
    		assertTrue( isLeftDistributive( x, y, z ) );    		
    	}
    }
    *****/
}

// End ///////////////////////////////////////////////////////////////

