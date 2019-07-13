package pareeksha

/**
 * @author Ayush Mittal
 */
sealed trait ValidationResult[T]{

  def isValid: Boolean

  def errors: List[ValidationError]

  def value: T

}

case class Valid[T](val value: T) extends ValidationResult[T] {

  override def isValid: Boolean = true

  override def errors: List[ValidationError] = Nil

}

case class Invalid[T](val value: T, val errors: List[ValidationError]) extends ValidationResult[T] {

  override def isValid: Boolean = false

}

object ValidationResult {

  def apply[T](value: T, result: List[RuleResult[T]]): ValidationResult[T] = {
    ResultUtil.fromRuleResults(value, result)
  }

  def failFast[T](value: T, validations: List[Validation[T]]): ValidationResult[T] = {
    ResultUtil.fromValidationList(value, validations)
  }
}
