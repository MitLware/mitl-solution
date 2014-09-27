package statelet.bitvector;

/***
public static final class BitVectorIterator implements Iterator< Integer > {

final int numBits, numWords;
final long[] bits;
int doc = -1;

// Creates an iterator over the given {@link BitVector}. 
public FixedBitSetIterator(BitVector bits) {
this(bits.bits, bits.numBits, bits.numWords);
}

// Creates an iterator over the given array of bits. 
public FixedBitSetIterator(long[] bits, int numBits, int wordLength) {
this.bits = bits;
this.numBits = numBits;
this.numWords = wordLength;
}

@Override
public int nextDoc() {
if (doc == NO_MORE_DOCS || ++doc >= numBits) {
  return doc = NO_MORE_DOCS;
}
int i = doc >> 6;
long word = bits[i] >> doc;  // skip all the bits to the right of index

if (word != 0) {
  return doc = doc + Long.numberOfTrailingZeros(word);
}

while (++i < numWords) {
  word = bits[i];
  if (word != 0) {
    return doc = (i << 6) + Long.numberOfTrailingZeros(word);
  }
}

return doc = NO_MORE_DOCS;
}

@Override
public int docID() {
return doc;
}

@Override
public long cost() {
return numBits;
}

@Override
public int advance(int target) {
if (doc == NO_MORE_DOCS || target >= numBits) {
  return doc = NO_MORE_DOCS;
}
int i = target >> 6;
long word = bits[i] >> target; // skip all the bits to the right of index

if (word != 0) {
  return doc = target + Long.numberOfTrailingZeros(word);
}

while (++i < numWords) {
  word = bits[i];
  if (word != 0) {
    return doc = (i << 6) + Long.numberOfTrailingZeros(word);
  }
}

return doc = NO_MORE_DOCS;
}

@Override
public boolean hasNext() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public Integer next() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void remove() {
	// TODO Auto-generated method stub
	
}
}

***/
