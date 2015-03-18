package statelet.bitstring;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

public class TestBitString {

	@Test
	public void testConstructors() {
		BitString a = BitString.fromBinaryString("1010");
		BitString b = BitString.fromBinaryString("00000000000000000000000000001010");

		assertEquals(b, BitString.fromInt(10));
		assertEquals(b, BitString.fromLong(10L));
		assertEquals(b, BitString.fromLong(10L, 64));
	}
	
	@Test
	public void testConstructorsBlanks() {
		BitString a = new BitString(32);
		
		assertEquals(a, new BitString(32, new java.util.Random(1)));
		assertEquals(a, BitString.fromBinaryString(""));
		assertEquals(a, BitString.fromBinaryString("0"));
		assertEquals(a, BitString.fromBinaryString("00000000000000000000000000000000"));
		assertEquals(a, BitString.fromInt(0));
		assertEquals(a, BitString.fromLong(0L));
		assertEquals(a, BitString.fromLong(0L, 64));
		
		assertEquals(a, new BitString(new BitSet(32), 0, 31));
		assertNotEquals(a, new BitString(new BitSet(128), 0, 31));
	}
	
	@Test
	public void testEqualsBlanks() {
		BitString a = new BitString( 32 );
		BitString b = BitString.fromInt( 0 );
		assertEquals( a, b );
	}
	
	@Test
	public void testEqualsNonBlanks() {
		BitString a = BitString.fromInt(7777);
		BitString b = BitString.fromInt(7777);
		assertEquals( a, b );
	}
	
	@Test
	public void testEqualsNot() {
		BitString a = BitString.fromInt( 0 );
		BitString b = BitString.fromInt( 1 );
		assertNotEquals( a, b );
	}
	
	@Test
	public void testEqualsDisjoint() {
		BitString a = new BitString( 32 );
		BitString b = new BitString( a );
		assertEquals( a, b );
		a.set( 0 );
		assertNotEquals( a, b );		
		assertEquals( a, BitString.fromInt( 1 ) );
		assertNotEquals( b, BitString.fromInt( 1 ) );		
	}

	@Test
	public void testAnd() {
		BitString a = BitString.fromInt(7777);
		BitString b = new BitString(a);
		
		BitString full = new BitString(32);
		full.set(0, 31);
		BitString blank = new BitString(32);
		
		// and with 111111 should return identical string
		a.and(full);
		assertEquals( a, b );
		assertNotEquals( a, full );
		
		// and with 000000 should return 0000000
		a.and(blank);
		assertEquals( a, blank );
		assertNotEquals( a, b );
	}

	@Test
	public void testNot() {
		BitString a = BitString.fromInt(15);
		BitString b = new BitString(a);
		
		BitString full = new BitString(32);
		full.set(0, 31);
		BitString blank = new BitString(32);
		
		// should leave unchanged
		a.andNot(blank);
		assertEquals(a, b);
		
		// should clear bit 4
		a.andNot(BitString.fromInt(8));
		assertEquals( a, BitString.fromInt(7) );
		
		// should clear all
		a.andNot(full);
		assertEquals( a, blank );
	}

	@Test
	public void testCardinality() {
		assertEquals(new BitString(32).cardinality(), 0);
		assertEquals(BitString.fromInt(1).cardinality(), 1);
		assertEquals(BitString.fromInt(15).cardinality(), 4);
		assertEquals(BitString.fromInt(16).cardinality(), 1);
		assertEquals(BitString.fromInt(-1).cardinality(), 32);
	}

	@Test
	public void testClear() {
		BitString blank = new BitString(32);
		
		BitString bs0 = BitString.fromInt(0);
		bs0.clear();
		assertEquals(bs0, blank);
		assertEquals(bs0.cardinality(), 0);
		
		BitString bs1 = BitString.fromInt(1);
		bs1.clear();
		assertEquals(bs1, blank);
		assertEquals(bs1.cardinality(), 0);
		
		BitString bsNeg1 = BitString.fromInt(1);
		bsNeg1.clear();
		assertEquals(bsNeg1, blank);
		assertEquals(bsNeg1.cardinality(), 0);
	}
	
	@Test
	public void testClearOneBit() {
		BitString blank = new BitString(32);
		
		BitString bs0 = BitString.fromInt(0);
		bs0.clear(0);
		assertEquals(bs0, blank);
		
		BitString bs1 = BitString.fromInt(1);
		bs1.clear(0);
		assertEquals(bs1, blank);
		
		BitString bs3 = BitString.fromInt(3);
		bs3.clear(0);
		assertEquals(bs3, BitString.fromInt(2));
	}

//		clear(int fromIndex, int toIndex)
//
//		sparseIterator() ?
//
//		invariant()
//
//		clone()
//
//		flip()
//
//		flip(int fromIndex, int toIndex)
//
//		get(int bitIndex)
//
//		hashCode()
//
//		intersects(BitString rhs)
//
//		isEmpty()
//
//		length()
//
//		nextClearBit(int fromIndex)
//
//		nextSetBit(int fromIndex)
//
//		not()
//
//		or(BitString rhs)
//
//		set( int bitIndex )
//
//		set(int bitIndex, boolean value)
//
//		set(int fromIndex, int toIndex)
//
//		set(int fromIndex, int toIndex, boolean value)
//
//		subVector( int fromIndex, int toIndex )
//
//		xor(BitString rhs)
//
//		static int HammingDistance( BitString a, BitString b )
}

// End ///////////////////////////////////////////////////////////////
