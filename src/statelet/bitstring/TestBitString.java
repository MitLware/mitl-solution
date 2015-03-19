package statelet.bitstring;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.Iterator;

import org.junit.Test;

public class TestBitString {

	@Test
	public void testConstructorsInts() {
		BitString a = BitString.fromBinaryString("1010");
		BitString b = BitString.fromBinaryString("00000000000000000000000000001010");

		assertNotEquals(a, b);
		assertEquals(b, BitString.fromInt(10));
		assertEquals(b, BitString.fromLong(10L, 32));
	}
	
	@Test
	public void testConstructorsLongs() {
		BitString a = BitString.fromBinaryString("0000000000000000000000000000000000000000000000000000000000001010");

		assertEquals(a, BitString.fromLong(10L));
		assertEquals(a, BitString.fromLong(10L, 64));
	}
	
	@Test
	public void testConstructorsBlanks() {
		BitString a = new BitString(32);
		
		assertNotEquals(a, BitString.fromBinaryString(""));
		assertNotEquals(a, BitString.fromBinaryString("0"));
		assertEquals(a, BitString.fromBinaryString("00000000000000000000000000000000"));
		assertEquals(a, BitString.fromInt(0));
		
		BitString b = BitString.fromLong(0L);
		assertNotEquals(a, b);
		assertEquals(b, BitString.fromLong(0L, 64));
		
		BitString c = new BitString(new BitSet(32), 0, 32);
		assertEquals(a, c);
		assertNotEquals(b, c);
		
		BitSet bs = new BitSet(128);
		assertEquals(a, new BitString(bs, 0, 32));
		assertEquals(b, new BitString(bs, 0, 64));
		assertEquals(a, new BitString(bs, 0, 32));
	}
	
	@Test
	public void testConstructorsRandom() {
		BitString a = new BitString(32, new java.util.Random(1));
		BitString b = new BitString(32, new java.util.Random(1));
		
		assertEquals(a, b);
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
		full.set(0, full.length());
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
	public void testAndNot() {
		BitString a = BitString.fromInt(15);
		BitString b = new BitString(a);
		
		BitString full = new BitString(32);
		full.set(0, full.length());
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

	@Test
	public void testClearRange() {
		BitString full = BitString.fromBinaryString("111111");
		
		assertEquals(full.cardinality(), 6);
		full.clear(2, 4); // clear bits 2 and 3
		assertEquals(full.cardinality(), 4);
		assertEquals(full, BitString.fromBinaryString("110011"));

		full.clear(4, 5); // clear bit 4
		assertEquals(full.cardinality(), 3);
		assertEquals(full, BitString.fromBinaryString("100011"));
		
		full.clear(0, 4); // clear bits 0 to 3
		assertEquals(full.cardinality(), 1);
		assertEquals(full, BitString.fromBinaryString("100000"));
		
		full.clear(5, 6); // clear bit 5
		assertEquals(full.cardinality(), 0);
		assertEquals(full, BitString.fromBinaryString("000000"));
		assertEquals(full, new BitString(6));
	}

	@Test
	public void testSparseIterator() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		byte[] cmp1 = new byte[s.length()];
		for (Iterator<Integer> it = a.sparseIterator(); it.hasNext(); ) {
			cmp1[it.next()] = 1;
		}
		
		// the order of bits in the BitString is reversed from the string!
		byte[] cmp2 = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			cmp2[cmp2.length - (i + 1)] = (byte) ((s.charAt(i) == '1') ? 1 : 0);
		}

		assertArrayEquals(cmp1, cmp2);
	}

	@Test
	public void testInvariant() {
		BitString a = BitString.fromBinaryString("11001111010101011010");
		assertTrue(a.invariant());
		
		BitString b = BitString.fromInt(0);
		assertTrue(b.invariant());

		BitString c = new BitString(32);
		assertTrue(c.invariant());
	}

	@Test
	public void testClone() {
		BitString a = BitString.fromInt(4);
		BitString b = a.clone();
		
		assertEquals(a, b);
		assertNotSame(a, b);
		
		b.set(0);
		assertNotEquals(a, b);
		assertEquals(b, BitString.fromInt(5));
	}
	
	@Test
	public void testFlipOneBit() {
		BitString a = BitString.fromBinaryString("11001111010101011010");
		
		a.flip(1);
		assertEquals(a, BitString.fromBinaryString("11001111010101011000"));

		BitString b = new BitString(32);
		b.flip(0);
		assertEquals(b, BitString.fromInt(1));
	}
	
	@Test
	public void testFlipRange() {
		BitString a = BitString.fromBinaryString("11001111010101011010");
		a.flip(1, 4);
		assertEquals(a, BitString.fromBinaryString("11001111010101010100"));

		BitString b = new BitString(32);
		b.flip(0, 3);
		assertEquals(b, BitString.fromInt(7));
	}

	@Test
	public void testGet() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		byte[] cmp1 = new byte[a.length()];
		for (int i = 0; i < cmp1.length; i++) {
			cmp1[i] = (byte) (a.get(i) ? 1 : 0);
		}
		
		// the order of bits in the BitString is reversed from the string!
		byte[] cmp2 = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			cmp2[cmp2.length - (i + 1)] = (byte) ((s.charAt(i) == '1') ? 1 : 0);
		}
		
		assertArrayEquals(cmp1, cmp2);
	}
	
	@Test
	public void testHashcode() {
		// generate a few bitstrings and check that hashcodes are same if equal()
		
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		BitString b = BitString.fromBinaryString(s);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		
		BitString c = BitString.fromInt(1);
		BitString d = BitString.fromInt(1);
		assertEquals(c, d);
		assertEquals(c.hashCode(), d.hashCode());
	}
	
	@Test
	public void testIntersects() {
		String s1 = "11001111010101011010";
		String s2 = "11000111010101011010";
		String s3 = "00001100010010101101";
		String s4 = "00000000000000000000";
		
		BitString a1 = BitString.fromBinaryString(s1);
		BitString a2 = BitString.fromBinaryString(s2);
		BitString a3 = BitString.fromBinaryString(s3);
		BitString a4 = BitString.fromBinaryString(s4);
		
		assertTrue(a1.intersects(a2));
		assertTrue(a1.intersects(a3));
		assertTrue(a2.intersects(a1));
		assertTrue(a2.intersects(a3));
		assertTrue(a3.intersects(a1));
		assertTrue(a3.intersects(a2));
		
		assertFalse(a1.intersects(a4));
		assertFalse(a2.intersects(a4));
		assertFalse(a3.intersects(a4));

		assertFalse(a4.intersects(new BitString(20)));
	}

	@Test
	public void testIsEmpty() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		assertFalse(a.isEmpty());
		
		a.clear();
		assertTrue(a.isEmpty());
		
		assertTrue(new BitString(32).isEmpty());
	}

	@Test
	public void testLength() {
		BitString a = new BitString(32);
		
		assertNotEquals(a, BitString.fromBinaryString(""));
		assertNotEquals(a, BitString.fromBinaryString("0"));
		assertEquals(a, BitString.fromBinaryString("00000000000000000000000000000000"));
		assertEquals(a, BitString.fromInt(0));
		
		BitString b = BitString.fromLong(0L);
		assertNotEquals(a, b);
		assertEquals(b, BitString.fromLong(0L, 64));
		
		BitString c = new BitString(new BitSet(32), 0, 32);
		assertEquals(a, c);
		assertNotEquals(b, c);
		
		BitSet bs = new BitSet(128);
		assertEquals(a, new BitString(bs, 0, 32));
		assertEquals(b, new BitString(bs, 0, 64));
		assertEquals(a, new BitString(bs, 0, 32));
	}
	
	@Test
	public void testNextClearBit() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		assertEquals(a.nextClearBit(0), 0);
		assertEquals(a.nextClearBit(1), 2);
		assertEquals(a.nextClearBit(2), 2);

		assertEquals(a.nextClearBit(18), -1);
		assertEquals(a.nextClearBit(19), -1);
		
		assertEquals(new BitString(32).nextClearBit(0), 0);
	}
	
	@Test
	public void testNextSetBit() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		assertEquals(a.nextSetBit(0), 1);
		assertEquals(a.nextSetBit(1), 1);
		assertEquals(a.nextSetBit(2), 3);

		assertEquals(a.nextSetBit(18), 18);
		assertEquals(a.nextSetBit(19), 19);
		
		assertEquals(new BitString(32).nextSetBit(0), -1);
	}

	@Test
	public void testNot() {
		String s1 = "11001111010101011010";
		String s2 = "00110000101010100101";
		
		BitString a1 = BitString.fromBinaryString(s1);
		BitString a2 = BitString.fromBinaryString(s2);
		
		assertNotEquals(a1, a2);
		assertFalse(a1.intersects(a2));
		
		a1.not();
		
		assertEquals(a1, a2);
		assertTrue(a1.intersects(a2));
		
		BitString a3 = new BitString(20);
		BitString a4 = new BitString(20);
		a4.set(0, 20);
		
		assertNotEquals(a3, a4);
		assertFalse(a3.intersects(a4));
		
		a3.not();
		assertEquals(a3, a4);
		assertTrue(a3.intersects(a4));
	}

	@Test
	public void testOr() {
		BitString a = BitString.fromInt(7777);
		BitString b = new BitString(a);
		
		BitString full = new BitString(32);
		full.set(0, full.length());
		BitString blank = new BitString(32);
		
		// or with 000000 should return original string
		a.or(blank);
		assertEquals( a, b );
		assertNotEquals( a, blank );
		
		// or with 111111 should return 111111
		a.or(full);
		assertEquals( a, full );
		assertNotEquals( a, b );
	}
	
	@Test
	public void testSetOneBit() {
		BitString bs0 = new BitString(32);
		
		bs0.set(0);
		assertEquals(bs0, BitString.fromInt(1));
	}
	
	@Test
	public void testSetOneBitValue() {
		BitString a = new BitString(32);
		
		a.set(0, false);
		assertEquals(a, new BitString(32));
		
		a.set(0, true);
		assertEquals(a, BitString.fromInt(1));
		
		a.set(0, false);
		assertEquals(a, new BitString(32));
	}

	@Test
	public void testSetRange() {
		BitString a = BitString.fromBinaryString("000000");
		
		assertEquals(a.cardinality(), 0);
		a.set(2, 4, true); // set bits 2 and 3
		assertEquals(a.cardinality(), 2);
		assertEquals(a, BitString.fromBinaryString("001100"));

		a.set(4, 5, true); // set bit 4
		assertEquals(a.cardinality(), 3);
		assertEquals(a, BitString.fromBinaryString("011100"));
		
		a.set(0, 4, true); // set bits 0 to 3
		assertEquals(a.cardinality(), 5);
		assertEquals(a, BitString.fromBinaryString("011111"));
		
		a.set(5, 6, true); // set bit 5
		assertEquals(a.cardinality(), 6);
		assertEquals(a, BitString.fromBinaryString("111111"));
	}

	@Test
	public void testSetRangeValue() {
		BitString a = BitString.fromBinaryString("000000");
		
		assertEquals(a.cardinality(), 0);
		a.set(2, 4, true); // set bits 2 and 3
		assertEquals(a.cardinality(), 2);
		assertEquals(a, BitString.fromBinaryString("001100"));

		a.set(4, 5, true); // set bit 4
		assertEquals(a.cardinality(), 3);
		assertEquals(a, BitString.fromBinaryString("011100"));
		
		a.set(0, 4, true); // set bits 0 to 3
		assertEquals(a.cardinality(), 5);
		assertEquals(a, BitString.fromBinaryString("011111"));
		
		a.set(5, 6, true); // set bit 5
		assertEquals(a.cardinality(), 6);
		assertEquals(a, BitString.fromBinaryString("111111"));
		
		assertEquals(a.cardinality(), 6);
		a.set(2, 4, false); // clear bits 2 and 3
		assertEquals(a.cardinality(), 4);
		assertEquals(a, BitString.fromBinaryString("110011"));

		a.set(4, 5, false); // clear bit 4
		assertEquals(a.cardinality(), 3);
		assertEquals(a, BitString.fromBinaryString("100011"));
		
		a.set(0, 4, false); // clear bits 0 to 3
		assertEquals(a.cardinality(), 1);
		assertEquals(a, BitString.fromBinaryString("100000"));
		
		a.set(5, 6, false); // clear bit 5
		assertEquals(a.cardinality(), 0);
		assertEquals(a, BitString.fromBinaryString("000000"));
		assertEquals(a, new BitString(6));
	}

	@Test
	public void testSubVector() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		assertEquals(a.subVector(0, a.length()), a);
		assertEquals(a.subVector(0, 2), BitString.fromBinaryString("10"));
		assertEquals(a.subVector(0, 4), BitString.fromBinaryString("1010"));
		assertEquals(a.subVector(0, 6), BitString.fromBinaryString("011010"));

		assertEquals(a.subVector(14, 20), BitString.fromBinaryString("110011"));
		assertEquals(a.subVector(16, 20), BitString.fromBinaryString("1100"));
		assertEquals(a.subVector(18, 20), BitString.fromBinaryString("11"));

		assertEquals(a.subVector(8, 12), BitString.fromBinaryString("0101"));
	}
	
	@Test
	public void testSubVectorDisjoint() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		BitString b = a.subVector(0, a.length());
		
		assertEquals(a, b);
		assertNotSame(a, b);
		
		a.not();
		
		assertNotEquals(a, b);
	}

	@Test
	public void testXor() {
		BitString a = BitString.fromBinaryString("11001111010101011010");
		BitString b = new BitString(a);
		BitString c = new BitString(a);
		c.flip(0, c.length());
		
		BitString full = new BitString(a.length());
		full.set(0, full.length());
		BitString blank = new BitString(a.length());
		
		// xor with 000000 should return original string
		a.xor(blank);
		assertEquals( a, b );
		assertNotEquals( a, blank );
		
		// xor with 111111 should return inverse
		a.xor(full);
		assertEquals( a, c );
		assertNotEquals( a, b );
	}
	
	@Test
	public void testHammingDistance() {
		String s = "11001111010101011010";
		BitString a = BitString.fromBinaryString(s);
		
		BitString full = new BitString(a.length());
		full.set(0, full.length());
		BitString blank = new BitString(a.length());
		
		assertEquals(BitString.HammingDistance(a, blank), 12);
		assertEquals(BitString.HammingDistance(a, full), 8);
		assertEquals(BitString.HammingDistance(blank, full), 20);
	}
}

// End ///////////////////////////////////////////////////////////////
