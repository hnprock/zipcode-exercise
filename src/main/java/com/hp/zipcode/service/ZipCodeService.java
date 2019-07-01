package com.hp.zipcode.service;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.zipcode.exception.InvalidZipCodeRangeException;
import com.hp.zipcode.model.ZipCodeRange;

/**
 * This service is used to merge overlapping zip code ranges and also validate the ranges.
 * 
 * @author Huy Pham
 *
 */
public class ZipCodeService {
	//the regular expression to validate the five digits zip code
	public static final String VALID_ZIPCODE_RANGE_REGEX = "\\d{5}$";
	
	//the regular expression to parse the zip code ranges in the bracket
	public static final String PARSE_ZIPCODE_RANGE_REGEX = "\\[(.*?)\\]";
	
	//the delimiter separating the lower and upper bound of the zip code range
	public static final String COMMA_DELIMITER = ",";
	
	//error messages
	public static final String EXTRACT_ZIPCODE_RANGE_ERROR = "Failed to extract zip codes.";
	public static final String ZIPCODE_RANGE_VALIDATION_ERROR = "The provided zip code range {0} is not a valid zip code range.";
	public static final String ZIPCODE_RANGE_OUT_OF_BOUND_ERROR = "Lower bound zip code {0} cannot be greater than upper bound zip code {1}.";


	/**
	 * This method will merge all the overlapping zip code ranges.
	 * It also validates the zip code ranges.
	 * 
	 * @param zipCodeRanges	the string represents the zip code ranges
	 * @return a list of merged zip code ranges
	 * 
	 * @throws InvalidZipCodeRangeException when detects that the zip code ranges contains invalid ranges.
	 */
	public List<ZipCodeRange>  mergeOverlappingZipCodeRanges(String zipCodeRanges) throws InvalidZipCodeRangeException {
		if (zipCodeRanges == null || zipCodeRanges.length() == 0) {
			throw new InvalidZipCodeRangeException(MessageFormat.format(ZIPCODE_RANGE_VALIDATION_ERROR, zipCodeRanges));
		}
		
		//extract the zip code ranges first
		List<String> zipCodeRangeStringList = extractZipcodeRanges(zipCodeRanges);	
		
		//validate the zip code ranges and transform it to a list of type ZipcodeRange
		List<ZipCodeRange> zipCodeRangeList = validateAndTransformZipcodeRanges(zipCodeRangeStringList);
		
		//finally merge the overlapping zip code ranges
		return doMergeZipCodeRanges(zipCodeRangeList);
	}

	/**
	 * This method extracts the zip code ranges.
	 * 
	 * @param zipCodeRanges	the string represent the zip code ranges
	 * @return the list of zip code ranges of type String
	 * 
	 * @throws InvalidZipCodeRangeException when it fails to extract the zip code ranges
	 */
	protected List<String> extractZipcodeRanges(String zipCodeRanges) throws InvalidZipCodeRangeException {
		List<String> zipCodeRangeList = new ArrayList<String>();
		try {
			//compile the given expression to a pattern to match the zip code ranges in the bracket
			Pattern p3 = Pattern.compile(PARSE_ZIPCODE_RANGE_REGEX);

			Matcher m3 = p3.matcher(zipCodeRanges);
			while (m3.find()) {
				zipCodeRangeList.add(m3.group(1));
			}			
		} catch (Exception e) {
			throw new InvalidZipCodeRangeException(EXTRACT_ZIPCODE_RANGE_ERROR);
		}


		return zipCodeRangeList;
	}
	
	/**
	 * This method validates and transform the list of zip code in String format to a list of ZipcodeRange.
	 * 
	 * @param zipCodeRanges	the list of zip code ranges of type String
	 * @return a list of zip code ranges of type ZipcodeRange
	 * 
	 * @throws InvalidZipCodeRangeException when detects invalid zip code ranges
	 */
	protected List<ZipCodeRange> validateAndTransformZipcodeRanges(List<String> zipCodeRanges) throws InvalidZipCodeRangeException {
		List<ZipCodeRange> zipCodeRangesList = new ArrayList<ZipCodeRange>();
		
		try {
			zipCodeRanges.forEach(i -> {
				// splits the input range by comma
				String[] rangeArr = i.split(COMMA_DELIMITER);

				if (rangeArr.length == 2) {
					rangeArr[0] = rangeArr[0].trim();
					rangeArr[1] = rangeArr[1].trim();
					
					//validate the lower bound
					//cannot throw checked exception in a Lambda Expressions so have to throw non-checked exception
					if (!rangeArr[0].trim().matches(VALID_ZIPCODE_RANGE_REGEX)) {
						throw new RuntimeException(MessageFormat.format(ZIPCODE_RANGE_VALIDATION_ERROR, rangeArr[0]));
					}
					
					//validate the upper bound
					if (!rangeArr[1].trim().matches(VALID_ZIPCODE_RANGE_REGEX)) {
						throw new RuntimeException(MessageFormat.format(ZIPCODE_RANGE_VALIDATION_ERROR, rangeArr[1]));
					}
					
					ZipCodeRange zipcodeRange = new ZipCodeRange(Integer.parseInt(rangeArr[0]), Integer.parseInt(rangeArr[1]));
					
					//lower bound has to be less than or equal to upper bound else throw exception
					if (zipcodeRange.getLowerBound() > zipcodeRange.getUpperBound()) {
						throw new RuntimeException(MessageFormat.format(ZIPCODE_RANGE_OUT_OF_BOUND_ERROR, rangeArr[0], rangeArr[1]));
					}					
					zipCodeRangesList.add(zipcodeRange);
				} else {
					throw new RuntimeException(MessageFormat.format(ZIPCODE_RANGE_VALIDATION_ERROR, i));	
				}
			});			
		//trap non-checked exception and re-throw as a checked exception
		} catch (Exception e) {
			throw new InvalidZipCodeRangeException(e.getMessage());
		}

		return zipCodeRangesList;
	}
	
	
	/**
	 * This method will merge all the overlapping zip code ranges.
	 * 
	 * @param zipCodeRanges	the list represent the zip code ranges of type ZipCodeRange
	 * @return a list of merged zip code ranges of type ZipCodeRange
	 * 
	 */
	protected List<ZipCodeRange> doMergeZipCodeRanges(List<ZipCodeRange> zipCodeRanges) {
        	//sort the zip code ranges in ascending order of the lower bound
		zipCodeRanges.sort((ZipCodeRange o1, ZipCodeRange o2) -> o1.getLowerBound() - (o2.getLowerBound()));
		
		//now do the merge
        	LinkedList<ZipCodeRange> mergedZipcodeRanges = new LinkedList<ZipCodeRange>();
        	for (ZipCodeRange zipcodeRange : zipCodeRanges) {
            		// if the list of merged zip code ranges is empty or if the current
            		// zip code range does not overlap with the previous one then just append it.
            		if (mergedZipcodeRanges.isEmpty() || mergedZipcodeRanges.getLast().getUpperBound() < zipcodeRange.getLowerBound()) {
            			mergedZipcodeRanges.add(zipcodeRange);
            		}
            		// otherwise, there is overlap, so we merge the current and previous zip code range.
            		else {
            			mergedZipcodeRanges.getLast().setUpperBound( Math.max(mergedZipcodeRanges.getLast().getUpperBound(), zipcodeRange.getUpperBound()));
            		}
        	}
        
		return mergedZipcodeRanges;
	}

}
