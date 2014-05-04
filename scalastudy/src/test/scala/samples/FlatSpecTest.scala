package samples
import org.scalatest.FlatSpec
import scala.collection.mutable.Stack
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FlatSpecTest extends FlatSpec {
  behavior of "A Stack"
  it should "pop values in last-in-first-out order" in {
    val stack = new Stack[String]
    stack.push("1")
    stack.push("2")
    assert(stack.pop === "2")
    assert(stack.pop === "1")
  }
}