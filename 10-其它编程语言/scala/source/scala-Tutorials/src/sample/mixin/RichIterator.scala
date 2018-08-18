package sample.mixin

trait RichIterator extends AbsIterator {
  def foreach(fn:T => Unit) = while(hasNext) fn(next)
}