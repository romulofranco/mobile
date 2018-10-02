package totalcross.hardware.camera;

public class CameraProperties {
	public static final int DOCKING_TOP = 1;
	public static final int DOCKING_BOTTOM = 2;
	public static final int DOCKING_LEFT = 3;
	public static final int DOCKING_RIGHT = 4;
	
	public static final int ORIENTATION_LANDSCAPE = 1;
	public static final int ORIENTATION_PORTRAIT = 2;
	
	public static final int CAMERA_FRONT_FACING = 1;
	public static final int CAMERA_REAR_FACING = 2;
	
	
	
	
	/** Setups the camera position on the screen */
	public int docking = DOCKING_BOTTOM;
	/** Setups which device camera should be used */
	public int cameraType = CAMERA_FRONT_FACING;
	/** Setups the captured image's width */
	public int captureWidth = 1920;
	/** Setups the captured image's height */
	public int captureHeight = 1080;
	/** Setups the preview size in relation to its docking position */
	public int previewSize = 600;
	/** Setups preview image orientation */
	public int previewOrientation = ORIENTATION_LANDSCAPE;
}
