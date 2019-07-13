package pariksha

import org.scalatest.{Matchers, WordSpec}
import pariksha.models.{Employee, Involved, Manager, Values}

/**
 * @author Ayush Mittal
 */
class ValidationSpec extends WordSpec with Matchers with Values {

  import syntax._
  "Validation" when {

    "there are simple case classes" should {

      "validate employee" in {
        validEmployee.validate.isValid shouldBe true

        invalidEmployee.validate.errors shouldBe List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )
      }

      "validate manager" in {
        validManager.validate.isValid shouldBe true

        invalidManager1.validate.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager2.validate.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager3.validate.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "the case class are composed" should {

      "validate office" in {
        validOffice.validate.errors shouldBe Nil
        validOffice.validate.isValid shouldBe true

        inValidOffice1.validate.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice2.validate.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice3.validate.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "exceptions validation rules throw exception" should {

      "validate involved" in {
        involved.validate.errors shouldBe List(
          ValidationError(Involved.exceptionMsg),
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )
      }
    }
  }
}
