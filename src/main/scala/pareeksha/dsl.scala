package pareeksha

/**
 * @author Ayush Mittal
 */
object dsl {

  type Validator[T] = pareeksha.Validator[T]
  val validator = pareeksha.Validator

  implicit class ValidatorOps[T](val v: Validator[T]) {

    def combine(validation: Validation[T]): Validator[T] = {
      validator(v.validations :+ (validation))
    }

    def check(rule: T => Boolean, msgWhenInvalid: String): Validator[T] =
      combine(new SimpleValidation[T](rule, msgWhenInvalid))

    def check[M](rule: T => ValidationResult[M]): Validator[T] =
      combine(new ComposedValidation(rule))

  }
}
