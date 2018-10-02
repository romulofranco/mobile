package totalcross.ui;

import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.FocusListener;
import totalcross.ui.event.FontChangeEvent;
import totalcross.ui.event.FontChangeHandler;
import totalcross.ui.event.TimerEvent;
import totalcross.ui.event.TimerListener;
import totalcross.ui.event.ValueChangeEvent;
import totalcross.ui.event.ValueChangeHandler;
import totalcross.ui.font.Font;

public class FloatingLabel<T extends Control & HasValue<?>> {

  private T target;

  private Font captionAnimationFontTarget;

  private Font captionFontSmall;

  public Font fcap;

  public int xcap, ycap, ycap0, xcap0, inccap;

  public boolean isRunning;

  private boolean isBig = true;

  private TimerEvent timer;

  private final TimerListener updateListener = new TimerListener() {

    @Override
    public void timerTriggered(TimerEvent event) {
      singleStep(target.getFont());
      Window.needsPaint = true;
      if (fcap.size == captionAnimationFontTarget.size) {
        isBig = !isBig;
        isRunning = false;
        target.removeTimerListener(updateListener);
        target.removeTimer(timer);
        //          if (postPopupKCC) {
        //            postPopupKCC = false;

        // show keyboard, triggering event should be better
        //            popupKCC();
        //          }
      }
    }
  };

  public FloatingLabel(T target) {
    this.target = target;
    captionFontSmall = Font.getFont(target.getFont().name, false, target.getFont().size * 75 / 100);
    fcap = target.getFont();

    target.addFontChangeHandler(new FontChangeHandler() {

      @Override
      public void onFontChange(FontChangeEvent event) {
        captionFontSmall = Font.getFont(event.font.name, false, event.font.size * 75 / 100);
        fcap = event.font;
      }
    });

    if (target instanceof TextControl) {
      target.addFocusListener(new FocusListener() {

        @Override
        public void focusOut(ControlEvent e) {
          if (target.getValue() != null ^ !isBig) {
            animateMaterial(true);
          }
        }

        @Override
        public void focusIn(ControlEvent e) {
          if (target.getValue() == null & isBig) {
            animateMaterial(true);
          }
        }
      });
    }
    target.addValueChangeHandler(new ValueChangeHandler<Object>() {

      @Override
      public void onValueChange(ValueChangeEvent<Object> event) {
        if (target.getValue() != null & isBig) {
          animateMaterial(target.isDisplayed());
        }
      }
    });
  }

  private void animateMaterial(boolean slow) {
    if (!isRunning) {
      final Font targetFont = target.getFont();
      captionAnimationFontTarget = isBig ? captionFontSmall : targetFont;
      if (fcap.size != captionAnimationFontTarget.size) {
        if (slow) {
          inccap = fcap.size == targetFont.size ? -1 : 1;
          isRunning = true;
          target.addTimerListener(updateListener);
          timer = target.addTimer(1);
        } else {
          inccap = captionAnimationFontTarget.size - fcap.size;
          singleStep(targetFont);
        }
      }
    }
  }

  private void singleStep(Font targetFont) {
    int fmHmin = captionFontSmall.fm.height;
    fcap = fcap.adjustedBy(inccap);
    ycap = ycap0 * (fcap.fm.height - fmHmin) / (targetFont.fm.height - fmHmin);
    xcap = xcap0 * (fcap.fm.height - fmHmin) / (targetFont.fm.height - fmHmin);
  }

  public int getExtraHeight() {
    return captionFontSmall.fm.height;
  }
}
