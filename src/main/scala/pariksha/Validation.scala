package pariksha

import scala.util.Try

/**
 * @author Ayush Mittal
 */

trait Validation[T] {
  def check(t: T): RuleResult[T]
}

class SimpleValidation[T](rule: T => Boolean, msgWhenFails: String) extends Validation[T] {
  override def check(t: T): RuleResult[T] = {
    if (Try(rule.apply(t)).recover{
      case _: Throwable => false
    }.get) new RulePass(t)
    else new RuleFailed(List(ValidationError(msgWhenFails)))
  }
}

class ComposedValidation[T, M](val rule: T => ValidationResult[M]) extends Validation[T] {
  override def check(t: T): RuleResult[T] = {
    val validationResult = rule(t)
    if (validationResult.isValid) new RulePass[T](t)
    else new RuleFailed(validationResult.errors)
  }
}
