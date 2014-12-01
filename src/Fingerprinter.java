import java.util.*;
import java.math.*;
import java.io.*;
import java.net.*;

public class Fingerprinter {

	private static final int[] BITRATES = {235,375,560,750,1050,1750,2350,3000};

	public static void main(String args[]) {
		
		Scanner sc = new Scanner(System.in);

		while (sc.hasNextLine()) {
			String pathToURLs = sc.nextLine();

			List<Encoding> encodings = retrieveEncodings(pathToURLs);

			System.out.print(pathToURLs + "\t");
			
			int[][] segmentSizes = new int[encodings.size()][];

			int count = 0;

			for (Encoding encoding : encodings) {
				segmentSizes[count] = encoding.getSegmentSizes();

				System.out.print(BITRATES[count]);

				count++;

				if (count < encodings.size()) {
					System.out.print(",");
				} else {
					System.out.print("\t");
				}
			}
			
			int numSegments = segmentSizes[0].length;

			for (int j = 0; j < numSegments; j++) {
				for (int i = 0; i < count; i++) {
					System.out.print(segmentSizes[i][j]);

					if ((i == (count-1)) && (j == (numSegments-1))) {
						System.out.print("\n");
					} else {
						System.out.print(",");
					}
				}
			}
		}

		sc.close();
	}

	private static List<Encoding> retrieveEncodings(String pathToURLs) {
		List<Encoding> encodings = new LinkedList<Encoding>();
		
		try {
			File urlList = new File(pathToURLs + "/urls.txt");

			Scanner sc = new Scanner(urlList);

			while (sc.hasNextLine()) {
				Encoding encoding = new Encoding(sc.nextLine());

				if (encoding.getAvgBitrate() > 100) {
					encodings.add(encoding);
				}
			}

			sc.close();

		} catch (Exception e) {
			System.out.println("ERROR: Unable to retrieve encodings.");
			System.exit(0);
		}

		Encoding[] sortingArray = encodings.toArray(new Encoding[encodings.size()]);

		for (int j = 1; j < sortingArray.length; j++) {
			Encoding key = sortingArray[j];
			int i = j - 1;
			while ((i > -1) && (sortingArray[i].getAvgBitrate() > key.getAvgBitrate())) {
				sortingArray[i+1] = sortingArray[i];
				i--;
			}
			sortingArray[i+1] = key;
		}

		List<Encoding> sortedEncodings = new LinkedList<Encoding>();
		for (int i=0; i < sortingArray.length; i++) {
			sortedEncodings.add(sortingArray[i]);
		}
		return sortedEncodings;
	}
}
