package pareeksha

import scala.collection.GenTraversable

/**
 * @author Ayush Mittal
 */
object syntax {

  implicit class ValidationsOps[T](t: T) {

    def validate(implicit validator: Validator[T]): ValidationResult[T] =
      validator.validate(t)

    def validateFailFast(implicit validator: Validator[T]): ValidationResult[T] =
      validator.validateFailFast(t)
  }

  implicit class ValidationsTraversableOps[T](ts: GenTraversable[T]) {

    def validate(implicit validator: Validator[T]): Traversable[ValidationResult[T]] =
      ts.seq.map {
        t =>
          validator.validate(t)
      }
  }
}
