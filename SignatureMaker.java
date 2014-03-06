import java.util.*;
import java.math.*;
import java.io.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class SignatureMaker {

	public static void main(String args[]) {
		
		Scanner sc = new Scanner(System.in);

		while (sc.hasNextLine()) {
			String pathToManifest = sc.nextLine();

			List<Encoding> encodings = retrieveEncodings(pathToManifest);

			System.out.print(pathToManifest + "\t");
			
			int[][] segmentSizes = new int[encodings.size()][];

			int count = 0;

			for (Encoding encoding : encodings) {
				segmentSizes[count] = encoding.getSegmentSizes();

				System.out.print(encoding.getBitrate());

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

	private static List<Encoding> retrieveEncodings(String pathToManifest) {
		List<Encoding> encodings = new LinkedList<Encoding>();
		
		try {
			File xmlFile = new File(pathToManifest + "/manifest.xml");
			String xmlString = new String("");

			Scanner sc = new Scanner(xmlFile);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (line.contains("<HMAC>")) {
					continue;
				}
				else {
					xmlString += line + "\n";
				}
			}

			sc.close();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlString));
			Document doc = dBuilder.parse(is);

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("nccp:videodownloadable");

			for (int temp = 0; temp < nList.getLength(); temp++) {
 
				Node nNode = nList.item(temp);
 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
					Element eElement = (Element) nNode;
 
					String bitrate = eElement.getElementsByTagName("nccp:bitrate").item(0).getTextContent();
					String url = eElement.getElementsByTagName("nccp:url").item(0).getTextContent();

					encodings.add(new Encoding(bitrate, url));
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR: Unable to retrieve encodings.");
			System.exit(0);
		}

		Encoding[] sortingArray = encodings.toArray(new Encoding[encodings.size()]);

		for (int j = 1; j < sortingArray.length; j++) {
			Encoding key = sortingArray[j];
			int i = j - 1;
			while ((i > -1) && (sortingArray[i].getBitrate() > key.getBitrate())) {
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
