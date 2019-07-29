package pariksha

import pariksha.Validation.{Composed, Simple, checks, notNull}
import shapeless.ops.hlist.{Mapper, ToTraversable}
import shapeless.{::, Generic, HList}

/**
 * @author Ayush Mittal
 */
object dsl {

  type Validator[T] = pariksha.Validator[T]
  val validator = pariksha.Validator

  implicit class ValidatorOps[T](val v: Validator[T]) {

    def combine(validation: Validation[T]): Validator[T] =
      validator(v.validations :+ validation)

    def check(rule: T => Boolean, msgWhenInvalid: String): Validator[T] =
      combine(new Simple[T](rule, msgWhenInvalid))

    def check[M](rule: T => ValidationResult): Validator[T] =
      combine(new Composed(rule))

    def checkNotNull[L<: HList, HL <: HList](msgWhenFails: String)(implicit gen: Generic.Aux[T, L],
                                                        mapper: Mapper.Aux[checks.type, L, HL], trav: ToTraversable.Aux[HL,List,Boolean]) : Validator[T] = {
      combine(new notNull(msgWhenFails))
    }

  }
}
