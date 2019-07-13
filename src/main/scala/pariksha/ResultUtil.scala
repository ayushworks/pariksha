package pariksha

/**
  * @author Ayush Mittal
  */
object ResultUtil {

  def fromRuleResults[T](value: T, result: List[RuleResult[T]]): ValidationResult[T] = {
    var errors = scala.collection.mutable.ListBuffer[ValidationError]()
    result.map {
      l =>
        if (!l.isValid) {
          errors ++= l.errors
        }
    }
    if(errors.length == 0) {
      new Valid[T](value)
    }
    else{
      new Invalid[T](value, errors.toList)
    }
  }

  def fromValidationList[T](value: T, validations: List[Validation[T]]): ValidationResult[T] = {
    validations.map {
      validation =>
        val checkResult = validation.check(value)
        if (!checkResult.isValid) return new Invalid[T](value, checkResult.errors)
    }
    new Valid[T](value)
  }
}
