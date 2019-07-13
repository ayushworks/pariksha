# Pariksha.

[![Build Status](https://travis-ci.org/ayushworks/pariksha.svg?branch=master)](https://travis-ci.com/ayushworks/pariksha)

A scala library for validation. 

The protagonist of our story is the `Validator[T]` **trait**  which validates instances of `T` by using 
a list of `Validation[T]`.   

Consider a simple case class
```scala
case class Employee(name: String, age: Int)

```

We can define a list of _**Validation**_ for this type 

```scala
import pariksha.dsl_

implicit val validations = validator[Employee]
                    .check(_.name.nonEmpty, "name must not be empty")
                    .check(_.age > 18, "age must be above 18")
                    .check(_.name != "Bob Vance", "He owns Vance Refrigeration and is not an employee")
```

And then we can validate any instance of `Employee` type. All we need is  `Validator[Employee]` implicitly
in scope 

```scala
import pareeskha.dsl_

val employee = Employee("Jim Halpert", 30)

employee.validate

```

`validate` returns a `ValidationResult`  which can have two possible values

* `Valid`
* `Invalid`

Everybody knows Jenna Fischer from the office!

```scala

val beesly = Employee("Pam Beesly", 28)

beesly.validate == Valid(beesly)

```

And for an invalid employee

```scala

val bob = Employee("Bob Vance", 45)

bob.validate == Invalid(bob, List(ValidationError("He owns Vance Refrigeration and is not an employee")))

```

#### Nested Validations

When we have a type that contains another type, and we already have a `Validator` for the nested type, we can use the existing validator and delegate to that.

```scala

case class Manager(name: String, age: Int)

case class Office(manager: Manager, region: String)

```

We could define **_validations_** for Manager to be used in multiple places

```scala
val validations = validator[Manager]
    .check(_.name.nonEmpty, msgNameEmpty)
    .check(_.age > 25, msgAgeInvalid)
```

Then we we can define **_validations_** for the Office class and use the previous validator 
automatically for the manager field, assuming it is available as an implicit in the current scope. 

Note, how we use the `validate` method on the _contained_ type.

```scala
val validations = validator[Office]
                    .check(_.manager.validate)
                    .check(_.region.nonEmpty, msgRegionNonEmpty)

val validManager = Manager("Michael", 35)

val office = Office(validManager, "Scranton")

office.validate == Valid(office)

```

#### Fail Fast Validations

Sometimes it is desirable to not run all validations exhaustively but rather stop on first failed validation.

We can use the `validateFailFast` method on a type `T` . The requirements remain the same with a presence of `Validator[T]` needed.

```scala
val manager = Manager("", 18)

val validations = validator[Manager]
    .check(_.name.nonEmpty, msgNameEmpty)
    .check(_.age > 25, msgAgeInvalid)
    
manager.validate == Invalid(manager, List(
          ValidationError(Manager.msgNameEmpty),
          ValidationError(Manager.msgAgeInvalid)
        ))    

manager.validateFailFast == Invalid(manager, List(
                                      ValidationError(Manager.msgNameEmpty))
                                      
``` 

The second **_check_** is not even called in this case as the first one had failed. This is useful when 
the validations are resource/time consuming and we would like to stop at the first sign of problems.

##### Next goals :

* Provide validation helpers like `not null` on all fields
* ValidationRule to support intake of exceptions
* Support for effects types like IO, ZIO etc. 
