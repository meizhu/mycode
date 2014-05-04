package samples
import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.testng.annotations.Test
//@RunWith(classOf[JUnitRunner])
class SpecTest extends Spec with MustMatchers {
  describe("A Map") {

    it("should only contain keys and values that were added to it") {
      Map("ho" -> 12) must (not contain key("hi") and not contain value(13))
      Map("hi" -> 13) must (contain key ("hi") and contain value (13))
    }

    it("should report its size as the number of key/value pairs it contains") {
      Map() must have size (0)
      Map("ho" -> 12) must have size (1)
      Map("hi" -> 13, "ho" -> 12) must have size (2)
    }
  }

}

