package pareeksha

/**
 * @author Ayush Mittal
 */
trait Validator[T] {

  val validations: List[Validation[T]]

  def validate(obj: T): ValidationResult[T] = {
    ValidationResult(obj, validations.map {
      validation =>
        validation.check(obj)
    })
  }

  def validateFailFast(obj: T): ValidationResult[T] = {
    ValidationResult.failFast(obj, validations)
  }
}

object Validator {

  def apply[T]: Validator[T] = new Validator[T] {
    val validations: List[Validation[T]] = Nil
  }

  def apply[T](validationList: List[Validation[T]]): Validator[T] = new Validator[T] {
    override val validations: List[Validation[T]] = validationList
  }
}
