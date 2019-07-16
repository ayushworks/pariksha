package pariksha

import pariksha.RuleResult.{Failed, Passed}
import pariksha.ValidationResult.{Invalid, Valid}

import scala.util.Try

/**
 * @author Ayush Mittal
 */

trait Validation[T] {
  def check(value: T): RuleResult[T]
}

object Validation {
  class Simple[T](rule: T => Boolean, msgWhenFails: String) extends Validation[T] {
    override def check(value: T): RuleResult[T] = {
      Try(rule.apply(value)).toOption match {
        case Some(true) => Passed(value)
        case _ => Failed(List(ValidationError(msgWhenFails)))
      }
    }
  }

  class Composed[T, M](val rule: T => ValidationResult) extends Validation[T] {
    override def check(value: T): RuleResult[T] = {
      rule(value) match {
        case Valid(_) => Passed(value)
        case Invalid(_, errors) => Failed(errors)
      }
    }
  }
}
