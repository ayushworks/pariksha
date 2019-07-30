package pariksha.models

import pariksha.dsl._
/**
 * @author Ayush Mittal
 */
case class Employee(name: String, age: Int)

object Employee {

  val msgNameEmpty = "employee name must not be empty"
  val msgAgeInvalid = "employee age must be above 18"

  implicit val validations: Validator[Employee] = validator[Employee]
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
  implicit val validations: Validator[Office] = validator[Office]
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

case class TVCharacter(name: String, showName: String)

object TVCharacter {

  val notNullValidations : Validator[TVCharacter] = validator[TVCharacter]
    .checkNotNull()

  val nameNonEmptyMsg = "character name must be not be empty"
  val showNameNonEmptyMsg = "show name must not be empty"

  val allValidations : Validator[TVCharacter] = validator[TVCharacter]
    .checkNotNull()
    .check(_.name.nonEmpty, nameNonEmptyMsg)
    .check(_.showName.nonEmpty, showNameNonEmptyMsg)
}

case class Artist(name: String, character: TVCharacter)

object Artist {

  import pariksha.syntax._
  implicit val validations : Validator[Artist] = validator[Artist]
    .checkNotNull()
    .check(_.character.validate(TVCharacter.allValidations))
}