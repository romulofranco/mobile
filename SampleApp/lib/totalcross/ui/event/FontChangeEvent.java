package totalcross.ui.event;

import totalcross.ui.font.Font;

public class FontChangeEvent extends Event<FontChangeHandler> {

  private static Type<FontChangeHandler> TYPE;

  public final Font font;

  public static final Type<FontChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<FontChangeHandler>();
    }
    return TYPE;
  }

  public FontChangeEvent(Font font) {
    this.font = font;
  }

  @Override
  public void dispatch(FontChangeHandler listener) {
    listener.onFontChange(this);
  }

//  @Override
//  public Type<FontChangeHandler> getAssociatedType() {
//    return TYPE;
//  }
}
