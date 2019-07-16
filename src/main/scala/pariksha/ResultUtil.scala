package pariksha

import pariksha.RuleResult.Failed
import pariksha.ValidationResult.{Invalid, Valid}

/**
  * @author Ayush Mittal
  */
object ResultUtil {

  def fromRuleResults[T](value: T, result: List[RuleResult]): ValidationResult = {
    result.collect { case Failed(errors) => errors } match {
      case Nil => Valid(value)
      case errors => Invalid(value, errors.flatten)
    }
  }

  def fromValidationList[T](value: T, validations: List[Validation[T]]): ValidationResult = {
    validations.collectFirst { case validation if validation.check(value).isInstanceOf[Failed] =>
      Invalid(value, validation.check(value).asInstanceOf[Failed].errors)
    }.getOrElse(Valid(value))
  }
}
