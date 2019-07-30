package pariksha

import pariksha.RuleResult.{Failed, Passed}
import pariksha.ValidationResult.{Invalid, Valid}
import shapeless.ops.hlist.Mapper
import shapeless.{Generic, HList, Poly1}
import shapeless._
import ops.hlist._
import shapeless.labelled.FieldType
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

  /**
    * Our first implicit parameter gen can turn a T into an HList of type L.
    * Our second implicit parameter mapper can use our checks polymorphic function to map an HList of type L to an HList of type HL.
    * To turn our HList HL (the result of mapping to checks) to a List[Boolean] we need a ToTraversable, so we add a third implicit parameter
    * @param msgWhenFails
    * @param gen
    * @param mapper
    * @param trav
    * @tparam T
    * @tparam L
    * @tparam HL
    */
  class notNullB[T,L <: HList, HL <: HList](msgWhenFails: String)(implicit gen: Generic.Aux[T, L],
                                                                  mapper: Mapper.Aux[checks.type, L, HL], trav: ToTraversable.Aux[HL,List,Boolean]) extends Validation[T] {

    override def check(value: T): RuleResult[T] = {
      val result = gen.to(value).map(checks).toList
      result.forall(identity) match {
        case true => Passed(value)
        case false => Failed(List(ValidationError(msgWhenFails)))
      }
    }
  }

  object bind extends FieldPoly {
    implicit def rpb[T, K](implicit witness: Witness.Aux[K]): Case.Aux[
      FieldType[K, T],
      FieldType[K, (String,Boolean)]
      ] = atField[T](witness){ a =>
      (witness.value.toString,a!=null)
    }
  }

  /**
    * Our first implicit parameter gen can turn a T into an HList of type L.
    * Our second implicit parameter mapper can use our checks polymorphic function to map an HList of type L to an HList of type HL.
    * To turn our HList HL (the result of mapping to checks) to a List[Boolean] we need a ToTraversable, so we add a third implicit parameter
    * @param msgWhenFails
    * @param gen
    * @param mapper
    * @param trav
    * @tparam T
    * @tparam L
    * @tparam HL
    */
  class notNull[T,L <: HList, HL <: HList](msgWhenFails: String)(implicit gen: LabelledGeneric.Aux[T, L],
                                                                                        mapper: Mapper.Aux[bind.type, L, HL], trav: ToTraversable.Aux[HL,List,(String,Boolean)]) extends Validation[T] {

    override def check(value: T): RuleResult[T] = {
      if(value==null) {
        //what to do here? this can happen for a composed case class where not null check is present on composable fields
        Passed(value)
      }
      else {
        val record = gen.to(value)
        val result = record.map(bind).toList
        val failed = result.filter(!_._2)
        val failedResult = failed.map(x => ValidationError(s"${value.getClass.getSimpleName}.${x._1}$msgWhenFails"))
        failedResult match {
          case Nil => Passed(value)
          case _ => Failed(failedResult)
        }
      }
    }
  }
}
