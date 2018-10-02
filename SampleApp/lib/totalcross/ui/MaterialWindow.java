package totalcross.ui;

import totalcross.sys.SpecialKeys;
import totalcross.ui.anim.ControlAnimation;
import totalcross.ui.anim.ControlAnimation.AnimationFinished;
import totalcross.ui.anim.FadeAnimation;
import totalcross.ui.anim.PathAnimation;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.KeyListener;
import totalcross.ui.event.PressListener;
import totalcross.ui.font.Font;
import totalcross.ui.icon.Icon;
import totalcross.ui.icon.MaterialIcons;

/**
 * A window with a top bar + return button supporting slide-in animations.
 */
public class MaterialWindow extends Window {
  private Bar bar;
  private Container content;
  private ControlAnimation currentAnimation;
  private int animDir;
  private int totalTime = 400;

  public MaterialWindow() {
    this("");
  }
  
  private SideMenuContainer findSideMenu(Container c) {
    if (c instanceof SideMenuContainer) {
      return (SideMenuContainer) c;
    }
    Control[] children = c.getChildren();
    for (Control control : children) {
      if (control instanceof SideMenuContainer) {
        return (SideMenuContainer) control;
      }
      if (control instanceof Container) {
        return findSideMenu((Container) control);
      }
    }
    return null;
  }

  public MaterialWindow(String title) {
    super(null, Window.NO_BORDER);
    Font medium = Font.getFont("Roboto Medium", false, 20);
    Icon i = new Icon(MaterialIcons._ARROW_BACK);

    // cannot use empty constructor for Bar, otherwise we won't be able to use setTitle later
    bar = new Bar(title);
    bar.setFont(medium != null ? medium : Font.getFont(bar.getFont().name, false, 20));
    bar.drawBorders = false;
    bar.backgroundStyle = Container.BACKGROUND_SOLID;
    
    bar.backColor =  0x4A90E2;
    SideMenuContainer smc = findSideMenu(MainWindow.getMainWindow());
    if (smc != null) {
      Control[] children2 = smc.getChildren();
      for (Control control2 : children2) {
        if (control2 instanceof Bar) {
          bar.setBackForeColors(control2.getBackColor(), control2.getForeColor());
        }
      }
    }
    bar.addButton(i, false);

    bar.addPressListener(new PressListener() {
      @Override
      public void controlPressed(ControlEvent e) {
        if (bar.getSelectedIndex() == 1) {
          MaterialWindow.this.unpop();
        }
      }
    });

    fadeOtherWindows = false;
    animDir = BOTTOM;
    
    this.addKeyListener(new KeyListener() {
      
      @Override
      public void specialkeyPressed(KeyEvent e) {
        if (e.key == SpecialKeys.ESCAPE) {
          MaterialWindow.this.unpop();
        }
      }
      
      @Override
      public void keyPressed(KeyEvent e) {
      }
      
      @Override
      public void actionkeyPressed(KeyEvent e) {
      }
    });
  }

  public void setBarFont(Font f) {
    bar.setFont(f);
  }

  @Override
  public void setForeColor(int color) {
    bar.setForeColor(color);
  }

  @Override
  public void setBackColor(int color) {
    bar.setBackColor(color);
  }

  protected void setRect(boolean screenResized) {
    switch (animDir) {
    case LEFT:
    case RIGHT:
      setRect(animDir, TOP, SCREENSIZE, SCREENSIZE, null, screenResized);
      break;
    default:
      setRect(100000, 100000, SCREENSIZE, SCREENSIZE, null, screenResized);
      break;
    }
  }

  @Override
  public void setTitle(String title) {
    if (title != null) {
      bar.setTitle(title);
    }
  }

  public String getTitle() {
    return bar.getTitle();
  }

  @Override
  public void unpop() {
    if (currentAnimation != null) {
      return;
    }

    if (animDir == CENTER) {
      currentAnimation = FadeAnimation.create(this, false, null, totalTime);
    } else {
      currentAnimation = PathAnimation.create(this, -animDir, null, totalTime);
    }
    currentAnimation.setAnimationFinishedAction(new AnimationFinished() {
      @Override
      public void onAnimationFinished(ControlAnimation anim) {
        currentAnimation = null;
        MaterialWindow.super.unpop();
      }
    });
    currentAnimation.start();
  }

  @Override
  public void popup() {
    setRect(false);
    super.popup();
  }

  @Override
  public void initUI() {
    content = initialize();

    add(bar, LEFT, TOP);
    add(content, LEFT, AFTER, FILL, FILL);
  }

  @Override
  public void onPopup() {
    initUI();

    if (currentAnimation != null) {
      return;
    }

    screenResized(); // fix problem when the container is on portrait, then landscape, then closed, then portrait, then open
    if (animDir == CENTER) {
      resetSetPositions();
      setRect(CENTER, CENTER, KEEP, KEEP);
      currentAnimation = FadeAnimation.create(this, true, null, totalTime);
    } else {
      currentAnimation = PathAnimation.create(this, animDir, null, totalTime);
    }
    currentAnimation.setAnimationFinishedAction(new AnimationFinished() {
      @Override
      public void onAnimationFinished(ControlAnimation anim) {
        currentAnimation = null;
      }
    });
    currentAnimation.start();
  }

  @Override
  public void screenResized() {
    /* needed to void the original 'screenResized' implementation */
    reposition();
  }

  public Container initialize() {
    return new Container();
  }
}
