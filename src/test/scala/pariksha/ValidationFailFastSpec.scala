package pariksha

import org.scalatest.{Matchers, WordSpec}
import pariksha.models.{Employee, Involved, Manager, Values, Visits}

/**
 * @author Ayush Mittal
 */
class ValidationFailFastSpec extends WordSpec with Matchers with Values {

  import syntax._
  "Fail-Fast Validation" when {

    "there are simple case classes" should {

      "validate employee" in {
        validEmployee.validateFailFast.isValid shouldBe true

        invalidEmployee.validateFailFast.errors shouldBe List(
          ValidationError(Employee.msgNameEmpty)
        )
      }

      "validate manager" in {
        validManager.validateFailFast.errors shouldBe Nil

        invalidManager1.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty)
        )

        invalidManager2.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid)
        )

        invalidManager3.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "the classes are composed" should {

      "validate office" in {
        validOffice.validateFailFast.errors shouldBe Nil
        validOffice.validateFailFast.isValid shouldBe true

        inValidOffice1.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice2.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice3.validateFailFast.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "validations mutating statue" should {

      "stop when first validation fails" in {

        validVisits.validateFailFast.errors shouldBe Nil
        validVisits.value shouldBe 4

        validVisits.validateFailFast.isValid shouldBe true

        invalidVisits.validateFailFast.errors shouldBe List(
          ValidationError(Visits.errorMsg)
        )

        invalidVisits.value shouldBe -2
      }
    }

    "exceptions validation rules throw exception" should {

      "validate involved" in {
        involved.validateFailFast.errors shouldBe List(
          ValidationError(Involved.exceptionMsg)
        )
      }
    }
  }
}
