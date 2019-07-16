package pariksha

import pariksha.Validation.{Composed, Simple}

/**
 * @author Ayush Mittal
 */
object dsl {

  type Validator[T] = pariksha.Validator[T]
  val validator = pariksha.Validator

  implicit class ValidatorOps[T](val v: Validator[T]) {

    def combine(validation: Validation[T]): Validator[T] = {
      validator(v.validations :+ validation)
    }

    def check(rule: T => Boolean, msgWhenInvalid: String): Validator[T] =
      combine(new Simple[T](rule, msgWhenInvalid))

    def check[M](rule: T => ValidationResult): Validator[T] =
      combine(new Composed(rule))

  }
}
