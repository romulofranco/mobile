package totalcross.hardware.map;

import com.totalcross.annotations.ReplacedByNativeOnDeploy;

public class Map {
	@ReplacedByNativeOnDeploy
	private static boolean nativeMapOpen() { return false; }
	@ReplacedByNativeOnDeploy
	private static void nativeMapClose() { }

	
	public static void mapOpen() {
		System.out.println("TotalCross: mapOpen");
		nativeMapOpen();
	}
	
	public static void mapClose() {
		System.out.println("TotalCross: mapClose");
		nativeMapClose();
	}
}
