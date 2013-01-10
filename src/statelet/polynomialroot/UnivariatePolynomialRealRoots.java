package statelet.polynomialroot;

//////////////////////////////////////////////////////////////////////

public final class UnivariatePolynomialRealRoots
{
	private double [] impl;
	
	///////////////////////////////
	
	public UnivariatePolynomialRealRoots( double [] impl ) 
	{
		if( !isValidImpl( impl ) )
			throw new IllegalArgumentException();
		
		this.impl = impl;
		
		assert( invariant() );
	}
	
	///////////////////////////////	

	public double apply( double x )
	{
		return evaluatePolynomial( x, impl );
	}
	
	///////////////////////////////
	
	public static boolean isValidImpl( double [] impl )
	{
		final int len = impl.length;
		final long degree = Math.round( impl[ 0 ] );
		return len > 0 && degree >= 0 && len >= degree + 2;			
	}
	
	///////////////////////////////	
	
	@Override
	public String toString()
	{
		final long degree = Math.round( impl[ 0 ] ); 
		StringBuffer result = new StringBuffer();
		result.append( "degree: " + degree + " poly:" );
			
		result.append( impl[ 1 ] + '*' );			
		for( int i=0; i<degree; ++i )
		{
			result.append( "(x - " );
			final double value = Math.abs( impl[ i + 2 ] );
			result.append( value + ")" );
		}

		return result.toString();
	}

	///////////////////////////////
	
	public boolean invariant()
	{
		return isValidImpl( impl );
	}
	
	///////////////////////////////

	@Override
	public int hashCode()
	{
		return impl.hashCode();
	}

	@Override	
	public boolean equals( Object o )
	{
		if( !( o instanceof UnivariatePolynomialRealRoots ) )
			return false;
		
		UnivariatePolynomialRealRoots rhs = (UnivariatePolynomialRealRoots)o;
		return impl.equals( rhs.impl );
	}
	
	///////////////////////////////	

	private static double evaluatePolynomial( double x, double [] impl ) 
	{
		final long degree = Math.round( impl[ 0 ] );

		if( degree + 1 >= impl.length )
			throw new IllegalStateException();

		double outputValue = impl[ 1 ];
		for( int i=0; i<degree; ++i )		
			outputValue *= x - impl[ i + 2 ];

		return outputValue;
	} 
}

// End ///////////////////////////////////////////////////////////////
