package pareeksha.models

import pareeksha.dsl._

/**
 * @author Ayush Mittal
 */
case class Employee(name: String, age: Int)

object Employee {

  val msgNameEmpty = "employee name must not be empty"
  val msgAgeInvalid = "employee age must be above 18"

  implicit val validations = validator[Employee]
    .check(_.name.nonEmpty, msgNameEmpty)
    .check(_.age > 18, msgAgeInvalid)
    .check(_.name != "Bob Vance", "He owns Vance Refrigeration and is not an employee")
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

  import pareeksha.syntax._

  val exceptionMsg = "exception thrown"
  implicit val validations = validator[Involved]
    .check((t => throw new RuntimeException), exceptionMsg)
    .check(_.employee1.validate)
    .check(_.employee2.validate)

}
case class Office(manager: Manager)

object Office {

  import pareeksha.syntax._

  implicit val validations = validator[Office].check(_.manager.validate)
}