package pariksha

import pariksha.RuleResult.{Failed, Passed}
import pariksha.ValidationResult.{Invalid, Valid}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Ayush Mittal
  */
object ResultUtil {

  def fromRuleResults[T](value: T, result: List[RuleResult[T]]): ValidationResult = result.collect { case Failed(errors) => errors } match {
      case Nil => Valid(value)
      case errors => Invalid(value, errors.flatten)
    }


  def fromValidationListAsync[T](value: T, validations: List[Validation[T]])(implicit executionContext: ExecutionContext): Future[ValidationResult] = Future.sequence(validations.map {
      validation =>
        Future(validation.check(value))
    }).map(fromRuleResults(value, _))


  def fromValidationList[T](value: T, validations: List[Validation[T]]): ValidationResult = {
    var result : RuleResult[T] = Passed(value)
    validations.collectFirst { case validation if {
      result = validation.check(value)
      !result.isSuccess
    } =>
      Invalid(value, result.getErrors)
    }.getOrElse(Valid(value))
  }
}
