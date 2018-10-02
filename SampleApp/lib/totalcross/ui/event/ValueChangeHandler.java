package totalcross.ui.event;

public interface ValueChangeHandler<T> extends EventHandler {

  void onValueChange(ValueChangeEvent<T> event);
}
