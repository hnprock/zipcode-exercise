package com.hp.zipcode.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.MessageFormat;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.hp.zipcode.exception.InvalidZipCodeRangeException;
import com.hp.zipcode.model.ZipCodeRange;

/**
 * 
 * @author Huy Pham
 *
 */
public class ZipCodeServiceTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private ZipCodeService zipcodeService = new ZipCodeService();

	/**
	 * Test for zip code ranges than contains invalid lower bound zip code. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInvalidLowerBoundZipCodeRange() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, "9574a"));

		// A zip code ranges with non-numeric lower bound zip code
		String zipcodeRanges = "[9574a ,95756] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges than contains invalid upper bound zip code. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInvalidUppperBoundZipCodeRange() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, "9575a"));

		// A zip code ranges with non-numeric upper bound zip code
		String zipcodeRanges = "[95746, 9575a] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges than contains range with missing lower bound. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMissingLowerBoundZipCodeRange() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, ""));

		// A zip code ranges with missing lower bound zip code
		String zipcodeRanges = "[, 95756] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges than contains range with missing upper bound. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMissingUpperBoundZipCodeRange() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, "95746,"));

		// A zip code ranges with missing upper bound zip code
		String zipcodeRanges = "[95746,] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges than contains more than two zip codes. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTooManyZipCodeWithinOneRange() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(
				MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, "95746, 95756, 95757"));

		// A zip code ranges with three zip codes
		String zipcodeRanges = "[95746, 95756, 95757] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges with lower bound greater than upper bound. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_OUT_OF_BOUND_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testZipCodeRangeOutOfBound() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_OUT_OF_BOUND_ERROR, "95756", "95746"));

		// A zip code ranges with lower bound greater than upper bound
		String zipcodeRanges = "[95756, 95746] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for zip code ranges with missing lower and upper bound zip codes. 
	 * Expect InvalidZipCodeRangeException to be thrown with an
	 * ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithMissingLowerAndUpperBoundZipCodes() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, ""));

		// A range of zip codes with missing lower and upper bound zip codes
		String zipcodeRanges = "[95746, 95756] [] [95766, 95776]";
		zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
	}

	/**
	 * Test for empty zip code ranges. 
	 * Expect InvalidZipCodeRangeException to be thrown with an ZIPCODE_RANGE_VALIDATION_ERROR error message.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithNullZipCodeRanges() throws Exception {
		expectedEx.expect(InvalidZipCodeRangeException.class);
		expectedEx.expectMessage(MessageFormat.format(ZipCodeService.ZIPCODE_RANGE_VALIDATION_ERROR, ""));

		// A empty zip code ranges
		zipcodeService.mergeOverlappingZipCodeRanges("");
	}

	/**
	 * Test for zip code ranges with order overlapping ranges. 
	 * Expect a non-overlapping zip code ranges to be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithOrderOverlappingZipCodeRanges() throws Exception {
		// A zip code ranges with order overlapping zip codes
		String zipcodeRanges = "[95746, 95766] [95756, 95776]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
		assertNotNull(zipCodeRangeList);
		assertEquals(1, zipCodeRangeList.size());
		assertEquals(95746, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(95776, zipCodeRangeList.get(0).getUpperBound());
	}

	/**
	 * Test for zip code ranges with a no order overlapping ranges. 
	 * Expect a non-overlapping zip code ranges to be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithNoOrderOverlappingZipCodeRanges() throws Exception {
		// A zip code ranges with no order overlapping zip codes
		String zipcodeRanges = "[95746, 95766]  [95786, 95796] [95756, 95776]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
		assertNotNull(zipCodeRangeList);
		assertEquals(2, zipCodeRangeList.size());
		assertEquals(95746, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(95776, zipCodeRangeList.get(0).getUpperBound());
		assertEquals(95786, zipCodeRangeList.get(1).getLowerBound());
		assertEquals(95796, zipCodeRangeList.get(1).getUpperBound());
	}

	/**
	 * Test for zip code ranges that contains all overlapping ranges. 
	 * Expect one non-overlapping zip code range to be returned.
	 *
	 * @throws Exception
	 */
	@Test
	public void testMergeWithJustOverlapping() throws Exception {
		String zipcodeRanges = "[10000,10001] [10002,10003] [10004,10005]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);

		assertEquals(1, zipCodeRangeList.size());
		assertEquals(10000, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(10005, zipCodeRangeList.get(0).getUpperBound());
	}

	/**
	 * Test for zip code ranges with no overlapping ranges. 
	 * Expect the original zip code ranges to be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithNoOverlappingZipCodeRanges() throws Exception {
		// A zip code ranges of zip codes with no overlapping
		String zipcodeRanges = "[95746, 95756] [95766, 95776]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
		assertNotNull(zipCodeRangeList);
		assertEquals(2, zipCodeRangeList.size());
		assertEquals(95746, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(95756, zipCodeRangeList.get(0).getUpperBound());
		assertEquals(95766, zipCodeRangeList.get(1).getLowerBound());
		assertEquals(95776, zipCodeRangeList.get(1).getUpperBound());
	}

	/**
	 * Test for same lower and upper bounds in the zip code ranges. 
	 * Expect the original code ranges to be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithSameLowerAndUpperBoundZipCodeRanges() throws Exception {
		// A range of zip codes with same lower and upper bounds
		String zipcodeRanges = "[95746, 95746] [95766, 95766]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
		assertNotNull(zipCodeRangeList);
		assertEquals(2, zipCodeRangeList.size());
		assertEquals(95746, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(95746, zipCodeRangeList.get(0).getUpperBound());
		assertEquals(95766, zipCodeRangeList.get(1).getLowerBound());
		assertEquals(95766, zipCodeRangeList.get(1).getUpperBound());
	}

	/**
	 * Test for zip code ranges that contains tabs in the ranges. 
	 * Expect a non-overlapping zip code ranges to be returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMergeWithTabsInZipCodeRanges() throws Exception {
		String TAB = "\t";

		// A zip code ranges with order overlapping zip codes that contains tabs
		String zipcodeRanges = "[95746" + TAB + ", 95766] [95756 ," + TAB + "	95776]";
		List<ZipCodeRange> zipCodeRangeList = zipcodeService.mergeOverlappingZipCodeRanges(zipcodeRanges);
		assertNotNull(zipCodeRangeList);
		assertEquals(1, zipCodeRangeList.size());
		assertEquals(95746, zipCodeRangeList.get(0).getLowerBound());
		assertEquals(95776, zipCodeRangeList.get(0).getUpperBound());
	}
}
