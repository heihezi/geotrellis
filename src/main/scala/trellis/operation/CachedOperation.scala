package trellis.operation

import trellis.process._

/**
 * Trait providing caching support for operations which want to save their
 * result and return those on future invocations of run().
 */
trait CachedOperation[T] extends Op[T] {
  var cachedOutput:Option[StepOutput[T]] = None

  def cacheOutput(output:StepOutput[T]) = {
    cachedOutput = Some(output)
    output
  }

  override def run(context:Context): StepOutput[T] = {
    cachedOutput match {
      case Some(o) => { println("Cached operation.  woot."); o }
      case None => cacheOutput(_run(context))
    }
  }
}

case class Cache[T:Manifest](op:Op[T])
extends SimpleOperation[T] with CachedOperation[T] {
  def _value(context:Context) = context.run(op)
}
