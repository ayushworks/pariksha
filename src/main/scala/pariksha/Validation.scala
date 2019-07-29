package pariksha

import pariksha.RuleResult.{Failed, Passed}
import pariksha.ValidationResult.{Invalid, Valid}
import shapeless.ops.hlist.Mapper
import shapeless.{Generic, HList, Poly1}
import shapeless._, ops.hlist._
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

  class Composed[T](val rule: T => ValidationResult) extends Validation[T] {
    override def check(value: T): RuleResult[T] = {
      rule(value) match {
        case Valid(_) => Passed(value)
        case Invalid(_, errors) => Failed(errors)
      }
    }
  }

  object checks extends Poly1 {
    implicit def checka[A] = at[A]{ a => a!=null }
  }

  class notNull[T,L <: HList, HL <: HList](msgWhenFails: String)(implicit gen: Generic.Aux[T, L],
                                                     mapper: Mapper.Aux[checks.type, L, HL], trav: ToTraversable.Aux[HL,List,Boolean]) extends Validation[T] {

    override def check(value: T): RuleResult[T] = {
      val result = gen.to(value).map(checks).toList
      result.forall(identity) match {
        case true => Passed(value)
        case false => Failed(List(ValidationError(msgWhenFails)))
      }
    }
  }
}
