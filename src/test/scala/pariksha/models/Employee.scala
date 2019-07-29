package pariksha.models

import pariksha.dsl._
/**
 * @author Ayush Mittal
 */
case class Employee(name: String, age: Int)

object Employee {

  val msgNameEmpty = "employee name must not be empty"
  val msgAgeInvalid = "employee age must be above 18"
  val nameIsNull = "name field is null"

  implicit val validations: Validator[Employee] = validator[Employee]
    .check(_.name.nonEmpty, msgNameEmpty)
    .check(_.age > 18, msgAgeInvalid)
    .check(_.name != "Bob Vance", "He owns Vance Refrigeration and is not an employee")
    .checkNotNull(nameIsNull)
}


case class Manager(name: String, age: Int, employees: List[Employee])

object Manager {

  val msgNameEmpty = "manager name is empty"
  val msgAgeInvalid = "manager age must be above 25"
  val msgNoSubordinates = "manager must have employees"

  implicit val validations = validator[Manager]
    .check(_.name.nonEmpty, msgNameEmpty)
    .check(_.age > 25, msgAgeInvalid)
    .check(_.employees.nonEmpty, msgNoSubordinates)

}

case class Involved(employee1: Employee, employee2: Employee)

object Involved {

  import pariksha.syntax._

  val exceptionMsg = "exception thrown"
  implicit val validations: Validator[Involved] = validator[Involved]
    .check(_ => throw new RuntimeException, exceptionMsg)
    .check(_.employee1.validate)
    .check(_.employee2.validate)

}

case class Office(manager: Manager)

object Office {

  import pariksha.syntax._
  val nullMsg = "manager must not be null"
  implicit val validations: Validator[Office] = validator[Office]
    .checkNotNull(nullMsg)
    .check(_.manager.validate)
}

case class Visits(var value: Int)

object Visits {

  val errorMsg = "value must be greater than zero"

  implicit val validations: Validator[Visits] = validator[Visits]
    .check( {
      x => x.value = x.value + 1
        x.value > 0
    }, errorMsg)
    .check( {
      x => x.value = x.value + 1
        x.value > 0
    }, errorMsg)
}