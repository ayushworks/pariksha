package pariksha

import org.scalatest.{Matchers, WordSpec}
import pariksha.ValidationResult.{Invalid, Valid}
import pariksha.models.{Employee, Involved, Manager, Values, Visits}

/**
  * @author Ayush Mittal
  */
class ValidationResultSpec extends WordSpec with Matchers with Values {

  import syntax._
  "Validation" when {

    "there are simple case classes" should {

      "validate employee" in {
        validEmployee.validate == Valid(validEmployee)

        bob.validate == Invalid(bob, List(ValidationError("He owns Vance Refrigeration and is not an employee")))

        invalidEmployee.validate ==  Invalid(invalidEmployee, List(
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        ))
      }

      "validate manager" in {
        validManager.validate == Valid(validManager)

        invalidManager1.validate == Invalid(invalidManager1,List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        ))

        invalidManager2.validate ==  Invalid(invalidManager2, List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        ))

        invalidManager3.validate == Invalid(invalidManager3, List(
          ValidationError(Manager.msgNoSubordinates)
        ))
      }
    }

    "the case class are composed" should {

      "validate office" in {
        validOffice.validate == Valid(validOffice)

        inValidOffice1.validate == Invalid(inValidOffice1, List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        ))

        inValidOffice2.validate == Invalid(inValidOffice2, List(
          ValidationError(Manager.msgAgeInvalid),
          ValidationError(Manager.msgNoSubordinates)
        ))

        inValidOffice3.validate == Invalid(inValidOffice3, List(
          ValidationError(Manager.msgNoSubordinates)
        ))
      }
    }

    "validations mutating statue" should {

      "validate visits" in {

        validVisits.validate == Valid(validVisits)
        validVisits.value shouldBe 4

        invalidVisits.validate == Invalid(invalidVisits, List(
          ValidationError(Visits.errorMsg),
          ValidationError(Visits.errorMsg)
        ))

        invalidVisits.value shouldBe -1
      }
    }

    "exceptions validation rules throw exception" should {

      "validate involved" in {
        involved.validate == Invalid(involved, List(
          ValidationError(Involved.exceptionMsg),
          ValidationError(Employee.msgNameEmpty),
          ValidationError(Employee.msgAgeInvalid)
        ))
      }
    }
  }
}
