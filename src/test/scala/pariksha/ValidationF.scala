package pariksha

import cats.effect.IO
import org.scalatest.{Matchers, WordSpec}
import pariksha.ValidationResult.Invalid
import pariksha.models.{Employee, Involved, Manager, Values}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ayush Mittal
  */
class ValidationF extends WordSpec with Matchers with Values {

  import syntax._
  import pariksha.helpers.TestHelpers.ValidationTestOps

  "Async Validation" when {

    "there are simple case classes" should {

      "validate employee" in {

        validEmployee.validateIO.isValid shouldBe true

        invalidEmployee.validateIO.isValid shouldBe false

        invalidEmployee.validateIO == Invalid(invalidEmployee, List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        ))

        invalidEmployee.validateIO.errors shouldBe List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )

      }

      "validate manager" in {
        validManager.validateIO.isValid shouldBe true

        invalidManager1.validateIO.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager2.validateIO.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        invalidManager3.validateIO.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "the case class are composed" should {

      "validate office" in {
        validOffice.validateIO.errors shouldBe Nil
        validOffice.validateIO.isValid shouldBe true

        inValidOffice1.validateIO.errors shouldBe List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice2.validateIO.errors shouldBe List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        )

        inValidOffice3.validateIO.errors shouldBe List(
          ValidationError(Manager.msgNoSubordinates)
        )
      }
    }

    "exceptions validation rules throw exception" should {

      "validate involved" in {
        involved.validateIO.errors shouldBe List(
          ValidationError(Involved.exceptionMsg),
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        )
      }
    }
  }
}
