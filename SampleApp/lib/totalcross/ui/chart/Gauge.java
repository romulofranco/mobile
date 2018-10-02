package totalcross.ui.chart;
import totalcross.ui.font.*;
import totalcross.ui.Control;
import totalcross.ui.Label;
import totalcross.ui.gfx.*;

public class Gauge extends Control {
	
	int prefferedHeight;
	int sectionCount;
	int sum = 0;
	int centerX, centerY;
	int startAngle, endAngle;
	int[] sections = new int[255];
	int[] colors = new int[255];
	int angleRange;
	double markWidthScale = 0.15;
	//int valuesCount;
	int value; 
	Label valueLabel;
	int min;
	int max;
	String valuePrefix = "";
	String valueSuffix = "";
	Font valueLabelFont = Font.getFont(true, Font.BIG_SIZE);
	Font ticksLabelFont = Font.getFont(Font.NORMAL_SIZE);
	boolean showTicks = true;
	private boolean showTickValues;
  
	public Gauge() {
		this(0, 180, 180);
	}
	
	public Gauge(int minValue, int maxValue) {
		this (minValue, maxValue, 180);
	}
	
	public Gauge(int angleRange) {
		this (0, angleRange, angleRange);
	}
	
	public Gauge(int minValue, int maxValue, int angleRange) {
		this.min = minValue;
		this.max = maxValue;
		if(angleRange < 0)
			this.angleRange = 0;
		else if(angleRange > 360)
			this.angleRange = 360;
		else {
			this.angleRange = angleRange;
		}
		valueLabel = new Label(valuePrefix+value+valueSuffix);
	}
	
	public Gauge section(int value, int color) {
		sections[sectionCount] = value;
		colors[sectionCount++] = color;
		sum += value;
		
		return this;
	}
	
	public Gauge setValue(int value) {
		this.value = value;
		return this;
	}
	
	public Gauge setMarkWidthScale(double markWidthScale) {
		if(markWidthScale < 0 )
			this.markWidthScale = 0;
		if(markWidthScale > 1)
			this.markWidthScale = 1;
		else 
			this.markWidthScale = markWidthScale;
		return this;
	}
	
	public Gauge setValueSuffix(String suffix) {
		this.valueSuffix = suffix;
		return this;
	}
	
	public Gauge setValuePrefix (String prefix) {
		this.valuePrefix = prefix;
		return this;
	}
	
	public Gauge setValueLabelFont(Font font) {
		this.valueLabelFont = font;
		return this;
	}
	
	public Gauge setTickFont(Font font) {
		this.ticksLabelFont = font;
		return this;
	}
	
	public Gauge showTickValues(boolean showTickValues) {
		this.showTickValues = showTickValues;
		return this;
	}
	
	@Override
	public int getPreferredHeight() {
		return prefferedHeight;
	}
	
  @Override
  public void onPaint(Graphics g) {
    g.backColor = this.backColor;
    g.fillRect(0, 0, width, height);
    
    final int center = width / 2;
    int radius = (int) (width * 0.49f);
    if(showTicks) radius = radius - fm.stringWidth(""+min) - fm.stringWidth(""+max);
    final int maxHeight = height - 1;
    centerY = angleRange <= 180? maxHeight: 
    	maxHeight - (int)(Math.sin(((angleRange - 180)/2.0)* Math.PI/180)* (double)radius); // Center goes up if angleRange > 180
    
    if(showTicks) centerY = centerY - fm.height - 2; //margin
    
    centerX = width/2;
    int start = (180 - angleRange)/2;        
    int lastStart = start;
    g.foreColor = getForeColor();
    Coord out = new Coord();
    
    g.setFont(ticksLabelFont);
    int incremntSum = 0;
    // Marks are mounted backwards
    for (int i = sectionCount - 1; i >= 0; i--) {
      g.backColor = colors[i];
      final int sectionSize = (int) (((double) sections[i] / sum) * angleRange);
      g.fillPie(centerX, centerY, radius, lastStart, lastStart + sectionSize);
      
      if (showTickValues) {
      	incremntSum += sections[i];
        int value = (int)((1.0 - (double)incremntSum/sum) * (max - min));
        g.foreColor = Color.BLACK;
				g.backColor = Color.WHITE;
				//g.foreColor = Color.WHITE;
				getTickLabelPlacement(""+value, lastStart+sectionSize, radius, 2, out);
				g.drawText(""+value, out.x, out.y);
			
      }
      
      lastStart += sectionSize;
    }
    
    int maxY = maxHeight, minY;
    
    if (showTickValues) {
			getTickLabelPlacement(""+max, start+1, radius, 2, out);
			g.drawText(""+max, out.x, out.y);
			maxY = out.y + ticksLabelFont.fm.height;
    }
    
    g.backColor = this.backColor;
    g.fillPie(centerX, centerY, (int)(radius * (1 - markWidthScale)), start, lastStart);
    
    startAngle = lastStart-1;
    endAngle = start+1;
		
 // Draw value Label
    g.foreColor = Color.BLACK;
    minY = centerY - (radius+20+ticksLabelFont.fm.height);
    String valueString = valuePrefix+value+valueSuffix;
    g.setFont(valueLabelFont);
    g.drawText(valueString,
    		centerX - (fm.stringWidth(valuePrefix+value+valueSuffix)/2),
    		centerY - (minY), true, Color.WHITE);
    prefferedHeight = maxY - minY;
    
    setRect(getRect());    
    drawArrow(g, radius);
    
  }
  
  private void getTickLabelPlacement (String label, int angle, int radius, int margin, Coord out) {
  	
  	out.x = centerX + (int)((double)(radius + margin)*Math.cos(angle*Math.PI/180)); 
  	out.y = centerY - (int)((double)(radius + margin)*Math.sin(angle*Math.PI/180)); 
  	
  	if(angle == 0) {
  		out.y = out.y - fm.height/2;
  	}
  	else if(angle == 90) {
  		out.y = out.y - fm.height;
  		out.x = out.x - fm.stringWidth(label)/2;
  	}
  	else if(angle == 180) {
  		out.y = out.y - fm.height/2;
  		out.x = out.x - fm.stringWidth(label);
  	}
  	else if(angle > 0 && angle < 90) { // 1st quadrant
  		out.y = out.y - fm.height;
  	}
  	else if(angle > 90 && angle < 180) { // 2nd quadrant
  		out.y = out.y - fm.height;
  		out.x = out.x - fm.stringWidth(label);
  	}
  	else if(angle > 180 && angle <= 270) { // 3rd quadrant
  		out.x = out.x - fm.stringWidth(label);
  	}
  }
  
  private void drawArrow (Graphics g, int radius) {
  	Coord out = new Coord();
  	int arrowLength = (int)(radius*(1 - markWidthScale) - 5);
  	int ptrCircleRadius = (int)(arrowLength*0.20);
  	g.fillPie(centerX, centerY, ptrCircleRadius, 0, 360);
		double angle = (double)(value - min)/(double)(max - min); // normalized in function of min and max value
		angle = startAngle - angle * (startAngle - endAngle);
	
		out.x = (int)(Math.cos(angle * Math.PI/180) * arrowLength + centerX); // centerX + radius = originX
		out.y = (int)(- Math.sin(angle * Math.PI/180) * arrowLength + (centerY)); // centerY = originY
		//g.drawThickLine(centerX, centerY, out.x, out.y, 3);
		double beta = angle - 180;
		double delta = Math.toDegrees(Math.atan(ptrCircleRadius/2.0/(double)arrowLength));
		g.foreColor = g.backColor = Color.BLACK;
		
		
		g.fillPie(out.x, out.y, arrowLength, beta, (beta+delta));
		g.fillPie(out.x, out.y, arrowLength, beta-delta, beta);
  	
  }
  
}