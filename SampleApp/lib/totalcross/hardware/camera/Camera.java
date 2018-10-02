package totalcross.hardware.camera;

import com.totalcross.annotations.ReplacedByNativeOnDeploy;

import totalcross.ui.image.Image;

public class Camera {
	@ReplacedByNativeOnDeploy
	private static boolean nativeCameraOpen(CameraProperties properties) { return false; }
	@ReplacedByNativeOnDeploy
	private static void nativeCameraClose() { }
	@ReplacedByNativeOnDeploy
	private static String nativeCameraCapture() { return null; }
	
	
	private static CameraProperties properties;
	private static boolean isOpen;
	
	/**
	 * Opens the device camera using the default parameters.
	 * When successfully opened, a camera preview will be added to the screen.
	 * If the camera was already open, it will be reset
	 * @throws CameraException happens when a camera is not available or present
	 */
	public static void open() throws CameraException {
		Camera.open(new CameraProperties());
	}
	/**
	 * Opens the device camera using the given parameters.
	 * When successfully opened, a camera preview will be added to the screen.
	 * If the camera was already open, it will be reset to the given parameters
	 * @throws CameraException happens when a camera is not available or present
	 */
	public static void open(CameraProperties properties) throws CameraException {
		if (nativeCameraOpen(properties)) {
			isOpen = true;
		} else {
			throw new CameraException("Could not open camera");
		}
	}
	
	/**
	 * Closes the current opened camera.
	 * If the camera is closed, nothing happens.
	 */
	public static void close() {
		if (isOpen) {
			nativeCameraClose();
			isOpen = false;
		}
	}
	
	/**
	 * Captures an image using the currently opened camera
	 * @return the path to the captured image
	 */
	public static String captureImage() {
		if (isOpen) {
			return nativeCameraCapture();
		} else {
			return null;
		}
	}
}
