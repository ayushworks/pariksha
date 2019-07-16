package pariksha

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Ayush Mittal
 */
sealed trait ValidationResult

object ValidationResult {
  case class Valid[T](value: T) extends ValidationResult
  case class Invalid[T](value: T, errors: List[ValidationError]) extends ValidationResult

  def apply[T](value: T, result: List[RuleResult[T]]): ValidationResult = {
    ResultUtil.fromRuleResults(value, result)
  }

  def failFast[T](value: T, validations: List[Validation[T]]): ValidationResult = {
    ResultUtil.fromValidationList(value, validations)
  }

  def validateAsync[T](value: T, validations: List[Validation[T]])(implicit executionContext: ExecutionContext): Future[ValidationResult] = {
    ResultUtil.fromValidationListAsync(value, validations)
  }
}
