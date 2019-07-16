package pariksha

import scala.concurrent.Future

/**
 * @author Ayush Mittal
 */
trait Validator[T] {

  val validations: List[Validation[T]]

  def validate(obj: T): ValidationResult = {
    ValidationResult(obj, validations.map(_.check(obj)))
  }

  def validateFailFast(obj: T): ValidationResult = {
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
