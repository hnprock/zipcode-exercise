package com.hp.zipcode;

import java.util.List;
import java.util.Scanner;

import com.hp.zipcode.exception.InvalidZipCodeRangeException;
import com.hp.zipcode.model.ZipCodeRange;
import com.hp.zipcode.service.ZipCodeService;

/**
 *
 * Main class that accepts a list of range of 5 digits zip code from the command line
 * then merge the overlapping ranges and print the result on the console.
 * 
 * EXAMPLES:
 * If the input = [94133,94133] [94200,94299] [94600,94699]
 * Then the output should be = [94133,94133] [94200,94299] [94600,94699]
 *
 * If the input = [94133,94133] [94200,94299] [94226,94399] 
 * Then the output should be = [94133,94133] [94200,94399]
 * 
 * @author Huy Pham
 *
 */
public class ZipCodeMainApp
{	
    public static void main( String[] args )
    {   
        Scanner scanner = null;
        try {
            System.out.println("Please input the zipcode ranges: ");
            scanner = new Scanner(System.in);
            String line = scanner.nextLine();
                        
            ZipCodeService zipCodeService = new ZipCodeService();
            
            //validate and merge overlapping zip code ranges
            //throw exception when detects an invalid range
            List<ZipCodeRange> mergeZipcodeRanges = zipCodeService.mergeOverlappingZipCodeRanges(line);
        	        	
            //print the merged zip code ranges to the console
            printZipCodeRangesToConsole(mergeZipcodeRanges);
        } catch (InvalidZipCodeRangeException e) {
        	e.printStackTrace(System.err);
        } finally {
            if (scanner != null) {
                scanner.close();        		
            }
        }
    }
    /**
     * Print zip code ranges to the console.
     * 
     * @param mergeZipcodeRanges list of zip code ranges
     */
    private static void printZipCodeRangesToConsole(List<ZipCodeRange> mergeZipcodeRanges) {
    	System.out.println("\nMerged zipcode ranges:\n");
    	mergeZipcodeRanges.forEach(item->System.out.print(item));   	
   }
}
