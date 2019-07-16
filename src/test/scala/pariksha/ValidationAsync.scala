package pariksha

import org.scalatest.{Matchers, WordSpec}
import pariksha.ValidationResult.Invalid
import pariksha.models.{Employee, Involved, Manager, Values}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ayush Mittal
  */
class ValidationAsync extends WordSpec with Matchers with Values {

  import syntax._
  import pariksha.helpers.FutureHelper.ValidationFutureTestOps

  "Async Validation" when {

    "there are simple case classes" should {

      "validate employee" in {

        validEmployee.validateAsyncF.isValid shouldBe true

        invalidEmployee.validateAsyncF.isValid shouldBe false

        invalidEmployee.validateAsync == Future.successful(Invalid(invalidEmployee, List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )))

        invalidEmployee.validateAsyncF.errors shouldBe List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )
      }

      "validate manager" in {
        validManager.validateAsyncF.isValid shouldBe true

        invalidManager1.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager2.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager3.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "the case class are composed" should {

      "validate office" in {
        validOffice.validateAsyncF.errors shouldBe Nil
        validOffice.validateAsyncF.isValid shouldBe true

        inValidOffice1.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice2.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice3.validateAsyncF.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "exceptions validation rules throw exception" should {

      "validate involved" in {
        involved.validateAsyncF.errors shouldBe List(
          ValidationError(Involved.exceptionMsg),
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )
      }
    }
  }
}
