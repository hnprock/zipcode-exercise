package com.hp.zipcode.model;

/**
 * Define the lower and upper bounds of the zip code range.
 * 
 * @author Huy Pham
 *
 */
public class ZipCodeRange {
	private int lowerBound; //the lower bound
	private int upperBound; //the upper bound
	
	public ZipCodeRange(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}
	
	public String toString() {
		return new StringBuilder().append("[").append(lowerBound).append(", ").append(upperBound).append("]").toString();
	}
}
