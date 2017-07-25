
package org.mitlware.solution.permutation;

import java.util.Set;

//////////////////////////////////////////////////////////////////////

public interface Permutation {
	/**
	 * @return all values that that are non-identically mapped by this permutation
	 */
	
	public Set< Integer > preimage();
	
	/**
	 * @return smallest value that is non-identically mapped by this permutation
	 */
	public int minPreimage();

	/**
	 * @return largest value that is non-identically mapped by this permutation
	 */
	public int maxPreimage();

	/**
	 * @return mapping of value under by this permutation
	 */
	
	public int image( int value );
}

// End ///////////////////////////////////////////////////////////////
