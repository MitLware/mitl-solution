
package statelet.bitvector;

import java.util.Arrays;

public final class BitVector {
	
	private final long [] bits;
	private final int numBits;
	// private final int numWords;
	
	private int numWords() { return bits.length; } 
	
	///////////////////////////////

	// number of 64 bit words it would take to hold numBits
	private static int numLongs( int numBits ) {
		int numLong = numBits >>> 6;
		if( ( numBits & 63 ) != 0 )
			++numLong;
		
		return numLong;
	}
	
	///////////////////////////////	
	
	public BitVector( int numBits ) {
		this.numBits = numBits;
		bits = new long[ numLongs( numBits ) ];
		// numWords = bits.length;
	}

	public BitVector( BitVector rhs ) {
		bits = rhs.bits.clone();
		numBits = rhs.numBits;
	}
	
	/***
	public BitVector( long [] storedBits, int numBits ) {
		this.numWords = numLongs( numBits );
		if (numWords > storedBits.length)
			throw new IllegalArgumentException("The given long array is too small  to hold " + numBits + " bits");

		this.numBits = numBits;
		this.bits = storedBits;
	}
	***/
	
	///////////////////////////////
	
	public int length() { return numBits; }

	public int cardinality() {
		return (int) pop_array(bits, 0, bits.length);
	}

	public boolean get( int index ) {
		assert index >= 0 && index < numBits: "index=" + index + ", numBits=" + numBits;
		int i = index >> 6;               // div 64
		// signed shift will keep a negative index and force an
		// array-index-out-of-bounds-exception, removing the need for an explicit check.
		long bitmask = 1L << index;
		return (bits[i] & bitmask) != 0;
	}

	public void set(int index) {
		assert index >= 0 && index < numBits: "index=" + index + ", numBits=" + numBits;
		int wordNum = index >> 6;      // div 64
		long bitmask = 1L << index;
		bits[wordNum] |= bitmask;
	}

	public boolean getAndSet(int index) {
		assert index >= 0 && index < numBits;
		int wordNum = index >> 6;      // div 64
		long bitmask = 1L << index;
		boolean val = (bits[wordNum] & bitmask) != 0;
		bits[wordNum] |= bitmask;
		return val;
	}

	public void clear(int index) {
		assert index >= 0 && index < numBits;
		int wordNum = index >> 6;
		long bitmask = 1L << index;
		bits[wordNum] &= ~bitmask;
	}

	public boolean getAndClear(int index) {
		assert index >= 0 && index < numBits;
		int wordNum = index >> 6;      // div 64
		long bitmask = 1L << index;
		boolean val = (bits[wordNum] & bitmask) != 0;
		bits[wordNum] &= ~bitmask;
		return val;
	}

	/** Returns the index of the first set bit starting at the index specified.
	 * -1 is returned if there are no more set bits.
	 */
	public int nextSetBit(int index) {
		assert index >= 0 && index < numBits : "index=" + index + ", numBits=" + numBits;
		int i = index >> 6;
		long word = bits[i] >> index;  // skip all the bits to the right of index

		if( word!=0 )
			return index + Long.numberOfTrailingZeros(word);

		while(++i < numWords()) {
			word = bits[i];
			if( word != 0 )
				return (i<<6) + Long.numberOfTrailingZeros(word);
		}

		return -1;
	}

	/** Returns the index of the last set bit before or on the index specified.
   	 * -1 is returned if there are no more set bits.
   	 */
	public int prevSetBit(int index) {
		assert index >= 0 && index < numBits: "index=" + index + " numBits=" + numBits;
		int i = index >> 6;
		final int subIndex = index & 0x3f;  // index within the word
		long word = (bits[i] << (63-subIndex));  // skip all the bits to the left of index

		if( word != 0 )
			return (i << 6) + subIndex - Long.numberOfLeadingZeros(word);

		while (--i >= 0) {
			word = bits[i];
			if (word !=0 )
				return (i << 6) + 63 - Long.numberOfLeadingZeros(word);
		}

		return -1;
	}

	public void or(BitVector other) {
		or(other.bits, other.numWords());
	}
  
	private void or(final long[] otherArr, final int otherNumWords) {
		assert otherNumWords <= numWords() : "numWords=" + numWords() + ", otherNumWords=" + otherNumWords;
		final long[] thisArr = this.bits;
		int pos = Math.min(numWords(), otherNumWords);
		while (--pos >= 0)
			thisArr[pos] |= otherArr[pos];
	}
  
	public void xor(BitVector other) {
		assert other.numWords() <= numWords() : "numWords=" + numWords() + ", other.numWords=" + other.numWords();
		final long[] thisBits = this.bits;
		final long[] otherBits = other.bits;
		int pos = Math.min(numWords(), other.numWords());
		while( --pos >= 0 )
			thisBits[pos] ^= otherBits[pos];
	}
  

	public boolean intersects(BitVector other) {
		int pos = Math.min(numWords(), other.numWords());
		while(--pos>=0 )
			if( (bits[pos] & other.bits[pos]) != 0 ) 
				return true;

		return false;
	}

	public void and(BitVector other) {
		and(other.bits, other.numWords());
	}
  
	private void and(final long[] otherArr, final int otherNumWords) {
		final long[] thisArr = this.bits;
		int pos = Math.min(this.numWords(), otherNumWords);
		while(--pos >= 0)
			thisArr[pos] &= otherArr[pos];

		if (this.numWords() > otherNumWords)
			Arrays.fill(thisArr, otherNumWords, this.numWords(), 0L);
	}

	public void andNot(BitVector other) {
		andNot(other.bits, other.bits.length);
	}
  
	private void andNot(final long[] otherArr, final int otherNumWords) {
		final long[] thisArr = this.bits;
		int pos = Math.min(this.numWords(), otherNumWords);
		while(--pos >= 0)
			thisArr[pos] &= ~otherArr[pos];
	}

	@Override
	public BitVector clone() {
//		long[] bits = new long[this.bits.length];
//		System.arraycopy(this.bits, 0, bits, 0, bits.length);
//		return new BitVector(bits, numBits);
		return new BitVector( this );
	}

	@Override
	public int hashCode() {
		long h = 0;
		for (int i = numWords(); --i>=0;) {
			h ^= bits[i];
			h = (h << 1) | (h >>> 63); // rotate left
		}
		
		// fold leftmost bits into right and add a constant to prevent
		// empty sets from returning 0, which is too common.
		return (int) ((h>>32) ^ h) + 0x98761234;
	}
	
	/** returns true if both sets have the same bits set */
	@Override
	public boolean equals( Object o ) {
		if( this == o )
			return true;
		else if( !( o instanceof BitVector ) )
			return false;
		else {
			BitVector other = (BitVector) o;
			if( numBits != other.numBits )
				return false;
			return Arrays.equals(bits, other.bits);
		}
	}
  
	private static long pop_array(long[] arr, int wordOffset, int numWords) {
		long popCount = 0;
		for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i)
	      popCount += Long.bitCount(arr[i]);

		return popCount;
	}
}

// End ///////////////////////////////////////////////////////////////

