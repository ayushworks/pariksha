package pariksha.models

/**
 * @author Ayush Mittal
 */
trait Values {

  val invalidEmployee = Employee("", 0)

  val validEmployee = Employee("Jim", 20)

  val nullEmployee = Employee(null, 20)

  val bob = Employee("Bob Vance", 45)

  val invalidManager1 = Manager("", 18, Nil)

  val invalidManager2 = Manager("name", 18, Nil)

  val invalidManager3 = Manager("name", 26, Nil)

  val validManager = Manager("Michael", 26, List(Employee("Jim", 20), Employee("Dwight", 21))) // this could be a long list

  val validOffice = Office(validManager)

  val nullManagerOffice = Office(null)

  val inValidOffice1 = Office(invalidManager1)

  val inValidOffice2 = Office(invalidManager2)

  val inValidOffice3 = Office(invalidManager3)

  val involved = Involved(validEmployee, invalidEmployee)

  val validVisits = Visits(2)

  val invalidVisits = Visits(-3)

}
