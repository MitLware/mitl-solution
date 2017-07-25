package org.mitlware.solution.polynomialroot;

import java.util.function.Function;

//////////////////////////////////////////////////////////////////////

public final class UnivariatePolynomialRealRootsFixedDegree
implements Function< Double, Double >
{
	private double [] impl;
	
	///////////////////////////////
	
	public UnivariatePolynomialRealRootsFixedDegree( double [] impl ) {
		if( !isValidImpl( impl ) )
			throw new IllegalArgumentException();
		
		this.impl = impl;
		
		assert( invariant() );
	}
	
	///////////////////////////////	

	@Override
	public Double apply( Double x )	{
		return evaluatePolynomial( x, impl );
	}
	
	///////////////////////////////
	
	public static boolean isValidImpl( double [] impl )	{
		return impl.length > 0;			
	}
	
	///////////////////////////////	
	
	@Override
	public String toString() {
		final long degree = impl.length - 1; 
		StringBuffer result = new StringBuffer();
		result.append( "degree: " + degree + " poly:" );
			
		result.append( impl[ 1 ] + '*' );			
		for( int i=0; i<degree; ++i ) {
			result.append( "(x - " );
			final double value = Math.abs( impl[ i + 1 ] );
			result.append( value + ")" );
		}

		return result.toString();
	}

	///////////////////////////////
	
	public boolean invariant() { return isValidImpl( impl ); }
	
	///////////////////////////////

	@Override
	public int hashCode() { return impl.hashCode();	}

	@Override	
	public boolean equals( Object o ) {
		if( !( o instanceof UnivariatePolynomialRealRootsFixedDegree ) )
			return false;
		
		UnivariatePolynomialRealRootsFixedDegree rhs = (UnivariatePolynomialRealRootsFixedDegree)o;
		return impl.equals( rhs.impl );
	}
	
	///////////////////////////////	

	private static double evaluatePolynomial( double x, double [] impl ) {
		final long degree = impl.length - 1;

		double outputValue = impl[ 1 ];
		for( int i=0; i<degree; ++i )		
			outputValue *= x - impl[ i + 1 ];

		return outputValue;
	} 
}

// End ///////////////////////////////////////////////////////////////
