package statelet.permutation.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jeep.lang.Diag;
import jeep.math.UnitInterval;

import org.junit.Test;

import statelet.Mutator;
import statelet.permutation.ArrayForm;
import statelet.permutation.ArrayUtils;
import statelet.permutation.Cycle;
import statelet.permutation.DisjointCycleForm;
import statelet.permutation.CycleUtils;
import statelet.permutation.mutation.H1;
import statelet.permutation.mutation.H2;
import statelet.permutation.mutation.H3;
import statelet.permutation.mutation.H5_FlowShop;
import statelet.permutation.mutation.H5_TSP;


//////////////////////////////////////////////////////////////////////

public final class TestPermutation {

	@Test
	public void test() {
		final long seed = System.currentTimeMillis();
		// final long seed = 0x12345678;
		System.out.println( "random seed: " + seed );
		Random random = new Random( seed );

		ArrayForm p1 = new ArrayForm( 10 );
		ArrayForm p2 = p1.clone();
		assertEquals( p1, p2 );
		
		final int numTrials = 1000;

		for( int i=0; i<numTrials; ++i )
			p1.randomShuffle( random );

		for( int i=0; i<numTrials; ++i )		
			p1.randomInsert( random );
		
		for( int i=0; i<numTrials; ++i )
		{
			UnitInterval mutationDegree = new UnitInterval( random.nextDouble() );			
			p1.randomShuffleSubset( mutationDegree, random );
		}
		
		for( int i=0; i<numTrials; ++i )
		{
			UnitInterval mutationDegree = new UnitInterval( random.nextDouble() );			
			p1.nOpt( mutationDegree, random );
		}
	}
	
	///////////////////////////////
	
	@Test	
	public void testRandomMutatorSequence()
	{
		final long seed = System.currentTimeMillis();
		// final long seed = 0x12345678;

		System.out.println( "random seed: " + seed );
		Random random = new Random( seed );
		
		ArrayForm p1 = new ArrayForm( 10, random );
		
		List< Mutator< ArrayForm > 
			> mutators = new ArrayList< Mutator< ArrayForm > >();
		mutators.add( new H1() );
		mutators.add( new H2() );
		mutators.add( new H3() );
		mutators.add( new H5_FlowShop() );
		mutators.add( new H5_TSP() );		
		
		final int numTrials = 10;
		for( int i=0; i<numTrials; ++i )
		{
			UnitInterval mutationDegree = new UnitInterval( random.nextDouble() ); 
			p1 = mutators.get( random.nextInt( mutators.size() ) 
				).apply( p1, mutationDegree, random );
		}
	}
	
	///////////////////////////////
	
	@Test
	public void testInsertion()
	{
		final long seed = System.currentTimeMillis();
		// final long seed = 0x12345678;
		System.out.println( "random seed: " + seed );
		Random random = new Random( seed );
		
		ArrayForm p = new ArrayForm( 3 );
		p.insert( 0, 2 );
		assertEquals( new ArrayForm( 1, 2, 0 ), p  );

		ArrayForm p2 = new ArrayForm( 4 );
		p2.insert( 0, 3 );
		assertEquals( new ArrayForm( 1, 2, 3, 0 ), p2 );
		
		///////////////////////////
		
		for( int i=0; i<1000; ++i )
			p.rotate( random.nextInt() );			
		
		///////////////////////////		

		ArrayForm q = new ArrayForm( 3 );		
		q.insert( 2, 0 );
		assertEquals( new ArrayForm( 2, 0, 1 ), q );
		
		ArrayForm pp = new ArrayForm( 10 ); 
		final int numTrials = 1000;
		for( int i=0; i<numTrials; ++i )
		{
			final int r1 = random.nextInt( pp.size() );
			final int r2 = random.nextInt( pp.size() );
			pp.insert( r1, r2 );
			
			pp.randomInsert( random );
		}
	}

	///////////////////////////////
	
	@Test
	public void testMultiply()
	{
		{
			int [] p1 = new int [] { 1,2,3,0 };
			int [] p2 = new int [] { 3,2,0,1 };		

			int [] product = ArrayUtils.multiply( p2, p1 ); 
			// System.out.println( Arrays.toString( product ) );
			assertTrue( Arrays.equals(  new int [] { 0, 3, 1, 2 }, product ) );
		}
		
		///////////////////////////
		
		{
			Cycle c1 = new Cycle( 1,2 );
			Cycle c2 = new Cycle( 2,3 );
			Set< Cycle > actualProduct = CycleUtils.multiply( c1, c2 );
			Set< Cycle > expectedProduct = Collections.singleton( 
					new Cycle( 1,3,2 ) );			
			assertEquals( expectedProduct, actualProduct );
		}
		
		{
			// c1 := 0->1,1->2,2->0, (0,1,2)
			int [] inPerm1 = new int [] { 1,2,0 };
			int [] inCycle1 = new int [] { 0,1,2 };			
			Cycle c1 = new Cycle( inCycle1 );
			int [] outPerm1 = c1.toPermutationArray();
			assertTrue( Arrays.equals( outPerm1, inPerm1 ) );
			
			// c2 := 0->0, 1->2,2->3,3->1 = (1,2,3)
			int [] inPerm2 = new int [] { 0,2,3,1 };			
			int [] inCycle2 = new int [] { 1,2,3 };
			Cycle c2 = new Cycle( inCycle2 );
			int [] outPerm2 = c2.toPermutationArray();
			assertArrayEquals( inPerm2, outPerm2 );

			Set< Cycle > actualProduct = CycleUtils.multiply( c1, c2 );
			Set< Cycle > expectedProduct = new HashSet< Cycle >();
			expectedProduct.add( new Cycle( 0, 2 ) );
			expectedProduct.add( new Cycle( 1, 3 ) );			
			assertEquals( expectedProduct, actualProduct );
		}
		
		// Multiplication for DisjointCycleForm
		// http://web.science.mq.edu.au/~chris/groups/chap02.pdf		
		// (14)(256) * (13465) -> (162)(34)
	}

	///////////////////////////////
	
	@Test
	public void testCycles()
	{
		{
			int [] perm = new int [] { 0,1,2 };		
			Set< Cycle > disjointCycles = CycleUtils.fromArray( 
					perm );
			
			Set< Cycle > expected = new HashSet< Cycle >();
			assertEquals( expected, disjointCycles );
		}
		
		{
			int [] perm = new int [] { 0,2,1 };
			Set< Cycle > disjointCycles = CycleUtils.fromArray( 0,2,1 );
			Set< Cycle > expected = new HashSet< Cycle >();
			expected.add( new Cycle( 1,2 ) );
			assertEquals( expected, disjointCycles );
			
			///////////////////////
			
			DisjointCycleForm pc = new DisjointCycleForm( disjointCycles );
			assertTrue( Arrays.equals( perm, pc.toPermutationArray() ) );			
		}
		
		{
			int [] perm = new int [] { 0,3,2,1 };	
			// 0->0,1->3,2->2,3->1
			Set< Cycle > disjointCycles = CycleUtils.fromArray( perm );
			Set< Cycle > expected = new HashSet< Cycle >();
			// expected.add( new Cycle( new int [] { 1 } ) );
			Cycle exc = new Cycle( 1, 3 );
			expected.add( exc );		
			assertEquals( expected, disjointCycles );
			
			DisjointCycleForm pc = new DisjointCycleForm( disjointCycles );
			assertTrue( Arrays.equals( perm, pc.toPermutationArray() ) );		
		}
	}
}

// End ///////////////////////////////////////////////////////////////

