package pariksha

import pariksha.Validation.{Composed, Simple, bind, notNull}
import shapeless.ops.hlist.{Mapper, ToTraversable}
import shapeless.{HList, LabelledGeneric}

/**
 * @author Ayush Mittal
 */
object dsl {

  type Validator[T] = pariksha.Validator[T]
  val validator = pariksha.Validator

  implicit class ValidatorOps[T](val v: Validator[T]) {

    private def combine(validation: Validation[T]): Validator[T] =
      validator(v.validations :+ validation)

    def check(rule: T => Boolean, msgWhenInvalid: String): Validator[T] =
      combine(new Simple[T](rule, msgWhenInvalid))

    def check[M](rule: T => ValidationResult): Validator[T] =
      combine(new Composed(rule))

    /*def checkNotNullB[L<: HList, HL <: HList](msgWhenFails: String)(implicit gen: Generic.Aux[T, L],
                                                                    mapper: Mapper.Aux[checks.type, L, HL], trav: ToTraversable.Aux[HL,List,Boolean]) : Validator[T] = {
      combine(new notNullB(msgWhenFails))
    }*/

    def checkNotNull[L<: HList, HL <: HList](msgWhenFails: String = "' must not be null")(implicit gen: LabelledGeneric.Aux[T, L],
                                             mapper: Mapper.Aux[bind.type, L, HL], trav: ToTraversable.Aux[HL,List,(String,Boolean)]) : Validator[T] = {
      combine(new notNull(msgWhenFails))
    }

  }
}
