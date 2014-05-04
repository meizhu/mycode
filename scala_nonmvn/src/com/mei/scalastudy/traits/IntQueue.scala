package com.mei.scalastudy.traits
import scala.collection.mutable.ArrayBuffer

abstract class IntQueue {
  def put(x: Int)
  def get(): Int
}

class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]
  override def get() = buf.remove(0)
  override def put(x: Int) { buf += x }
}

//This means that the Doubling can only be mixed into a class that also extends IntQueue
trait Doubling extends IntQueue {

  //the abstract override is only allowed in trait method. it means that the trait must be mixed in after some class
  //that has a concrete implementation of the method in question
  abstract override def put(x: Int) { super.put(2 * x) }
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int) { super.put(x + 1) }
}

trait FilterNegative extends IntQueue {
  abstract override def put(x: Int) {
    if (x >= 0) super.put(x)
  }
}

